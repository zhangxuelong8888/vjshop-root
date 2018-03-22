
package com.vjshop.service.impl;

import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TDeliveryTemplateDao;
import com.vjshop.entity.TDeliveryTemplate;
import com.vjshop.generated.db.tables.records.TDeliveryTemplateRecord;
import com.vjshop.service.TDeliveryTemplateService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 快递单模板
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tDeliveryTemplateServiceImpl")
public class TDeliveryTemplateServiceImpl extends TBaseServiceImpl<TDeliveryTemplateRecord,TDeliveryTemplate, Long> implements TDeliveryTemplateService {

	@Autowired
	private TDeliveryTemplateDao tDeliveryTemplateDao;

	@Transactional(readOnly = true)
	public TDeliveryTemplate findDefault() {
		return tDeliveryTemplateDao.findDefault();
	}


	@Override
	public Page<TDeliveryTemplate> findPage(Pageable pageable){
		if (StringUtils.isBlank(pageable.getOrderProperty())) {
			pageable.setOrderProperty("createDate");
			pageable.setOrderDirection(Order.Direction.desc);
		}
		Page<TDeliveryTemplate> tDeliveryTemplatePage = this.tDeliveryTemplateDao.findPage(pageable);
		return tDeliveryTemplatePage;
	}

}