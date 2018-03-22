[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("shop.member.review.list")}[#if showPowered] - Powered By VJSHOP[/#if]</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/shop/${theme}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/shop/${theme}/css/member.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/common.js"></script>
<script type="text/javascript">
$().ready(function() {

	[@flash_message /]

});
</script>
</head>
<body>
	[#assign current = "reviewList" /]
	[#include "/shop/${theme}/include/header.ftl" /]
	<div class="container member">
		<div class="row">
			[#include "/shop/${theme}/member/include/navigation.ftl" /]
			<div class="span10">
				<div class="list">
					<div class="title">${message("shop.member.review.list")}</div>
					<table class="list">
						<tr>
							<th>
								${message("shop.member.review.productImage")}
							</th>
							<th>
								${message("Review.goods")}
							</th>
							<th>
								${message("Review.score")}
							</th>
							<th>
								${message("shop.common.createDate")}
							</th>
						</tr>
						[#list page.content as review]
							<tr[#if !review_has_next] class="last"[/#if]>
								<td>
									<input type="hidden" name="id" value="${review.id}" />
									<img src="${review.goodsInfo.thumbnail!setting.defaultThumbnailProductImage}" class="thumbnail" alt="${review.goodsInfo.name}" />
								</td>
								<td>
									<a href="${review.goodsInfo.url}#review" title="${review.goodsInfo.name}" target="_blank">${abbreviate(review.goodsInfo.name, 30)}</a>
								</td>
								<td>
									${review.score}
								</td>
								<td>
									<span title="${review.createDate?string("yyyy-MM-dd HH:mm:ss")}">${review.createDate}</span>
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