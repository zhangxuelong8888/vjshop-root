[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.member.edit")} - Powered By VJSHOP</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.lSelect.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $areaId = $("#areaId");
	
	[@flash_message /]
	
	// 地区选择
	$areaId.lSelect({
		url: "${base}/admin/common/area.jhtml"
	});
	
	// 表单验证
	$inputForm.validate({
		rules: {
			password: {
				minlength: ${setting.passwordMinLength}
			},
			rePassword: {
				equalTo: "#password"
			},
			email: {
				required: true,
				email: true
				[#if !setting.isDuplicateEmail]
					,remote: {
						url: "check_email.jhtml?previousEmail=${member.email?url}",
						cache: false
					}
				[/#if]
			}
			[#list memberAttributes as memberAttribute]
				[#if memberAttribute.isRequired || memberAttribute.pattern?has_content]
					,memberAttribute_${memberAttribute.id}: {
						[#if memberAttribute.isRequired]
							required: true
							[#if memberAttribute.pattern?has_content],[/#if]
						[/#if]
						[#if memberAttribute.pattern?has_content]
							pattern: /${memberAttribute.pattern}/
						[/#if]
					}
				[/#if]
			[/#list]
		},
		messages: {
			[#if !setting.isDuplicateEmail]
				email: {
					remote: "${message("admin.validate.exist")}"
				}
			[/#if]
		}
	});

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.member.edit")}
	</div>
	<form id="inputForm" action="update.jhtml" method="post">
		<input type="hidden" name="id" value="${member.id}" />
		<ul id="tab" class="tab">
			<li>
				<input type="button" value="${message("admin.member.base")}" />
			</li>
			[#if memberAttributes?has_content]
				<li>
					<input type="button" value="${message("admin.member.profile")}" />
				</li>
			[/#if]
		</ul>
		<table class="input tabContent">
			<tr>
				<th>
					${message("Member.username")}:
				</th>
				<td>
					${member.username}
					[#if loginPlugin??]
						<span class="silver">[${loginPlugin.name}]</span>
					[/#if]
				</td>
			</tr>
			<tr>
				<th>
					${message("Member.password")}:
				</th>
				<td>
					<input type="password" id="password" name="password" class="text" maxlength="${setting.passwordMaxLength}" autocomplete="off" title="${message("admin.member.passwordTitle")}" />
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.member.rePassword")}:
				</th>
				<td>
					<input type="password" name="rePassword" class="text" maxlength="${setting.passwordMaxLength}" autocomplete="off" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Member.email")}:
				</th>
				<td>
					<input type="text" name="email" class="text" value="${member.email}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Member.nickname")}:
				</th>
				<td>
					<input type="text" name="nickname" class="text" value="${member.nickname}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Member.memberRank")}:
				</th>
				<td>
					<select name="memberRankId">
						[#list memberRanks as memberRank]
							<option value="${memberRank.id}"[#if memberRank.id == member.memberRank] selected="selected"[/#if]>${memberRank.name}</option>
						[/#list]
					</select>
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.common.setting")}:
				</th>
				<td>
					<label>
						<input type="checkbox" name="isEnabled" value="true"[#if member.isEnabled] checked="checked"[/#if] />${message("Member.isEnabled")}
						<input type="hidden" name="_isEnabled" value="false" />
					</label>
					[#if member.isLocked]
						<label>
							<input type="checkbox" name="isLocked" value="true" checked="checked" />${message("Member.isLocked")}
							<input type="hidden" name="_isLocked" value="false" />
						</label>
					[/#if]
				</td>
			</tr>
			<tr>
				<th>
					${message("Member.point")}:
				</th>
				<td>
					${member.point}
				</td>
			</tr>
			<tr>
				<th>
					${message("Member.balance")}:
				</th>
				<td>
					${currency(member.balance, true)}
				</td>
			</tr>
			<tr>
				<th>
					${message("Member.amount")}:
				</th>
				<td>
					${currency(member.amount, true)}
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.common.createDate")}:
				</th>
				<td>
					${member.createDate?string("yyyy-MM-dd HH:mm:ss")}
				</td>
			</tr>
			<tr>
				<th>
					${message("Member.registerIp")}:
				</th>
				<td>
					${member.registerIp}
				</td>
			</tr>
		</table>
		[#if memberAttributes?has_content]
			<table class="input tabContent">
				[#list memberAttributes as memberAttribute]
					<tr>
						<th>
							[#if memberAttribute.isRequired]<span class="requiredField">*</span>[/#if]${memberAttribute.name}:
						</th>
						<td>
							[#if memberAttribute.enumType == "name"]
								<input type="text" name="memberAttribute_${memberAttribute.id}" class="text" value="${member.name}" maxlength="200" />
							[#elseif memberAttribute.enumType == "gender"]
								<span class="fieldSet">
									[#list genders as gender]
										<label>
											<input type="radio" name="memberAttribute_${memberAttribute.id}" value="${gender.ordinal()}"[#if gender.ordinal() == member.gender] checked="checked"[/#if] />${message("Member.Gender." + gender)}
										</label>
									[/#list]
								</span>
							[#elseif memberAttribute.enumType == "birth"]
								<input type="text" name="memberAttribute_${memberAttribute.id}" class="text Wdate" value="${member.birth}" onfocus="WdatePicker();" />
							[#elseif memberAttribute.enumType == "area"]
								<span class="fieldSet">
									<input type="hidden" id="areaId" name="memberAttribute_${memberAttribute.id}" value="${(member.areaInfo.id)!}" treePath="${(member.areaInfo.treePath)!}" />
								</span>
							[#elseif memberAttribute.enumType == "address"]
								<input type="text" name="memberAttribute_${memberAttribute.id}" class="text" value="${member.address}" maxlength="200" />
							[#elseif memberAttribute.enumType == "zipCode"]
								<input type="text" name="memberAttribute_${memberAttribute.id}" class="text" value="${member.zipCode}" maxlength="200" />
							[#elseif memberAttribute.enumType == "phone"]
								<input type="text" name="memberAttribute_${memberAttribute.id}" class="text" value="${member.phone}" maxlength="200" />
							[#elseif memberAttribute.enumType == "mobile"]
								<input type="text" name="memberAttribute_${memberAttribute.id}" class="text" value="${member.mobile}" maxlength="200" />
							[#elseif memberAttribute.enumType == "text"]
								<input type="text" name="memberAttribute_${memberAttribute.id}" class="text" value="${member.getAttributeValue(memberAttribute.enumType, memberAttribute.propertyIndex)}" maxlength="200" />
							[#elseif memberAttribute.enumType == "select"]
								<select name="memberAttribute_${memberAttribute.id}">
									<option value="">${message("admin.common.choose")}</option>
									[#list memberAttribute.optionList as option]
										<option value="${option}"[#if option == member.getAttributeValue(memberAttribute.enumType, memberAttribute.propertyIndex)] selected="selected"[/#if]>
											${option}
										</option>
									[/#list]
								</select>
							[#elseif memberAttribute.enumType == "checkbox"]
								<span class="fieldSet">
									[#list memberAttribute.optionList as option]
										<label>
											<input type="checkbox" name="memberAttribute_${memberAttribute.id}" value="${option}"[#if (member.getAttributeValue(memberAttribute.enumType, memberAttribute.propertyIndex)?seq_contains(option))!] checked="checked"[/#if] />${option}
										</label>
									[/#list]
								</span>
							[/#if]
						</td>
					</tr>
				[/#list]
			</table>
		[/#if]
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