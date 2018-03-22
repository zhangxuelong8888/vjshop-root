
package com.vjshop.service.impl;

import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TDeliveryCenterDao;
import com.vjshop.entity.TDeliveryCenter;
import com.vjshop.generated.db.tables.records.TDeliveryCenterRecord;
import com.vjshop.service.TAreaService;
import com.vjshop.service.TDeliveryCenterService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service - 广告
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tDeliveryCenterServiceImpl")
public class TDeliveryCenterServiceImpl extends TBaseServiceImpl<TDeliveryCenterRecord,TDeliveryCenter, Long> implements TDeliveryCenterService {

	@Autowired
	private TDeliveryCenterDao tDeliveryCenterDao;
	@Autowired
	private TAreaService tAreaService;

	@Transactional(readOnly = true)
	public TDeliveryCenter findDefault() {
		TDeliveryCenter tDeliveryCenter = this.tDeliveryCenterDao.findDefault();
		queryForeignObj(tDeliveryCenter);
		return tDeliveryCenter;
	}

	@Override
	public Page<TDeliveryCenter> findPage(Pageable pageable){
		if (StringUtils.isBlank(pageable.getOrderProperty())) {
			pageable.setOrderProperty("createDate");
			pageable.setOrderDirection(Order.Direction.desc);
		}
		Page<TDeliveryCenter> tDeliveryCenterPage = this.tDeliveryCenterDao.findPage(pageable);
		List<TDeliveryCenter> newContent = new ArrayList<>();
		for(TDeliveryCenter tDeliveryCenter : tDeliveryCenterPage.getContent()){
			queryForeignObj(tDeliveryCenter);
			newContent.add(tDeliveryCenter);
		}
		return new Page<TDeliveryCenter>(newContent,tDeliveryCenterPage.getTotal(),pageable);
	}

	@Override
	public TDeliveryCenter findDetailById(Long id){
		TDeliveryCenter tDeliveryCenter = super.find(id);
		queryForeignObj(tDeliveryCenter);
		return tDeliveryCenter;
	}


	private void queryForeignObj(TDeliveryCenter tDeliveryCenter){
		tDeliveryCenter.setAreaVO(this.tAreaService.find(tDeliveryCenter.getArea()));
	}



}