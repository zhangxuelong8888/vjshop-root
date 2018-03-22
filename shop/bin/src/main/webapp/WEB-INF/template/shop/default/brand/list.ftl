[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
[@seo type = "brandList"]
	<title>[#if seo.title??][@seo.title?interpret /][#else]${message("shop.brand.title")}[/#if][#if showPowered] - Powered By VJSHOP[/#if]</title>
	<meta name="author" content="VJSHOP Team" />
	<meta name="copyright" content="VJSHOP" />
	[#if seo.keywords??]
		<meta name="keywords" content="[@seo.keywords?interpret /]" />
	[/#if]
	[#if seo.description??]
		<meta name="description" content="[@seo.description?interpret /]" />
	[/#if]
[/@seo]
<link href="${base}/resources/shop/${theme}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/shop/${theme}/css/goods.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/shop/${theme}/css/brand.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.lazyload.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/common.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $logo = $("#list img");

	$logo.lazyload({
		threshold: 100,
		effect: "fadeIn"
	});

});
</script>
</head>
<body>
	[#include "/shop/${theme}/include/header.ftl" /]
	<div class="container brandList">
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
						<li>${message("shop.brand.title")}</li>
					</ul>
				</div>
				<div id="list" class="list clearfix">
					[#if page.content?has_content]
						<ul>
							[#list page.content?sort_by("type")?chunk(5) as row]
								[#list row as brand]
									<li[#if !row_has_next] class="last"[/#if]>
										<a href="${base}${brand.path}">
											<img src="${base}/upload/image/blank.gif"[#if brand.logo?has_content] data-original="${brand.logo}"[/#if] />
											<span title="${brand.name}">${brand.name}</span>
										</a>
									</li>
								[/#list]
							[/#list]
						</ul>
					[#else]
						[#noescape]
							${message("shop.brand.noResult")}
						[/#noescape]
					[/#if]
				</div>
				[@pagination pageNumber = page.pageNumber totalPages = page.totalPages pattern = "{pageNumber}.jhtml"]
					[#include "/shop/${theme}/include/pagination.ftl"]
				[/@pagination]
			</div>
		</div>
	</div>
	[#include "/shop/${theme}/include/footer.ftl" /]
</body>
</html>
[/#escape]