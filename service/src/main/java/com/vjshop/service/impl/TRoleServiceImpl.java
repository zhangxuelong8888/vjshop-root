package com.vjshop.service.impl;

import com.vjshop.dao.TRoleDao;
import com.vjshop.entity.TRole;
import com.vjshop.generated.db.tables.records.TRoleRecord;
import com.vjshop.service.TRoleService;
import com.vjshop.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * Service - 角色
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TRoleServiceImpl extends TBaseServiceImpl<TRoleRecord, TRole, Long> implements TRoleService {

	@Autowired
	private TRoleDao tRoleDao;

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public TRole save(TRole role) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		role.setModifyDate(now);
		role.setVersion(0L);
		role.setAuthorities(JsonUtils.toJson(role.getAuthoritiesList()));
		role.setCreateDate(now);
		return super.save(role);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public TRole update(TRole role) {
		return super.update(role);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public TRole update(TRole role, String... ignoreProperties) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		role.setModifyDate(now);
		role.setAuthorities(JsonUtils.toJson(role.getAuthoritiesList()));
		return super.update(role, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(TRole role) {
		super.delete(role.getId());
	}

	@Override
	public Boolean hasUser(Long id) {
		return tRoleDao.hasUser(id);
	}

	@Override
	public List<TRole> findRolesByAdminId(Long adminId) {
		return tRoleDao.findRolesByAdminId(adminId);
	}
}