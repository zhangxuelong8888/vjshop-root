
package com.vjshop.controller.admin;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.vjshop.Message;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.Setting;
import com.vjshop.entity.*;
import com.vjshop.service.*;
import com.vjshop.util.SystemUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 订单
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminOrderController")
@RequestMapping("/admin/order")
public class OrderController extends BaseController {

    @Autowired
    private TAdminService tAdminService;
    @Autowired
    private TOrderService tOrderService;
    @Autowired
    private TMemberService tMemberService;
    @Autowired
    private TPaymentMethodService tPaymentMethodService;
    @Autowired
    private TShippingService tShippingService;
    @Autowired
    private TShippingMethodService tShippingMethodService;
    @Autowired
    private TDeliveryCorpService tDeliveryCorpService;
    @Autowired
    private TAreaService tAreaService;

    /**
     * 检查锁定
     */
    @RequestMapping(value = "/check_lock", method = RequestMethod.POST)
    public @ResponseBody
    Message checkLock(Long id) {
        TOrder tOrder = this.tOrderService.find(id);
        if (tOrder == null) {
            return ERROR_MESSAGE;
        }
        TAdmin admin = this.tAdminService.getCurrent();
        if (this.tOrderService.isLocked(tOrder, admin, true)) {
            return Message.warn("admin.order.locked");
        }
        return SUCCESS_MESSAGE;
    }

    /**
     * 计算
     */
    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> calculate(Long id, BigDecimal freight, BigDecimal tax, BigDecimal offsetAmount) {
        Map<String, Object> data = new HashMap<String, Object>();
        TOrder order = tOrderService.find(id);
        if (order == null) {
            data.put("message", ERROR_MESSAGE);
            return data;
        }
        data.put("message", SUCCESS_MESSAGE);
        data.put("amount", tOrderService.calculateAmount(order.getPrice(), order.getFee(), freight, tax, order.getPromotionDiscount(), order.getCouponDiscount(), offsetAmount));
        return data;
    }

    /**
     * 物流动态
     */
    @RequestMapping(value = "/transit_step", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> transitStep(Long shippingId) {
        Map<String, Object> data = new HashMap<String, Object>();
        TShipping shipping = tShippingService.find(shippingId);
        if (shipping == null) {
            data.put("message", ERROR_MESSAGE);
            return data;
        }
        Setting setting = SystemUtils.getSetting();
        if (StringUtils.isEmpty(setting.getKuaidi100Key()) || StringUtils.isEmpty(shipping.getDeliveryCorpCode()) || StringUtils.isEmpty(shipping.getTrackingNo())) {
            data.put("message", ERROR_MESSAGE);
            return data;
        }
        data.put("message", SUCCESS_MESSAGE);
        data.put("transitSteps", tShippingService.getTransitSteps(shipping));
        return data;
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        TOrder order = this.tOrderService.findDetailById(id);
        if (order == null || order.hasExpired() || (!Integer.valueOf(TOrder.Status.pendingPayment.ordinal()).equals(order.getStatus()) && !Integer.valueOf(TOrder.Status.pendingReview.ordinal()).equals(order.getStatus()))) {
            return ERROR_VIEW;
        }
        model.addAttribute("paymentMethods", this.tPaymentMethodService.findAll());
        model.addAttribute("shippingMethods", this.tShippingMethodService.findAll());
        model.addAttribute("orderTypes", TOrder.Type.values());
        model.addAttribute("orderStatus", TOrder.Status.values());
        model.addAttribute("goodsTypes", TGoods.Type.values());
        model.addAttribute("order", order);
        return "/admin/order/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Long id, Long areaId, Long paymentMethodId, Long shippingMethodId, BigDecimal freight, BigDecimal tax, BigDecimal offsetAmount, Long rewardPoint, String consignee, String address, String zipCode, String phone, String invoiceTitle, String memo,
                         RedirectAttributes redirectAttributes) {
        TOrder order = this.tOrderService.findDetailById(id);
        if (order == null || order.hasExpired() || (!Integer.valueOf(TOrder.Status.pendingPayment.ordinal()).equals(order.getStatus()) && !Integer.valueOf(TOrder.Status.pendingReview.ordinal()).equals(order.getStatus()))) {
            return ERROR_VIEW;
        }
        Invoice invoice = StringUtils.isNotEmpty(invoiceTitle) ? new Invoice(invoiceTitle, null) : null;
        order.setTax(invoice != null ? tax : BigDecimal.ZERO);
        order.setOffsetAmount(offsetAmount);
        order.setRewardPoint(rewardPoint);
        order.setMemo(memo);
        order.setInvoiceTitle(invoiceTitle);
        order.setPaymentMethod(paymentMethodId);
        if (order.getIsDelivery()) {
            order.setFreight(freight);
            order.setConsignee(consignee);
            order.setAddress(address);
            order.setZipCode(zipCode);
            order.setPhone(phone);
            order.setArea(areaId);
            order.setShippingMethod(shippingMethodId);
            if (!isValid(order, TOrder.Delivery.class)) {
                return ERROR_VIEW;
            }
        } else {
            order.setFreight(BigDecimal.ZERO);
            order.setConsignee(null);
            order.setAreaName(null);
            order.setAddress(null);
            order.setZipCode(null);
            order.setPhone(null);
            order.setShippingMethodName(null);
            order.setArea(null);
            order.setShippingMethod(null);
            if (!isValid(order)) {
                return ERROR_VIEW;
            }
        }

        TAdmin admin = this.tAdminService.getCurrent();
        if (this.tOrderService.isLocked(order, admin, true)) {
            return ERROR_VIEW;
        }
        this.tOrderService.update(order, admin);

        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    /**
     * 查看
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String view(Long id, ModelMap model) {
        Setting setting = SystemUtils.getSetting();
        model.addAttribute("methods", TPayment.Method.values());
        model.addAttribute("orderTypes", TOrder.Type.values());
        model.addAttribute("status", TOrder.Status.values());
        model.addAttribute("goodsTypes", TGoods.Type.values());
        model.addAttribute("refundsMethods", TRefunds.Method.values());
        model.addAttribute("orderLogTypes", TOrderLog.Type.values());


        model.addAttribute("refundsMethods", TRefunds.Method.values());
        model.addAttribute("paymentMethods", this.tPaymentMethodService.findAll());
        model.addAttribute("shippingMethods", this.tShippingMethodService.findAll());
        model.addAttribute("deliveryCorps", this.tDeliveryCorpService.findAll());
        model.addAttribute("isKuaidi100Enabled", StringUtils.isNotEmpty(setting.getKuaidi100Key()));
        model.addAttribute("order", this.tOrderService.findDetailById(id));
        return "/admin/order/view";
    }

    /**
     * 审核
     */
    @RequestMapping(value = "/review", method = RequestMethod.POST)
    public String review(Long id, Boolean passed, RedirectAttributes redirectAttributes) {
        TOrder order = this.tOrderService.findDetailById(id);
        if (order == null || order.hasExpired() || !Integer.valueOf(TOrder.Status.pendingReview.ordinal()).equals(order.getStatus())) {
            return ERROR_VIEW;
        }
        TAdmin admin = this.tAdminService.getCurrent();
        if (this.tOrderService.isLocked(order, admin, true)) {
            return ERROR_VIEW;
        }
        this.tOrderService.review(order, passed, admin);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:view.jhtml?id=" + id;
    }

    /**
     * 收款
     */
    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public String payment(TPayment payment, Long orderId, Long paymentMethodId, RedirectAttributes redirectAttributes) {
        TOrder order = this.tOrderService.findDetailById(orderId);
        if (order == null) {
            return ERROR_VIEW;
        }
        payment.setOrders(orderId);
        payment.setPaymentMethod(this.tPaymentMethodService.find(paymentMethodId));
        if (!isValid(payment)) {
            return ERROR_VIEW;
        }
        TAdmin admin = this.tAdminService.getCurrent();
        if (this.tOrderService.isLocked(order, admin, true)) {
            return ERROR_VIEW;
        }
        TMember member = this.tMemberService.find(order.getMember());
        if (Integer.valueOf(TPayment.Method.deposit.ordinal()).equals(payment.getMethod()) && payment.getAmount().compareTo(member.getBalance()) > 0) {
            return ERROR_VIEW;
        }
        payment.setFee(BigDecimal.ZERO);
        payment.setOperator(admin);
        this.tOrderService.payment(order, payment, admin);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:view.jhtml?id=" + orderId;
    }

    /**
     * 退款
     */
    @RequestMapping(value = "/refunds", method = RequestMethod.POST)
    public String refunds(TRefunds refunds, Long orderId, Long paymentMethodId, RedirectAttributes redirectAttributes) {
        TOrder order = this.tOrderService.findDetailById(orderId);
        if (order == null || order.getRefundableAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return ERROR_VIEW;
        }
        refunds.setOrders(orderId);
        refunds.setPaymentMethod(this.tPaymentMethodService.find(paymentMethodId));
        if (!isValid(refunds)) {
            return ERROR_VIEW;
        }
        TAdmin admin = this.tAdminService.getCurrent();
        if (this.tOrderService.isLocked(order, admin, true)) {
            return ERROR_VIEW;
        }
        refunds.setOperator(admin);
        this.tOrderService.refunds(order, refunds, admin);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:view.jhtml?id=" + orderId;
    }

    /**
     * 发货
     */
    @RequestMapping(value = "/shipping", method = RequestMethod.POST)
    public String shipping(TShipping shipping, Long orderId, Long shippingMethodId, Long deliveryCorpId, Long areaId, RedirectAttributes redirectAttributes) {
        TOrder order = this.tOrderService.findDetailById(orderId);
        if (order == null || order.getShippableQuantity() <= 0) {
            return ERROR_VIEW;
        }
        boolean isDelivery = false;
        for (Iterator<TShippingItem> iterator = shipping.getShippingItems().iterator(); iterator.hasNext(); ) {
            TShippingItem shippingItem = iterator.next();
            if (shippingItem == null || StringUtils.isEmpty(shippingItem.getSn()) || shippingItem.getQuantity() == null || shippingItem.getQuantity() <= 0) {
                iterator.remove();
                continue;
            }
            TOrderItem orderItem = order.getOrderItem(shippingItem.getSn());
            if (orderItem == null || shippingItem.getQuantity() > orderItem.getShippableQuantity()) {
                return ERROR_VIEW;
            }
            TProduct product = orderItem.getProductVO();
            if (product != null && shippingItem.getQuantity() > product.getStock()) {
                return ERROR_VIEW;
            }
            shippingItem.setName(orderItem.getName());
            shippingItem.setIsDelivery(orderItem.getIsDelivery());
            shippingItem.setProduct(product.getId());
            shippingItem.setShipping(shipping.getId());
            shippingItem.setSpecifications(orderItem.getSpecifications());
            if (orderItem.getIsDelivery()) {
                isDelivery = true;
            }
        }
        shipping.setOrders(orderId);
        shipping.setShippingMethod(this.tShippingMethodService.find(shippingMethodId));
        shipping.setDeliveryCorp(this.tDeliveryCorpService.find(deliveryCorpId));
        shipping.setArea(this.tAreaService.find(areaId));
        if (isDelivery) {
            if (!isValid(shipping, TShipping.Delivery.class)) {
                return ERROR_VIEW;
            }
        } else {
            shipping.setShippingMethod((String) null);
            shipping.setDeliveryCorp((String) null);
            shipping.setDeliveryCorpUrl(null);
            shipping.setDeliveryCorpCode(null);
            shipping.setTrackingNo(null);
            shipping.setFreight(null);
            shipping.setConsignee(null);
            shipping.setArea((String) null);
            shipping.setAddress(null);
            shipping.setZipCode(null);
            shipping.setPhone(null);
            if (!isValid(shipping)) {
                return ERROR_VIEW;
            }
        }

        TAdmin admin = this.tAdminService.getCurrent();
        if (this.tOrderService.isLocked(order, admin, true)) {
            return ERROR_VIEW;
        }
        shipping.setOperator(admin);
        this.tOrderService.shipping(order, shipping, admin);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:view.jhtml?id=" + orderId;
    }

    /**
     * 退货
     */
    @RequestMapping(value = "/returns", method = RequestMethod.POST)
    public String returns(TReturns returns, Long orderId, Long shippingMethodId, Long deliveryCorpId, Long areaId, RedirectAttributes redirectAttributes) {
        TOrder order = this.tOrderService.findDetailById(orderId);
        if (order == null || order.getReturnableQuantity() <= 0) {
            return ERROR_VIEW;
        }
        for (Iterator<TReturnsItem> iterator = returns.getReturnsItems().iterator(); iterator.hasNext(); ) {
            TReturnsItem returnsItem = iterator.next();
            if (returnsItem == null || StringUtils.isEmpty(returnsItem.getSn()) || returnsItem.getQuantity() == null || returnsItem.getQuantity() <= 0) {
                iterator.remove();
                continue;
            }
            TOrderItem orderItem = order.getOrderItem(returnsItem.getSn());
            if (orderItem == null || returnsItem.getQuantity() > orderItem.getReturnableQuantity()) {
                return ERROR_VIEW;
            }
            returnsItem.setName(orderItem.getName());
            returnsItem.setReturns(returns.getId());
            returnsItem.setSpecifications(orderItem.getSpecifications());
        }
        returns.setOrders(orderId);
        returns.setShippingMethod(this.tShippingMethodService.find(shippingMethodId));
        returns.setDeliveryCorp(this.tDeliveryCorpService.find(deliveryCorpId));
        returns.setArea(this.tAreaService.find(areaId));
        if (!isValid(returns)) {
            return ERROR_VIEW;
        }
        TAdmin admin = this.tAdminService.getCurrent();
        if (this.tOrderService.isLocked(order, admin, true)) {
            return ERROR_VIEW;
        }
        returns.setOperator(admin);
        this.tOrderService.returns(order, returns, admin);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:view.jhtml?id=" + orderId;
    }

    /**
     * 收货
     */
    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    public String receive(Long id, RedirectAttributes redirectAttributes) {
        TOrder order = tOrderService.findDetailById(id);
        if (order == null || order.hasExpired() || !Integer.valueOf(TOrder.Status.shipped.ordinal()).equals(order.getStatus())) {
            return ERROR_VIEW;
        }
        TAdmin admin = this.tAdminService.getCurrent();
        if (this.tOrderService.isLocked(order, admin, true)) {
            return ERROR_VIEW;
        }
        this.tOrderService.receive(order, admin);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:view.jhtml?id=" + id;
    }

    /**
     * 完成
     */
    @RequestMapping(value = "/complete", method = RequestMethod.POST)
    public String complete(Long id, RedirectAttributes redirectAttributes) {
        TOrder order = tOrderService.find(id);
        if (order == null || order.hasExpired() || TOrder.Status.received.ordinal() != order.getStatus()) {
            return ERROR_VIEW;
        }
        TAdmin admin = tAdminService.getCurrent();
        if (tOrderService.isLocked(order, admin, true)) {
            return ERROR_VIEW;
        }
        tOrderService.complete(order, admin);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:view.jhtml?id=" + id;
    }

    /**
     * 失败
     */
    @RequestMapping(value = "/fail", method = RequestMethod.POST)
    public String fail(Long id, RedirectAttributes redirectAttributes) {
        TOrder order = tOrderService.find(id);
        if (order == null || order.hasExpired() || (!TOrder.Status.pendingShipment.equals(order.getStatus()) && !TOrder.Status.shipped.equals(order.getStatus()) && !TOrder.Status.received.equals(order.getStatus()))) {
            return ERROR_VIEW;
        }
        TAdmin admin = tAdminService.getCurrent();
        if (tOrderService.isLocked(order, admin, true)) {
            return ERROR_VIEW;
        }
        tOrderService.fail(order, admin);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:view.jhtml?id=" + id;
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(TOrder.Type type, TOrder.Status status, String memberUsername, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable, ModelMap model) {
        model.addAttribute("types", TOrder.Type.values());
        model.addAttribute("statuses", TOrder.Status.values());
        model.addAttribute("type", type);
        model.addAttribute("status", status);
        model.addAttribute("memberUsername", memberUsername);
        model.addAttribute("isPendingReceive", isPendingReceive);
        model.addAttribute("isPendingRefunds", isPendingRefunds);
        model.addAttribute("isAllocatedStock", isAllocatedStock);
        model.addAttribute("hasExpired", hasExpired);

        TMember member = this.tMemberService.findByUsername(memberUsername);
        if (StringUtils.isNotEmpty(memberUsername) && member == null) {
            model.addAttribute("page", new Page<TOrder>(Collections.EMPTY_LIST, 0, pageable));
        } else {
            model.addAttribute("page", this.tOrderService.findPage(type, status, member != null ? member.getId() : null, null,
                    isPendingReceive, isPendingRefunds, null, null, isAllocatedStock, hasExpired, pageable));
        }
        return "/admin/order/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        if (ids != null) {
            TAdmin admin = tAdminService.getCurrent();
            for (Long id : ids) {
                TOrder order = tOrderService.find(id);
                if (order != null && tOrderService.isLocked(order, admin, true)) {
                    return Message.error("admin.order.deleteLockedNotAllowed", order.getSn());
                }
            }
            tOrderService.delete(ids);
        }
        return SUCCESS_MESSAGE;
    }

}