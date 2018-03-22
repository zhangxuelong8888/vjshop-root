
package com.vjshop.service.impl;

import com.vjshop.dao.TAreaDao;
import com.vjshop.entity.TArea;
import com.vjshop.generated.db.tables.records.TAreaRecord;
import com.vjshop.service.TAreaService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

/**
 * Service - 地区
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TAreaServiceImpl extends TBaseServiceImpl<TAreaRecord, TArea, Long> implements TAreaService {

	@Resource
	private TAreaDao tAreaDao;

	@Transactional(readOnly = true)
	public List<TArea> findRoots() {
		return tAreaDao.findRoots(null);
	}

	@Transactional(readOnly = true)
	public List<TArea> findRoots(Integer count) {
		return tAreaDao.findRoots(count);
	}

	@Transactional(readOnly = true)
	public List<TArea> findParents(TArea area, boolean recursive, Integer count) {
		return tAreaDao.findParents(area, recursive, count);
	}

	@Transactional(readOnly = true)
	public List<TArea> findChildren(TArea area, boolean recursive, Integer count) {
		return tAreaDao.findChildren(area, recursive, count);
	}

	@Override
	@Transactional
	@CacheEvict(value = "area", allEntries = true)
	public TArea save(TArea area) {
		Assert.notNull(area);

		setValue(area);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		area.setModifyDate(timestamp);
		if (area.getId() == null){
			area.setCreateDate(timestamp);
			area.setVersion(0L);
		}
		return super.save(area);
	}

	@Override
	@Transactional
	@CacheEvict(value = "area", allEntries = true)
	public TArea update(TArea area) {
		Assert.notNull(area);

		setValue(area);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		area.setModifyDate(timestamp);
		return tAreaDao.update(area, null);
	}

	/**
	 * 设置值
	 * 
	 * @param area
	 *            地区
	 */
	private void setValue(TArea area) {
		if (area == null) {
			return;
		}
		TArea parent = tAreaDao.find(area.getParent());
		if (parent != null) {
			area.setFullName(parent.getFullName() + area.getName());
			area.setTreePath(parent.getTreePath() + parent.getId() + TArea.TREE_PATH_SEPARATOR);
		} else {
			area.setFullName(area.getName());
			area.setTreePath(TArea.TREE_PATH_SEPARATOR);
		}
		area.setGrade(area.getParentIds().length);
	}

}