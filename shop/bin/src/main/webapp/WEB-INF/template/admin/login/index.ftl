[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.login.title")} - Powered By VJSHOP</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/admin/css/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jsbn.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/prng4.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/rng.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/rsa.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/base64.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript">
$().ready( function() {

	var $loginForm = $("#loginForm");
	var $enPassword = $("#enPassword");
	var $username = $("#username");
	var $password = $("#password");
	var $captcha = $("#captcha");
	var $captchaImage = $("#captchaImage");
	var $isRememberUsername = $("#isRememberUsername");
	
	[#if failureMessage??]
		$.message("${failureMessage.type}", "${failureMessage.content}");
	[/#if]
	
	// 记住用户名
	if(getCookie("adminUsername") != null) {
		$isRememberUsername.prop("checked", true);
		$username.val(getCookie("adminUsername"));
		$password.focus();
	} else {
		$isRememberUsername.prop("checked", false);
		$username.focus();
	}
	
	// 更换验证码
	$captchaImage.click( function() {
		$captchaImage.attr("src", "common/captcha.jhtml?captchaId=${captchaId}&timestamp=" + new Date().getTime());
	});
	
	// 表单验证、记住用户名
	$loginForm.submit( function() {
		if ($username.val() == "") {
			$.message("warn", "${message("admin.login.usernameRequired")}");
			return false;
		}
		if ($password.val() == "") {
			$.message("warn", "${message("admin.login.passwordRequired")}");
			return false;
		}
		if ($captcha.val() == "") {
			$.message("warn", "${message("admin.login.captchaRequired")}");
			return false;
		}
		
		if ($isRememberUsername.prop("checked")) {
			addCookie("adminUsername", $username.val(), {expires: 7 * 24 * 60 * 60});
		} else {
			removeCookie("adminUsername");
		}
		
		var rsaKey = new RSAKey();
		rsaKey.setPublic(b64tohex("${modulus}"), b64tohex("${exponent}"));
		var enPassword = hex2b64(rsaKey.encrypt($password.val()));
		$enPassword.val(enPassword);
	});

});
</script>
</head>
<body>
	<div class="login">
		<form id="loginForm" action="login.jhtml" method="post">
			<input type="hidden" id="enPassword" name="enPassword" />
			[#if setting.captchaTypes?? && setting.captchaTypes?seq_contains("adminLogin")]
				<input type="hidden" name="captchaId" value="${captchaId}" />
			[/#if]
			<table>
				<tr>
					<td width="190" rowspan="2" align="center" valign="bottom">
						<img src="${base}/resources/admin/images/login_logo.gif" alt="VJSHOP" />
					</td>
					<th>
						${message("admin.login.username")}:
					</th>
					<td>
						<input type="text" id="username" name="username" class="text" maxlength="20" />
					</td>
				</tr>
				<tr>
					<th>
						${message("admin.login.password")}:
					</th>
					<td>
						<input type="password" id="password" class="text" maxlength="20" autocomplete="off" />
					</td>
				</tr>
				[#if setting.captchaTypes?? && setting.captchaTypes?seq_contains("adminLogin")]
					<tr>
						<td>
							&nbsp;
						</td>
						<th>
							${message("admin.captcha.name")}:
						</th>
						<td>
							<input type="text" id="captcha" name="captcha" class="text captcha" maxlength="4" autocomplete="off" /><img id="captchaImage" class="captchaImage" src="common/captcha.jhtml?captchaId=${captchaId}" title="${message("admin.captcha.imageTitle")}" />
						</td>
					</tr>
				[/#if]
				<tr>
					<td>
						&nbsp;
					</td>
					<th>
						&nbsp;
					</th>
					<td>
						<label>
							<input type="checkbox" id="isRememberUsername" value="true" />
							${message("admin.login.rememberUsername")}
						</label>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<th>
						&nbsp;
					</th>
					<td>
						<input type="button" class="homeButton" value="" onclick="location.href='${base}/'" /><input type="submit" class="loginButton" value="${message("admin.login.login")}" />
					</td>
				</tr>
			</table>
			<div class="powered">COPYRIGHT © 2005-2015 vjshop.com ALL RIGHTS RESERVED.</div>
			<div class="link">
				<a href="${base}/">${message("admin.login.home")}</a> |
				<a href="http://www.vjshop.com">${message("admin.login.official")}</a> |
				<a href="http://bbs.vjshop.com">${message("admin.login.bbs")}</a> |
				<a href="http://www.vjshop.com/about.html">${message("admin.login.about")}</a> |
				<a href="http://www.vjshop.com/contact.html">${message("admin.login.contact")}</a> |
				<a href="http://www.vjshop.com/license.html">${message("admin.login.license")}</a>
			</div>
		</form>
	</div>
</body>
</html>
[/#escape]