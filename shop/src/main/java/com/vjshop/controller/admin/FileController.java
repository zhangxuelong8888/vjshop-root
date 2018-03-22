
package com.vjshop.controller.admin;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.vjshop.FileType;
import com.vjshop.Message;
import com.vjshop.service.FileService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller - 文件
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminFileController")
@RequestMapping("/admin/file")
public class FileController extends BaseController {

	@Resource(name = "fileServiceImpl")
	private FileService fileService;

	/**
	 * 上传
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> upload(FileType fileType, MultipartFile file) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (fileType == null || file == null || file.isEmpty()) {
			data.put("message", ERROR_MESSAGE);
			data.put("state", message("admin.message.error"));
			return data;
		}
		if (!fileService.isValid(fileType, file)) {
			data.put("message", Message.warn("admin.upload.invalid"));
			data.put("state", message("admin.upload.invalid"));
			return data;
		}
		String url = fileService.upload(fileType, file, false);
		if (StringUtils.isEmpty(url)) {
			data.put("message", Message.warn("admin.upload.error"));
			data.put("state", message("admin.upload.error"));
			return data;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("state", "SUCCESS");
		data.put("url", url);
		return data;
	}

}