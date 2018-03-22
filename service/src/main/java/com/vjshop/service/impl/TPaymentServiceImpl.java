package com.vjshop.service.impl;

import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TPaymentDao;
import com.vjshop.entity.TPayment;
import com.vjshop.generated.db.tables.records.TPaymentRecord;
import com.vjshop.service.TOrderService;
import com.vjshop.service.TPaymentService;
import com.vjshop.util.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * TPayment服务对象实现类
 */
@Service
public class TPaymentServiceImpl extends TBaseServiceImpl<TPaymentRecord,TPayment,Long> implements TPaymentService {
	
	@Autowired
	private TPaymentDao tPaymentDao;
	@Autowired
	private TOrderService tOrderService;

	public void insert(TPayment tPayment) {
		Assert.notNull(tPayment);

		tPaymentDao.insert(tPayment);
	}

	public TPayment updateSelective(TPayment tPayment) {
		Assert.notNull(tPayment);

		tPaymentDao.updateSelective(BeanUtils.transBean2Map(tPayment));
		return tPayment;
	}

	public TPayment update(TPayment tPayment, String... ignoreProperties) {
		Assert.notNull(tPayment);

        Timestamp now = new Timestamp(System.currentTimeMillis());
        tPayment.setModifyDate(now);
		tPaymentDao.update(tPayment, ignoreProperties);
		return tPayment;
	}

	public TPayment save(TPayment tPayment){
		Assert.notNull(tPayment);

		Timestamp now = new Timestamp(System.currentTimeMillis());
		tPayment.setModifyDate(now);
		tPayment.setVersion(0L);
		if(null == tPayment.getId()){
			tPayment.setCreateDate(now);
			this.insert(tPayment);
		}else{
			this.updateSelective(tPayment);
		}
		return tPayment;
	}

	public void delete(Long... ids) {
		if (ids == null || ids.length == 0) {
			return;
		}
		tPaymentDao.deleteById(ids);
	}

	public Page<TPayment> findPage(Pageable pageable) {
		if (StringUtils.isBlank(pageable.getOrderProperty())) {
			pageable.setOrderProperty("createDate");
			pageable.setOrderDirection(Order.Direction.desc);
		}
		Page<TPayment> tPaymentPage = tPaymentDao.findPage(pageable);
		List<TPayment> newContent = new ArrayList<>();
		for(TPayment tPayment : tPaymentPage.getContent()){
			tPayment.setOrder(this.tOrderService.findDetailById(tPayment.getOrders()));
			newContent.add(tPayment);
		}
		return new Page<TPayment>(newContent,tPaymentPage.getTotal(),pageable);
	}

	public TPayment findDetailById(Long id){
		TPayment tPayment = tPaymentDao.fetchOneById(id);
		tPayment.setOrder(this.tOrderService.findDetailById(tPayment.getOrders()));
		return tPayment;
	}

	public List<TPayment> findByOrderIds(Long... ids){
		List<TPayment> tPayments = this.tPaymentDao.fetchByOrders(ids);
		for(TPayment tPayment : tPayments){
			tPayment.setOrder(this.tOrderService.find(tPayment.getOrders()));
		}
		return tPayments;
	}

}
