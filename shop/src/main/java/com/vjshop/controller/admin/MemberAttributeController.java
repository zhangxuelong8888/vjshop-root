
package com.vjshop.controller.admin;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.TMember;
import com.vjshop.entity.TMemberAttribute;
import com.vjshop.entity.ValidGroup;
import com.vjshop.service.TMemberAttributeService;

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

/**
 * Controller - 会员注册项
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminMemberAttributeController")
@RequestMapping("/admin/member_attribute")
public class MemberAttributeController extends BaseController {

	@Autowired
	private TMemberAttributeService memberAttributeService;

	/**
	 * 检查配比语法是否正确
	 */
	@RequestMapping(value = "/check_pattern", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkPattern(String pattern) {
		if (StringUtils.isEmpty(pattern)) {
			return false;
		}
		try {
			Pattern.compile(pattern);
		} catch (PatternSyntaxException e) {
			return false;
		}
		return true;
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model, RedirectAttributes redirectAttributes) {
		if (memberAttributeService.findUnusedPropertyIndex() == null) {
			addFlashMessage(redirectAttributes, Message.warn("admin.memberAttribute.addCountNotAllowed", TMember.ATTRIBUTE_VALUE_PROPERTY_COUNT));
			return "redirect:list.jhtml";
		}
		return "/admin/member_attribute/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TMemberAttribute memberAttribute, RedirectAttributes redirectAttributes) {
		if (!isValid(memberAttribute, ValidGroup.Save.class)) {
			return ERROR_VIEW;
		}
		if (memberAttribute.getType() != null && (TMemberAttribute.Type.select.ordinal() == memberAttribute.getType().intValue()
				|| TMemberAttribute.Type.checkbox.ordinal()  == memberAttribute.getType())) {
			List<String> options = memberAttribute.getOptionList();
			CollectionUtils.filter(options, new AndPredicate(new UniquePredicate(), new Predicate() {
				public boolean evaluate(Object object) {
					String option = (String) object;
					return StringUtils.isNotEmpty(option);
				}
			}));
			if (CollectionUtils.isEmpty(options)) {
				return ERROR_VIEW;
			}
			memberAttribute.setPattern(null);
		} else if (memberAttribute.getType() != null && TMemberAttribute.Type.text.ordinal() == memberAttribute.getType().intValue()) {
			memberAttribute.setOptions(null);
		} else {
			return ERROR_VIEW;
		}
		if (StringUtils.isNotEmpty(memberAttribute.getPattern())) {
			try {
				Pattern.compile(memberAttribute.getPattern());
			} catch (PatternSyntaxException e) {
				return ERROR_VIEW;
			}
		}
		Integer propertyIndex = memberAttributeService.findUnusedPropertyIndex();
		if (propertyIndex == null) {
			return ERROR_VIEW;
		}
		memberAttribute.setPropertyIndex(null);
		memberAttributeService.save(memberAttribute);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("memberAttribute", memberAttributeService.find(id));
		return "/admin/member_attribute/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TMemberAttribute memberAttribute, RedirectAttributes redirectAttributes) {
		if (!isValid(memberAttribute)) {
			return ERROR_VIEW;
		}
		TMemberAttribute pMemberAttribute = memberAttributeService.find(memberAttribute.getId());
		if (pMemberAttribute == null) {
			return ERROR_VIEW;
		}
		if (pMemberAttribute.getType() != null && (TMemberAttribute.Type.select.equals(pMemberAttribute.getType())
				|| TMemberAttribute.Type.checkbox.equals(pMemberAttribute.getType()))) {
			List<String> options = memberAttribute.getOptionList();
			CollectionUtils.filter(options, new AndPredicate(new UniquePredicate(), new Predicate() {
				public boolean evaluate(Object object) {
					String option = (String) object;
					return StringUtils.isNotEmpty(option);
				}
			}));
			if (CollectionUtils.isEmpty(options)) {
				return ERROR_VIEW;
			}
			memberAttribute.setPattern(null);
		} else {
			memberAttribute.setOptions(null);
		}
		if (StringUtils.isNotEmpty(memberAttribute.getPattern())) {
			try {
				Pattern.compile(memberAttribute.getPattern());
			} catch (PatternSyntaxException e) {
				return ERROR_VIEW;
			}
		}
		memberAttributeService.update(memberAttribute, "type", "propertyIndex");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", memberAttributeService.findPage(pageable));
		return "/admin/member_attribute/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		memberAttributeService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}