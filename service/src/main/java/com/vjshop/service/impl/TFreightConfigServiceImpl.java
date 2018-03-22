package com.vjshop.service.impl;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TFreightConfigDao;
import com.vjshop.entity.*;
import com.vjshop.generated.db.tables.records.TFreightConfigRecord;
import com.vjshop.service.TFreightConfigService;
import com.vjshop.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.sql.Timestamp;


/**
 * Service - 运费配置
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TFreightConfigServiceImpl extends TBaseServiceImpl<TFreightConfigRecord, TFreightConfig, Long> implements TFreightConfigService {

	@Autowired
	private TFreightConfigDao freightConfigDao;

	@Transactional(readOnly = true)
	public boolean exists(Long shippingMethodId, Long areaId) {
		return freightConfigDao.exists(shippingMethodId, areaId);
	}

	@Transactional(readOnly = true)
	public boolean unique(TShippingMethod shippingMethod, TArea previousArea, TArea currentArea) {
		if (previousArea != null && previousArea.equals(currentArea)) {
			return true;
		}
		return !freightConfigDao.exists(shippingMethod.getId(), currentArea.getId());
	}

	@Transactional(readOnly = true)
	public Page<TFreightConfig> findPage(Long shippingMethodId, Pageable pageable) {
		return freightConfigDao.findPage(shippingMethodId, pageable);
	}

	@Override
	public TFreightConfig save(TFreightConfig tFreightConfig){
		Assert.notNull(tFreightConfig);

		Timestamp now = new Timestamp(System.currentTimeMillis());
		tFreightConfig.setModifyDate(now);
		tFreightConfig.setVersion(0L);
		if (null == tFreightConfig.getId()) {
			tFreightConfig.setCreateDate(now);
			tFreightConfig = freightConfigDao.insertAndFetch(tFreightConfig);
		} else {
			freightConfigDao.updateSelective(BeanUtils.transBean2Map(tFreightConfig));
		}

		return tFreightConfig;
	}

}