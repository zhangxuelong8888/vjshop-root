
package com.vjshop.controller.admin;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.TSpecification;

import com.vjshop.entity.ValidGroup;
import com.vjshop.service.TProductCategoryService;
import com.vjshop.service.TSpecificationService;
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
 * Controller - 规格
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminSpecificationController")
@RequestMapping("/admin/specification")
public class SpecificationController extends BaseController {

	@Autowired
	private TSpecificationService tSpecificationService;
	@Autowired
	private TProductCategoryService tProductCategoryService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Long sampleId, ModelMap model) {
		model.addAttribute("sample", this.tSpecificationService.findDetailById(sampleId));
		model.addAttribute("productCategoryTree", this.tProductCategoryService.findTree());
		return "/admin/specification/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TSpecification specification, RedirectAttributes redirectAttributes) {
		CollectionUtils.filter(specification.getOptionsList(), new AndPredicate(new UniquePredicate(), new Predicate() {
			public boolean evaluate(Object object) {
				String option = (String) object;
				return StringUtils.isNotEmpty(option);
			}
		}));
		if (!isValid(specification, ValidGroup.Save.class)) {
			return ERROR_VIEW;
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		specification.setCreateDate(now);
		specification.setModifyDate(now);
		specification.setVersion(0L);
		specification.setOptions(JsonUtils.toJson(specification.getOptionsList()));
		this.tSpecificationService.save(specification);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("productCategoryTree", this.tProductCategoryService.findTree());
		model.addAttribute("specification", this.tSpecificationService.find(id));
		return "/admin/specification/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TSpecification specification, RedirectAttributes redirectAttributes) {
		CollectionUtils.filter(specification.getOptionsList(), new AndPredicate(new UniquePredicate(), new Predicate() {
			public boolean evaluate(Object object) {
				String option = (String) object;
				return StringUtils.isNotEmpty(option);
			}
		}));
		if (!isValid(specification)) {
			return ERROR_VIEW;
		}
		specification.setModifyDate(new Timestamp(System.currentTimeMillis()));
		specification.setOptions(JsonUtils.toJson(specification.getOptionsList()));
		this.tSpecificationService.update(specification, "productCategory");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
//		model.addAttribute("page", specificationService.findPage(pageable));
		model.addAttribute("page", this.tSpecificationService.findPage(pageable));
		return "/admin/specification/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		this.tSpecificationService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}