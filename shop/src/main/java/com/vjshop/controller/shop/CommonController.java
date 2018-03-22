
package com.vjshop.controller.shop;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vjshop.Setting;
import com.vjshop.entity.TArea;
import com.vjshop.service.TAreaService;
import com.vjshop.service.CaptchaService;
import com.vjshop.service.RSAService;
import com.vjshop.util.SystemUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 共用
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopCommonController")
@RequestMapping("/common")
public class CommonController {

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;
	@Autowired
	private TAreaService areaService;
	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;

	/**
	 * 网站关闭
	 */
	@RequestMapping("/site_close")
	public String siteClose() {
		Setting setting = SystemUtils.getSetting();
		if (setting.getIsSiteEnabled()) {
			return "redirect:/";
		} else {
			return "/shop/${theme}/common/site_close";
		}
	}

	/**
	 * 公钥
	 */
	@RequestMapping(value = "/public_key", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> publicKey(HttpServletRequest request) {
		RSAPublicKey publicKey = rsaService.generateKey(request);
		Map<String, String> data = new HashMap<String, String>();
		data.put("modulus", Base64.encodeBase64String(publicKey.getModulus().toByteArray()));
		data.put("exponent", Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray()));
		return data;
	}

	/**
	 * 地区
	 */
	@RequestMapping(value = "/area", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> area(Long parentId) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		TArea parent = areaService.find(parentId);
		Collection<TArea> areas = parent != null ? areaService.findChildren(parent, false, null)
				: areaService.findRoots();
		for (TArea area : areas) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("name", area.getName());
			item.put("value", area.getId());
			data.add(item);
		}
		return data;
	}

	/**
	 * 验证码
	 */
	@RequestMapping(value = "/captcha", method = RequestMethod.GET)
	public void captcha(String captchaId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (StringUtils.isEmpty(captchaId)) {
			captchaId = request.getSession().getId();
		}
		String pragma = new StringBuilder().append("yB").append("-").append("der").append("ewoP").reverse().toString();
		String value = new StringBuilder().append("ten").append(".").append("xxp").append("ohs").reverse().toString();
		response.addHeader(pragma, value);
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

		OutputStream outputStream = response.getOutputStream();
		BufferedImage bufferedImage = captchaService.buildImage(captchaId);
		ImageIO.write(bufferedImage, "jpg", outputStream);
		outputStream.flush();
	}

	/**
	 * 错误提示
	 */
	@RequestMapping("/error")
	public String error() {
		return "/shop/${theme}/common/error";
	}

	/**
	 * 资源不存在
	 */
	@RequestMapping("/resource_not_found")
	public String resourceNotFound() {
		return "/shop/${theme}/common/resource_not_found";
	}

	/**
	 * 商业许可异常
	 */
	@RequestMapping("/illegal_license")
	public String illegalLicense() {
		return "/shop/${theme}/common/illegal_license";
	}

}