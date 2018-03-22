[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.role.add")} - Powered By VJSHOP</title>
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
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.role.add")}
	</div>
	<form id="inputForm" action="save.jhtml" method="post">
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Role.name")}:
				</th>
				<td>
					<input type="text" name="name" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Role.description")}:
				</th>
				<td>
					<input type="text" name="description" class="text" maxlength="200" />
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
							<input type="checkbox" name="authoritiesList" value="admin:goods" />${message("admin.role.goods")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:stock" />${message("admin.role.stock")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:productCategory" />${message("admin.role.productCategory")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:parameter" />${message("admin.role.parameter")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:attribute" />${message("admin.role.attribute")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:specification" />${message("admin.role.specification")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:brand" />${message("admin.role.brand")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:productNotify" />${message("admin.role.productNotify")}
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
							<input type="checkbox" name="authoritiesList" value="admin:order" />${message("admin.role.order")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:print" />${message("admin.role.print")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:payment" />${message("admin.role.payment")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:refunds" />${message("admin.role.refunds")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:shipping" />${message("admin.role.shipping")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:returns" />${message("admin.role.returns")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:deliveryCenter" />${message("admin.role.deliveryCenter")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:deliveryTemplate" />${message("admin.role.deliveryTemplate")}
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
							<input type="checkbox" name="authoritiesList" value="admin:member" />${message("admin.role.member")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:memberRank" />${message("admin.role.memberRank")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:memberAttribute" />${message("admin.role.memberAttribute")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:point" />${message("admin.role.point")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:deposit" />${message("admin.role.deposit")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:review" />${message("admin.role.review")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:consultation" />${message("admin.role.consultation")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:messageConfig" />${message("admin.role.messageConfig")}
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
							<input type="checkbox" name="authoritiesList" value="admin:navigation" />${message("admin.role.navigation")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:article" />${message("admin.role.article")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:articleCategory" />${message("admin.role.articleCategory")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:tag" />${message("admin.role.tag")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:friendLink" />${message("admin.role.friendLink")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:adPosition" />${message("admin.role.adPosition")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:ad" />${message("admin.role.ad")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:template" />${message("admin.role.template")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:theme" />${message("admin.role.theme")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:cache" />${message("admin.role.cache")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:static" />${message("admin.role.static")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:index" />${message("admin.role.index")}
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
							<input type="checkbox" name="authoritiesList" value="admin:promotion" />${message("admin.role.promotion")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:coupon" />${message("admin.role.coupon")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:seo" />${message("admin.role.seo")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:sitemap" />${message("admin.role.sitemap")}
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
							<input type="checkbox" name="authoritiesList" value="admin:statistics" />${message("admin.role.statistics")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:memberStatistic" />${message("admin.role.memberStatistic")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:orderStatistic" />${message("admin.role.orderStatistic")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:memberRanking" />${message("admin.role.memberRanking")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:goodsRanking" />${message("admin.role.goodsRanking")}
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
							<input type="checkbox" name="authoritiesList" value="admin:setting" />${message("admin.role.setting")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:area" />${message("admin.role.area")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:paymentMethod" />${message("admin.role.paymentMethod")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:shippingMethod" />${message("admin.role.shippingMethod")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:deliveryCorp" />${message("admin.role.deliveryCorp")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:paymentPlugin" />${message("admin.role.paymentPlugin")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:storagePlugin" />${message("admin.role.storagePlugin")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:loginPlugin" />${message("admin.role.loginPlugin")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:admin" />${message("admin.role.admin")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:role" />${message("admin.role.role")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:message" />${message("admin.role.message")}
						</label>
						<label>
							<input type="checkbox" name="authoritiesList" value="admin:log" />${message("admin.role.log")}
						</label>
					</span>
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="submit" class="button" value="${message("admin.common.submit")}" />
					<input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
[/#escape]