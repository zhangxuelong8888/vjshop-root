
package com.vjshop.service.impl;

import com.vjshop.CommonAttributes;
import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.dao.*;
import com.vjshop.entity.TArea;
import com.vjshop.entity.TMember;
import com.vjshop.entity.TMemberAttribute;
import com.vjshop.generated.db.tables.records.TMemberAttributeRecord;
import com.vjshop.service.TMemberAttributeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Service - 会员注册项
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TMemberAttributeServiceImpl extends TBaseServiceImpl<TMemberAttributeRecord, TMemberAttribute, Long>
		implements TMemberAttributeService {

	@Autowired
	private TMemberAttributeDao tMemberAttributeDao;
	@Autowired
	private TMemberDao tMemberDao;
	@Autowired
	private TAreaDao tAreaDao;

	@Transactional(readOnly = true)
	public Integer findUnusedPropertyIndex() {
		return tMemberAttributeDao.findUnusedPropertyIndex();
	}

	@Transactional(readOnly = true)
	public List<TMemberAttribute> findList(Boolean isEnabled, Integer count, List<Filter> filters, List<Order> orders) {
		return tMemberAttributeDao.findList(isEnabled, count, filters, orders);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "memberAttribute", condition = "#useCache")
	public List<TMemberAttribute> findList(Boolean isEnabled, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		return tMemberAttributeDao.findList(isEnabled, count, filters, orders);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "memberAttribute", condition = "#useCache")
	public List<TMemberAttribute> findList(Boolean isEnabled, boolean useCache) {
		return tMemberAttributeDao.findList(isEnabled, null, null, null);
	}

	@Transactional(readOnly = true)
	public boolean isValid(TMemberAttribute memberAttribute, String[] values) {
		Assert.notNull(memberAttribute);
		Assert.notNull(memberAttribute.getType());

		String value = ArrayUtils.isNotEmpty(values) ? values[0].trim() : null;
		switch (memberAttribute.getEnumType()) {
			case name:
			case address:
			case zipCode:
			case phone:
			case mobile:
			case text:
				if (memberAttribute.getIsRequired() && StringUtils.isEmpty(value)) {
					return false;
				}
				if (StringUtils.isNotEmpty(memberAttribute.getPattern()) && StringUtils.isNotEmpty(value) && !Pattern.compile(memberAttribute.getPattern()).matcher(value).matches()) {
					return false;
				}
				break;
			case gender:
				if (memberAttribute.getIsRequired() && StringUtils.isEmpty(value)) {
					return false;
				}
				if (StringUtils.isNotEmpty(value)) {
					int v = Integer.parseInt(value);
					if (v < 0 || v >= TMember.Gender.values().length){
						return false;
					}
				}
				break;
			case birth:
				if (memberAttribute.getIsRequired() && StringUtils.isEmpty(value)) {
					return false;
				}
				if (StringUtils.isNotEmpty(value)) {
					try {
						DateUtils.parseDate(value, CommonAttributes.DATE_PATTERNS);
					} catch (ParseException e) {
						return false;
					}
				}
				break;
			case area:
				Long id = NumberUtils.toLong(value, -1L);
				TArea area = tAreaDao.find(id);
				if (memberAttribute.getIsRequired() && area == null) {
					return false;
				}
				break;
			case select:
				if (memberAttribute.getIsRequired() && StringUtils.isEmpty(value)) {
					return false;
				}
				if (CollectionUtils.isEmpty(memberAttribute.getOptionList())) {
					return false;
				}
				if (StringUtils.isNotEmpty(value) && !memberAttribute.getOptions().contains(value)) {
					return false;
				}
				break;
			case checkbox:
				if (memberAttribute.getIsRequired() && ArrayUtils.isEmpty(values)) {
					return false;
				}
				if (CollectionUtils.isEmpty(memberAttribute.getOptionList())) {
					return false;
				}
				if (ArrayUtils.isNotEmpty(values) && !memberAttribute.getOptionList().containsAll(Arrays.asList(values))) {
					return false;
				}
				break;
			}
		return true;
	}

	@Transactional(readOnly = true)
	public Object toMemberAttributeValue(TMemberAttribute memberAttribute, String[] values) {
		Assert.notNull(memberAttribute);
		Assert.notNull(memberAttribute.getType());

		if (ArrayUtils.isEmpty(values)) {
			return null;
		}

		String value = values[0].trim();
		switch (memberAttribute.getEnumType()) {
		case name:
		case address:
		case zipCode:
		case phone:
		case mobile:
		case text:
			return StringUtils.isNotEmpty(value) ? value : null;
		case gender:
			if (StringUtils.isEmpty(value)) {
				return null;
			}
			int v = Integer.parseInt(value);
			if (v >= 0 && v < TMember.Gender.values().length){
				return v;
			}
		case birth:
			if (StringUtils.isEmpty(value)) {
				return null;
			}
			try {
				return DateUtils.parseDate(value, CommonAttributes.DATE_PATTERNS);
			} catch (ParseException e) {
				return null;
			}
		case area:
			Long id = NumberUtils.toLong(value, -1L);
			return tAreaDao.find(id);
		case select:
			if (CollectionUtils.isNotEmpty(memberAttribute.getOptionList()) && memberAttribute.getOptionList().contains(value)) {
				return value;
			}
			break;
		case checkbox:
			if (CollectionUtils.isNotEmpty(memberAttribute.getOptionList()) && memberAttribute.getOptionList().containsAll(Arrays.asList(values))) {
				return Arrays.asList(values);
			}
			break;
		}
		return null;
	}

	@Override
	@Transactional
	@CacheEvict(value = "memberAttribute", allEntries = true)
	public TMemberAttribute save(TMemberAttribute memberAttribute) {
		Assert.notNull(memberAttribute);

		Integer unusedPropertyIndex = tMemberAttributeDao.findUnusedPropertyIndex();
		Assert.notNull(unusedPropertyIndex);

		memberAttribute.setPropertyIndex(unusedPropertyIndex);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		memberAttribute.setModifyDate(now);
		if (memberAttribute.getId() == null){
			memberAttribute.setCreateDate(now);
			memberAttribute.setVersion(0L);
		}
		return super.save(memberAttribute);
	}

	@Override
	@Transactional
	@CacheEvict(value = "memberAttribute", allEntries = true)
	public TMemberAttribute update(TMemberAttribute memberAttribute) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		memberAttribute.setModifyDate(now);
		return tMemberAttributeDao.update(memberAttribute, null);
	}

	@Override
	@Transactional
	@CacheEvict(value = "memberAttribute", allEntries = true)
	public void delete(Long memberAttributeId) {
		TMemberAttribute memberAttribute = tMemberAttributeDao.find(memberAttributeId);
		if (memberAttribute == null) {
			return;
		}
		tMemberAttributeDao.deleteById(memberAttributeId);
		tMemberDao.clearAttributeValue(memberAttribute);
	}

}