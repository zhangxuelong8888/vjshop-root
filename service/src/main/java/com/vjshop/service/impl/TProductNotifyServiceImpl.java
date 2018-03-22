
package com.vjshop.service.impl;

import com.vjshop.Filter;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TProductNotifyDao;
import com.vjshop.entity.TProductNotify;
import com.vjshop.generated.db.tables.records.TProductNotifyRecord;
import com.vjshop.service.MailService;
import com.vjshop.service.TMemberService;
import com.vjshop.service.TProductNotifyService;
import com.vjshop.service.TProductService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Service - 到货通知
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tProductNotifyServiceImpl")
public class TProductNotifyServiceImpl extends TBaseServiceImpl<TProductNotifyRecord,TProductNotify, Long> implements TProductNotifyService {

	@Resource(name = "mailServiceImpl")
	private MailService mailService;

	@Autowired
	private TProductNotifyDao tProductNotifyDao;
	@Autowired
	private TProductService tProductService;
	@Autowired
	private TMemberService tMemberService;

	@Transactional(readOnly = true)
	public Page<TProductNotify> findPage(Long memberId, Boolean isMarketable, Boolean isOutOfStock, Boolean hasSent, Pageable pageable) {
		Page<TProductNotify> tProductNotifyPage = this.tProductNotifyDao.findPage(memberId, isMarketable, isOutOfStock, hasSent, pageable);
		List<TProductNotify> newConteng = new ArrayList<>(tProductNotifyPage.getContent().size());
		for(TProductNotify tProductNotify : tProductNotifyPage.getContent()){
			tProductNotify.setMemberVO(this.tMemberService.find(tProductNotify.getMember()));
			tProductNotify.setProductVO(this.tProductService.findDetailById(tProductNotify.getProduct()));
			newConteng.add(tProductNotify);
		}
		return new Page<>(newConteng,tProductNotifyPage.getTotal(),pageable);
	}

	@Override
	public Long count(Long memberId, Boolean isMarketable, Boolean isOutOfStock, Boolean hasSent) {
		return tProductNotifyDao.count(memberId, isMarketable, isOutOfStock, hasSent);
	}

	public int send(List<TProductNotify> productNotifies) {
		if (CollectionUtils.isEmpty(productNotifies)) {
			return 0;
		}

		int count = 0;
		for (TProductNotify productNotify : productNotifies) {
			if (productNotify != null && StringUtils.isNotEmpty(productNotify.getEmail())) {
				mailService.sendProductNotifyMail(productNotify);
				productNotify.setHasSent(true);
				this.tProductNotifyDao.update(productNotify);
				count++;
			}
		}
		return count;
	}

	@Override
	public boolean exists(Long productId, String email) {
		return tProductNotifyDao.exists(productId, email);
	}

	@Override
	public List<TProductNotify> findByMemberId(Long memberId) {
		List<Filter> filters = new ArrayList<Filter>(1);
		filters.add(new Filter());
		return this.tProductNotifyDao.findByMemberId(memberId);
	}

}