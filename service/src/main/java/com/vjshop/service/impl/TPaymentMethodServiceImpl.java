package com.vjshop.service.impl;

import com.vjshop.service.TShippingMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TPaymentMethodDao;
import com.vjshop.entity.TPaymentMethod;
import com.vjshop.service.TPaymentMethodService;
import org.springframework.util.Assert;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.vjshop.util.BeanUtils;

/**
 * TPaymentMethod服务对象实现类
 */
@Service
 public class TPaymentMethodServiceImpl implements TPaymentMethodService {
	
	@Autowired
	private TPaymentMethodDao tPaymentMethodDao;

	@Autowired
	private TShippingMethodService tShippingMethodService;

	public void insert(TPaymentMethod tPaymentMethod) {
		Assert.notNull(tPaymentMethod);

		tPaymentMethodDao.insert(tPaymentMethod);
	}

	public void updateSelective(TPaymentMethod tPaymentMethod) {
		Assert.notNull(tPaymentMethod);

		tPaymentMethodDao.updateSelective(BeanUtils.transBean2Map(tPaymentMethod));
	}

	public void update(TPaymentMethod tPaymentMethod, String... ignoreProperties) {
		Assert.notNull(tPaymentMethod);

        Timestamp now = new Timestamp(System.currentTimeMillis());
		tPaymentMethod.setModifyDate(now);
		tPaymentMethodDao.update(tPaymentMethod, ignoreProperties);
	}

	public void save(TPaymentMethod tPaymentMethod){
		Assert.notNull(tPaymentMethod);

		Timestamp now = new Timestamp(System.currentTimeMillis());
		tPaymentMethod.setModifyDate(now);
		tPaymentMethod.setVersion(0L);
		if(null == tPaymentMethod.getId()){
			tPaymentMethod.setCreateDate(now);
			this.insert(tPaymentMethod);
		}else{
			this.updateSelective(tPaymentMethod);
		}
	}

	public void delete(Long... ids) {
		if (ids == null || ids.length == 0) {
			return;
		}
		tPaymentMethodDao.deleteById(ids);
	}

	/**
	 * 查询符合条件的记录数
	 *
	 * @return
	 */
	@Override
	public Long count() {
		return tPaymentMethodDao.count();
	}

	public Page<TPaymentMethod> findPage(Pageable pageable) {
		return tPaymentMethodDao.findPage(pageable);
	}

	public TPaymentMethod find(Long id){
		return tPaymentMethodDao.find(id);
	}

    @Override
    public List<TPaymentMethod> findListByIds(Long[] ids) {
		List<TPaymentMethod> list = new ArrayList<>(ids.length);
		for (Long id : ids) {
			TPaymentMethod paymentMethod = tPaymentMethodDao.findById(id);
			if (paymentMethod != null) {
				list.add(paymentMethod);
			}
		}
		return list;
    }

	@Override
	public List<TPaymentMethod> findListByShippingMethodId(Long id) {
		return tPaymentMethodDao.findListByShippingMethodId(id);
	}

	@Override
    public List<TPaymentMethod> findAll() {
		List<TPaymentMethod> paymentMethods = tPaymentMethodDao.findAll();
		for (TPaymentMethod paymentMethod : paymentMethods) {
			paymentMethod.setShippingMethods(new HashSet<>(tShippingMethodService.findByPaymentMethodId(paymentMethod.getId())));
		}
		return paymentMethods;
    }

}
