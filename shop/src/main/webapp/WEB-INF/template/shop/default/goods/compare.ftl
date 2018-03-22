[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("shop.goods.compare")}</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/shop/${theme}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/shop/${theme}/css/goods.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/common.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $parameterTr = $("table.parameterTable tr");
	
	$parameterTr.hover(
		function() {
			var $this = $(this);
			var group = $this.data("group");
			var name = $this.data("name");
			$parameterTr.filter("[data-group='" + group + "'][data-name='" + name + "']").addClass("current");
		},
		function() {
			$parameterTr.removeClass("current");
		}
	);

});
</script>
</head>
<body>
	[#include "/shop/${theme}/include/header.ftl" /]
	<div class="container goodsCompare">
		<div class="row">
			<div class="span2">
				[#include "/shop/${theme}/include/hot_product_category.ftl" /]
				[#include "/shop/${theme}/include/hot_brand.ftl" /]
				[#include "/shop/${theme}/include/hot_goods.ftl" /]
				[#include "/shop/${theme}/include/hot_promotion.ftl" /]
			</div>
			<div class="span10">
				<div class="breadcrumb">
					<ul>
						<li>
							<a href="${base}/">${message("shop.breadcrumb.home")}</a>
						</li>
						<li>${message("shop.goods.compare")}</li>
					</ul>
				</div>
				<ul class="main">
					[#list goodsList as goods]
						<li>
							<table>
								<tr>
									<td>
										<a href="${goods.url}" target="_blank">${goods.name}</a>
									</td>
								</tr>
								<tr>
									<td>
										<img src="${goods.thumbnail!setting.defaultThumbnailProductImage}" alt="${goods.name}" />
									</td>
								</tr>
								<tr>
									<td>
										<strong>${currency(goods.price, true)}</strong>
									</td>
								</tr>
							</table>
							[#if goods.parameterValuesList?has_content]
								<table class="parameterTable">
									[#list goods.parameterValuesList as parameterValue]
										<tr>
											<th class="group" colspan="2">${parameterValue.group}</th>
										</tr>
										[#list parameterValue.entries as entry]
											<tr data-group="${parameterValue.group}" data-name="${entry.name}">
												<th>${entry.name}</th>
												<td>${entry.value}</td>
											</tr>
										[/#list]
									[/#list]
								</table>
							[/#if]
						</li>
					[/#list]
				</ul>
			</div>
		</div>
	</div>
	[#include "/shop/${theme}/include/footer.ftl" /]
</body>
</html>
[/#escape]