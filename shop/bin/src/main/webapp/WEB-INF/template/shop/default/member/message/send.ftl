[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("shop.member.message.send")}[#if showPowered] - Powered By VJSHOP[/#if]</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/shop/${theme}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/shop/${theme}/css/member.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/common.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $isDraft = $("#isDraft");
	var $type = $("input[name='type']");
	var $username = $("#username");
	var $send = $("#send");
	var $save = $("#save");
	
	// 发送类型
	$type.click(function() {
		var $this = $(this);
		if ($this.val() == "member") {
			$username.prop("disabled", false).closest("tr").show();
		} else {
			$username.prop("disabled", true).closest("tr").hide();
		}
	});
	
	// 立即发送
	$send.click(function() {
		$isDraft.val("false");
		$inputForm.submit();
	});
	
	// 保存为草稿
	$save.click(function() {
		$isDraft.val("true");
		$inputForm.submit();
	});
	
	$.validator.addMethod("notEqualsIgnoreCase", 
		function(value, element, param) {
			return this.optional(element) || param.toLowerCase() != value.toLowerCase()
		}
	);
	
	// 表单验证
	$inputForm.validate({
		rules: {
			username: {
				required: true,
				notEqualsIgnoreCase: "${member.username}",
				remote: {
					url: "check_username.jhtml",
					cache: false
				}
			},
			title: {
				required: true
			},
			content: {
				required: true,
				maxlength: 4000
			}
		},
		messages: {
			username: {
				notEqualsIgnoreCase: "${message("shop.member.message.notAllowSelf")}",
				remote: "${message("shop.member.message.memberNotExist")}"
			}
		}
	});

});
</script>
</head>
<body>
	[#assign current = "messageSend" /]
	[#include "/shop/${theme}/include/header.ftl" /]
	<div class="container member">
		<div class="row">
			[#include "/shop/${theme}/member/include/navigation.ftl" /]
			<div class="span10">
				<div class="input">
					<div class="title">${message("shop.member.message.send")}</div>
					<form id="inputForm" action="send.jhtml" method="post">
						<input type="hidden" name="draftMessageId" value="${(draftMessage.id)!}" />
						<input type="hidden" id="isDraft" name="isDraft" value="false" />
						<table class="input">
							<tr>
								<th>
									<span class="requiredField">*</span>${message("shop.member.message.sendTo")}:
								</th>
								<td>
									<label>
										<input type="radio" name="type" value="member"[#if !draftMessage?? || (draftMessage?? && draftMessage.receiver??)] checked="checked"[/#if] />${message("shop.member.message.otherMember")}
									</label>
									<label>
										<input type="radio" name="type" value="admin"[#if draftMessage?? && !draftMessage.receiver??] checked="checked"[/#if] />${message("shop.member.message.admin")}
									</label>
								</td>
							</tr>
							<tr[#if draftMessage?? && !draftMessage.receiver??] class="hidden"[/#if]>
								<th>
									<span class="requiredField">*</span>${message("shop.member.message.receiverUsername")}:
								</th>
								<td>
									<input type="text" id="username" name="username" class="text" value="${(draftMessage.receiver.username)!}" maxlength="200"[#if draftMessage?? && !draftMessage.receiver??] disabled="disabled"[/#if] />
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("Message.title")}:
								</th>
								<td>
									<input type="text" name="title" class="text" value="${(draftMessage.title)!}" maxlength="200" />
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("Message.content")}:
								</th>
								<td>
									<textarea name="content" class="text">${(draftMessage.content)!}</textarea>
								</td>
							</tr>
							<tr>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="button" id="send" class="button" value="${message("shop.member.message.sendNow")}" />
									[#if !draftMessage??]
										<input type="button" id="save" class="button" value="${message("shop.member.message.saveDraft")}" />
									[/#if]
								</td>
							</tr>
						</table>
					</form>
				</div>
			</div>
		</div>
	</div>
	[#include "/shop/${theme}/include/footer.ftl" /]
</body>
</html>
[/#escape]