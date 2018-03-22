
package com.vjshop.controller.admin;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.TDeliveryTemplate;

import com.vjshop.service.TDeliveryTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;

/**
 * Controller - 快递单模板
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminDeliveryTemplateController")
@RequestMapping("/admin/delivery_template")
public class DeliveryTemplateController extends BaseController {

    @Autowired
    private TDeliveryTemplateService tDeliveryTemplateService;

    /**
     * 添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Pageable pageable) {
        return "/admin/delivery_template/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(TDeliveryTemplate deliveryTemplate, RedirectAttributes redirectAttributes) {
        if (!isValid(deliveryTemplate)) {
            return ERROR_VIEW;
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        deliveryTemplate.setCreateDate(now);
        deliveryTemplate.setModifyDate(now);
        deliveryTemplate.setVersion(0L);
        this.tDeliveryTemplateService.save(deliveryTemplate);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String eidt(Long id, Model model) {
        model.addAttribute("deliveryTemplate", this.tDeliveryTemplateService.find(id));
        return "/admin/delivery_template/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String udpate(TDeliveryTemplate deliveryTemplate, RedirectAttributes redirectAttributes) {
        if (!isValid(deliveryTemplate)) {
            return ERROR_VIEW;
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        deliveryTemplate.setModifyDate(now);
        this.tDeliveryTemplateService.update(deliveryTemplate);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Pageable pageable, Model model) {
        model.addAttribute("page", this.tDeliveryTemplateService.findPage(pageable));
        return "/admin/delivery_template/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        this.tDeliveryTemplateService.delete(ids);
        return SUCCESS_MESSAGE;
    }
}