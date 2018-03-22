[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.order.view")} - Powered By VJSHOP</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.lSelect.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<style type="text/css">
	.shipping, .returns {
		height: 380px;
		overflow-x: hidden;
		overflow-y: auto;
	}
	
	.shipping .item, .returns .item {
		margin: 6px 0px;
	}
	
	.transitSteps {
		height: 240px;
		line-height: 28px;
		padding: 0px 6px;
		overflow-x: hidden;
		overflow-y: auto;
	}
	
	.transitSteps th {
		width: 150px;
		color: #888888;
		font-weight: normal;
		text-align: left;
	}
</style>
<script type="text/javascript">
$().ready(function() {
	var $reviewForm = $("#reviewForm");
	var $passed = $("#passed");
	var $receiveForm = $("#receiveForm");
	var $completeForm = $("#completeForm");
	var $failForm = $("#failForm");
	var $reviewButton = $("#reviewButton");
	var $paymentButton = $("#paymentButton");
	var $refundsButton = $("#refundsButton");
	var $shippingButton = $("#shippingButton");
	var $returnsButton = $("#returnsButton");
	var $receiveButton = $("#receiveButton");
	var $completeButton = $("#completeButton");
	var $failButton = $("#failButton");
	var $transitStep = $("a.transitStep");
	var isLocked = false;
	
	[@flash_message /]
	
	// 检查锁定
	function checkLock() {
		if (!isLocked) {
		    $.ajax({
				url: "check_lock.jhtml",
				type: "POST",
				data: {id: ${order.id}},
				dataType: "json",
				cache: false,
				success: function(message) {
				    if (message.type != "success") {
						$.message(message);
						$reviewButton.add($paymentButton).add($refundsButton).add($shippingButton).add($returnsButton).add($receiveButton).add($completeButton).add($failButton).prop("disabled", true);
						isLocked = true;
					}
				}
			});
		}
	}
	
	// 检查锁定
	checkLock();
	setInterval(function() {
		checkLock();
	}, 50000);
	
	[#if !order.hasExpired() && order.status == "1"]
		// 审核
		$reviewButton.click(function() {
			var $this = $(this);
			$.dialog({
				type: "warn",
				content: "${message("admin.order.reviewConfirm")}",
				ok: "${message("admin.common.true")}",
				cancel: "${message("admin.common.false")}",
				onOk: function() {
					$passed.val("true");
					$reviewForm.submit();
					return false;
				},
				onCancel: function() {
					$passed.val("false");
					$reviewForm.submit();
					return false;
				}
			});
		});
	[/#if]
	
	// 收款
	$paymentButton.click(function() {
		$.dialog({
			title: "${message("admin.order.payment")}",
			content: 
				[@compress single_line = true]
					'<form id="paymentForm" action="payment.jhtml" method="post">
						<input type="hidden" name="token" value="${token}" \/>
						<input type="hidden" name="orderId" value="${order.id}" \/>
						<table class="input">
							<tr>
								<th>
									${message("Order.sn")}:
								<\/th>
								<td width="300">
									${order.sn}
								<\/td>
								<th>
									${message("admin.common.createDate")}:
								<\/th>
								<td>
									${order.createDate?string("yyyy-MM-dd HH:mm:ss")}
								<\/td>
							<\/tr>
							<tr>
								<th>
									${message("Order.amount")}:
								<\/th>
								<td>
									${currency(order.amount, true)}
								<\/td>
								<th>
									${message("Order.amountPayable")}:
								<\/th>
								<td>
									${currency(order.amountPayable, true)}
								<\/td>
							<\/tr>
							<tr>
								<th>
									${message("Payment.bank")}:
								<\/th>
								<td>
									<input type="text" name="bank" class="text" maxlength="200" \/>
								<\/td>
								<th>
									${message("Payment.account")}:
								<\/th>
								<td>
									<input type="text" name="account" class="text" maxlength="200" \/>
								<\/td>
							<\/tr>
							<tr>
								<th>
									${message("Payment.amount")}:
								<\/th>
								<td>
									<input type="text" name="amount" class="text"[#if order.amountPayable > 0] value="${order.amountPayable}"[/#if] maxlength="16" \/>
								<\/td>
								<th>
									${message("Payment.payer")}:
								<\/th>
								<td>
									<input type="text" name="payer" class="text" maxlength="200" \/>
								<\/td>
							<\/tr>
							<tr>
								<th>
									${message("Payment.method")}:
								<\/th>
								<td>
									<select name="method">
										[#list methods as method]
											<option value="${method.ordinal()}">${message("Payment.Method." + method)}<\/option>
										[/#list]
									<\/select>
								<\/td>
								<th>
									${message("Payment.paymentMethod")}:
								<\/th>
								<td>
									<select name="paymentMethodId">
										<option value="">${message("admin.common.choose")}<\/option>
										[#list paymentMethods as paymentMethod]
											[#noescape]
												<option value="${paymentMethod.id}">${paymentMethod.name?html?js_string}<\/option>
											[/#noescape]
										[/#list]
									<\/select>
								<\/td>
							<\/tr>
							<tr>
								<th>
									${message("Payment.memo")}:
								<\/th>
								<td colspan="3">
									<input type="text" name="memo" class="text" maxlength="200" \/>
								<\/td>
							<\/tr>
							<tr>
								<td colspan="4" style="border-bottom: none;">
									&nbsp;
								<\/td>
							<\/tr>
						<\/table>
					<\/form>'
				[/@compress]
			,
			width: 900,
			modal: true,
			ok: "${message("admin.dialog.ok")}",
			cancel: "${message("admin.dialog.cancel")}",
			onShow: function() {
				var $amount = $("#paymentForm input[name='amount']");
				var $method = $("#paymentForm select[name='method']");
				$.validator.addMethod("balance", 
					function(value, element, param) {
						return this.optional(element) || $method.val() != "deposit" || parseFloat(value) <= parseFloat(param);
					},
					"${message("admin.order.insufficientBalance")}"
				);
				$("#paymentForm").validate({
					rules: {
						amount: {
							required: true,
							positive: true,
							decimal: {
								integer: 12,
								fraction: ${setting.priceScale}
							},
							balance: ${order.memberVO.balance}
						}
					},
					submitHandler: function(form) {
						if (parseFloat($amount.val()) <= ${order.amountPayable} || confirm("${message("admin.order.paymentConfirm")}")) {
							form.submit();
						}
					}
				});
			},
			onOk: function() {
				$("#paymentForm").submit();
				return false;
			}
		});
	});
	
	[#if order.refundableAmount > 0]
		// 退款
		$refundsButton.click(function() {
			$.dialog({
				title: "${message("admin.order.refunds")}",
				content: 
					[@compress single_line = true]
						'<form id="refundsForm" action="refunds.jhtml" method="post">
							<input type="hidden" name="token" value="${token}" \/>
							<input type="hidden" name="orderId" value="${order.id}" \/>
							<table class="input">
								<tr>
									<th>
										${message("Order.sn")}:
									<\/th>
									<td width="300">
										${order.sn}
									<\/td>
									<th>
										${message("admin.common.createDate")}:
									<\/th>
									<td>
										${order.createDate?string("yyyy-MM-dd HH:mm:ss")}
									<\/td>
								<\/tr>
								<tr>
									<th>
										${message("Order.amount")}:
									<\/th>
									<td>
										${currency(order.amount, true)}
									<\/td>
									<th>
										${message("Order.refundableAmount")}:
									<\/th>
									<td>
										${currency(order.refundableAmount, true)}
									<\/td>
								<\/tr>
								<tr>
									<th>
										${message("Refunds.bank")}:
									<\/th>
									<td>
										<input type="text" name="bank" class="text" maxlength="200" \/>
									<\/td>
									<th>
										${message("Refunds.account")}:
									<\/th>
									<td>
										<input type="text" name="account" class="text" maxlength="200" \/>
									<\/td>
								<\/tr>
								<tr>
									<th>
										${message("Refunds.amount")}:
									<\/th>
									<td>
										<input type="text" name="amount" class="text" value="${order.refundableAmount}" maxlength="16" \/>
									<\/td>
									<th>
										${message("Refunds.payee")}:
									<\/th>
									<td>
										<input type="text" name="payee" class="text" maxlength="200" \/>
									<\/td>
								<\/tr>
								<tr>
									<th>
										${message("Refunds.method")}:
									<\/th>
									<td>
										<select name="method">
											[#list refundsMethods as refundsMethod]
												<option value="${refundsMethod.ordinal()}">${message("Refunds.Method." + refundsMethod)}<\/option>
											[/#list]
										<\/select>
									<\/td>
									<th>
										${message("Refunds.paymentMethod")}:
									<\/th>
									<td>
										<select name="paymentMethodId">
											<option value="">${message("admin.common.choose")}<\/option>
											[#list paymentMethods as paymentMethod]
												[#noescape]
													<option value="${paymentMethod.id}">${paymentMethod.name?html?js_string}<\/option>
												[/#noescape]
											[/#list]
										<\/select>
									<\/td>
								<\/tr>
								<tr>
									<th>
										${message("Refunds.memo")}:
									<\/th>
									<td colspan="3">
										<input type="text" name="memo" class="text" maxlength="200" \/>
									<\/td>
								<\/tr>
								<tr>
									<td colspan="4" style="border-bottom: none;">
										&nbsp;
									<\/td>
								<\/tr>
							<\/table>
						<\/form>'
					[/@compress]
				,
				width: 900,
				modal: true,
				ok: "${message("admin.dialog.ok")}",
				cancel: "${message("admin.dialog.cancel")}",
				onShow: function() {
					$("#refundsForm").validate({
						rules: {
							amount: {
								required: true,
								positive: true,
								max: ${order.refundableAmount},
								decimal: {
									integer: 12,
									fraction: ${setting.priceScale}
								}
							}
						}
					});
				},
				onOk: function() {
					$("#refundsForm").submit();
					return false;
				}
			});
		});
	[/#if]
	
	[#if order.shippableQuantity > 0]
		// 发货
		$shippingButton.click(function() {
			$.dialog({
				title: "${message("admin.order.shipping")}",
				content: 
					[@compress single_line = true]
						'<form id="shippingForm" action="shipping.jhtml" method="post">
							<input type="hidden" name="token" value="${token}" \/>
							<input type="hidden" name="orderId" value="${order.id}" \/>
							<div class="shipping">
								<table id="shippingLogistics" class="input">
									<tr>
										<th>
											${message("Order.sn")}:
										<\/th>
										<td width="300">
											${order.sn}
										<\/td>
										<th>
											${message("admin.common.createDate")}:
										<\/th>
										<td>
											${order.createDate?string("yyyy-MM-dd HH:mm:ss")}
										<\/td>
									<\/tr>
									<tr>
										<th>
											${message("Shipping.shippingMethod")}:
										<\/th>
										<td>
											<select name="shippingMethodId">
												<option value="">${message("admin.common.choose")}<\/option>
												[#list shippingMethods as shippingMethod]
													[#noescape]
														<option value="${shippingMethod.id}"[#if shippingMethod == order.shippingMethod] selected="selected"[/#if]>${shippingMethod.name?html?js_string}<\/option>
													[/#noescape]
												[/#list]
											<\/select>
										<\/td>
										<th>
											${message("Shipping.deliveryCorp")}:
										<\/th>
										<td>
											<select name="deliveryCorpId">
												<option value="">${message("admin.common.choose")}<\/option>
												[#list deliveryCorps as deliveryCorp]
													[#noescape]
														<option value="${deliveryCorp.id}"[#if order.shippingMethod?? && deliveryCorp == order.shippingMethod.defaultDeliveryCorp] selected="selected"[/#if]>${deliveryCorp.name?html?js_string}<\/option>
													[/#noescape]
												[/#list]
											<\/select>
										<\/td>
									<\/tr>
									<tr>
										<th>
											${message("Shipping.trackingNo")}:
										<\/th>
										<td>
											<input type="text" name="trackingNo" class="text" maxlength="200" \/>
										<\/td>
										<th>
											${message("Shipping.freight")}:
										<\/th>
										<td>
											<input type="text" name="freight" class="text" maxlength="16" \/>
										<\/td>
									<\/tr>
									<tr>
										<th>
											${message("Shipping.consignee")}:
										<\/th>
										<td>
											<input type="text" name="consignee" class="text" value="[#noescape]${order.consignee?html?js_string}[/#noescape]" maxlength="200" \/>
										<\/td>
										<th>
											${message("Shipping.zipCode")}:
										<\/th>
										<td>
											<input type="text" name="zipCode" class="text" value="[#noescape]${order.zipCode?html?js_string}[/#noescape]" maxlength="200" \/>
										<\/td>
									<\/tr>
									<tr>
										<th>
											${message("Shipping.area")}:
										<\/th>
										<td>
											<span class="fieldSet">
												<input type="hidden" name="areaId" value="${(order.area.id)!}" treePath="${(order.area.treePath)!}" \/>
											<\/span>
										<\/td>
										<th>
											${message("Shipping.address")}:
										<\/th>
										<td>
											<input type="text" name="address" class="text" value="[#noescape]${order.address?html?js_string}[/#noescape]" maxlength="200" \/>
										<\/td>
									<\/tr>
									<tr>
										<th>
											${message("Shipping.phone")}:
										<\/th>
										<td>
											<input type="text" name="phone" class="text" value="[#noescape]${order.phone?html?js_string}[/#noescape]" maxlength="200" \/>
										<\/td>
										<th>
											${message("Shipping.memo")}:
										<\/th>
										<td>
											<input type="text" name="memo" class="text" maxlength="200" \/>
										<\/td>
									<\/tr>
								<\/table>
								<table class="item">
									<tr>
										<th>
											${message("ShippingItem.sn")}
										<\/th>
										<th>
											${message("ShippingItem.name")}
										<\/th>
										<th>
											${message("ShippingItem.isDelivery")}
										<\/th>
										<th>
											${message("admin.order.productStock")}
										<\/th>
										<th>
											${message("admin.order.productQuantity")}
										<\/th>
										<th>
											${message("admin.order.shippedQuantity")}
										<\/th>
										<th>
											${message("admin.order.shippingQuantity")}
										<\/th>
									<\/tr>
									[#list order.orderItemsList as orderItem]
										<tr>
											<td>
												<input type="hidden" name="shippingItems[${orderItem_index}].sn" value="${orderItem.sn}" \/>
												${orderItem.sn}
											<\/td>
											[#noescape]
												<td width="300">
													<span title="${orderItem.name?html?js_string}">${abbreviate(orderItem.name, 50, "...")?html?js_string}<\/span>
													[#if orderItem.specificationsList?has_content]
														<span class="silver">[${orderItem.specificationsList?join(", ")?html?js_string}]<\/span>
													[/#if]
													[#if orderItem.type != "0"]
														<span class="red">[${message("Goods.Type." + goodsTypes[orderItem.type])}]<\/span>
													[/#if]
												<\/td>
											[/#noescape]
											<td>
												${message(orderItem.isDelivery?string('admin.common.true', 'admin.common.false'))}
											<\/td>
											<td>
												${(orderItem.product.stock)!"-"}
											<\/td>
											<td>
												${orderItem.quantity}
											<\/td>
											<td>
												${orderItem.shippedQuantity}
											<\/td>
											<td>
												[#if orderItem.productVO?? && orderItem.productVO.stock < orderItem.shippableQuantity]
													[#assign shippingQuantity = orderItem.productVO.stock /]
												[#else]
													[#assign shippingQuantity = orderItem.shippableQuantity /]
												[/#if]
												<input type="text" name="shippingItems[${orderItem_index}].quantity" class="text shippingItemsQuantity" value="${shippingQuantity}" style="width: 30px;"[#if shippingQuantity <= 0] disabled="disabled"[/#if] max="${shippingQuantity}" data-is-delivery="${orderItem.isDelivery?string('true', 'false')}" \/>
											<\/td>
										<\/tr>
									[/#list]
								<\/table>
							<\/div>
						<\/form>'
					[/@compress]
				,
				width: 900,
				modal: true,
				ok: "${message("admin.dialog.ok")}",
				cancel: "${message("admin.dialog.cancel")}",
				onShow: function() {
					var $shippingForm = $("#shippingForm");
					var $shippingLogistics = $("#shippingLogistics");
					var $shippingItemsQuantity = $("#shippingForm input.shippingItemsQuantity");
					
					$("#shippingForm input[name='areaId']").lSelect({
						url: "${base}/common/area.jhtml"
					});
					
					function checkDelivery() {
						var isDelivery = false;
						$shippingItemsQuantity.each(function() {
							var $this = $(this);
							if ($this.data("isDelivery") && $this.val() > 0) {
								isDelivery = true;
								return false;
							}
						});
						if (isDelivery) {
							$shippingLogistics.find(":input:not([name='memo'])").prop("disabled", false);
						} else {
							$shippingLogistics.find(":input:not([name='memo'])").prop("disabled", true);
						}
					}
					
					checkDelivery();
					
					$shippingItemsQuantity.on("input propertychange change", function(event) {
						if (event.type != "propertychange" || event.originalEvent.propertyName == "value") {
							checkDelivery()
						}
					});
					
					$.validator.addClassRules({
						shippingItemsQuantity: {
							required: true,
							digits: true
						}
					});
					
					$shippingForm.validate({
						rules: {
							deliveryCorpId: "required",
							freight: {
								min: 0,
								decimal: {
									integer: 12,
									fraction: ${setting.priceScale}
								}
							},
							consignee: "required",
							zipCode: {
								required: true,
								pattern: /^\d{6}$/
							},
							areaId: "required",
							address: "required",
							phone: {
								required: true,
								pattern: /^\d{3,4}-?\d{7,9}$/
							}
						}
					});
				},
				onOk: function() {
					var total = 0;
					$("#shippingForm input.shippingItemsQuantity").each(function() {
						var quantity = $(this).val();
						if ($.isNumeric(quantity)) {
							total += parseInt(quantity);
						}
					});
					
					if (total <= 0) {
						$.message("warn", "${message("admin.order.shippingQuantityPositive")}");
					} else {
						$("#shippingForm").submit();
					}
					return false;
				}
			});
		});
	[/#if]
	
	[#if order.returnableQuantity > 0]
		// 退货
		$returnsButton.click(function() {
			$.dialog({
				title: "${message("admin.order.returns")}",
				content: 
					[@compress single_line = true]
						'<form id="returnsForm" action="returns.jhtml" method="post">
							<input type="hidden" name="token" value="${token}" \/>
							<input type="hidden" name="orderId" value="${order.id}" \/>
							<div class="returns">
								<table class="input">
									<tr>
										<th>
											${message("Order.sn")}:
										<\/th>
										<td width="300">
											${order.sn}
										<\/td>
										<th>
											${message("admin.common.createDate")}:
										<\/th>
										<td>
											${order.createDate?string("yyyy-MM-dd HH:mm:ss")}
										<\/td>
									<\/tr>
									<tr>
										<th>
											${message("Returns.shippingMethod")}:
										<\/th>
										<td>
											<select name="shippingMethodId">
												<option value="">${message("admin.common.choose")}<\/option>
												[#list shippingMethods as shippingMethod]
													[#noescape]
														<option value="${shippingMethod.id}">${shippingMethod.name?html?js_string}<\/option>
													[/#noescape]
												[/#list]
											<\/select>
										<\/td>
										<th>
											${message("Returns.deliveryCorp")}:
										<\/th>
										<td>
											<select name="deliveryCorpId">
												<option value="">${message("admin.common.choose")}<\/option>
												[#list deliveryCorps as deliveryCorp]
													[#noescape]
														<option value="${deliveryCorp.id}">${deliveryCorp.name?html?js_string}<\/option>
													[/#noescape]
												[/#list]
											<\/select>
										<\/td>
									<\/tr>
									<tr>
										<th>
											${message("Returns.trackingNo")}:
										<\/th>
										<td>
											<input type="text" name="trackingNo" class="text" maxlength="200" \/>
										<\/td>
										<th>
											${message("Returns.freight")}:
										<\/th>
										<td>
											<input type="text" name="freight" class="text" maxlength="16" \/>
										<\/td>
									<\/tr>
									<tr>
										<th>
											${message("Returns.shipper")}:
										<\/th>
										<td>
											<input type="text" name="shipper" class="text" value="[#noescape]${order.consignee?html?js_string}[/#noescape]" maxlength="200" \/>
										<\/td>
										<th>
											${message("Returns.zipCode")}:
										<\/th>
										<td>
											<input type="text" name="zipCode" class="text" value="[#noescape]${order.zipCode?html?js_string}[/#noescape]" maxlength="200" \/>
										<\/td>
									<\/tr>
									<tr>
										<th>
											${message("Returns.area")}:
										<\/th>
										<td>
											<span class="fieldSet">
												<input type="hidden" id="areaId" name="areaId" value="${(order.area.id)!}" treePath="${(order.area.treePath)!}" \/>
											<\/span>
										<\/td>
										<th>
											${message("Returns.address")}:
										<\/th>
										<td>
											<input type="text" name="address" class="text" value="[#noescape]${order.address?html?js_string}[/#noescape]" maxlength="200" \/>
										<\/td>
									<\/tr>
									<tr>
										<th>
											${message("Returns.phone")}:
										<\/th>
										<td>
											<input type="text" name="phone" class="text" value="[#noescape]${order.phone?html?js_string}[/#noescape]" maxlength="200" \/>
										<\/td>
										<th>
											${message("Returns.memo")}:
										<\/th>
										<td>
											<input type="text" name="memo" class="text" maxlength="200" \/>
										<\/td>
									<\/tr>
								<\/table>
								<table class="item">
									<tr>
										<th>
											${message("ReturnsItem.sn")}
										<\/th>
										<th>
											${message("ReturnsItem.name")}
										<\/th>
										<th>
											${message("admin.order.productStock")}
										<\/th>
										<th>
											${message("admin.order.shippedQuantity")}
										<\/th>
										<th>
											${message("admin.order.returnedQuantity")}
										<\/th>
										<th>
											${message("admin.order.returnsQuantity")}
										<\/th>
									<\/tr>
									[#list order.orderItemsList as orderItem]
										<tr>
											<td>
												<input type="hidden" name="returnsItems[${orderItem_index}].sn" value="${orderItem.sn}" \/>
												${orderItem.sn}
											<\/td>
											[#noescape]
												<td width="300">
													<span title="${orderItem.name?html?js_string}">${abbreviate(orderItem.name, 50, "...")?html?js_string}<\/span>
													[#if orderItem.specificationsList?has_content]
														<span class="silver">[${orderItem.specificationsList?join(", ")?html?js_string}]<\/span>
													[/#if]
													[#if orderItem.type != "0"]
														<span class="red">[${message("Goods.Type." + goodsTypes[orderItem.type])}]<\/span>
													[/#if]
												<\/td>
											[/#noescape]
											<td>
												${(orderItem.product.stock)!"-"}
											<\/td>
											<td>
												${orderItem.shippedQuantity}
											<\/td>
											<td>
												${orderItem.returnedQuantity}
											<\/td>
											<td>
												<input type="text" name="returnsItems[${orderItem_index}].quantity" class="text returnsItemsQuantity" value="${orderItem.returnableQuantity}" maxlength="9" style="width: 30px;"[#if orderItem.returnableQuantity <= 0] disabled="disabled"[/#if] max="${orderItem.returnableQuantity}" \/>
											<\/td>
										<\/tr>
									[/#list]
								<\/table>
							<\/div>
						<\/form>'
					[/@compress]
				,
				width: 900,
				modal: true,
				ok: "${message("admin.dialog.ok")}",
				cancel: "${message("admin.dialog.cancel")}",
				onShow: function() {
					$("#returnsForm input[name='areaId']").lSelect({
						url: "${base}/common/area.jhtml"
					});
					$.validator.addClassRules({
						returnsItemsQuantity: {
							required: true,
							digits: true
						}
					});
					$("#returnsForm").validate({
						rules: {
							freight: {
								min: 0,
								decimal: {
									integer: 12,
									fraction: ${setting.priceScale}
								}
							},
							zipCode: {
								pattern: /^\d{6}$/
							},
							phone: {
								pattern: /^\d{3,4}-?\d{7,9}$/
							}
						}
					});
				},
				onOk: function() {
					var total = 0;
					$("#returnsForm input.returnsItemsQuantity").each(function() {
						var quantity = $(this).val();
						if ($.isNumeric(quantity)) {
							total += parseInt(quantity);
						}
					});
					if (total <= 0) {
						$.message("warn", "${message("admin.order.returnsQuantityPositive")}");
					} else {
						$("#returnsForm").submit();
					}
					return false;
				}
			});
		});
	[/#if]
	
	[#if !order.hasExpired() && order.status == "3"]
		// 收货
		$receiveButton.click(function() {
			var $this = $(this);
			$.dialog({
				type: "warn",
				content: "${message("admin.order.receiveConfirm")}",
				onOk: function() {
					$receiveForm.submit();
				}
			});
		});
	[/#if]
	
	[#if !order.hasExpired() && order.status == "4"]
		// 完成
		$completeButton.click(function() {
			var $this = $(this);
			$.dialog({
				type: "warn",
				content: "${message("admin.order.completeConfirm")}",
				onOk: function() {
					$completeForm.submit();
				}
			});
		});
	[/#if]
	
	[#if !order.hasExpired() && (order.status == "2" || order.status == "3" || order.status == "4")]
		// 失败
		$failButton.click(function() {
			var $this = $(this);
			$.dialog({
				type: "warn",
				content: "${message("admin.order.failConfirm")}",
				onOk: function() {
					$failForm.submit();
				}
			});
		});
	[/#if]
	
	// 物流动态
	$transitStep.click(function() {
		var $this = $(this);
		$.ajax({
			url: "transit_step.jhtml?shippingId=" + $this.attr("shippingId"),
			type: "GET",
			dataType: "json",
			cache: true,
			beforeSend: function() {
				$this.hide().after('<span class="loadingIcon">&nbsp;<\/span>');
			},
			success: function(data) {
				if (data.message.type == "success") {
					if (data.transitSteps.length <= 0) {
						$.message("warn", "${message("admin.order.noResult")}");
						return false;
					}
					var transitStepsHtml = "";
					$.each(data.transitSteps, function(i, transitStep) {
						transitStepsHtml += 
							[@compress single_line = true]
								'<tr>
									<th>' + escapeHtml(transitStep.time) + '<\/th>
									<td>' + escapeHtml(transitStep.context) + '<\/td>
								<\/tr>'
							[/@compress]
						;
					});
					$.dialog({
						title: "${message("admin.order.transitStep")}",
						content: 
							[@compress single_line = true]
								'<div class="transitSteps">
									<table>' + transitStepsHtml + '<\/table>
								<\/div>'
							[/@compress]
						,
						width: 600,
						modal: true,
						ok: null,
						cancel: null
					});
				} else {
					$.message(data.message);
				}
			},
			complete: function() {
				$this.show().next("span.loadingIcon").remove();
			}
		});
		return false;
	});

});
</script>
</head>
<body>
	<form id="reviewForm" action="review.jhtml" method="post">
		<input type="hidden" name="id" value="${order.id}" />
		<input type="hidden" id="passed" name="passed" />
	</form>
	<form id="receiveForm" action="receive.jhtml" method="post">
		<input type="hidden" name="id" value="${order.id}" />
	</form>
	<form id="completeForm" action="complete.jhtml" method="post">
		<input type="hidden" name="id" value="${order.id}" />
	</form>
	<form id="failForm" action="fail.jhtml" method="post">
		<input type="hidden" name="id" value="${order.id}" />
	</form>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.order.view")}
	</div>
	<ul id="tab" class="tab">
		<li>
			<input type="button" value="${message("admin.order.orderInfo")}" />
		</li>
		<li>
			<input type="button" value="${message("admin.order.productInfo")}" />
		</li>
		<li>
			<input type="button" value="${message("admin.order.paymentInfo")}" />
		</li>
		<li>
			<input type="button" value="${message("admin.order.refundsInfo")}" />
		</li>
		<li>
			<input type="button" value="${message("admin.order.shippingInfo")}" />
		</li>
		<li>
			<input type="button" value="${message("admin.order.returnsInfo")}" />
		</li>
		<li>
			<input type="button" value="${message("admin.order.orderLog")}" />
		</li>
	</ul>
	<table class="input tabContent">
		<tr>
			<td>
				&nbsp;
			</td>
			<td colspan="3">
				<input type="button" id="reviewButton" class="button" value="${message("admin.order.review")}"[#if order.hasExpired() || order.status != "1"] disabled="disabled"[/#if] />
				<input type="button" id="paymentButton" class="button" value="${message("admin.order.payment")}" />
				<input type="button" id="refundsButton" class="button" value="${message("admin.order.refunds")}"[#if order.refundableAmount <= 0] disabled="disabled"[/#if] />
				<input type="button" id="shippingButton" class="button" value="${message("admin.order.shipping")}"[#if order.shippableQuantity <= 0] disabled="disabled"[/#if] />
				<input type="button" id="returnsButton" class="button" value="${message("admin.order.returns")}"[#if order.returnableQuantity <= 0] disabled="disabled"[/#if] />
				<input type="button" id="receiveButton" class="button" value="${message("admin.order.receive")}"[#if order.hasExpired() || order.status != "3"] disabled="disabled"[/#if] />
				<input type="button" id="completeButton" class="button" value="${message("admin.order.complete")}"[#if order.hasExpired() || order.status != "4"] disabled="disabled"[/#if] />
				<input type="button" id="failButton" class="button" value="${message("admin.order.fail")}"[#if order.hasExpired() || (order.status != "2" && order.status != "3" && order.status != "4")] disabled="disabled"[/#if] />
			</td>
		</tr>
		<tr>
			<th>
				${message("Order.sn")}:
			</th>
			<td width="360">
				${order.sn}
			</td>
			<th>
				${message("admin.common.createDate")}:
			</th>
			<td>
				${order.createDate?string("yyyy-MM-dd HH:mm:ss")}
			</td>
		</tr>
		<tr>
			<th>
				${message("Order.type")}:
			</th>
			<td>
				${message("Order.Type." + orderTypes[order.type])}
			</td>
			<th>
				${message("Order.status")}:
			</th>
			<td>
				${message("Order.Status." + status[order.status])}
				[#if order.hasExpired()]
					<span class="silver">(${message("admin.order.hasExpired")})</span>
				[#else]
					[#if order.expire??]
						<span class="silver">(${message("Order.expire")}: ${order.expire?string("yyyy-MM-dd HH:mm:ss")})</span>
					[/#if]
				[/#if]
			</td>
		</tr>
		<tr>
			<th>
				${message("Order.member")}:
			</th>
			<td>
				<a href="../member/view.jhtml?id=${order.memberVO.id}">${order.memberVO.username}</a>
			</td>
			<th>
				${message("Member.memberRank")}:
			</th>
			<td>
				${order.memberVO.memberRankInfo.name}
			</td>
		</tr>
		<tr>
			<th>
				${message("Order.price")}:
			</th>
			<td>
				${currency(order.price, true)}
			</td>
			<th>
				${message("Order.exchangePoint")}:
			</th>
			<td>
				${order.exchangePoint}
			</td>
		</tr>
		<tr>
			<th>
				${message("Order.amount")}:
			</th>
			<td>
				<span class="red">${currency(order.amount, true)}</span>
			</td>
			<th>
				${message("Order.amountPaid")}:
			</th>
			<td>
				${currency(order.amountPaid, true)}
				[#if order.amountPayable > 0]
					<span class="silver">(${message("Order.amountPayable")}: ${currency(order.amountPayable, true)})</span>
				[/#if]
			</td>
		</tr>
		[#if order.refundAmount > 0 || order.refundableAmount > 0]
			<tr>
				<th>
					${message("Order.refundAmount")}:
				</th>
				<td>
					${currency(order.refundAmount, true)}
				</td>
				<th>
					${message("Order.refundableAmount")}:
				</th>
				<td>
					${currency(order.refundableAmount, true)}
				</td>
			</tr>
		[/#if]
		<tr>
			<th>
				${message("Order.weight")}:
			</th>
			<td>
				${order.weight}
			</td>
			<th>
				${message("Order.quantity")}:
			</th>
			<td>
				${order.quantity}
			</td>
		</tr>
		<tr>
			<th>
				${message("Order.shippedQuantity")}:
			</th>
			<td>
				${order.shippedQuantity}
			</td>
			<th>
				${message("Order.returnedQuantity")}:
			</th>
			<td>
				${order.returnedQuantity}
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.order.promotion")}:
			</th>
			<td>
				[#if order.promotionNamesList?has_content]
					${order.promotionNamesList?join(", ")}
				[#else]
					-
				[/#if]
			</td>
			<th>
				${message("Order.promotionDiscount")}:
			</th>
			<td>
				${currency(order.promotionDiscount, true)}
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.order.coupon")}:
			</th>
			<td>
				${(order.couponCodeVO.couponInfo.name)!"-"}
			</td>
			<th>
				${message("Order.couponDiscount")}:
			</th>
			<td>
				${currency(order.couponDiscount, true)}
			</td>
		</tr>
		<tr>
			<th>
				${message("Order.fee")}:
			</th>
			<td>
				${currency(order.fee, true)}
			</td>
			<th>
				${message("Order.freight")}:
			</th>
			<td>
				${currency(order.freight, true)}
			</td>
		</tr>
		<tr>
			<th>
				${message("Order.offsetAmount")}:
			</th>
			<td>
				${currency(order.offsetAmount, true)}
			</td>
			<th>
				${message("Order.rewardPoint")}:
			</th>
			<td>
				${order.rewardPoint}
			</td>
		</tr>
		<tr>
			<th>
				${message("Order.paymentMethod")}:
			</th>
			<td>
				${order.paymentMethodName!"-"}
			</td>
			<th>
				${message("Order.shippingMethod")}:
			</th>
			<td>
				${order.shippingMethodName!"-"}
			</td>
		</tr>
		[#if order.invoiceTitle??]
			<tr>
				<th>
					${message("Invoice.title")}:
				</th>
				<td>
					${order.invoiceTitle}
				</td>
				<th>
					${message("Order.tax")}:
				</th>
				<td>
					${currency(order.tax, true)}
				</td>
			</tr>
		[/#if]
		<tr>
			<th>
				${message("Order.consignee")}:
			</th>
			<td>
				${order.consignee}
			</td>
			<th>
				${message("Order.area")}:
			</th>
			<td>
				${order.areaName}
			</td>
		</tr>
		<tr>
			<th>
				${message("Order.address")}:
			</th>
			<td>
				${order.address}
			</td>
			<th>
				${message("Order.zipCode")}:
			</th>
			<td>
				${order.zipCode}
			</td>
		</tr>
		<tr>
			<th>
				${message("Order.phone")}:
			</th>
			<td>
				${order.phone}
			</td>
			<th>
				${message("Order.memo")}:
			</th>
			<td>
				${order.memo}
			</td>
		</tr>
	</table>
	<table class="item tabContent">
		<tr>
			<th>
				${message("OrderItem.sn")}
			</th>
			<th>
				${message("OrderItem.name")}
			</th>
			<th>
				${message("OrderItem.price")}
			</th>
			<th>
				${message("OrderItem.quantity")}
			</th>
			<th>
				${message("OrderItem.subtotal")}
			</th>
		</tr>
		[#list order.orderItemsList as orderItem]
			<tr>
				<td>
					${orderItem.sn}
				</td>
				<td width="400">
					[#if orderItem.productVO??]
						<a href="${orderItem.productVO.url}" title="${orderItem.name}" target="_blank">${abbreviate(orderItem.name, 50, "...")}</a>
					[#else]
						<span title="${orderItem.name}">${abbreviate(orderItem.name, 50, "...")}</span>
					[/#if]
					[#if orderItem.specificationsList?has_content]
						<span class="silver">[${orderItem.specificationsList?join(", ")}]</span>
					[/#if]
					[#if orderItem.type != "0"]
						<span class="red">[${message("Goods.Type." + goodsTypes[orderItem.type])}]</span>
					[/#if]
				</td>
				<td>
					[#if orderItem.type == "0"]
						${currency(orderItem.price, true)}
					[#else]
						-
					[/#if]
				</td>
				<td>
					${orderItem.quantity}
				</td>
				<td>
					[#if orderItem.type == "0"]
						${currency(orderItem.subtotal, true)}
					[#else]
						-
					[/#if]
				</td>
			</tr>
		[/#list]
	</table>
	<table class="item tabContent">
		<tr>
			<th>
				${message("Payment.sn")}
			</th>
			<th>
				${message("Payment.method")}
			</th>
			<th>
				${message("Payment.paymentMethod")}
			</th>
			<th>
				${message("Payment.fee")}
			</th>
			<th>
				${message("Payment.amount")}
			</th>
			<th>
				${message("admin.common.createDate")}
			</th>
		</tr>
		[#list order.paymentsList as payment]
			<tr>
				<td>
					${payment.sn}
				</td>
				<td>
					${message("Payment.Method." + methods[payment.method])}
				</td>
				<td>
					${payment.paymentMethod!"-"}
				</td>
				<td>
					${currency(payment.fee, true)}
				</td>
				<td>
					${currency(payment.amount, true)}
				</td>
				<td>
					<span title="${payment.createDate?string("yyyy-MM-dd HH:mm:ss")}">${payment.createDate}</span>
				</td>
			</tr>
		[/#list]
	</table>
	<table class="item tabContent">
		<tr>
			<th>
				${message("Refunds.sn")}
			</th>
			<th>
				${message("Refunds.method")}
			</th>
			<th>
				${message("Refunds.paymentMethod")}
			</th>
			<th>
				${message("Refunds.amount")}
			</th>
			<th>
				${message("admin.common.createDate")}
			</th>
		</tr>
		[#list order.refundsList as refunds]
			<tr>
				<td>
					${refunds.sn}
				</td>
				<td>
					${message("Refunds.Method." + refundsMethods[refunds.method])}
				</td>
				<td>
					${refunds.paymentMethod!"-"}
				</td>
				<td>
					${currency(refunds.amount, true)}
				</td>
				<td>
					<span title="${refunds.createDate?string("yyyy-MM-dd HH:mm:ss")}">${refunds.createDate}</span>
				</td>
			</tr>
		[/#list]
	</table>
	<table class="item tabContent">
		<tr>
			<th>
				${message("Shipping.sn")}
			</th>
			<th>
				${message("Shipping.shippingMethod")}
			</th>
			<th>
				${message("Shipping.deliveryCorp")}
			</th>
			<th>
				${message("Shipping.trackingNo")}
			</th>
			<th>
				${message("Shipping.consignee")}
			</th>
			<th>
				${message("Shipping.isDelivery")}
			</th>
			<th>
				${message("admin.common.createDate")}
			</th>
		</tr>
		[#list order.shippingsList as shipping]
			<tr>
				<td>
					${shipping.sn}
				</td>
				<td>
					${shipping.shippingMethod!"-"}
				</td>
				<td>
					${shipping.deliveryCorp!"-"}
				</td>
				<td width="260">
					${shipping.trackingNo!"-"}
					[#if isKuaidi100Enabled && shipping.deliveryCorpCode?has_content && shipping.trackingNo?has_content]
						<a href="javascript:;" class="transitStep" shippingId="${shipping.id}">[${message("admin.order.transitStep")}]</a>
					[/#if]
				</td>
				<td>
					${shipping.consignee!"-"}
				</td>
				<td>
					${message(shipping.isDelivery?string('admin.common.true', 'admin.common.false'))}
				</td>
				<td>
					<span title="${shipping.createDate?string("yyyy-MM-dd HH:mm:ss")}">${shipping.createDate}</span>
				</td>
			</tr>
		[/#list]
	</table>
	<table class="item tabContent">
		<tr>
			<th>
				${message("Returns.sn")}
			</th>
			<th>
				${message("Returns.shippingMethod")}
			</th>
			<th>
				${message("Returns.deliveryCorp")}
			</th>
			<th>
				${message("Returns.trackingNo")}
			</th>
			<th>
				${message("Returns.shipper")}
			</th>
			<th>
				${message("admin.common.createDate")}
			</th>
		</tr>
		[#list order.returnsList as returns]
			<tr>
				<td>
					${returns.sn}
				</td>
				<td>
					${returns.shippingMethod!"-"}
				</td>
				<td>
					${returns.deliveryCorp!"-"}
				</td>
				<td>
					${returns.trackingNo!"-"}
				</td>
				<td>
					${returns.shipper}
				</td>
				<td>
					<span title="${returns.createDate?string("yyyy-MM-dd HH:mm:ss")}">${returns.createDate}</span>
				</td>
			</tr>
		[/#list]
	</table>
	<table class="item tabContent">
		<tr>
			<th>
				${message("OrderLog.type")}
			</th>
			<th>
				${message("OrderLog.operator")}
			</th>
			<th>
				${message("OrderLog.content")}
			</th>
			<th>
				${message("admin.common.createDate")}
			</th>
		</tr>
		[#list order.orderLogsList as orderLog]
			<tr>
				<td>
					${message("OrderLog.Type." + orderLogTypes[orderLog.type])}
				</td>
				<td>
					${orderLog.operator!"-"}
				</td>
				<td>
					[#if orderLog.content??]
						<span title="${orderLog.content}">${abbreviate(orderLog.content, 50, "...")}</span>
					[#else]
						-
					[/#if]
				</td>
				<td>
					<span title="${orderLog.createDate?string("yyyy-MM-dd HH:mm:ss")}">${orderLog.createDate}</span>
				</td>
			</tr>
		[/#list]
	</table>
	<table class="input">
		<tr>
			<th>
				&nbsp;
			</th>
			<td>
				<input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
			</td>
		</tr>
	</table>
</body>
</html>
[/#escape]