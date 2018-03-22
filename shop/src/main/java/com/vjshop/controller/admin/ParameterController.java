
package com.vjshop.controller.admin;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.TParameter;
import com.vjshop.entity.ValidGroup;
import com.vjshop.service.TParameterService;
import com.vjshop.service.TProductCategoryService;
import com.vjshop.util.JsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AndPredicate;
import org.apache.commons.collections.functors.UniquePredicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;

/**
 * Controller - 参数
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminParameterController")
@RequestMapping("/admin/parameter")
public class ParameterController extends BaseController {

	@Autowired
	private TParameterService tParameterService;
	@Autowired
	private TProductCategoryService tProductCategoryService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Long sampleId, ModelMap model) {
		model.addAttribute("sample", this.tParameterService.findDetailById(sampleId));
		model.addAttribute("productCategoryTree", this.tProductCategoryService.findTree());
		return "/admin/parameter/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TParameter parameter, RedirectAttributes redirectAttributes) {
		CollectionUtils.filter(parameter.getNameList(), new AndPredicate(new UniquePredicate(), new Predicate() {
			public boolean evaluate(Object object) {
				String name = (String) object;
				return StringUtils.isNotEmpty(name);
			}
		}));
		if (!isValid(parameter, ValidGroup.Save.class)) {
			return ERROR_VIEW;
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		parameter.setCreateDate(now);
		parameter.setModifyDate(now);
		parameter.setVersion(0L);
		this.tParameterService.save(parameter);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("parameter", this.tParameterService.findDetailById(id));
		return "/admin/parameter/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TParameter parameter, RedirectAttributes redirectAttributes) {
		CollectionUtils.filter(parameter.getNameList(), new AndPredicate(new UniquePredicate(), new Predicate() {
			public boolean evaluate(Object object) {
				String name = (String) object;
				return StringUtils.isNotEmpty(name);
			}
		}));
		if (!isValid(parameter, ValidGroup.Update.class)) {
			return ERROR_VIEW;
		}
		parameter.setModifyDate(new Timestamp(System.currentTimeMillis()));
		parameter.setNames(JsonUtils.toJson(parameter.getNameList()));
		this.tParameterService.update(parameter,"productCategory");
//		parameterService.update(parameter, "productCategory");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", this.tParameterService.findPage(pageable));
		return "/admin/parameter/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		this.tParameterService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}