package com.vjshop.service.impl;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.Setting;
import com.vjshop.dao.TShippingMethodDao;
import com.vjshop.entity.*;
import com.vjshop.generated.db.tables.records.TShippingMethodRecord;
import com.vjshop.service.TPaymentMethodService;
import com.vjshop.service.TShippingMethodService;
import com.vjshop.service.TShippingPaymentMethodService;
import com.vjshop.util.BeanUtils;
import com.vjshop.util.SystemUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TShippingMethod服务对象实现类
 */
@Service
public class TShippingMethodServiceImpl extends TBaseServiceImpl<TShippingMethodRecord,TShippingMethod,Long> implements TShippingMethodService {
	
	@Autowired
	private TShippingMethodDao tShippingMethodDao;

	@Autowired
	private TPaymentMethodService paymentMethodService;

	@Autowired
	private TShippingPaymentMethodService tShippingPaymentMethodService;

	public TShippingMethod insertAndFetch(TShippingMethod tShippingMethod) {
		Assert.notNull(tShippingMethod);

		return tShippingMethodDao.insertAndFetch(tShippingMethod);
	}

	public TShippingMethod updateSelective(TShippingMethod tShippingMethod) {
		Assert.notNull(tShippingMethod);

		tShippingMethodDao.updateSelective(BeanUtils.transBean2Map(tShippingMethod));
		return tShippingMethod;
	}

	@Transactional
	public TShippingMethod save(TShippingMethod tShippingMethod){
		Assert.notNull(tShippingMethod);

		Timestamp now = new Timestamp(System.currentTimeMillis());
		tShippingMethod.setModifyDate(now);
		tShippingMethod.setVersion(0L);
		Set<TPaymentMethod> paymentMethods = tShippingMethod.getPaymentMethods();
		tShippingMethod.setPaymentMethods(null);
		if(null == tShippingMethod.getId()){
			tShippingMethod.setCreateDate(now);
            tShippingMethod = this.insertAndFetch(tShippingMethod);
		}else{
		    tShippingMethod.setModifyDate(now);
			this.updateSelective(tShippingMethod);
		}

		if (CollectionUtils.isNotEmpty(paymentMethods)) {
			List<TShippingPaymentMethod> list = new ArrayList<>(paymentMethods.size());
            TShippingMethod finalTShippingMethod = tShippingMethod;
            paymentMethods.forEach(paymentMethod -> {
				TShippingPaymentMethod spm = new TShippingPaymentMethod();
				spm.setShippingMethods(finalTShippingMethod.getId());
				spm.setPaymentMethods(paymentMethod.getId());
				list.add(spm);
			});
			tShippingPaymentMethodService.save(list);
		}
		return tShippingMethod;
	}

	@Transactional
	public void delete(Long... ids) {
		if (ids == null || ids.length == 0) {
			return;
		}
		List<TShippingPaymentMethod> tShippingPaymentMethodList = new ArrayList<>();
		for (Long id : ids) {
			List<TShippingPaymentMethod> shippingMethods = tShippingPaymentMethodService.findByShippingMethodId(id);
			if (CollectionUtils.isNotEmpty(shippingMethods)) {
				tShippingPaymentMethodList.addAll(shippingMethods);
			}
		}
		tShippingPaymentMethodService.delete(tShippingPaymentMethodList);
		tShippingMethodDao.deleteById(ids);
	}

	public Page<TShippingMethod> findPage(Pageable pageable) {
		return tShippingMethodDao.findPage(pageable);
	}

	public TShippingMethod findDetails(Long id){
		if (id == null) {
			return null;
		}
		TShippingMethod shippingMethod = tShippingMethodDao.find(id);
		shippingMethod.setPaymentMethods(new HashSet<TPaymentMethod>(paymentMethodService.findListByShippingMethodId(id)));
		return shippingMethod;
	}

	/**
	 * 查询实体记录数
	 *
	 * @return
	 */
	@Override
	public long count() {
		return tShippingMethodDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal calculateFreight(TShippingMethod shippingMethod, TArea area, Integer weight) {
		Assert.notNull(shippingMethod);

		Setting setting = SystemUtils.getSetting();
		BigDecimal firstPrice = shippingMethod.getDefaultFirstPrice();
		BigDecimal continuePrice = shippingMethod.getDefaultContinuePrice();
		if (area != null && CollectionUtils.isNotEmpty(shippingMethod.getFreightConfigs())) {
			List<TArea> areas = new ArrayList<TArea>();
//			areas.addAll(area.getParentArea());
			areas.add(area.getParentArea());
			areas.add(area);
			for (int i = areas.size() - 1; i >= 0; i--) {
				TFreightConfig freightConfig = shippingMethod.getFreightConfig(areas.get(i));
				if (freightConfig != null) {
					firstPrice = freightConfig.getFirstPrice();
					continuePrice = freightConfig.getContinuePrice();
					break;
				}
			}
		}
		if (weight == null || weight <= shippingMethod.getFirstWeight() || continuePrice.compareTo(BigDecimal.ZERO) == 0) {
			return setting.setScale(firstPrice);
		} else {
			double contiuneWeightCount = Math.ceil((weight - shippingMethod.getFirstWeight()) / (double) shippingMethod.getContinueWeight());
			return setting.setScale(firstPrice.add(continuePrice.multiply(new BigDecimal(String.valueOf(contiuneWeightCount)))));
		}
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal calculateFreight(TShippingMethod shippingMethod, TReceiver receiver, Integer weight) {
		return calculateFreight(shippingMethod, receiver != null ? receiver.getAreaVO() : null, weight);
	}

    @Override
    public List<TShippingMethod> findByPaymentMethodId(Long payemntMethodId) {
        return tShippingMethodDao.findByPaymentMethodId(payemntMethodId);
    }

    @Override
    public List<TShippingMethod> findAll(){
		List<TShippingMethod> shippingMethods = super.findAll();
		for (TShippingMethod shippingMethod : shippingMethods) {
			shippingMethod.setPaymentMethods(new HashSet<>(paymentMethodService.findListByShippingMethodId(shippingMethod.getId())));
		}
		return shippingMethods;
	}
}
