
package com.vjshop.plugin.ftpStorage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import com.vjshop.entity.TPluginConfig;
import com.vjshop.plugin.StoragePlugin;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Component;

/**
 * Plugin - FTP存储
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Component("ftpStoragePlugin")
public class FtpStoragePlugin extends StoragePlugin {

	@Override
	public String getName() {
		return "FTP存储";
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
		return "ftp_storage/install.jhtml";
	}

	@Override
	public String getUninstallUrl() {
		return "ftp_storage/uninstall.jhtml";
	}

	@Override
	public String getSettingUrl() {
		return "ftp_storage/setting.jhtml";
	}

	@Override
	public void upload(String path, File file, String contentType) {
		TPluginConfig pluginConfig = getPluginConfig();
		if (pluginConfig != null) {
			String host = pluginConfig.getAttributesMap("host");
			Integer port = Integer.valueOf(pluginConfig.getAttributesMap("port"));
			String username = pluginConfig.getAttributesMap("username");
			String password = pluginConfig.getAttributesMap("password");
			FTPClient ftpClient = new FTPClient();
			InputStream inputStream = null;
			try {
				inputStream = new BufferedInputStream(new FileInputStream(file));
				ftpClient.connect(host, port);
				ftpClient.login(username, password);
				ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				ftpClient.enterLocalPassiveMode();
				if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
					String directory = StringUtils.substringBeforeLast(path, "/");
					String filename = StringUtils.substringAfterLast(path, "/");
					if (!ftpClient.changeWorkingDirectory(directory)) {
						String[] paths = StringUtils.split(directory, "/");
						String p = "/";
						ftpClient.changeWorkingDirectory(p);
						for (String s : paths) {
							p += s + "/";
							if (!ftpClient.changeWorkingDirectory(p)) {
								ftpClient.makeDirectory(s);
								ftpClient.changeWorkingDirectory(p);
							}
						}
					}
					ftpClient.storeFile(filename, inputStream);
					ftpClient.logout();
				}
			} catch (SocketException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} finally {
				IOUtils.closeQuietly(inputStream);
				try {
					if (ftpClient.isConnected()) {
						ftpClient.disconnect();
					}
				} catch (IOException e) {
				}
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