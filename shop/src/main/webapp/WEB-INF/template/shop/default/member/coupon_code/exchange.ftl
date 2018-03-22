[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("shop.member.couponCode.exchange")}[#if showPowered] - Powered By VJSHOP[/#if]</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/shop/${theme}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/shop/${theme}/css/member.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/common.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $listTable = $("#listTable");
	var $exchange = $("#listTable a.exchange");
	
	[@flash_message /]

	// 兑换
	$exchange.click(function() {
		if (confirm("${message("shop.member.couponCode.exchangeConfirm")}")) {
			var $tr = $(this).closest("tr");
			var id = $tr.find("input[name='id']").val();
			$.ajax({
				url: "exchange.jhtml",
				type: "POST",
				data: {id: id},
				dataType: "json",
				cache: false,
				success: function(message) {
					$.message(message);
					if (message.type == "success") {
						setTimeout(function() {
							location.href = "list.jhtml";
						}, 3000);
					}
				}
			});
		}
		return false;
	});

});
</script>
</head>
<body>
	[#assign current = "couponCodeExchange" /]
	[#include "/shop/${theme}/include/header.ftl" /]
	<div class="container member">
		<div class="row">
			[#include "/shop/${theme}/member/include/navigation.ftl" /]
			<div class="span10">
				<div class="list">
					<div class="title">${message("shop.member.couponCode.exchange")}</div>
					<table id="listTable" class="list">
						<tr>
							<th>
								${message("Coupon.name")}
							</th>
							<th>
								${message("Coupon.point")}
							</th>
							<th>
								${message("Coupon.beginDate")}
							</th>
							<th>
								${message("Coupon.endDate")}
							</th>
							<th>
								${message("shop.member.action")}
							</th>
						</tr>
						[#list page.content as coupon]
							<tr[#if !coupon_has_next] class="last"[/#if]>
								<td>
									<input type="hidden" name="id" value="${coupon.id}" />
									${coupon.name}
								</td>
								<td>
									${coupon.point}
								</td>
								<td>
									[#if coupon.beginDate??]
										<span title="${coupon.beginDate?string("yyyy-MM-dd HH:mm:ss")}">${coupon.beginDate}</span>
									[#else]
										-
									[/#if]
								</td>
								<td>
									[#if coupon.endDate??]
										<span title="${coupon.endDate?string("yyyy-MM-dd HH:mm:ss")}">${coupon.endDate}</span>
									[#else]
										-
									[/#if]
								</td>
								<td>
									<a href="javascript:;" class="exchange">[${message("shop.member.action.exchange")}]</a>
								</td>
							</tr>
						[/#list]
					</table>
					[#if !page.content?has_content]
						<p>${message("shop.member.noResult")}</p>
					[/#if]
				</div>
				[@pagination pageNumber = page.pageNumber totalPages = page.totalPages pattern = "?pageNumber={pageNumber}"]
					[#include "/shop/${theme}/include/pagination.ftl"]
				[/@pagination]
			</div>
		</div>
	</div>
	[#include "/shop/${theme}/include/footer.ftl" /]
</body>
</html>
[/#escape]