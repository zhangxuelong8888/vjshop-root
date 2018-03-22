
package com.vjshop.controller.admin;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.*;

import com.vjshop.service.TAreaService;
import com.vjshop.service.TFreightConfigService;
import com.vjshop.service.TShippingMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 运费配置
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminFreightConfigController")
@RequestMapping("/admin/freight_config")
public class FreightConfigController extends BaseController {

	@Autowired
	private TFreightConfigService tFreightConfigService;
	@Autowired
	private TShippingMethodService tShippingMethodService;
	@Autowired
	private TAreaService tAreaService;

	/**
	 * 检查地区是否唯一
	 */
	@RequestMapping(value = "/check_area", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkArea(Long shippingMethodId, Long previousAreaId, Long areaId) {
		if (areaId == null) {
			return false;
		}
		TShippingMethod shippingMethod = tShippingMethodService.find(shippingMethodId);
		TArea previousArea = tAreaService.find(previousAreaId);
		TArea area = tAreaService.find(areaId);
		return tFreightConfigService.unique(shippingMethod, previousArea, area);
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Long shippingMethodId, ModelMap model) {
		model.addAttribute("shippingMethod", tShippingMethodService.find(shippingMethodId));
		return "/admin/freight_config/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TFreightConfig freightConfig, Long shippingMethodId, Long areaId, RedirectAttributes redirectAttributes) {
		freightConfig.setArea(areaId);
		freightConfig.setShippingMethod(shippingMethodId);
		if (!isValid(freightConfig, ValidGroup.Save.class)) {
			return ERROR_VIEW;
		}
		if (tFreightConfigService.exists(shippingMethodId, areaId)) {
			return ERROR_VIEW;
		}
		tFreightConfigService.save(freightConfig);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		TFreightConfig freightConfig = tFreightConfigService.find(id);
		model.addAttribute("freightConfig", freightConfig);
		model.addAttribute("shippingMethod", tShippingMethodService.find(freightConfig.getShippingMethod()));
		model.addAttribute("area", tAreaService.find(freightConfig.getArea()));
		return "/admin/freight_config/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TFreightConfig freightConfig, Long id, Long areaId, RedirectAttributes redirectAttributes) {
		TArea area = tAreaService.find(areaId);
		freightConfig.setArea(areaId);
		if (!isValid(freightConfig, ValidGroup.Update.class)) {
			return ERROR_VIEW;
		}
		TFreightConfig pFreightConfig = tFreightConfigService.find(id);
		TShippingMethod tShippingMethod = tShippingMethodService.find(pFreightConfig.getShippingMethod());
		TArea previousArea = tAreaService.find(areaId);
		if (!tFreightConfigService.unique(tShippingMethod, previousArea, area)) {
			return ERROR_VIEW;
		}
		tFreightConfigService.save(freightConfig);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, Long shippingMethodId, ModelMap model) {
		TShippingMethod shippingMethod = tShippingMethodService.find(shippingMethodId);
		model.addAttribute("shippingMethod", shippingMethod);
		model.addAttribute("page", tFreightConfigService.findPage(shippingMethodId, pageable));
		return "/admin/freight_config/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		tFreightConfigService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}