[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.admin.edit")} - Powered By VJSHOP</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<style type="text/css">
.roles label {
	width: 150px;
	display: block;
	float: left;
	padding-right: 5px;
}
</style>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	
	[@flash_message /]
	
	// 表单验证
	$inputForm.validate({
		rules: {
			password: {
				minlength: 4
			},
			rePassword: {
				equalTo: "#password"
			},
			email: {
				required: true,
				email: true
			},
			roleIds: "required"
		}
	});

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.admin.edit")}
	</div>
	<form id="inputForm" action="update.jhtml" method="post">
		<input type="hidden" name="id" value="${admin.id}" />
		<ul id="tab" class="tab">
			<li>
				<input type="button" value="${message("admin.admin.base")}" />
			</li>
			<li>
				<input type="button" value="${message("admin.admin.profile")}" />
			</li>
		</ul>
		<table class="input tabContent">
			<tr>
				<th>
					${message("Admin.username")}:
				</th>
				<td>
					${admin.username}
				</td>
			</tr>
			<tr>
				<th>
					${message("Admin.password")}:
				</th>
				<td>
					<input type="password" id="password" name="password" class="text" maxlength="20" autocomplete="off" />
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.admin.rePassword")}:
				</th>
				<td>
					<input type="password" name="rePassword" class="text" maxlength="20" autocomplete="off" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Admin.email")}:
				</th>
				<td>
					<input type="text" name="email" class="text" value="${admin.email}" maxlength="200" />
				</td>
			</tr>
			<tr class="roles">
				<th>
					<span class="requiredField">*</span>${message("Admin.roles")}:
				</th>
				<td>
					<span class="fieldSet">
						[#list roles as role]
							<label>
								<input type="checkbox" name="roleIds" value="${role.id}"[#if adminRole?seq_contains(role)] checked="checked"[/#if] />${role.name}
							</label>
						[/#list]
					</span>
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.common.setting")}:
				</th>
				<td>
					<label>
						<input type="checkbox" name="isEnabled" value="true"[#if admin.isEnabled] checked="checked"[/#if] />${message("Admin.isEnabled")}
						<input type="hidden" name="_isEnabled" value="false" />
					</label>
					[#if admin.isLocked]
						<label>
							<input type="checkbox" name="isLocked" value="true" checked="checked" />${message("Admin.isLocked")}
							<input type="hidden" name="_isLocked" value="false" />
						</label>
					[/#if]
				</td>
			</tr>
		</table>
		<table class="input tabContent">
			<tr>
				<th>
					${message("Admin.department")}:
				</th>
				<td>
					<input type="text" name="department" class="text" value="${admin.department}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Admin.name")}:
				</th>
				<td>
					<input type="text" name="name" class="text" value="${admin.name}" maxlength="200" />
				</td>
			</tr>
		</table>
		<table class="input">
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