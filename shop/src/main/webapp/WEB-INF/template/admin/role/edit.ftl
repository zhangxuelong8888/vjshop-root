[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.role.edit")} - Powered By VJSHOP</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<style type="text/css">
.authorities label {
	min-width: 120px;
	_width: 120px;
	display: block;
	float: left;
	padding-right: 4px;
	_white-space: nowrap;
}
</style>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $selectAll = $("#inputForm .selectAll");
	
	[@flash_message /]
	
	$selectAll.click(function() {
		var $this = $(this);
		var $thisCheckbox = $this.closest("tr").find("input:checkbox");
		if ($thisCheckbox.filter(":checked").size() > 0) {
			$thisCheckbox.prop("checked", false);
		} else {
			$thisCheckbox.prop("checked", true);
		}
		return false;
	});
	
	// 表单验证
	$inputForm.validate({
		rules: {
			name: "required",
			authorities: "required"
		}
	});

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.role.edit")}
	</div>
	<form id="inputForm" action="update.jhtml" method="post">
		<input type="hidden" name="id" value="${role.id}" />
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Role.name")}:
				</th>
				<td>
					<input type="text" name="name" class="text" value="${role.name}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Role.description")}:
				</th>
				<td>
					<input type="text" name="description" class="text" value="${role.description}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<td colspan="2">
					&nbsp;
				</td>
			</tr>
			<tr class="authorities">
				<th>
					<a href="javascript:;" class="selectAll" title="${message("admin.role.selectAll")}">${message("admin.role.productGroup")}</a>
				</th>
				<td>
					<span class="fieldSet">
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:goods"[#if role.authoritiesList?seq_contains("admin:goods")] checked="checked"[/#if] />${message("admin.role.goods")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:stock"[#if role.authoritiesList?seq_contains("admin:stock")] checked="checked"[/#if] />${message("admin.role.stock")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:productCategory"[#if role.authoritiesList?seq_contains("admin:productCategory")] checked="checked"[/#if] />${message("admin.role.productCategory")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:parameter"[#if role.authoritiesList?seq_contains("admin:parameter")] checked="checked"[/#if] />${message("admin.role.parameter")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:attribute"[#if role.authoritiesList?seq_contains("admin:attribute")] checked="checked"[/#if] />${message("admin.role.attribute")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:specification"[#if role.authoritiesList?seq_contains("admin:specification")] checked="checked"[/#if] />${message("admin.role.specification")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:brand"[#if role.authoritiesList?seq_contains("admin:brand")] checked="checked"[/#if] />${message("admin.role.brand")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:productNotify"[#if role.authoritiesList?seq_contains("admin:productNotify")] checked="checked"[/#if] />${message("admin.role.productNotify")}
						</label>
					</span>
				</td>
			</tr>
			<tr class="authorities">
				<th>
					<a href="javascript:;" class="selectAll" title="${message("admin.role.selectAll")}">${message("admin.role.orderGroup")}</a>
				</th>
				<td>
					<span class="fieldSet">
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:order"[#if role.authoritiesList?seq_contains("admin:order")] checked="checked"[/#if] />${message("admin.role.order")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:print"[#if role.authoritiesList?seq_contains("admin:print")] checked="checked"[/#if] />${message("admin.role.print")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:payment"[#if role.authoritiesList?seq_contains("admin:payment")] checked="checked"[/#if] />${message("admin.role.payment")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:refunds"[#if role.authoritiesList?seq_contains("admin:refunds")] checked="checked"[/#if] />${message("admin.role.refunds")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:shipping"[#if role.authoritiesList?seq_contains("admin:shipping")] checked="checked"[/#if] />${message("admin.role.shipping")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:returns"[#if role.authoritiesList?seq_contains("admin:returns")] checked="checked"[/#if] />${message("admin.role.returns")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:deliveryCenter"[#if role.authoritiesList?seq_contains("admin:deliveryCenter")] checked="checked"[/#if] />${message("admin.role.deliveryCenter")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:deliveryTemplate"[#if role.authoritiesList?seq_contains("admin:deliveryTemplate")] checked="checked"[/#if] />${message("admin.role.deliveryTemplate")}
						</label>
					</span>
				</td>
			</tr>
			<tr class="authorities">
				<th>
					<a href="javascript:;" class="selectAll" title="${message("admin.role.selectAll")}">${message("admin.role.memberGroup")}</a>
				</th>
				<td>
					<span class="fieldSet">
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:member"[#if role.authoritiesList?seq_contains("admin:member")] checked="checked"[/#if] />${message("admin.role.member")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:memberRank"[#if role.authoritiesList?seq_contains("admin:memberRank")] checked="checked"[/#if] />${message("admin.role.memberRank")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:memberAttribute"[#if role.authoritiesList?seq_contains("admin:memberAttribute")] checked="checked"[/#if] />${message("admin.role.memberAttribute")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:point"[#if role.authoritiesList?seq_contains("admin:point")] checked="checked"[/#if] />${message("admin.role.point")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:deposit"[#if role.authoritiesList?seq_contains("admin:deposit")] checked="checked"[/#if] />${message("admin.role.deposit")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:review"[#if role.authoritiesList?seq_contains("admin:review")] checked="checked"[/#if] />${message("admin.role.review")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:consultation"[#if role.authoritiesList?seq_contains("admin:consultation")] checked="checked"[/#if] />${message("admin.role.consultation")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:messageConfig"[#if role.authoritiesList?seq_contains("admin:messageConfig")] checked="checked"[/#if] />${message("admin.role.messageConfig")}
						</label>
					</span>
				</td>
			</tr>
			<tr class="authorities">
				<th>
					<a href="javascript:;" class="selectAll" title="${message("admin.role.selectAll")}">${message("admin.role.contentGroup")}</a>
				</th>
				<td>
					<span class="fieldSet">
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:navigation"[#if role.authoritiesList?seq_contains("admin:navigation")] checked="checked"[/#if] />${message("admin.role.navigation")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:article"[#if role.authoritiesList?seq_contains("admin:article")] checked="checked"[/#if] />${message("admin.role.article")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:articleCategory"[#if role.authoritiesList?seq_contains("admin:articleCategory")] checked="checked"[/#if] />${message("admin.role.articleCategory")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:tag"[#if role.authoritiesList?seq_contains("admin:tag")] checked="checked"[/#if] />${message("admin.role.tag")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:friendLink"[#if role.authoritiesList?seq_contains("admin:friendLink")] checked="checked"[/#if] />${message("admin.role.friendLink")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:adPosition"[#if role.authoritiesList?seq_contains("admin:adPosition")] checked="checked"[/#if] />${message("admin.role.adPosition")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:ad"[#if role.authoritiesList?seq_contains("admin:ad")] checked="checked"[/#if] />${message("admin.role.ad")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:template"[#if role.authoritiesList?seq_contains("admin:template")] checked="checked"[/#if] />${message("admin.role.template")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:theme"[#if role.authoritiesList?seq_contains("admin:theme")] checked="checked"[/#if] />${message("admin.role.theme")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:cache"[#if role.authoritiesList?seq_contains("admin:cache")] checked="checked"[/#if] />${message("admin.role.cache")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:static"[#if role.authoritiesList?seq_contains("admin:static")] checked="checked"[/#if] />${message("admin.role.static")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:index"[#if role.authoritiesList?seq_contains("admin:index")] checked="checked"[/#if] />${message("admin.role.index")}
						</label>
					</span>
				</td>
			</tr>
			<tr class="authorities">
				<th>
					<a href="javascript:;" class="selectAll" title="${message("admin.role.selectAll")}">${message("admin.role.marketingGroup")}</a>
				</th>
				<td>
					<span class="fieldSet">
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:promotion"[#if role.authoritiesList?seq_contains("admin:promotion")] checked="checked"[/#if] />${message("admin.role.promotion")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:coupon"[#if role.authoritiesList?seq_contains("admin:coupon")] checked="checked"[/#if] />${message("admin.role.coupon")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:seo"[#if role.authoritiesList?seq_contains("admin:seo")] checked="checked"[/#if] />${message("admin.role.seo")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:sitemap"[#if role.authoritiesList?seq_contains("admin:sitemap")] checked="checked"[/#if] />${message("admin.role.sitemap")}
						</label>
					</span>
				</td>
			</tr>
			<tr class="authorities">
				<th>
					<a href="javascript:;" class="selectAll" title="${message("admin.role.selectAll")}">${message("admin.role.statisticGroup")}</a>
				</th>
				<td>
					<span class="fieldSet">
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:statistics"[#if role.authoritiesList?seq_contains("admin:statistics")] checked="checked"[/#if] />${message("admin.role.statistics")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:memberStatistic"[#if role.authoritiesList?seq_contains("admin:memberStatistic")] checked="checked"[/#if] />${message("admin.role.memberStatistic")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:orderStatistic"[#if role.authoritiesList?seq_contains("admin:orderStatistic")] checked="checked"[/#if] />${message("admin.role.orderStatistic")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:memberRanking"[#if role.authoritiesList?seq_contains("admin:memberRanking")] checked="checked"[/#if] />${message("admin.role.memberRanking")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:goodsRanking"[#if role.authoritiesList?seq_contains("admin:goodsRanking")] checked="checked"[/#if] />${message("admin.role.goodsRanking")}
						</label>
					</span>
				</td>
			</tr>
			<tr class="authorities">
				<th>
					<a href="javascript:;" class="selectAll" title="${message("admin.role.selectAll")}">${message("admin.role.systemGroup")}</a>
				</th>
				<td>
					<span class="fieldSet">
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:setting"[#if role.authoritiesList?seq_contains("admin:setting")] checked="checked"[/#if] />${message("admin.role.setting")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:area"[#if role.authoritiesList?seq_contains("admin:area")] checked="checked"[/#if] />${message("admin.role.area")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:paymentMethod"[#if role.authoritiesList?seq_contains("admin:paymentMethod")] checked="checked"[/#if] />${message("admin.role.paymentMethod")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:shippingMethod"[#if role.authoritiesList?seq_contains("admin:shippingMethod")] checked="checked"[/#if] />${message("admin.role.shippingMethod")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:deliveryCorp"[#if role.authoritiesList?seq_contains("admin:deliveryCorp")] checked="checked"[/#if] />${message("admin.role.deliveryCorp")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:paymentPlugin"[#if role.authoritiesList?seq_contains("admin:paymentPlugin")] checked="checked"[/#if] />${message("admin.role.paymentPlugin")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:storagePlugin"[#if role.authoritiesList?seq_contains("admin:storagePlugin")] checked="checked"[/#if] />${message("admin.role.storagePlugin")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:loginPlugin"[#if role.authoritiesList?seq_contains("admin:loginPlugin")] checked="checked"[/#if] />${message("admin.role.loginPlugin")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:admin"[#if role.authoritiesList?seq_contains("admin:admin")] checked="checked"[/#if] />${message("admin.role.admin")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:role"[#if role.authoritiesList?seq_contains("admin:role")] checked="checked"[/#if] />${message("admin.role.role")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:message"[#if role.authoritiesList?seq_contains("admin:message")] checked="checked"[/#if] />${message("admin.role.message")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:log"[#if role.authoritiesList?seq_contains("admin:log")] checked="checked"[/#if] />${message("admin.role.log")}
						</label>
					</span>
				</td>
			</tr>
			[#if role.isSystem]
				<tr>
					<th>
						&nbsp;
					</th>
					<td>
						<span class="tips">${message("admin.role.editSystemNotAllowed")}</span>
					</td>
				</tr>
			[/#if]
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="submit" class="button" value="${message("admin.common.submit")}"[#if role.isSystem] disabled="disabled"[/#if] />
					<input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
[/#escape]