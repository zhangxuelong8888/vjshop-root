[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
[@seo type = "articleContent"]
	<title>[#if article.seoTitle??]${article.seoTitle}[#elseif seo.title??][@seo.title?interpret /][/#if][#if showPowered] - Powered By VJSHOP[/#if]</title>
	<meta name="author" content="VJSHOP Team" />
	<meta name="copyright" content="VJSHOP" />
	[#if article.seoKeywords??]
		<meta name="keywords" content="${article.seoKeywords}" />
	[#elseif seo.keywords??]
		<meta name="keywords" content="[@seo.keywords?interpret /]" />
	[/#if]
	[#if article.seoDescription??]
		<meta name="description" content="${article.seoDescription}" />
	[#elseif seo.description??]
		<meta name="description" content="[@seo.description?interpret /]" />
	[/#if]
[/@seo]
<link href="${base}/resources/shop/${theme}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/shop/${theme}/css/article.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/common.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $hits = $("#hits");
	var $articleSearchForm = $("#articleSearchForm");
	var $keyword = $("#articleSearchForm input");
	
	// 查看点击数
	$.ajax({
		url: "${base}/article/hits/${article.id}.jhtml",
		type: "GET",
		cache: true,
		success: function(data) {
			$hits.text(data);
		}
	});
	
	$articleSearchForm.submit(function() {
		if ($.trim($keyword.val()) == "") {
			return false;
		}
	});

});
</script>
</head>
<body>
	[#assign articleCategory = article.category /]
	[#include "/shop/${theme}/include/header.ftl" /]
	<div class="container articleContent">
		<div class="row">
			<div class="span2">
				[#include "/shop/${theme}/include/hot_article_category.ftl" /]
				[#include "/shop/${theme}/include/hot_article.ftl" /]
				[#include "/shop/${theme}/include/article_search.ftl" /]
			</div>
			<div class="span10">
				<div class="breadcrumb">
					<ul>
						<li>
							<a href="${base}/">${message("shop.breadcrumb.home")}</a>
						</li>
						[@article_category_parent_list articleCategoryId = articleCategory.id]
							[#list articleCategories as articleCategory]
								<li>
									<a href="${base}${articleCategory.path}">${articleCategory.name}</a>
								</li>
							[/#list]
						[/@article_category_parent_list]
						<li>
							<a href="${base}${articleCategory.path}">${articleCategory.name}</a>
						</li>
					</ul>
				</div>
				<div class="main">
					<h1 class="title">${article.title}[#if pageNumber > 1] (${pageNumber})[/#if]</h1>
					<div class="info">
						${message("shop.article.createDate")}: ${article.createDate?string("yyyy-MM-dd HH:mm:ss")}
						${message("shop.article.author")}: ${article.author}
						${message("shop.article.hits")}: <span id="hits">&nbsp;</span>
					</div>
					[#noescape]
						<div class="content">${article.getPageContent(pageNumber)}</div>
					[/#noescape]
				</div>
				[@pagination pageNumber = pageNumber totalPages = article.totalPages pattern = "{pageNumber}.jhtml"]
					[#include "/shop/${theme}/include/pagination.ftl"]
				[/@pagination]
			</div>
		</div>
	</div>
	[#include "/shop/${theme}/include/footer.ftl" /]
</body>
</html>
[/#escape]