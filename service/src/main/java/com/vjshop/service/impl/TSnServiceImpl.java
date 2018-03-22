
package com.vjshop.service.impl;

import com.vjshop.dao.TSnDao;
import com.vjshop.entity.TSn;
import com.vjshop.service.TSnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 序列号
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tSnServiceImpl")
public class TSnServiceImpl implements TSnService {

	@Autowired
	private TSnDao tSnDao;

	@Transactional
	public String generate(TSn.Type type) {
		return tSnDao.generate(type);
	}

}