[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("shop.register.title")}[#if showPowered] - Powered By VJSHOP[/#if]</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/shop/${theme}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/shop/${theme}/css/register.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.lSelect.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jsbn.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/prng4.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/rng.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/rsa.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/base64.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $registerForm = $("#registerForm");
	var $username = $("#username");
	var $password = $("#password");
	var $email = $("#email");
	var $areaId = $("#areaId");
	var $captcha = $("#captcha");
	var $captchaImage = $("#captchaImage");
	var $submit = $("input:submit");
	var $agreement = $("#agreement");
	
	// 地区选择
	$areaId.lSelect({
		url: "${base}/common/area.jhtml"
	});
	
	// 更换验证码
	$captchaImage.click(function() {
		$captchaImage.attr("src", "${base}/common/captcha.jhtml?captchaId=${captchaId}&timestamp=" + new Date().getTime());
	});
	
	// 注册协议
	$agreement.hover(function() {
		$(this).height(200);
	});
	
	// 表单验证
	$registerForm.validate({
		rules: {
			username: {
				required: true,
				pattern: /^[0-9a-zA-Z_\u4e00-\u9fa5]+$/,
				minlength: ${setting.usernameMinLength},
				remote: {
					url: "${base}/register/check_username.jhtml",
					cache: false
				}
			},
			password: {
				required: true,
				minlength: ${setting.passwordMinLength}
			},
			rePassword: {
				required: true,
				equalTo: "#password"
			},
			email: {
				required: true,
				email: true
				[#if !setting.isDuplicateEmail]
					,remote: {
						url: "${base}/register/check_email.jhtml",
						cache: false
					}
				[/#if]
			},
			captcha: "required"
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
		},
		messages: {
			username: {
				pattern: "${message("shop.register.usernameIllegal")}",
				remote: "${message("shop.register.disabledExist")}"
			}
			[#if !setting.isDuplicateEmail]
				,email: {
					remote: "${message("shop.register.emailExist")}"
				}
			[/#if]
		},
        errorPlacement: function(error, element) {
            debugger
        },
        submitHandler: function(form) {
			$.ajax({
				url: "${base}/common/public_key.jhtml",
				type: "GET",
				dataType: "json",
				cache: false,
				beforeSend: function() {
					$submit.prop("disabled", true);
				},
				success: function(data) {
					var rsaKey = new RSAKey();
					rsaKey.setPublic(b64tohex(data.modulus), b64tohex(data.exponent));
					var enPassword = hex2b64(rsaKey.encrypt($password.val()));
					$.ajax({
						url: $registerForm.attr("action"),
						type: "POST",
						data: {
							username: $username.val(),
							enPassword: enPassword,
							email: $email.val()
							[#if setting.captchaTypes?? && setting.captchaTypes?seq_contains("memberRegister")]
								,captchaId: "${captchaId}",
								captcha: $captcha.val()
							[/#if]
							[@member_attribute_list]
								[#list memberAttributes as memberAttribute]
									,memberAttribute_${memberAttribute.id}: [#if memberAttribute.enumType == "gender"]$(":input[name='memberAttribute_${memberAttribute.id}']:checked").val()[#else]$(":input[name='memberAttribute_${memberAttribute.id}']").val()[/#if]
								[/#list]
							[/@member_attribute_list]
						},
						dataType: "json",
						cache: false,
						success: function(message) {
							$.message(message);
							if (message.type == "success") {
								setTimeout(function() {
									$submit.prop("disabled", false);
									location.href = "${base}/";
								}, 3000);
							} else {
								$submit.prop("disabled", false);
								[#if setting.captchaTypes?? && setting.captchaTypes?seq_contains("memberRegister")]
									$captcha.val("");
									$captchaImage.attr("src", "${base}/common/captcha.jhtml?captchaId=${captchaId}&timestamp=" + new Date().getTime());
								[/#if]
							}
						}
					});
				}
			});
		}
	});

});
</script>
</head>
<body>
	[#include "/shop/${theme}/include/header.ftl" /]
	<div class="container register">
		<div class="row">
			<div class="span12">
				<div class="wrap">
					<div class="main clearfix">
						<div class="title">
							<strong>${message("shop.register.title")}</strong>USER REGISTER
						</div>
						<form id="registerForm" action="${base}/register/submit.jhtml" method="post">
							<table>
								<tr>
									<th>
										<span class="requiredField">*</span>${message("shop.register.username")}:
									</th>
									<td>
										<input type="text" id="username" name="username" class="text" maxlength="${setting.usernameMaxLength}" />
									</td>
								</tr>
								<tr>
									<th>
										<span class="requiredField">*</span>${message("shop.register.password")}:
									</th>
									<td>
										<input type="password" id="password" name="password" class="text" maxlength="${setting.passwordMaxLength}" autocomplete="off" />
									</td>
								</tr>
								<tr>
									<th>
										<span class="requiredField">*</span>${message("shop.register.rePassword")}:
									</th>
									<td>
										<input type="password" name="rePassword" class="text" maxlength="${setting.passwordMaxLength}" autocomplete="off" />
									</td>
								</tr>
								<tr>
									<th>
										<span class="requiredField">*</span>${message("shop.register.email")}:
									</th>
									<td>
										<input type="text" id="email" name="email" class="text" maxlength="200" />
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
													<input type="text" name="memberAttribute_${memberAttribute.id}" class="text" maxlength="200" />
												[#elseif memberAttribute.enumType == "gender"]
													<span class="fieldSet">
														[#list genders as gender]
															<label>
																<input type="radio" name="memberAttribute_${memberAttribute.id}" value="${gender.ordinal()}" />${message("Member.Gender." + gender)}
															</label>
														[/#list]
													</span>
												[#elseif memberAttribute.enumType == "birth"]
													<input type="text" name="memberAttribute_${memberAttribute.id}" class="text" onfocus="WdatePicker();" />
												[#elseif memberAttribute.enumType == "area"]
													<span class="fieldSet">
														<input type="hidden" id="areaId" name="memberAttribute_${memberAttribute.id}" />
													</span>
												[#elseif memberAttribute.enumType == "address"]
													<input type="text" name="memberAttribute_${memberAttribute.id}" class="text" maxlength="200" />
												[#elseif memberAttribute.enumType == "zipCode"]
													<input type="text" name="memberAttribute_${memberAttribute.id}" class="text" maxlength="200" />
												[#elseif memberAttribute.enumType == "phone"]
													<input type="text" name="memberAttribute_${memberAttribute.id}" class="text" maxlength="200" />
												[#elseif memberAttribute.enumType == "mobile"]
													<input type="text" name="memberAttribute_${memberAttribute.id}" class="text" maxlength="200" />
												[#elseif memberAttribute.enumType == "text"]
													<input type="text" name="memberAttribute_${memberAttribute.id}" class="text" maxlength="200" />
												[#elseif memberAttribute.enumType == "select"]
													<select name="memberAttribute_${memberAttribute.id}">
														<option value="">${message("shop.common.choose")}</option>
														[#list memberAttribute.optionList as option]
															<option value="${option}">
																${option}
															</option>
														[/#list]
													</select>
												[#elseif memberAttribute.enumType == "checkbox"]
													<span class="fieldSet">
														[#list memberAttribute.optionList as option]
															<label>
																<input type="checkbox" name="memberAttribute_${memberAttribute.id}" value="${option}" />${option}
															</label>
														[/#list]
													</span>
												[/#if]
											</td>
										</tr>
									[/#list]
								[/@member_attribute_list]
								[#if setting.captchaTypes?? && setting.captchaTypes?seq_contains("memberRegister")]
									<tr>
										<th>
											<span class="requiredField">*</span>${message("shop.captcha.name")}:
										</th>
										<td>
											<span class="fieldSet">
												<input type="text" id="captcha" name="captcha" class="text captcha" maxlength="4" autocomplete="off" /><img id="captchaImage" class="captchaImage" src="${base}/common/captcha.jhtml?captchaId=${captchaId}" title="${message("shop.captcha.imageTitle")}" />
											</span>
										</td>
									</tr>
								[/#if]
								<tr>
									<th>
										&nbsp;
									</th>
									<td>
										<input type="submit" class="submit" value="${message("shop.register.submit")}" />
									</td>
								</tr>
								<tr>
									<th>
										&nbsp;
									</th>
									<td>
										${message("shop.register.agreement")}
									</td>
								</tr>
								<tr>
									<th>
										&nbsp;
									</th>
									<td>
										[#noescape]
											<div id="agreement" class="agreement">${setting.registerAgreement}</div>
										[/#noescape]
									</td>
								</tr>
							</table>
							<div class="login">
								<dl>
									<dt>${message("shop.register.hasAccount")}</dt>
									<dd>
										${message("shop.register.tips")}
										<a href="${base}/login.jhtml">${message("shop.register.login")}</a>
									</dd>
								</dl>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	[#include "/shop/${theme}/include/footer.ftl" /]
</body>
</html>
[/#escape]