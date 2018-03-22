
package com.vjshop.controller.admin;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.ValidGroup;
import com.vjshop.entity.TTag;
import com.vjshop.service.TTagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;

/**
 * Controller - 标签
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminTagController")
@RequestMapping("/admin/tag")
public class TagController extends BaseController {

	@Autowired
	private TTagService tagService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("types", TTag.Type.values());
		return "/admin/tag/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TTag tag, RedirectAttributes redirectAttributes) {
		if (!isValid(tag, ValidGroup.Save.class)) {
			return ERROR_VIEW;
		}
		tagService.save(tag);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("types", TTag.Type.values());
		model.addAttribute("tag", tagService.find(id));
		return "/admin/tag/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TTag tag, RedirectAttributes redirectAttributes) {
		if (!isValid(tag)) {
			return ERROR_VIEW;
		}
		tag.setModifyDate(new Timestamp(System.currentTimeMillis()));
		tagService.update(tag, "type", "articles", "goods");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", tagService.findPage(pageable));
		return "/admin/tag/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message delete(Long[] ids) {
		tagService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}