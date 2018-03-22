[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
[@seo type = "articleSearch"]
	<title>[#if seo.title??][@seo.title?interpret /][/#if][#if showPowered] - Powered By VJSHOP[/#if]</title>
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
<link href="${base}/resources/shop/${theme}/css/article.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/common.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $articleSearchForm = $("#articleSearchForm");
	var $keyword = $("#articleSearchForm input");
	var defaultKeyword = $keyword.val();
	
	$keyword.focus(function() {
		if ($.trim($keyword.val()) == defaultKeyword) {
			$keyword.val("");
		}
	});
	
	$keyword.blur(function() {
		if ($.trim($keyword.val()) == "") {
			$keyword.val(defaultKeyword);
		}
	});
	
	$articleSearchForm.submit(function() {
		if ($.trim($keyword.val()) == "" || $.trim($keyword.val()) == defaultKeyword) {
			return false;
		}
	});

});
</script>
</head>
<body>
	[#include "/shop/${theme}/include/header.ftl" /]
	<div class="container articleList">
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
						<li>${message("shop.article.searchPath", articleKeyword)}</li>
					</ul>
				</div>
				<div class="result">
					[#if page.content?has_content]
						<ul>
							[#list page.content as article]
								<li>
									<a href="${article.url}" title="${article.title}">${abbreviate(article.title, 80, "...")}</a>
									${article.author}
									<span title="${article.createDate?string("yyyy-MM-dd HH:mm:ss")}">${article.createDate}</span>
									<p>${abbreviate(article.text, 220, "...")}</p>
								</li>
							[/#list]
						</ul>
					[#else]
						[#noescape]
							${message("shop.article.noSearchResult", articleKeyword)}
						[/#noescape]
					[/#if]
				</div>
				[@pagination pageNumber = page.pageNumber totalPages = page.totalPages pattern = "search.jhtml?keyword=${articleKeyword?url}&pageNumber={pageNumber}"]
					[#include "/shop/${theme}/include/pagination.ftl"]
				[/@pagination]
			</div>
		</div>
	</div>
	[#include "/shop/${theme}/include/footer.ftl" /]
</body>
</html>
[/#escape]