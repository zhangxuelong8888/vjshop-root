package com.vjshop.service.impl;

import com.vjshop.Principal;
import com.vjshop.dao.TAdminDao;
import com.vjshop.entity.TAdmin;
import com.vjshop.entity.TAdminRole;
import com.vjshop.entity.TRole;
import com.vjshop.dao.TAdminRoleDao;
import com.vjshop.generated.db.tables.records.TAdminRecord;
import com.vjshop.service.TAdminService;
import com.vjshop.service.TRoleService;
import com.vjshop.util.JsonUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Service - 管理员
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tAdminServiceImpl")
public class TAdminServiceImpl extends TBaseServiceImpl<TAdminRecord, TAdmin, Long> implements TAdminService {

	@Autowired
	private TAdminDao tAdminDao;

	@Autowired
	private TAdminRoleDao tAdminRoleDao;

	@Autowired
	private TRoleService tRoleService;

	@Transactional(readOnly = true)
	public boolean usernameExists(String username) {
		return tAdminDao.usernameExists(username);
	}

	@Transactional(readOnly = true)
	public TAdmin findByUsername(String username) {
		return tAdminDao.findByUsername(username);
	}

	@Transactional(readOnly = true)
	public List<String> findAuthorities(Long id) {
		List<String> authorities = new ArrayList<String>();
		List<TRole> tRoles = tRoleService.findRolesByAdminId(id);
		tRoles.forEach(tRole -> {
			if (tRole != null && StringUtils.isNotEmpty(tRole.getAuthorities())) {
				authorities.addAll(JsonUtils.toObject(tRole.getAuthorities(), List.class));
			}
		});
		return authorities;
	}

	@Transactional(readOnly = true)
	public boolean isAuthenticated() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			return subject.isAuthenticated();
		}
		return false;
	}

	@Transactional(readOnly = true)
	public TAdmin getCurrent() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			Principal principal = (Principal) subject.getPrincipal();
			if (principal != null) {
				return tAdminDao.find(principal.getId());
			}
		}
		return null;
	}

	@Transactional(readOnly = true)
	public String getCurrentUsername() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			Principal principal = (Principal) subject.getPrincipal();
			if (principal != null) {
				return principal.getUsername();
			}
		}
		return null;
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "loginToken")
	public String getLoginToken() {
		return DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30));
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public TAdmin save(TAdmin admin) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		admin.setCreateDate(now);
		admin.setModifyDate(now);
		admin.setLockKey(DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
		admin.setVersion(0L);
		TAdmin tAdmin = tAdminDao.insertAndFetch(admin);
		admin.setId(tAdmin.getId());
		saveAdminRoles(admin);
		return admin;
	}

	private void saveAdminRoles(TAdmin admin) {
		tAdminRoleDao.deleteByAdminId(admin.getId());
		Set<TRole> rules = admin.getRules();
		List<TAdminRole> tAdminRoles = new ArrayList<>();
		rules.forEach(rule -> {
			TAdminRole t = new TAdminRole();
			t.setAdmins(admin.getId());
			t.setRoles(rule.getId());
			tAdminRoles.add(t);
		});
		tAdminRoleDao.insert(tAdminRoles);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public TAdmin update(TAdmin admin) {
		return super.update(admin);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public TAdmin update(TAdmin admin, String... ignoreProperties) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		admin.setModifyDate(now);
		saveAdminRoles(admin);
		return super.update(admin, ignoreProperties);
	}


	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		tAdminRoleDao.deleteByAdminId(ids);
		super.delete(ids);
	}

}