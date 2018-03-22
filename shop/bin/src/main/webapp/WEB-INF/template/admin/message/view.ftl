[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.message.view")} - Powered By VJSHOP</title>
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
		rules: {
			content: {
				required: true,
				maxlength: 4000
			}
		}
	});
});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.message.view")}
	</div>
	<form id="inputForm" action="reply.jhtml" method="post">
		<input type="hidden" name="id" value="${adminMessage.id}" />
		<table class="input">
			<tr>
				<th>
					${message("Message.title")}:
				</th>
				<td colspan="2">
					${adminMessage.title}
				</td>
			</tr>
			<tr>
				<th>
					${message("Message.ip")}:
				</th>
				<td colspan="2">
					${adminMessage.ip}
				</td>
			</tr>
			<tr>
				<td colspan="3">
					&nbsp;
				</td>
			</tr>
			<tr>
				<th>
					[#if adminMessage.senders??]
						<a href="${base}/admin/member/view.jhtml?id=${adminMessage.senders.id}">
							<span class="red">[${adminMessage.senders.username}]</span>
						</a>
					[#else]
						<span class="green">[${message("admin.message.admin")}]</span>
					[/#if]
				</th>
				<td>
					${adminMessage.content}
				</td>
				<td width="80">
					<span title="${adminMessage.createDate?string("yyyy-MM-dd HH:mm:ss")}">${adminMessage.createDate}</span>
				</td>
			</tr>
			[#list adminMessage.replyMessages as replyMessage]
				<tr>
					<th>
						[#if replyMessage.sender??]
							<a href="${base}/admin/member/view.jhtml?id=${replyMessage.senders.id}">
								<span class="red">[${replyMessage.senders.username}]</span>
							</a>
						[#else]
							<span class="green">[${message("admin.message.admin")}]</span>
						[/#if]
					</th>
					<td>
						${replyMessage.content}
					</td>
					<td width="80">
						<span title="${replyMessage.createDate?string("yyyy-MM-dd HH:mm:ss")}">${replyMessage.createDate}</span>
					</td>
				</tr>
			[/#list]
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Consultation.content")}:
				</th>
				<td colspan="2">
					<textarea name="content" class="text"></textarea>
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td colspan="2">
					<input type="submit" class="button" value="${message("admin.common.submit")}" />
					<input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
[/#escape]