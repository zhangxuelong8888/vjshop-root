[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.plugin.ftpStorage.setting")} - Powered By VJSHOP</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	
	[@flash_message /]
	
	// 表单验证
	$inputForm.validate({
		errorClass: "fieldError",
		ignoreTitle: true,
		rules: {
			host: "required",
			port: {
				required: true,
				digits: true
			},
			username: "required",
			urlPrefix: "required",
			order: "digits"
		}
	});
	
});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.plugin.ftpStorage.setting")}
	</div>
	<form id="inputForm" action="update.jhtml" method="post">
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>${message("admin.plugin.ftpStorage.host")}:
				</th>
				<td>
					<input type="text" name="host" class="text" value="${pluginConfig.getAttributesMap("host")}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("admin.plugin.ftpStorage.port")}:
				</th>
				<td>
					<input type="text" name="port" class="text" value="${pluginConfig.getAttributesMap("port")}" maxlength="9" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("admin.plugin.ftpStorage.username")}:
				</th>
				<td>
					<input type="text" name="username" class="text" value="${pluginConfig.getAttributesMap("username")}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.plugin.ftpStorage.password")}:
				</th>
				<td>
					<input type="password" name="password" class="text" value="${pluginConfig.getAttributesMap("password")}" maxlength="200" autocomplete="off" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("admin.plugin.ftpStorage.urlPrefix")}:
				</th>
				<td>
					<input type="text" name="urlPrefix" class="text" value="${pluginConfig.getAttributesMap("urlPrefix")}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.common.order")}:
				</th>
				<td>
					<input type="text" name="order" class="text" value="${pluginConfig.orders}" maxlength="9" />
				</td>
			</tr>
			<tr>
				<th>
					${message("StoragePlugin.isEnabled")}:
				</th>
				<td>
					<label>
						<input type="checkbox" name="isEnabled" value="true"[#if pluginConfig.isEnabled] checked[/#if] />
					</label>
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="submit" class="button" value="${message("admin.common.submit")}" />
					<input type="button" class="button" value="${message("admin.common.back")}" onclick="location.href='../list.jhtml'" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
[/#escape]