
package com.vjshop.controller.admin;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.TDeliveryCenter;
import com.vjshop.service.TAreaService;
import com.vjshop.service.TDeliveryCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;

/**
 * Controller - 发货点
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminDeliveryCenterController")
@RequestMapping("/admin/delivery_center")
public class DeliveryCenterController extends BaseController {

    @Autowired
    private TDeliveryCenterService tDeliveryCenterService;
    @Autowired
    private TAreaService tAreaService;

    /**
     * 添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return "/admin/delivery_center/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(TDeliveryCenter deliveryCenter, Long areaId, Model model, RedirectAttributes redirectAttributes) {
        deliveryCenter.setArea(areaId);
        deliveryCenter.setAreaVO(this.tAreaService.find(areaId));
        if (!isValid(deliveryCenter)) {
            return ERROR_VIEW;
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        deliveryCenter.prePersist();
        deliveryCenter.setCreateDate(now);
        deliveryCenter.setModifyDate(now);
        deliveryCenter.setVersion(0L);
        this.tDeliveryCenterService.save(deliveryCenter);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, Model model) {
        model.addAttribute("deliveryCenter", this.tDeliveryCenterService.findDetailById(id));
        return "/admin/delivery_center/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(TDeliveryCenter deliveryCenter, Long areaId, RedirectAttributes redirectAttributes) {
        deliveryCenter.setArea(areaId);
        deliveryCenter.setAreaVO(this.tAreaService.find(areaId));
        if (!isValid(deliveryCenter)) {
            return ERROR_VIEW;
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        deliveryCenter.setModifyDate(now);
        deliveryCenter.prePersist();
        this.tDeliveryCenterService.update(deliveryCenter, "areaName");
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model, Pageable pageable) {
        model.addAttribute("page", this.tDeliveryCenterService.findPage(pageable));
        return "/admin/delivery_center/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        this.tDeliveryCenterService.delete(ids);
        return SUCCESS_MESSAGE;
    }

}