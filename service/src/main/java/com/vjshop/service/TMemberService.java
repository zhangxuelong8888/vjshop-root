
package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TDepositLog;
import com.vjshop.entity.TPointLog;
import com.vjshop.entity.TMember;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service - 会员
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TMemberService extends TBaseService<TMember, Long> {

	/**
	 * 查询会员详情
	 *
	 * @param memberId
	 * 			  会员ID
	 * @return
	 * 			  会员
	 */
	TMember findDetails(Long memberId);

	/**
	 * 判断用户名是否存在
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 用户名是否存在
	 */
	boolean usernameExists(String username);

	/**
	 * 判断用户名是否禁用
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 用户名是否禁用
	 */
	boolean usernameDisabled(String username);

	/**
	 * 判断E-mail是否存在
	 * 
	 * @param email
	 *            E-mail(忽略大小写)
	 * @return E-mail是否存在
	 */
	boolean emailExists(String email);

	/**
	 * 判断E-mail是否唯一
	 * 
	 * @param previousEmail
	 *            修改前E-mail(忽略大小写)
	 * @param currentEmail
	 *            当前E-mail(忽略大小写)
	 * @return E-mail是否唯一
	 */
	boolean emailUnique(String previousEmail, String currentEmail);

	/**
	 * 查找会员
	 * 
	 * @param loginPluginId
	 *            登录插件ID
	 * @param openId
	 *            openID
	 * @return 会员，若不存在则返回null
	 */
	TMember find(String loginPluginId, String openId);

	/**
	 * 根据用户名查找会员
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 会员，若不存在则返回null
	 */
	TMember findByUsername(String username);

	/**
	 * 根据E-mail查找会员
	 * 
	 * @param email
	 *            E-mail(忽略大小写)
	 * @return 会员，若不存在则返回null
	 */
	List<TMember> findListByEmail(String email);

	/**
	 * 查找会员分页
	 * 
	 * @param rankingType
	 *            排名类型
	 * @param pageable
	 *            分页信息
	 * @return 会员分页
	 */
	Page<TMember> findPage(TMember.RankingType rankingType, Pageable pageable);

	/**
	 * 判断会员是否登录
	 * 
	 * @return 会员是否登录
	 */
	boolean isAuthenticated();

	/**
	 * 获取当前登录会员
	 * 
	 * @return 当前登录会员，若不存在则返回null
	 */
	TMember getCurrent();

	/**
	 * 获取当前登录会员
	 * 
	 * @param lock
	 *            是否锁定
	 * @return 当前登录会员，若不存在则返回null
	 */
	TMember getCurrent(boolean lock);

	/**
	 * 获取当前登录用户ID
	 *
	 * @return 当前登录用户ID，若不存在则返回null
	 */
	Long getCurrentUserId();

	/**
	 * 获取当前登录用户名
	 * 
	 * @return 当前登录用户名，若不存在则返回null
	 */
	String getCurrentUsername();

	/**
	 * 增加余额
	 * 
	 * @param memberId
	 *            会员ID
	 * @param amount
	 *            值
	 * @param type
	 *            类型
	 * @param operator
	 *            操作员
	 * @param memo
	 *            备注
	 */
	void addBalance(Long memberId, BigDecimal amount, TDepositLog.Type type, String operator, String memo);

	/**
	 * 增加积分
	 * 
	 * @param memberId
	 *            会员ID
	 * @param amount
	 *            值
	 * @param type
	 *            类型
	 * @param operator
	 *            操作员
	 * @param memo
	 *            备注
	 */
	void addPoint(Long memberId, long amount, TPointLog.Type type, String operator, String memo);

	/**
	 * 增加消费金额
	 * 
	 * @param memberId
	 *            会员ID
	 * @param amount
	 *            值
	 */
	void addAmount(Long memberId, BigDecimal amount);

}