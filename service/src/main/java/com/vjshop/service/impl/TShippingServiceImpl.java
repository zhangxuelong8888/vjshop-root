
package com.vjshop.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.Setting;
import com.vjshop.dao.TShippingDao;
import com.vjshop.entity.TShipping;
import com.vjshop.entity.TShippingItem;
import com.vjshop.generated.db.tables.records.TShippingRecord;
import com.vjshop.service.TOrderService;
import com.vjshop.service.TShippingItemService;
import com.vjshop.service.TShippingService;
import com.vjshop.util.JsonUtils;
import com.vjshop.util.SystemUtils;
import com.vjshop.util.WebUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Service - 发货单
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tShippingServiceImpl")
public class TShippingServiceImpl extends TBaseServiceImpl<TShippingRecord,TShipping, Long> implements TShippingService {

	@Autowired
	private TShippingDao tShippingDao;
	@Autowired
	private TOrderService tOrderService;
	@Autowired
	private TShippingItemService tShippingItemService;

	@Transactional(readOnly = true)
	public TShipping findBySn(String sn) {
		return tShippingDao.findBySn(sn);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Cacheable("shipping")
	public List<Map<String, String>> getTransitSteps(TShipping shipping) {
		Assert.notNull(shipping);

		Setting setting = SystemUtils.getSetting();
		if (StringUtils.isEmpty(setting.getKuaidi100Key()) || StringUtils.isEmpty(shipping.getDeliveryCorpCode()) || StringUtils.isEmpty(shipping.getTrackingNo())) {
			return Collections.emptyList();
		}
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("id", setting.getKuaidi100Key());
		parameterMap.put("com", shipping.getDeliveryCorpCode());
		parameterMap.put("nu", shipping.getTrackingNo());
		parameterMap.put("show", "0");
		parameterMap.put("muti", "1");
		parameterMap.put("order", "asc");
		String content = WebUtils.get("http://api.kuaidi100.com/api", parameterMap);
		Map<String, Object> data = JsonUtils.toObject(content, new TypeReference<Map<String, Object>>() {
		});
		if (!StringUtils.equals(String.valueOf(data.get("status")), "1")) {
			return Collections.emptyList();
		}
		return (List<Map<String, String>>) data.get("data");
	}

	@Override
	public List<TShipping> findByOrderIds(Long... ids){
		List<TShipping> tShippings = this.tShippingDao.fetchByOrders(ids);
		for(TShipping tShipping : tShippings){
			queryForeignObj(tShipping);
		}
		return tShippings;
	}

	@Override
	public TShipping findDetailById(Long id){
		TShipping tShipping = super.find(id);
		queryForeignObj(tShipping);
		return tShipping;
	}

	@Override
	@Transactional
	public TShipping insertAndFetch(TShipping tShipping){
		tShipping = this.tShippingDao.insertAndFetch(tShipping);

		for(TShippingItem tShippingItem : tShipping.getShippingItems()){
			tShippingItem.setShipping(tShipping.getId());
		}
		this.tShippingItemService.insert(tShipping.getShippingItems());

		return tShipping;
	}

	@Override
	public Page<TShipping> findPage(Pageable pageable){
		if (StringUtils.isBlank(pageable.getOrderProperty())) {
			pageable.setOrderProperty("createDate");
			pageable.setOrderDirection(Order.Direction.desc);
		}
		Page<TShipping> tShippingPage = this.tShippingDao.findPage(pageable);
		List<TShipping> newContent = new ArrayList<>();
		for(TShipping tShipping : tShippingPage.getContent()){
			tShipping.setOrderVO(this.tOrderService.findDetailById(tShipping.getOrders()));
			newContent.add(tShipping);
		}

		return new Page<TShipping>(newContent,tShippingPage.getTotal(),pageable);
	}

	private void queryForeignObj(TShipping tShipping){
		tShipping.setOrderVO(this.tOrderService.find(tShipping.getOrders()));
		tShipping.setShippingItems(this.tShippingItemService.findListByShippingIds(tShipping.getId()));
	}
}