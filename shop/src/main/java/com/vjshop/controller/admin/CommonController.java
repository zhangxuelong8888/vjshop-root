
package com.vjshop.controller.admin;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vjshop.entity.TArea;
import com.vjshop.entity.TOrder;
import com.vjshop.service.TAreaService;
import com.vjshop.service.CaptchaService;
import com.vjshop.service.TGoodsService;
import com.vjshop.service.TMemberService;
import com.vjshop.service.TMessageService;
import com.vjshop.service.TOrderService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

/**
 * Controller - 共用
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminCommonController")
@RequestMapping("/admin/common")
public class CommonController implements ServletContextAware {

	@Value("${system.name}")
	private String systemName;
	@Value("${system.version}")
	private String systemVersion;
	@Value("${system.description}")
	private String systemDescription;
	@Autowired
	private TAreaService areaService;
	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;
	@Autowired
	private TOrderService orderService;
	@Autowired
	private TGoodsService goodsService;
	@Autowired
	private TMemberService memberService;
	@Autowired
	private TMessageService messageService;

	/** ServletContext */
	private ServletContext servletContext;

	/**
	 * 设置ServletContext
	 * 
	 * @param servletContext
	 *            ServletContext
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	/**
	 * 主页
	 */
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String main() {
		return "/admin/common/main";
	}

	/**
	 * 首页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("systemName", systemName);
		model.addAttribute("systemVersion", systemVersion);
		model.addAttribute("systemDescription", systemDescription);
		model.addAttribute("javaVersion", System.getProperty("java.version"));
		model.addAttribute("javaHome", System.getProperty("java.home"));
		model.addAttribute("osName", System.getProperty("os.name"));
		model.addAttribute("osArch", System.getProperty("os.arch"));
		model.addAttribute("serverInfo", servletContext.getServerInfo());
		model.addAttribute("servletVersion", servletContext.getMajorVersion() + "." + servletContext.getMinorVersion());
		model.addAttribute("pendingReviewOrderCount", orderService.count(null, TOrder.Status.pendingReview, null, null, null, null, null, null, null, null));
		model.addAttribute("pendingShipmentOrderCount", orderService.count(null, TOrder.Status.pendingShipment, null, null, null, null, null, null, null, null));
		model.addAttribute("pendingReceiveOrderCount", orderService.count(null, null, null, null, true, null, null, null, null, null));
		model.addAttribute("pendingRefundsOrderCount", orderService.count(null, null, null, null, null, true, null, null, null, null));
		model.addAttribute("marketableProductCount", goodsService.count(null, null, true, null, null, null, null));
		model.addAttribute("notMarketableProductCount", goodsService.count(null, null, false, null, null, null, null));
		model.addAttribute("stockAlertProductCount", goodsService.count(null, null, null, null, null, null, true));
		model.addAttribute("outOfStockProductCount", goodsService.count(null, null, null, null, null, true, null));
		model.addAttribute("memberCount", memberService.count());
		model.addAttribute("unreadMessageCount", messageService.count(null, false));
		return "/admin/common/index";
	}

	/**
	 * 地区
	 */
	@RequestMapping(value = "/area", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> area(Long parentId) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		TArea parent = areaService.find(parentId);
		Collection<TArea> areas = parent != null ? areaService.findChildren(parent, false, null) : areaService.findRoots();
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
		return "/admin/common/error";
	}

	/**
	 * 权限错误
	 */
	@RequestMapping("/unauthorized")
	public String unauthorized(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String requestType = request.getHeader("X-Requested-With");
		if (requestType != null && requestType.equalsIgnoreCase("XMLHttpRequest")) {
			response.addHeader("loginStatus", "unauthorized");
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		return "/admin/common/unauthorized";
	}

}