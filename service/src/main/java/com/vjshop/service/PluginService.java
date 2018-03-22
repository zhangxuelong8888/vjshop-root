
package com.vjshop.service;

import java.util.List;

import com.vjshop.plugin.LoginPlugin;
import com.vjshop.plugin.PaymentPlugin;
import com.vjshop.plugin.StoragePlugin;

/**
 * Service - 插件
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface PluginService {

	/**
	 * 获取支付插件
	 * 
	 * @return 支付插件
	 */
	List<PaymentPlugin> getPaymentPlugins();

	/**
	 * 获取存储插件
	 * 
	 * @return 存储插件
	 */
	List<StoragePlugin> getStoragePlugins();

	/**
	 * 获取登录插件
	 * 
	 * @return 登录插件
	 */
	List<LoginPlugin> getLoginPlugins();

	/**
	 * 获取支付插件
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @return 支付插件
	 */
	List<PaymentPlugin> getPaymentPlugins(boolean isEnabled);

	/**
	 * 获取存储插件
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @return 存储插件
	 */
	List<StoragePlugin> getStoragePlugins(boolean isEnabled);

	/**
	 * 获取登录插件
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @return 登录插件
	 */
	List<LoginPlugin> getLoginPlugins(boolean isEnabled);

	/**
	 * 获取支付插件
	 * 
	 * @param id
	 *            ID
	 * @return 支付插件
	 */
	PaymentPlugin getPaymentPlugin(String id);

	/**
	 * 获取存储插件
	 * 
	 * @param id
	 *            ID
	 * @return 存储插件
	 */
	StoragePlugin getStoragePlugin(String id);

	/**
	 * 获取登录插件
	 * 
	 * @param id
	 *            ID
	 * @return 登录插件
	 */
	LoginPlugin getLoginPlugin(String id);

}