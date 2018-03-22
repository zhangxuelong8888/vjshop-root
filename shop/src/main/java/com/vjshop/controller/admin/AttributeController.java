
package com.vjshop.controller.admin;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.TGoods;
import com.vjshop.entity.TAttribute;
import com.vjshop.entity.ValidGroup;
import com.vjshop.service.TAttributeService;
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
 * Controller - 属性
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminAttributeController")
@RequestMapping("/admin/attribute")
public class AttributeController extends BaseController {

	@Autowired
	private TAttributeService tAttributeService;
	@Autowired
	private TProductCategoryService tProductCategoryService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Long sampleId, ModelMap model) {
		model.addAttribute("sample", this.tAttributeService.findDetailById(sampleId));
		model.addAttribute("productCategoryTree", this.tProductCategoryService.findTree());
		return "/admin/attribute/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TAttribute attribute, RedirectAttributes redirectAttributes) {
		CollectionUtils.filter(attribute.getOptionsList(), new AndPredicate(new UniquePredicate(), new Predicate() {
			public boolean evaluate(Object object) {
				String option = (String) object;
				return StringUtils.isNotEmpty(option);
			}
		}));
		if (!isValid(attribute, ValidGroup.Save.class)) {
			return ERROR_VIEW;
		}
		Integer propertyIndex = this.tAttributeService.findUnusedPropertyIndex(attribute.getProductCategory());
		if (propertyIndex == null) {
			addFlashMessage(redirectAttributes, Message.error("admin.attribute.addCountNotAllowed", TGoods.ATTRIBUTE_VALUE_PROPERTY_COUNT));
		} else {
			attribute.setPropertyIndex(propertyIndex);
			Timestamp now = new Timestamp(System.currentTimeMillis());
			attribute.setCreateDate(now);
			attribute.setModifyDate(now);
			attribute.setVersion(0L);
			attribute.setOptions(JsonUtils.toJson(attribute.getOptionsList()));
			this.tAttributeService.save(attribute);
			addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		}
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("productCategoryTree", this.tProductCategoryService.findTree());
		model.addAttribute("attribute", this.tAttributeService.findDetailById(id));
		return "/admin/attribute/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TAttribute attribute, RedirectAttributes redirectAttributes) {
		CollectionUtils.filter(attribute.getOptionsList(), new AndPredicate(new UniquePredicate(), new Predicate() {
			public boolean evaluate(Object object) {
				String option = (String) object;
				return StringUtils.isNotEmpty(option);
			}
		}));
		if (!isValid(attribute)) {
			return ERROR_VIEW;
		}
		attribute.setModifyDate(new Timestamp(System.currentTimeMillis()));
		attribute.setOptions(JsonUtils.toJson(attribute.getOptionsList()));
		this.tAttributeService.update(attribute,"propertyIndex", "productCategory");
//		attributeService.update(attribute, "propertyIndex", "productCategory");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
//		model.addAttribute("page", attributeService.findPage(pageable));
		model.addAttribute("page", this.tAttributeService.findPage(pageable));
		return "/admin/attribute/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		this.tAttributeService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}