[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("shop.member.index")}[#if showPowered] - Powered By VJSHOP[/#if]</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/shop/${theme}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/shop/${theme}/css/member.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/common.js"></script>
</head>
<body>
	[#include "/shop/${theme}/include/header.ftl" /]
	<div class="container member">
		<div class="row">
			[#include "/shop/${theme}/member/include/navigation.ftl" /]
			<div class="span10">
				<div class="index">
					<div class="top clearfix">
						<div>
							<ul>
								<li>
									${message("shop.member.index.memberRank")}: ${member.memberRankInfo.name}
								</li>
								<li>
									${message("shop.member.index.balance")}:
									<strong>${currency(member.balance, true, true)}</strong>
								</li>
								<li>
									${message("shop.member.index.amount")}:
									<strong>${currency(member.amount, true, true)}</strong>
								</li>
								<li>
									${message("shop.member.index.point")}:
									<em>${member.point}</em>
									<a href="coupon_code/exchange.jhtml" class="silver">${message("shop.member.index.exchange")}</a>
								</li>
							</ul>
							<ul>
								<li>
									<a href="order/list.jhtml?status=pendingPayment">${message("shop.member.index.pendingPaymentOrderCount")}(<em>${pendingPaymentOrderCount}</em>)</a>
									<a href="order/list.jhtml?status=pendingShipment">${message("shop.member.index.pendingShipmentOrderCount")}(<em>${pendingShipmentOrderCount}</em>)</a>
								</li>
								<li>
									<a href="message/list.jhtml">${message("shop.member.index.messageCount")}(<em>${messageCount}</em>)</a>
									<a href="coupon_code/list.jhtml">${message("shop.member.index.couponCodeCount")}(<em>${couponCodeCount}</em>)</a>
								</li>
								<li>
									<a href="favorite/list.jhtml">${message("shop.member.index.favoriteCount")}(<em>${favoriteCount}</em>)</a>
									<a href="product_notify/list.jhtml">${message("shop.member.index.productNotifyCount")}(<em>${productNotifyCount}</em>)</a>
								</li>
								<li>
									<a href="review/list.jhtml">${message("shop.member.index.reviewCount")}(<em>${reviewCount}</em>)</a>
									<a href="consultation/list.jhtml">${message("shop.member.index.consultationCount")}(<em>${consultationCount}</em>)</a>
								</li>
							</ul>
						</div>
					</div>
					<div class="list">
						<div class="title">${message("shop.member.index.newOrder")}</div>
						<table class="list">
							<tr>
								<th>
									${message("Order.sn")}
								</th>
								<th>
									${message("OrderItem.product")}
								</th>
								<th>
									${message("Order.consignee")}
								</th>
								<th>
									${message("Order.amount")}
								</th>
								<th>
									${message("Order.status")}
								</th>
								<th>
									${message("shop.common.createDate")}
								</th>
								<th>
									${message("shop.member.action")}
								</th>
							</tr>
							[#list newOrders as order]
								<tr[#if !order_has_next] class="last"[/#if]>
									<td>
										${order.sn}
									</td>
									<td>
										[#list order.orderItemsList as orderItem]
											<img src="${orderItem.thumbnail!setting.defaultThumbnailProductImage}" class="thumbnail" alt="${orderItem.name}" />
											[#if orderItem_index == 2]
												[#break /]
											[/#if]
										[/#list]
									</td>
									<td>
										${order.consignee!"-"}
									</td>
									<td>
										${currency(order.amount, true)}
									</td>
									<td>
										[#if order.hasExpired()]
											${message("shop.member.order.hasExpired")}
										[#else]
											${message("Order.Status." + order.enumStatus)}
										[/#if]
									</td>
									<td>
										<span title="${order.createDate?string("yyyy-MM-dd HH:mm:ss")}">${order.createDate}</span>
									</td>
									<td>
										<a href="order/view.jhtml?sn=${order.sn}">[${message("shop.member.action.view")}]</a>
									</td>
								</tr>
							[/#list]
						</table>
						[#if !newOrders?has_content]
							<p>${message("shop.member.noResult")}</p>
						[/#if]
					</div>
				</div>
			</div>
		</div>
		[#include "/shop/${theme}/include/footer.ftl" /]
	</div>
</body>
</html>
[/#escape]