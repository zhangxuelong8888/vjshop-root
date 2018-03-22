
package com.vjshop.plugin.ossStorage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.vjshop.entity.TPluginConfig;
import com.vjshop.plugin.StoragePlugin;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;

/**
 * Plugin - 阿里云存储
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Component("ossStoragePlugin")
public class OssStoragePlugin extends StoragePlugin {

	@Override
	public String getName() {
		return "阿里云存储";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "VJSHOP";
	}

	@Override
	public String getSiteUrl() {
		return "http://www.vjshop.com";
	}

	@Override
	public String getInstallUrl() {
		return "oss_storage/install.jhtml";
	}

	@Override
	public String getUninstallUrl() {
		return "oss_storage/uninstall.jhtml";
	}

	@Override
	public String getSettingUrl() {
		return "oss_storage/setting.jhtml";
	}

	@Override
	public void upload(String path, File file, String contentType) {
		TPluginConfig pluginConfig = getPluginConfig();
		if (pluginConfig != null) {
			String endpoint = pluginConfig.getAttributesMap("endpoint");
			String accessId = pluginConfig.getAttributesMap("accessId");
			String accessKey = pluginConfig.getAttributesMap("accessKey");
			String bucketName = pluginConfig.getAttributesMap("bucketName");
			InputStream inputStream = null;
			try {
				inputStream = new BufferedInputStream(new FileInputStream(file));
				OSSClient ossClient = new OSSClient(endpoint, accessId, accessKey);
				ObjectMetadata objectMetadata = new ObjectMetadata();
				objectMetadata.setContentType(contentType);
				objectMetadata.setContentLength(file.length());
				ossClient.putObject(bucketName, StringUtils.removeStart(path, "/"), inputStream, objectMetadata);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e.getMessage(), e);
			} finally {
				IOUtils.closeQuietly(inputStream);
			}
		}
	}

	@Override
	public String getUrl(String path) {
		TPluginConfig pluginConfig = getPluginConfig();
		if (pluginConfig != null) {
			String urlPrefix = pluginConfig.getAttributesMap("urlPrefix");
			return urlPrefix + path;
		}
		return null;
	}

}