[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("shop.member.consultation.list")}[#if showPowered] - Powered By VJSHOP[/#if]</title>
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
	[#assign current = "consultationList" /]
	[#include "/shop/${theme}/include/header.ftl" /]
	<div class="container member">
		<div class="row">
			[#include "/shop/${theme}/member/include/navigation.ftl" /]
			<div class="span10">
				<div class="list">
					<div class="title">${message("shop.member.consultation.list")}</div>
					<table class="list">
						<tr>
							<th>
								${message("shop.member.consultation.productImage")}
							</th>
							<th>
								${message("Consultation.goods")}
							</th>
							<th>
								${message("shop.common.createDate")}
							</th>
						</tr>
						[#list page.content as consultation]
							<tr[#if !consultation_has_next] class="last"[/#if]>
								<td>
									<img src="${consultation.goodsInfo.thumbnail!setting.defaultThumbnailProductImage}" class="thumbnail" alt="${consultation.goodsInfo.name}" />
								</td>
								<td>
									<a href="${consultation.goodsInfo.url}#consultation" title="${consultation.goodsInfo.name}" target="_blank">${abbreviate(consultation.goodsInfo.name, 30)}</a>
								</td>
								<td>
									<span title="${consultation.createDate?string("yyyy-MM-dd HH:mm:ss")}">${consultation.createDate}</span>
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