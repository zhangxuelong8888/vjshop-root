[#if notifyMessage??]
${notifyMessage}
[#else]
[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("shop.payment.pluginNotify")}[#if showPowered] - Powered By VJSHOP[/#if]</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/shop/${theme}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/shop/${theme}/css/payment.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/common.js"></script>
</head>
<body>
	[#include "/shop/${theme}/include/header.ftl" /]
	<div class="container payment">
		<div class="row">
			<div class="span12">
				[#if paymentLog??]
					<div class="title">
						[#if paymentLog.status == "wait"]
							${message("shop.payment.wait")}
						[#elseif paymentLog.status == "success"]
							[#if paymentLog.type == "recharge"]
								${message("shop.payment.rechargeSuccess")}
							[#elseif paymentLog.type == "payment"]
								${message("shop.payment.paymentSuccess")}
							[/#if]
						[#elseif paymentLog.status == "failure"]
							${message("shop.payment.failure")}
						[/#if]
					</div>
					<div class="bottom">
						[#if paymentLog.type == "recharge"]
							<a href="${base}/member/deposit/log.jhtml">${message("shop.payment.deposit")}</a>
						[#elseif paymentLog.type == "payment"]
							<a href="${base}/member/order/view.jhtml?sn=${paymentLog.order.sn}">${message("shop.payment.order")}</a>
						[/#if]
						| <a href="${base}/">${message("shop.payment.index")}</a>
					</div>
				[#else]
					<div class="title">
						${message("shop.payment.failure")}
					</div>
				[/#if]
			</div>
		</div>
	</div>
	[#include "/shop/${theme}/include/footer.ftl" /]
</body>
</html>
[/#escape]
[/#if]