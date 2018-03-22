[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("shop.member.favorite.list")}[#if showPowered] - Powered By VJSHOP[/#if]</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/shop/${theme}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/shop/${theme}/css/member.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/common.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $listTable = $("#listTable");
	var $delete = $("#listTable a.delete");
	
	[@flash_message /]

	// 删除
	$delete.click(function() {
		if (confirm("${message("shop.dialog.deleteConfirm")}")) {
			var $tr = $(this).closest("tr");
			var id = $tr.find("input[name='id']").val();
			$.ajax({
				url: "delete.jhtml",
				type: "POST",
				data: {id: id},
				dataType: "json",
				cache: false,
				success: function(message) {
					$.message(message);
					if (message.type == "success") {
						var $siblings = $tr.siblings();
						if ($siblings.size() <= 1) {
							$listTable.after('<p>${message("shop.member.noResult")}<\/p>');
						} else {
							$siblings.last().addClass("last");
						}
						$tr.remove();
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
	[#assign current = "favoriteList" /]
	[#include "/shop/${theme}/include/header.ftl" /]
	<div class="container member">
		<div class="row">
			[#include "/shop/${theme}/member/include/navigation.ftl" /]
			<div class="span10">
				<div class="list">
					<div class="title">${message("shop.member.favorite.list")}</div>
					<table id="listTable" class="list">
						<tr>
							<th>
								${message("shop.member.consultation.productImage")}
							</th>
							<th>
								${message("Product.sn")}
							</th>
							<th>
								${message("Product.name")}
							</th>
							<th>
								${message("Product.price")}
							</th>
							<th>
								${message("shop.member.action")}
							</th>
						</tr>
						[#list page.content as product]
							<tr[#if !product_has_next] class="last"[/#if]>
								<td>
									<input type="hidden" name="id" value="${product.id}" />
									<img src="${product.thumbnail!setting.defaultThumbnailProductImage}" class="thumbnail" alt="${product.name}" />
								</td>
								<td>
									${product.sn}
								</td>
								<td>
									<a href="${product.url}" title="${product.name}" target="_blank">${abbreviate(product.name, 30)}</a>
								</td>
								<td>
									${currency(product.price, true)}
								</td>
								<td>
									<a href="javascript:;" class="delete">[${message("shop.member.action.delete")}]</a>
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