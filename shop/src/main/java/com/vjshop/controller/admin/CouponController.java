
package com.vjshop.controller.admin;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.vjshop.ExcelView;
import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.TCoupon;
import com.vjshop.entity.TCouponCode;
import com.vjshop.service.TAdminService;
import com.vjshop.service.TCouponCodeService;
import com.vjshop.service.TCouponService;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 优惠券
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminCouponController")
@RequestMapping("/admin/coupon")
public class CouponController extends BaseController {

	@Autowired
	private TCouponService couponService;
	@Autowired
	private TCouponCodeService couponCodeService;
	@Autowired
	private TAdminService adminService;

	/**
	 * 检查价格运算表达式是否正确
	 */
	@RequestMapping(value = "/check_price_expression", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkPriceExpression(String priceExpression) {
		if (StringUtils.isEmpty(priceExpression)) {
			return false;
		}
		return couponService.isValidPriceExpression(priceExpression);
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		return "/admin/coupon/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TCoupon coupon, RedirectAttributes redirectAttributes) {
		if (!isValid(coupon)) {
			return ERROR_VIEW;
		}
		if (coupon.getBeginDate() != null && coupon.getEndDate() != null && coupon.getBeginDate().after(coupon.getEndDate())) {
			return ERROR_VIEW;
		}
		if (coupon.getMinimumQuantity() != null && coupon.getMaximumQuantity() != null && coupon.getMinimumQuantity() > coupon.getMaximumQuantity()) {
			return ERROR_VIEW;
		}
		if (coupon.getMinimumPrice() != null && coupon.getMaximumPrice() != null && coupon.getMinimumPrice().compareTo(coupon.getMaximumPrice()) > 0) {
			return ERROR_VIEW;
		}
		if (StringUtils.isNotEmpty(coupon.getPriceExpression()) && !couponService.isValidPriceExpression(coupon.getPriceExpression())) {
			return ERROR_VIEW;
		}
		if (coupon.getIsExchange() && coupon.getPoint() == null) {
			return ERROR_VIEW;
		}
		if (!coupon.getIsExchange()) {
			coupon.setPoint(null);
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		coupon.setCreateDate(now);
		coupon.setModifyDate(now);
		coupon.setVersion(0L);
		couponService.save(coupon);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("coupon", couponService.find(id));
		return "/admin/coupon/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TCoupon coupon, RedirectAttributes redirectAttributes) {
		if (!isValid(coupon)) {
			return ERROR_VIEW;
		}
		if (coupon.getBeginDate() != null && coupon.getEndDate() != null && coupon.getBeginDate().after(coupon.getEndDate())) {
			return ERROR_VIEW;
		}
		if (coupon.getMinimumQuantity() != null && coupon.getMaximumQuantity() != null && coupon.getMinimumQuantity() > coupon.getMaximumQuantity()) {
			return ERROR_VIEW;
		}
		if (coupon.getMinimumPrice() != null && coupon.getMaximumPrice() != null && coupon.getMinimumPrice().compareTo(coupon.getMaximumPrice()) > 0) {
			return ERROR_VIEW;
		}
		if (StringUtils.isNotEmpty(coupon.getPriceExpression()) && !couponService.isValidPriceExpression(coupon.getPriceExpression())) {
			return ERROR_VIEW;
		}
		if (coupon.getIsExchange() && coupon.getPoint() == null) {
			return ERROR_VIEW;
		}
		if (!coupon.getIsExchange()) {
			coupon.setPoint(null);
		}
		coupon.setModifyDate(new Timestamp(System.currentTimeMillis()));
		couponService.update(coupon, "couponCodes", "promotions", "orders");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", couponService.findPage(pageable));
		return "/admin/coupon/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message delete(Long[] ids) {
		couponService.delete(ids);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 生成优惠码
	 */
	@RequestMapping(value = "/generate", method = RequestMethod.GET)
	public String generate(Long id, ModelMap model) {
		model.addAttribute("coupon", couponService.find(id));
		model.addAttribute("totalCount", couponCodeService.count(id, null, null, null, null));
		model.addAttribute("usedCount", couponCodeService.count(id, null, null, null, true));
		return "/admin/coupon/generate";
	}

	/**
	 * 下载优惠码
	 */
	@RequestMapping(value = "/download", method = RequestMethod.POST)
	public ModelAndView download(Long id, Integer count, ModelMap model) {
		if (count == null || count <= 0) {
			count = 100;
		}
		TCoupon coupon = couponService.find(id);
		List<TCouponCode> data = couponCodeService.generate(id, coupon.getPrefix(), null, count);
		String filename = "coupon_code_" + DateFormatUtils.format(new Date(), "yyyyMM") + ".xls";
		String[] contents = new String[4];
		contents[0] = message("admin.coupon.type") + ": " + coupon.getName();
		contents[1] = message("admin.coupon.count") + ": " + count;
		contents[2] = message("admin.coupon.operator") + ": " + adminService.getCurrentUsername();
		contents[3] = message("admin.coupon.date") + ": " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
		return new ModelAndView(new ExcelView(filename, null, new String[] { "code" }, new String[] { message("admin.coupon.title") }, null, null, data, contents), model);
	}

}