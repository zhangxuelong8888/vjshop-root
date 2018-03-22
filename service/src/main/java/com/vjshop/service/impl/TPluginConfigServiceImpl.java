package com.vjshop.service.impl;

import com.vjshop.dao.TPluginConfigDao;
import com.vjshop.entity.TPluginConfig;
import com.vjshop.generated.db.tables.records.TPluginConfigRecord;
import com.vjshop.service.TPluginConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

/**
 * Service - 插件配置
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TPluginConfigServiceImpl extends TBaseServiceImpl<TPluginConfigRecord, TPluginConfig, Long> implements TPluginConfigService {

	@Autowired
	private TPluginConfigDao pluginConfigDao;

	@Transactional(readOnly = true)
	public boolean pluginIdExists(String pluginId) {
		return pluginConfigDao.pluginIdExists(pluginId);
	}

	@Transactional(readOnly = true)
	@Cacheable("pluginConfig")
	public TPluginConfig findByPluginId(String pluginId) {
		return pluginConfigDao.findByPluginId(pluginId);
	}

	@CacheEvict(value = "pluginConfig", allEntries = true)
	public void deleteByPluginId(String pluginId) {
		TPluginConfig pluginConfig = pluginConfigDao.findByPluginId(pluginId);
		pluginConfigDao.delete(pluginConfig);
	}

	@Override
	@Transactional
	@CacheEvict(value = "pluginConfig", allEntries = true)
	public TPluginConfig save(TPluginConfig pluginConfig) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		pluginConfig.setCreateDate(now);
		pluginConfig.setModifyDate(now);
		pluginConfig.setVersion(0L);
		return super.save(pluginConfig);
	}

	@Override
	@Transactional
	@CacheEvict(value = "pluginConfig", allEntries = true)
	public TPluginConfig update(TPluginConfig pluginConfig) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		pluginConfig.setModifyDate(now);
		return super.update(pluginConfig);
	}

	@Override
	@Transactional
	@CacheEvict(value = "pluginConfig", allEntries = true)
	public TPluginConfig update(TPluginConfig pluginConfig, String... ignoreProperties) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		pluginConfig.setModifyDate(now);
		return super.update(pluginConfig, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "pluginConfig", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "pluginConfig", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "pluginConfig", allEntries = true)
	public void delete(TPluginConfig pluginConfig) {
		super.delete(pluginConfig);
	}

}