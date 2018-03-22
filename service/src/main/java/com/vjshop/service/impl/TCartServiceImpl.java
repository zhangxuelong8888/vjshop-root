
package com.vjshop.service.impl;

import com.vjshop.dao.TCartDao;
import com.vjshop.dao.TCartItemDao;
import com.vjshop.entity.*;
import com.vjshop.generated.db.tables.records.TCartRecord;
import com.vjshop.service.TCartService;
import com.vjshop.service.TMemberRankService;
import com.vjshop.service.TMemberService;
import com.vjshop.util.WebUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

/**
 * Service - 购物车
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TCartServiceImpl extends TBaseServiceImpl<TCartRecord, TCart, Long> implements TCartService {

	@Autowired
	private TCartDao tCartDao;
	@Autowired
	private TCartItemDao tCartItemDao;
	@Autowired
	private TMemberService tMemberService;
	@Autowired
	private TMemberRankService tMemberRankService;

	/**
	 * 获取当前已选中商品购物车
	 *
	 * @return 当前购物车，若不存在则返回null
	 */
	@Override
	public TCart getCurrentSelected() {
		TCart cart = null;
		TMember member = tMemberService.getCurrent();
		if (member != null && member.getId() != null) {
			cart = tCartDao.findByMemberId(member.getId());
			if (cart != null) {
				HashSet<TCartItem> cartItemSet = new HashSet<TCartItem>(tCartItemDao.findListBySelected(cart.getId()));
				if (member.getMemberRank() != null){
					member.setMemberRankInfo(tMemberRankService.find(member.getMemberRank()));
					cart.setMemberVO(member);
					for (TCartItem cartItem : cartItemSet){
						cartItem.setCartVO(cart);
					}
				}
				Timestamp expire = new Timestamp(DateUtils.addSeconds(new Date(), TCart.TIMEOUT).getTime());
				if (!DateUtils.isSameDay(cart.getExpire(), expire)) {
					cart.setExpire(expire);
				}
				cart.setCartItems(cartItemSet);
			}
		}

		return cart;
	}

	/**
	 * 更新购物车item为已选中
	 *
	 * @param cartItemId 购物车项id
	 * @param isSelected 是否选中
	 */
	@Override
	public void updateSelectStatus(Long cartItemId, Boolean isSelected) {
		Assert.notNull(cartItemId);
		Assert.notNull(isSelected);
		TMember member = tMemberService.getCurrent();
		if (member != null && member.getId() != null) {
			TCart cart = tCartDao.findByMemberId(member.getId());
			if (cart != null) {
				tCartItemDao.updateSelectedStatus(new Long[]{cartItemId}, cart.getId(), isSelected);
			}
		}
	}

	@Override
	public void updateSelectStatus(String cartItemIds, Boolean isSelected) {
		String[] split = cartItemIds.split(",");
		Assert.noNullElements(split);
		Assert.notNull(isSelected);

		Long[] ids = new Long[split.length];
		for (int i = 0; i < split.length; i++) {
			ids[i] = Long.valueOf(split[i]);
		}
		TMember member = tMemberService.getCurrent();
		if (member != null && member.getId() != null) {
			TCart cart = tCartDao.findByMemberId(member.getId());
			if (cart != null) {
				tCartItemDao.updateSelectedStatus(ids, cart.getId(), isSelected);
			}
		}
	}

	@Transactional(readOnly = true)
	public TCart getCurrent() {
		TCart cart;
		TMember member = tMemberService.getCurrent();
		if (member != null && member.getId() != null) {
			cart = tCartDao.findByMemberId(member.getId());
		} else {
			RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
			HttpServletRequest request = requestAttributes != null ? ((ServletRequestAttributes) requestAttributes).getRequest() : null;
			if (request == null) {
				return null;
			}
			String key = WebUtils.getCookie(request, TCart.KEY_COOKIE_NAME);
			cart = tCartDao.findByKey(key);
		}
		if (cart != null) {
			HashSet<TCartItem> cartItemSet = new HashSet<TCartItem>(tCartItemDao.findList(cart.getId()));
			if (member != null && member.getMemberRank() != null){
				member.setMemberRankInfo(tMemberRankService.find(member.getMemberRank()));
				cart.setMemberVO(member);
				for (TCartItem cartItem : cartItemSet){
					cartItem.setCartVO(cart);
				}
			}
			Timestamp expire = new Timestamp(DateUtils.addSeconds(new Date(), TCart.TIMEOUT).getTime());
			if (!DateUtils.isSameDay(cart.getExpire(), expire)) {
				cart.setExpire(expire);
			}
			cart.setCartItems(cartItemSet);
		}
		return cart;
	}

	@Override
	public TCart findByMemberId(Long memberId) {
		return tCartDao.findByMemberId(memberId);
	}

	@Transactional
	public TCart add(Long productId, int quantity) {
		Assert.notNull(productId);
		Assert.state(quantity > 0);

		TCart cart = getCurrent();
		if (cart == null) {
			cart = new TCart();
			TMember member = tMemberService.getCurrent();
			if (member != null) {
				cart.setMember(member.getId());
			}
			cart = save(cart);
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		if (cart.containsProduct(productId)) {
			TCartItem cartItem = cart.getCartItemByProductId(productId);
			cartItem.add(quantity);
			tCartItemDao.updateSelective(cartItem);
		} else {
			TCartItem cartItem = new TCartItem();
			cartItem.setQuantity(quantity);
			cartItem.setProduct(productId);
			cartItem.setCart(cart.getId());
			cartItem.setCreateDate(now);
			cartItem.setModifyDate(now);
			cartItem.setVersion(0L);
			cartItem.setIsSelected(false);
			cartItem = tCartItemDao.insertAndFetch(cartItem);
			cart.getCartItems().add(tCartItemDao.findDetails(cartItem.getId()));
		}
		return cart;
	}

	@Transactional
	public void merge(Long memberId, TCart cart) {
		if (memberId == null || cart == null || cart.getMember() != null) {
			return;
		}
		TCart memberCart = tCartDao.findByMemberId(memberId);
		if (memberCart != null) {
			if (cart.getCartItems() != null) {
				Timestamp now = new Timestamp(System.currentTimeMillis());
				for (TCartItem cartItem : cart.getCartItems()) {
					if (memberCart.containsProduct(cartItem.getProduct())) {
						TCartItem memberCartItem = memberCart.getCartItemByProductId(cartItem.getProduct());
						if (TCartItem.MAX_QUANTITY != null && memberCartItem.getQuantity() + cartItem.getQuantity() > TCartItem.MAX_QUANTITY) {
							continue;
						}
						memberCartItem.add(cartItem.getQuantity());
					} else {
						if (TCart.MAX_CART_ITEM_COUNT != null && memberCart.getCartItems().size() >= TCart.MAX_CART_ITEM_COUNT) {
							continue;
						}
						if (TCartItem.MAX_QUANTITY != null && cartItem.getQuantity() > TCartItem.MAX_QUANTITY) {
							continue;
						}
						TCartItem item = new TCartItem();
						item.setQuantity(cartItem.getQuantity());
						item.setProduct(cartItem.getProduct());
						item.setCart(memberCart.getId());
						item.setCreateDate(now);
						item.setModifyDate(now);
						item.setVersion(0L);
						item.setIsSelected(false);
						item = tCartItemDao.insertAndFetch(item);
						cart.getCartItems().add(item);
					}
				}
			}
			this.deleteCascade(cart.getId());
		} else {
			TCart c = new TCart();
			cart.setId(cart.getId());
			cart.setMember(memberId);
			c.setModifyDate(new Timestamp(System.currentTimeMillis()));
			tCartDao.updateSelective(cart);
		}
	}

	@Override
	@Transactional
	public TCart save(TCart entity) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		entity.setModifyDate(now);
		if (entity.getId() == null){
			entity.setCreateDate(now);
			entity.setVersion(0L);
		}
		entity.setCartKey(DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
		entity.setExpire(new Timestamp(DateUtils.addSeconds(new Date(), TCart.TIMEOUT).getTime()));
		TCart cart = super.save(entity);
		entity.setId(cart.getId());
		return entity;
	}

	@Transactional
	public void evictExpired() {
		while (true) {
			List<TCart> carts = tCartDao.findList(true, 100);
			if (CollectionUtils.isNotEmpty(carts)) {
				for (TCart cart : carts){
					this.deleteCascade(cart.getId());
				}
			}
			if (carts.size() < 100) {
				break;
			}
		}
	}

	@Transactional
	public void deleteCascade(Long cartId){
		Assert.notNull(cartId);

		tCartItemDao.deleteByCartId(cartId);
		tCartDao.deleteById(cartId);

	}

	@Override
	public void deleteSelected(Long cartId) {
		tCartItemDao.deleteSelected(cartId);
	}

}