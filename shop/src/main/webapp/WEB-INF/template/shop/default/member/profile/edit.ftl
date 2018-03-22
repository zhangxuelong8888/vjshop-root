[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("shop.member.profile.edit")}[#if showPowered] - Powered By VJSHOP[/#if]</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/shop/${theme}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/shop/${theme}/css/member.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.lSelect.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $areaId = $("#areaId");
	
	[@flash_message /]
	
	// 地区选择
	$areaId.lSelect({
		url: "${base}/common/area.jhtml"
	});
	
	// 表单验证
	$inputForm.validate({
		rules: {
			email: {
				required: true,
				email: true
				[#if !setting.isDuplicateEmail]
					,remote: {
						url: "check_email.jhtml",
						cache: false
					}
				[/#if]
			}
			[@member_attribute_list]
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
			[/@member_attribute_list]
		}
		[#if !setting.isDuplicateEmail]
			,messages: {
				email: {
					remote: "${message("shop.validate.exist")}"
				}
			}
		[/#if]
	});

});
</script>
</head>
<body>
	[#assign current = "profileEdit" /]
	[#include "/shop/${theme}/include/header.ftl" /]
	<div class="container member">
		<div class="row">
			[#include "/shop/${theme}/member/include/navigation.ftl" /]
			<div class="span10">
				<div class="input">
					<div class="title">${message("shop.member.profile.edit")}</div>
					<form id="inputForm" action="update.jhtml" method="post">
						<table class="input">
							<tr>
								<th>
									${message("Member.username")}:
								</th>
								<td>
									${member.username}
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
							[@member_attribute_list]
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
															<input type="radio" name="memberAttribute_${memberAttribute.id}" value="${gender.ordinal()}"[#if gender == member.enumGender] checked="checked"[/#if] />${message("Member.Gender." + gender)}
														</label>
													[/#list]
												</span>
											[#elseif memberAttribute.enumType == "birth"]
												<input type="text" name="memberAttribute_${memberAttribute.id}" class="text" value="${member.birth}" onfocus="WdatePicker();" />
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
													<option value="">${message("shop.common.choose")}</option>
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
							[/@member_attribute_list]
							<tr>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="submit" class="button" value="${message("shop.member.submit")}" />
									<input type="button" class="button" value="${message("shop.member.back")}" onclick="history.back(); return false;" />
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