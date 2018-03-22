[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("shop.illegalLicense.title")}[#if showPowered] - Powered By VJSHOP[/#if]</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/shop/${theme}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/shop/${theme}/css/error.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/common.js"></script>
</head>
<body>
	[#include "/shop/${theme}/include/header.ftl" /]
	<div class="container error">
		<div class="row">
			<div class="span12">
				<div class="main">
					<dl>
						[#noescape]
							${message("shop.illegalLicense.message")}
						[/#noescape]
						<dd>
							<a href="javascript:;" onclick="history.back(); return false;">&gt;&gt; ${message("shop.illegalLicense.back")}</a>
						</dd>
						<dd>
							<a href="${base}/">&gt;&gt; ${message("shop.illegalLicense.home")}</a>
						</dd>
					</dl>
				</div>
			</div>
		</div>
	</div>
	[#include "/shop/${theme}/include/footer.ftl" /]
</body>
</html>
[/#escape]