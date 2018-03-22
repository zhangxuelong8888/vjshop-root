[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${goods.name} ${message("shop.consultation.title")}[#if showPowered] - Powered By VJSHOP[/#if]</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/shop/${theme}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/shop/${theme}/css/goods.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/shop/${theme}/css/consultation.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/common.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $addConsultation = $("#addConsultation");
	
	[#if setting.consultationAuthority != "anyone"]
		$addConsultation.click(function() {
			if ($.checkLogin()) {
				return true;
			} else {
				$.redirectLogin("${base}/consultation/add/${goods.id}.jhtml", "${message("shop.consultation.accessDenied")}");
				return false;
			}
		});
	[/#if]
	
});
</script>
</head>
<body>
	[#include "/shop/${theme}/include/header.ftl" /]
	<div class="container consultation">
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
							<a href="${goods.url}">${abbreviate(goods.name, 30)}</a>
						</li>
						<li>${message("shop.consultation.title")}</li>
					</ul>
				</div>
				<table class="info">
					<tr>
						<th rowspan="3">
							<img src="${goods.thumbnail!setting.defaultThumbnailProductImage}" alt="${goods.name}" />
						</th>
						<td>
							<a href="${goods.url}">${abbreviate(goods.name, 50, "...")}</a>
						</td>
						<td class="action" rowspan="3">
							<a href="${base}/consultation/add/${goods.id}.jhtml" id="addConsultation">[${message("shop.consultation.add")}]</a>
						</td>
					</tr>
					<tr>
						<td>
							${message("Goods.price")}: <strong>${currency(goods.price, true, true)}</strong>
						</td>
					</tr>
					<tr>
						<td>
							[#if goods.scoreCount > 0]
								<div>${message("Goods.score")}: </div>
								<div class="score${(goods.score * 2)?string("0")}"></div>
								<div>${goods.score?string("0.0")}</div>
							[#else]
								[#if setting.isShowMarketPrice]
									${message("Goods.marketPrice")}:
									<del>${currency(goods.marketPrice, true, true)}</del>
								[/#if]
							[/#if]
						</td>
					</tr>
				</table>
				<div class="content">
					[#if page.content?has_content]
						<ul>
							[#list page.content as consultation]
								<li[#if !consultation_has_next] class="last"[/#if]>
									${consultation.content}
									<span>
										[#if consultation.memberInfo??]
											${consultation.memberInfo.username}
										[#else]
											${message("shop.consultation.anonymous")}
										[/#if]
										<span title="${consultation.createDate?string("yyyy-MM-dd HH:mm:ss")}">${consultation.createDate}</span>
									</span>
									[#if consultation.replyConsultations?has_content]
										<div class="arrow"></div>
										<ul>
											[#list consultation.replyConsultations as replyConsultation]
												<li>
													${replyConsultation.content}
													<span title="${replyConsultation.createDate?string("yyyy-MM-dd HH:mm:ss")}">${replyConsultation.createDate}</span>
												</li>
											[/#list]
										</ul>
									[/#if]
								</li>
							[/#list]
						</ul>
					[#else]
						<p>${message("shop.consultation.noResult")}</p>
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