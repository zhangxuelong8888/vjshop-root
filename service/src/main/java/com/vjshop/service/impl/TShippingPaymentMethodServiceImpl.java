package com.vjshop.service.impl;

import com.vjshop.Filter;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TShippingPaymentMethodDao;
import com.vjshop.entity.TShippingPaymentMethod;
import com.vjshop.service.TShippingPaymentMethodService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vjshop.util.BeanUtils;

import static com.vjshop.generated.db.tables.TShippingPaymentMethod.T_SHIPPING_PAYMENT_METHOD;

/**
 * TShippingPaymentMethod服务对象实现类
 */
@Service
public class TShippingPaymentMethodServiceImpl implements TShippingPaymentMethodService {
	
	@Autowired
	private TShippingPaymentMethodDao tShippingPaymentMethodDao;

	public void insert(TShippingPaymentMethod tShippingPaymentMethod) {
		Assert.notNull(tShippingPaymentMethod);

		tShippingPaymentMethodDao.insert(tShippingPaymentMethod);
	}

	public void updateSelective(TShippingPaymentMethod tShippingPaymentMethod) {
		Assert.notNull(tShippingPaymentMethod);

		tShippingPaymentMethodDao.updateSelective(BeanUtils.transBean2Map(tShippingPaymentMethod));
	}

	public void update(TShippingPaymentMethod tShippingPaymentMethod, String... ignoreProperties) {
		Assert.notNull(tShippingPaymentMethod);

        Timestamp now = new Timestamp(System.currentTimeMillis());
		tShippingPaymentMethodDao.update(tShippingPaymentMethod, ignoreProperties);
	}

	@Transactional
	public void save(List<TShippingPaymentMethod> tShippingPaymentMethods){
		Assert.notNull(tShippingPaymentMethods);

		this.deleteByShippingMethodId(tShippingPaymentMethods.get(0).getShippingMethods());
		for (TShippingPaymentMethod tShippingPaymentMethod : tShippingPaymentMethods) {
			this.insert(tShippingPaymentMethod);
		}
	}

	public void deleteByShippingMethodId(Long id) {
		tShippingPaymentMethodDao.deleteByShippingMethodId(id);
	}

	public void delete(List<TShippingPaymentMethod> tShippingPaymentMethods) {
		if (CollectionUtils.isEmpty(tShippingPaymentMethods)) {
			return;
		}
		tShippingPaymentMethodDao.delete(tShippingPaymentMethods);
	}

	public Page<TShippingPaymentMethod> findPage(Pageable pageable) {
		return tShippingPaymentMethodDao.findPage(pageable);
	}

	@Override
	public List<TShippingPaymentMethod> findByShippingMethodId(Long id){
		List<Filter> filters = new ArrayList<>(1);
		Filter filter = new Filter("shippingMethods", Filter.Operator.eq, id);
		filters.add(filter);
		return tShippingPaymentMethodDao.findList(filters, null);
	}

}
