
package com.vjshop.service.impl;

import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TRefundsDao;
import com.vjshop.entity.TPayment;
import com.vjshop.entity.TRefunds;
import com.vjshop.generated.db.tables.records.TRefundsRecord;
import com.vjshop.service.TOrderService;
import com.vjshop.service.TRefundsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service - 退款单
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tRefundsServiceImpl")
public class TRefundsServiceImpl extends TBaseServiceImpl<TRefundsRecord,TRefunds, Long> implements TRefundsService {

	@Autowired
	private TRefundsDao tRefundsDao;
	@Autowired
	private TOrderService tOrderService;

	public List<TRefunds> findByOrderIds(Long... ids){
		List<TRefunds> tRefundses = this.tRefundsDao.fetchByOrders(ids);
		for(TRefunds tRefunds : tRefundses){
			tRefunds.setOrderVO(this.tOrderService.find(tRefunds.getOrders()));
		}
		return tRefundses;
	}

	@Override
	public TRefunds insertAndFetch(TRefunds tRefunds){
		return this.tRefundsDao.insertAndFetch(tRefunds);
	}

	@Override
	public Page<TRefunds> findPage(Pageable pageable){
		if (StringUtils.isBlank(pageable.getOrderProperty())) {
			pageable.setOrderProperty("createDate");
			pageable.setOrderDirection(Order.Direction.desc);
		}
		Page<TRefunds> tRefundsPage = this.tRefundsDao.findPage(pageable);
		List<TRefunds> newContent = new ArrayList<>();
		for(TRefunds tRefunds : tRefundsPage.getContent()){
			tRefunds.setOrderVO(this.tOrderService.findDetailById(tRefunds.getOrders()));
			newContent.add(tRefunds);
		}
		return new Page<TRefunds>(newContent,tRefundsPage.getTotal(),pageable);
	}

	@Override
	public TRefunds findDetailById(Long id){
		TRefunds tRefunds = super.find(id);
		tRefunds.setOrderVO(this.tOrderService.findDetailById(tRefunds.getOrders()));
		return tRefunds;
	}

}