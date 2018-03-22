[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.theme.setting")} - Powered By VJSHOP</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<style type="text/css">
.theme li {
	width: 120px;
	height: 150px;
	float: left;
	margin: 0px 20px 0px 2px;
	text-align: center;
}

.theme img {
	width: 120px;
	height: 120px;
	filter: alpha(opacity = 60);
	opacity: 0.6;
	padding: 2px;
	-webkit-box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.1);
	-moz-box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.1);
	box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.1);
	border: 1px solid #cccccc;
	background-color: #ffffff;
}

.theme img.current {
	filter: alpha(opacity = 100);
	opacity: 1;
}
</style>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $themeFile = $("#themeFile");
	
	[@flash_message /]
	
	// 表单验证
	$inputForm.validate({
		rules: {
			themeFile: {
				extension: "zip"
			}
		},
		submitHandler: function(form) {
			if ($.trim($themeFile.val()) != "") {
				$.dialog({
					type: "warn",
					content: "${message("admin.theme.uploadConfirm")}",
					onOk: function() {
						$(form).find("input:submit").prop("disabled", true);
						form.submit();
					}
				});
			} else {
				$(form).find("input:submit").prop("disabled", true);
				form.submit();
			}
		}
	});

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.theme.setting")}
	</div>
	<form id="inputForm" action="setting.jhtml" method="post" enctype="multipart/form-data">
		<table class="input">
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<ul class="theme">
						[#list themes as theme]
							<li>
								<img[#if theme.id == setting.theme] class="current"[/#if] src="[#if theme.preview?has_content]${base}${theme.preview}[#else]${base}/resources/admin/images/no_preview.jpg[/#if]" />
								<input type="radio" id="${theme.id}" name="id" value="${theme.id}"[#if theme.id == setting.theme] checked="checked"[/#if] />
								<label for="${theme.id}">[#if theme.name?has_content]${theme.name}[#else]${theme.id}[/#if]</label>
							</li>
						[/#list]
					</ul>
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.theme.upload")}
				</th>
				<td>
					<input type="file" id="themeFile" name="themeFile" />
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