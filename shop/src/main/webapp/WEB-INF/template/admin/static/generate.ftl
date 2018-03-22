[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.static.generate")} - Powered By VJSHOP</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $generateType = $("#generateType");
	var $articleCategoryId = $("#articleCategoryId");
	var $productCategoryId = $("#productCategoryId");
	var $beginDate = $("#beginDate");
	var $endDate = $("#endDate");
	var $count = $("#count");
	var $status = $("#status");
	var $submit = $("input:submit");
	var first;
	var generateCount;
	var generateTime;
	var generateType;
	var articleCategoryId;
	var productCategoryId;
	var beginDate;
	var endDate;
	var count;
	
	// 生成类型
	$generateType.change(function() {
		var $this = $(this);
		if ($this.val() == "article") {
			$articleCategoryId.closest("tr").show();
			$productCategoryId.closest("tr").hide();
			$beginDate.closest("tr").show();
			$endDate.closest("tr").show();
		} else if ($this.val() == "goods") {
			$articleCategoryId.closest("tr").hide();
			$productCategoryId.closest("tr").show();
			$beginDate.closest("tr").show();
			$endDate.closest("tr").show();
		} else {
			$articleCategoryId.closest("tr").hide();
			$productCategoryId.closest("tr").hide();
			$beginDate.closest("tr").hide();
			$endDate.closest("tr").hide();
		}
	});
	
	// 表单验证
	$inputForm.validate({
		rules: {
			count: {
				required: true,
				integer: true,
				min: 1
			}
		},
		submitHandler: function(form) {
			first = null;
			generateCount = 0;
			generateTime = 0;
			generateType = $generateType.val();
			articleCategoryId = $articleCategoryId.val();
			productCategoryId = $productCategoryId.val();
			beginDate = $beginDate.val();
			endDate = $endDate.val();
			count = parseInt($count.val());
			$generateType.prop("disabled", true);
			$articleCategoryId.prop("disabled", true);
			$productCategoryId.prop("disabled", true);
			$beginDate.prop("disabled", true);
			$endDate.prop("disabled", true);
			$count.prop("disabled", true);
			$submit.prop("disabled", true);
			$status.closest("tr").show();
			generate();
		}
	});
	
	function generate() {
		$.ajax({
			url: "generate.jhtml",
			type: "POST",
			data: {generateType: generateType, articleCategoryId: articleCategoryId, productCategoryId: productCategoryId, beginDate: beginDate, endDate: endDate, first: first, count: count},
			dataType: "json",
			cache: false,
			success: function(data) {
				generateCount += data.generateCount;
				generateTime += data.generateTime;
				if (!data.isCompleted) {
					if (generateType == "article" || generateType == "goods") {
						first = data.first;
						$status.text("${message("admin.static.beingProcessed")} [" + first + " - " + (first + count) + "]");
					} else {
						$status.text("${message("admin.static.beingProcessed")}");
					}
					generate();
				} else {
					$generateType.prop("disabled", false);
					$articleCategoryId.prop("disabled", false);
					$productCategoryId.prop("disabled", false);
					$beginDate.prop("disabled", false);
					$endDate.prop("disabled", false);
					$count.prop("disabled", false);
					$submit.prop("disabled", false);
					$status.closest("tr").hide();
					$status.empty();
					var time;
					if (generateTime < 60000) {
						time = (generateTime / 1000).toFixed(2) + "${message("admin.static.second")}";
					} else {
						time = (generateTime / 60000).toFixed(2) + "${message("admin.static.minute")}";
					}
					$.message("success", "${message("admin.static.success")} [${message("admin.static.generateCount")}: " + generateCount + " ${message("admin.static.generateTime")}: " + time + "]");
				}
			}
		});
	}

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.static.generate")}
	</div>
	<form id="inputForm" action="generate.jhtml" method="post">
		<table class="input">
			<tr>
				<th>
					${message("admin.static.generateType")}:
				</th>
				<td>
					<select id="generateType" name="generateType">
						[#list generateTypes as generateType]
							<option value="${generateType}">${message("admin.static." + generateType)}</option>
						[/#list]
					</select>
				</td>
			</tr>
			<tr class="hidden">
				<th>
					${message("Article.articleCategory")}:
				</th>
				<td>
					<select id="articleCategoryId" name="articleCategoryId">
						<option value="">${message("admin.static.emptyOption")}</option>
						[#list articleCategoryTree as articleCategory]
							<option value="${articleCategory.id}">
								[#if articleCategory.grade != 0]
									[#list 1..articleCategory.grade as i]
										&nbsp;&nbsp;
									[/#list]
								[/#if]
								${articleCategory.name}
							</option>
						[/#list]
					</select>
				</td>
			</tr>
			<tr class="hidden">
				<th>
					${message("Goods.productCategory")}:
				</th>
				<td>
					<select id="productCategoryId" name="productCategoryId">
						<option value="">${message("admin.static.emptyOption")}</option>
						[#list productCategoryTree as productCategory]
							<option value="${productCategory.id}">
								[#if productCategory.grade != 0]
									[#list 1..productCategory.grade as i]
										&nbsp;&nbsp;
									[/#list]
								[/#if]
								${productCategory.name}
							</option>
						[/#list]
					</select>
				</td>
			</tr>
			<tr class="hidden">
				<th>
					${message("admin.static.beginDate")}:
				</th>
				<td>
					<input type="text" id="beginDate" name="beginDate" class="text Wdate" value="${defaultBeginDate?string("yyyy-MM-dd")}" title="${message("admin.static.beginDateTitle")}" onfocus="WdatePicker({maxDate: '#F{$dp.$D(\'endDate\')}'});" />
				</td>
			</tr>
			<tr class="hidden">
				<th>
					${message("admin.static.endDate")}:
				</th>
				<td>
					<input type="text" id="endDate" name="endDate" class="text Wdate" value="${defaultEndDate?string("yyyy-MM-dd")}" title="${message("admin.static.endDateTitle")}" onfocus="WdatePicker({minDate: '#F{$dp.$D(\'beginDate\')}'});" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("admin.static.count")}:
				</th>
				<td>
					<input type="text" id="count" name="count" class="text" value="100" maxlength="9" />
				</td>
			</tr>
			<tr class="hidden">
				<th>
					&nbsp;
				</th>
				<td>
					<span class="loadingBar">&nbsp;</span>
					<div id="status"></div>
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