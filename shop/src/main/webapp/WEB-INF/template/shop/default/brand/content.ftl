[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
[@seo type = "brandContent"]
	<title>[#if seo.title??][@seo.title?interpret /][#else]${brand.name}[/#if][#if showPowered] - Powered By VJSHOP[/#if]</title>
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
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/common.js"></script>
</head>
<body>
	[#include "/shop/${theme}/include/header.ftl" /]
	<div class="container brandContent">
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
						<li>
							<a href="${base}/brand/list/1.jhtml">${message("shop.brand.title")}</a>
						</li>
					</ul>
				</div>
				<div class="top">
					[#if brand.enumType == "image"]
						<img src="${brand.logo}" alt="${brand.name}" />
					[/#if]
					<strong>${brand.name}</strong>
					[#if brand.url??]
						<a href="${brand.url}" target="_blank">${brand.url}</a>
					[/#if]
				</div>
				<div class="introduction">
					<div class="title">${message("Brand.introduction")}</div>
					[#noescape]
						${brand.introduction}
					[/#noescape]
				</div>
				[#if brand.productCategories?has_content]
					<div class="goods">
						<dl>
							<dt>
								<a href="${base}/goods/list.jhtml?brandId=${brand.id}">${message("shop.brand.goods")}</a>
							</dt>
							[#list brand.productCategories as productCategory]
								<dd>
									<a href="${base}${productCategory.path}?brandId=${brand.id}">${productCategory.name}</a>
								</dd>
								[#if productCategory_index == 5]
									[#break /]
								[/#if]
							[/#list]
						</dl>
					</div>
				[/#if]
			</div>
		</div>
	</div>
	[#include "/shop/${theme}/include/footer.ftl" /]
</body>
</html>
[/#escape]