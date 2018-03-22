[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("shop.member.order.view")}[#if showPowered] - Powered By VJSHOP[/#if]</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/shop/${theme}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/shop/${theme}/css/member.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/common.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $dialogOverlay = $("#dialogOverlay");
	var $dialog = $("#dialog");
	var $close = $("#close");
	var $transitStepTable = $("#transitSteps table");
	var $payment = $("#payment");
	var $cancel = $("#cancel");
	var $receive = $("#receive");
	var $transitStep = $("a.transitStep");
	
	[@flash_message /]
	
	// 订单支付
	$payment.click(function() {
		$.ajax({
			url: "check_lock.jhtml",
			type: "POST",
			data: {id: ${order.id}},
			dataType: "json",
			cache: false,
			success: function(message) {
				if (message.type == "success") {
					location.href = "${base}/order/payment.jhtml?sn=${order.sn}";
				} else {
					$.message(message);
				}
			}
		});
		return false;
	});
	
	// 订单取消
	$cancel.click(function() {
		if (confirm("${message("shop.member.order.cancelConfirm")}")) {
			$.ajax({
				url: "cancel.jhtml?sn=${order.sn}",
				type: "POST",
				dataType: "json",
				cache: false,
				success: function(message) {
					if (message.type == "success") {
						location.reload(true);
					} else {
						$.message(message);
					}
				}
			});
		}
		return false;
	});
	
	// 订单收货
	$receive.click(function() {
		if (confirm("${message("shop.member.order.receiveConfirm")}")) {
			$.ajax({
				url: "receive.jhtml?sn=${order.sn}",
				type: "POST",
				dataType: "json",
				cache: false,
				success: function(message) {
					if (message.type == "success") {
						location.reload(true);
					} else {
						$.message(message);
					}
				}
			});
		}
		return false;
	});
	
	// 物流动态
	$transitStep.click(function() {
		var $this = $(this);
		$.ajax({
			url: "transit_step.jhtml?shippingSn=" + $this.attr("shippingSn"),
			type: "GET",
			dataType: "json",
			cache: true,
			beforeSend: function() {
				$this.hide().after('<span class="loadingIcon">&nbsp;<\/span>');
			},
			success: function(data) {
				if (data.message.type == "success") {
					if (data.transitSteps.length <= 0) {
						$.message("warn", "${message("shop.member.noResult")}");
						return false;
					}
					$dialog.show();
					$dialogOverlay.show();
					$transitStepTable.empty();
					$.each(data.transitSteps, function(i, transitStep) {
						$transitStepTable.append(
							[@compress single_line = true]
								'<tr>
									<th>' + escapeHtml(transitStep.time) + '<\/th>
									<td>' + escapeHtml(transitStep.context) + '<\/td>
								<\/tr>'
							[/@compress]
						);
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
	
	// 关闭物流动态
	$close.click(function() {
		$dialog.hide();
		$dialogOverlay.hide();
	});

});
</script>
</head>
<body>
	<div id="dialogOverlay" class="dialogOverlay"></div>
	[#assign current = "orderList" /]
	[#include "/shop/${theme}/include/header.ftl" /]
	<div class="container member">
		<div class="row">
			[#include "/shop/${theme}/member/include/navigation.ftl" /]
			<div class="span10">
				<div class="input order">
					<div id="dialog" class="dialog">
						<div id="close" class="close"></div>
						<ul>
							<li>${message("shop.member.order.time")}</li>
							<li>${message("shop.member.order.content")}</li>
						</ul>
						<div id="transitSteps" class="transitSteps">
							<table></table>
						</div>
					</div>
					<div class="title">${message("shop.member.order.view")}</div>
					<div class="top">
						<span>${message("Order.sn")}: ${order.sn}</span>
						<span>
							[#if order.hasExpired()]
								${message("Order.status")}: <strong>${message("shop.member.order.hasExpired")}</strong>
							[#else]
								${message("Order.status")}: <strong>${message("Order.Status." + order.statusName())}</strong>
							[/#if]
						</span>
						<div class="action">
							[#if order.paymentMethod?? && order.amountPayable > 0]
								<a href="javascript:;" id="payment" class="button">${message("shop.member.order.payment")}</a>
							[/#if]
							[#if !order.hasExpired() && (order.statusName() == "pendingPayment" || order.statusName() == "pendingReview")]
								<a href="javascript:;" id="cancel" class="button">${message("shop.member.order.cancel")}</a>
							[/#if]
							[#if !order.hasExpired() && order.statusName() == "shipped"]
								<a href="javascript:;" id="receive" class="button">${message("shop.member.order.receive")}</a>
							[/#if]
						</div>
						<div class="tips">
							[#if order.hasExpired()]
								${message("shop.member.order.hasExpiredTips")}
							[#elseif order.statusName() == "pendingPayment"]
								${message("shop.member.order.pendingPaymentTips")}
							[#elseif order.statusName() == "pendingReview"]
								${message("shop.member.order.pendingReviewTips")}
							[#elseif order.statusName() == "pendingShipment"]
								${message("shop.member.order.pendingShipmentTips")}
							[#elseif order.statusName() == "shipped"]
								${message("shop.member.order.shippedTips")}
							[#elseif order.statusName() == "received"]
								${message("shop.member.order.receivedTips")}
							[#elseif order.statusName() == "completed"]
								${message("shop.member.order.completedTips")}
							[#elseif order.statusName() == "failed"]
								${message("shop.member.order.failedTips")}
							[#elseif order.statusName() == "canceled"]
								${message("shop.member.order.canceledTips")}
							[#elseif order.statusName() == "denied"]
								${message("shop.member.order.deniedTips")}
							[/#if]
							[#if order.expire?? && !order.hasExpired()]
								<em>(${message("Order.expire")}: ${order.expire?string("yyyy-MM-dd HH:mm:ss")})</em>
							[/#if]
						</div>
					</div>
					<table class="info">
						<tr>
							<th>
								${message("shop.common.createDate")}:
							</th>
							<td>
								${order.createDate?string("yyyy-MM-dd HH:mm:ss")}
							</td>
						</tr>
						[#if order.paymentMethodName??]
							<tr>
								<th>
									${message("Order.paymentMethod")}:
								</th>
								<td>
									${order.paymentMethodName}
								</td>
							</tr>
						[/#if]
						[#if order.shippingMethodName??]
							<tr>
								<th>
									${message("Order.shippingMethod")}:
								</th>
								<td>
									${order.shippingMethodName}
								</td>
							</tr>
						[/#if]
						<tr>
							<th>
								${message("Order.price")}:
							</th>
							<td>
								${currency(order.price, true)}
							</td>
						</tr>
						[#if order.fee > 0]
							<tr>
								<th>
									${message("Order.fee")}:
								</th>
								<td>
									${currency(order.fee, true)}
								</td>
							</tr>
						[/#if]
						[#if order.freight > 0]
							<tr>
								<th>
									${message("Order.freight")}:
								</th>
								<td>
									${currency(order.freight, true)}
								</td>
							</tr>
						[/#if]
						[#if order.promotionDiscount > 0]
							<tr>
								<th>
									${message("Order.promotionDiscount")}:
								</th>
								<td>
									${currency(order.promotionDiscount, true)}
								</td>
							</tr>
						[/#if]
						[#if order.couponDiscount > 0]
							<tr>
								<th>
									${message("Order.couponDiscount")}:
								</th>
								<td>
									${currency(order.couponDiscount, true)}
								</td>
							</tr>
						[/#if]
						[#if order.offsetAmount != 0]
							<tr>
								<th>
									${message("Order.offsetAmount")}:
								</th>
								<td>
									${currency(order.offsetAmount, true)}
								</td>
							</tr>
						[/#if]
						<tr>
							<th>
								${message("Order.amount")}:
							</th>
							<td>
								${currency(order.amount, true)}
							</td>
						</tr>
						[#if order.amountPaid > 0]
							<tr>
								<th>
									${message("Order.amountPaid")}:
								</th>
								<td>
									${currency(order.amountPaid, true)}
								</td>
							</tr>
						[/#if]
						[#if order.refundAmount > 0]
							<tr>
								<th>
									${message("Order.refundAmount")}:
								</th>
								<td>
									${currency(order.refundAmount, true)}
								</td>
							</tr>
						[/#if]
						[#if order.amountPayable > 0]
							<tr>
								<th>
									${message("Order.amountPayable")}:
								</th>
								<td>
									${currency(order.amountPayable, true)}
								</td>
							</tr>
						[/#if]
						[#if order.rewardPoint > 0]
							<tr>
								<th>
									${message("Order.rewardPoint")}:
								</th>
								<td>
									${order.rewardPoint}
								</td>
							</tr>
						[/#if]
						[#if order.exchangePoint > 0]
							<tr>
								<th>
									${message("Order.exchangePoint")}:
								</th>
								<td>
									${order.exchangePoint}
								</td>
							</tr>
						[/#if]
						[#if order.couponCode??]
							<tr>
								<th>
									${message("shop.member.order.coupon")}:
								</th>
								<td>
									${order.couponCode.coupon.name}
								</td>
							</tr>
						[/#if]
						[#if order.promotionNamesList?has_content]
							<tr>
								<th>
									${message("shop.member.order.promotion")}:
								</th>
								<td>
									${order.promotionNamesList?join(", ")}
								</td>
							</tr>
						[/#if]
						<tr>
							<th>
								${message("Order.memo")}:
							</th>
							<td>
								${order.memo}
							</td>
						</tr>
					</table>
					[#if order.invoice??]
						<table class="info">
							<tr>
								<th>
									${message("Invoice.title")}:
								</th>
								<td>
									${order.invoice.title}
								</td>
							</tr>
							<tr>
								<th>
									${message("Order.tax")}:
								</th>
								<td>
									${currency(order.tax, true)}
								</td>
							</tr>
						</table>
					[/#if]
					[#if order.isDelivery]
						<table class="info">
							<tr>
								<th>
									${message("Order.consignee")}:
								</th>
								<td>
									${order.consignee}
								</td>
							</tr>
							<tr>
								<th>
									${message("Order.zipCode")}:
								</th>
								<td>
									${order.zipCode}
								</td>
							</tr>
							<tr>
								<th>
									${message("Order.address")}:
								</th>
								<td>
									${order.areaName}${order.address}
								</td>
							</tr>
							<tr>
								<th>
									${message("Order.phone")}:
								</th>
								<td>
									${order.phone}
								</td>
							</tr>
						</table>
					[/#if]
					[#if order.shippingsList?has_content]
						<table class="info">
							[#list order.shippingsList as shipping]
								<tr>
									<th>
										${message("Shipping.deliveryCorp")}:
									</th>
									<td>
										[#if shipping.deliveryCorpUrl??]
											<a href="${shipping.deliveryCorpUrl}" target="_blank">${shipping.deliveryCorp}</a>
										[#else]
											${shipping.deliveryCorp!"-"}
										[/#if]
									</td>
									<th>
										${message("Shipping.trackingNo")}:
									</th>
									<td width="260">
										${shipping.trackingNo!"-"}
										[#if isKuaidi100Enabled && shipping.deliveryCorpCode?has_content && shipping.trackingNo?has_content]
											<a href="javascript:;" class="transitStep" shippingSn="${shipping.sn}">[${message("shop.member.order.transitStep")}]</a>
										[/#if]
									</td>
									<th>
										${message("shop.member.order.deliveryDate")}:
									</th>
									<td>
										${shipping.createDate?string("yyyy-MM-dd HH:mm")}
									</td>
								</tr>
							[/#list]
						</table>
					[/#if]
					<table class="orderItem">
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
								<td>
									[#if orderItem.product??]
										<a href="${orderItem.productVO.url}" title="${orderItem.name}" target="_blank">${abbreviate(orderItem.name, 30)}</a>
									[#else]
										<span title="${orderItem.name}">${abbreviate(orderItem.name, 30)}</span>
									[/#if]
									[#if orderItem.specificationsList?has_content]
										<span class="silver">[${orderItem.specificationsList?join(", ")}]</span>
									[/#if]
									[#if orderItem.type != "general"]
										<span class="red">[${message("Goods.Type." + orderItem.typeName())}]</span>
									[/#if]
								</td>
								<td>
									[#if orderItem.typeName() == "general"]
										${currency(orderItem.price, true)}
									[#else]
										-
									[/#if]
								</td>
								<td>
									${orderItem.quantity}
								</td>
								<td>
									[#if orderItem.typeName() == "general"]
										${currency(orderItem.subtotal, true)}
									[#else]
										-
									[/#if]
								</td>
							</tr>
						[/#list]
					</table>
				</div>
			</div>
		</div>
	</div>
	[#include "/shop/${theme}/include/footer.ftl" /]
</body>
</html>
[/#escape]