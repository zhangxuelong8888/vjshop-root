
package com.vjshop.service.impl;

import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TReturnsDao;
import com.vjshop.entity.TPayment;
import com.vjshop.entity.TReturns;
import com.vjshop.entity.TReturnsItem;
import com.vjshop.generated.db.tables.records.TReturnsRecord;
import com.vjshop.service.TOrderService;
import com.vjshop.service.TReturnsItemService;
import com.vjshop.service.TReturnsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service - 退货单
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tReturnsServiceImpl")
public class TReturnsServiceImpl extends TBaseServiceImpl<TReturnsRecord,TReturns, Long> implements TReturnsService {

	@Autowired
	private TReturnsDao tReturnsDao;
	@Autowired
	private TOrderService tOrderService;
	@Autowired
	private TReturnsItemService tReturnsItemService;

	@Override
	public List<TReturns> findByOrderIds(Long... ids){
		List<TReturns> tReturnses = this.tReturnsDao.fetchByOrders(ids);
		for(TReturns tReturns : tReturnses){
			queryForeignObj(tReturns);
		}
		return tReturnses;
	}

	@Override
	public TReturns insertAndFetch(TReturns tReturns){
		tReturns = this.tReturnsDao.insertAndFetch(tReturns);
		for(TReturnsItem tReturnsItem : tReturns.getReturnsItems()){
			tReturnsItem.setReturns(tReturns.getId());
		}
		this.tReturnsItemService.insert(tReturns.getReturnsItems());
		return tReturns;
	}

	@Override
	public TReturns findDetailById(Long id){
		TReturns tReturns = super.find(id);
		queryForeignObj(tReturns);
		return tReturns;
	}

	@Override
	public Page<TReturns> findPage(Pageable pageable){
		if (StringUtils.isBlank(pageable.getOrderProperty())) {
			pageable.setOrderProperty("createDate");
			pageable.setOrderDirection(Order.Direction.desc);
		}
		Page<TReturns> tReturnsPage = this.tReturnsDao.findPage(pageable);
		List<TReturns> newContent = new ArrayList<>();
		for(TReturns tReturns : tReturnsPage.getContent()){
			tReturns.setOrderVO(this.tOrderService.findDetailById(tReturns.getOrders()));
			newContent.add(tReturns);
		}
		return new Page<TReturns>(newContent,tReturnsPage.getTotal(),pageable);
	}

	private void queryForeignObj(TReturns tReturns){
		tReturns.setOrderVO(this.tOrderService.find(tReturns.getOrders()));
		tReturns.setReturnsItems(this.tReturnsItemService.findByReturnsIds(tReturns.getId()));
	}
}