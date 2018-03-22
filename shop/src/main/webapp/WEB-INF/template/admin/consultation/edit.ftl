[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.consultation.edit")} - Powered By VJSHOP</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $delete = $("#inputForm a.delete");

	[@flash_message /]
	
	// 删除
	$delete.click(function() {
		var $this = $(this);
		$.dialog({
			type: "warn",
			content: "${message("admin.dialog.deleteConfirm")}",
			onOk: function() {
				$.ajax({
					url: "delete_reply.jhtml",
					type: "POST",
					data: {id: $this.attr("val")},
					dataType: "json",
					cache: false,
					success: function(message) {
						$.message(message);
						if (message.type == "success") {
							$this.closest("tr").remove();
						}
					}
				});
			}
		});
		return false;
	});

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.consultation.edit")}
	</div>
	<form id="inputForm" action="update.jhtml" method="post">
		<input type="hidden" name="id" value="${consultation.id}" />
		<table class="input">
			<tr>
				<th>
					${message("Consultation.goods")}:
				</th>
				<td colspan="3">
					<a href="${consultation.goodsInfo.url}" target="_blank">${consultation.goodsInfo.name}</a>
				</td>
			</tr>
			<tr>
				<th>
					${message("Consultation.member")}:
				</th>
				<td colspan="3">
					[#if consultation.memberInfo??]
						<a href="../member/view.jhtml?id=${consultation.memberInfo.id}">${consultation.memberInfo.username}</a>
					[#else]
						${message("admin.consultation.anonymous")}
					[/#if]
				</td>
			</tr>
			<tr>
				<th>
					${message("Consultation.content")}:
				</th>
				<td colspan="3">
					${consultation.content}
				</td>
			</tr>
			<tr>
				<th>
					${message("Consultation.ip")}:
				</th>
				<td colspan="3">
					${consultation.ip}
				</td>
			</tr>
			[#if consultation.replyConsultations?has_content]
				<tr>
					<td colspan="4">
						&nbsp;
					</td>
				</tr>
				[#list consultation.replyConsultations as replyConsultation]
					<tr>
						<th>
							&nbsp;
						</th>
						<td>
							${replyConsultation.content}
						</td>
						<td width="80">
							<span title="${replyConsultation.createDate?string("yyyy-MM-dd HH:mm:ss")}">${replyConsultation.createDate}</span>
						</td>
						<td width="80">
							<a href="javascript:;" class="delete" val="${replyConsultation.id}">[${message("admin.common.delete")}]</a>
						</td>
					</tr>
				[/#list]
			[/#if]
			<tr>
				<th>
					${message("Consultation.isShow")}:
				</th>
				<td colspan="3">
					<input type="checkbox" name="isShow" value="true"[#if consultation.isShow] checked="checked"[/#if] />
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td colspan="3">
					<input type="submit" class="button" value="${message("admin.common.submit")}" />
					<input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
[/#escape]