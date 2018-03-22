[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("shop.member.receiver.add")}[#if showPowered] - Powered By VJSHOP[/#if]</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/shop/${theme}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/shop/${theme}/css/member.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.lSelect.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/common.js"></script>
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
			consignee: "required",
			areaId: "required",
			address: "required",
			zipCode: {
				required: true,
				pattern: /^\d{6}$/
			},
			phone: {
				required: true,
				pattern: /^\d{3,4}-?\d{7,9}$/
			}
		}
	});

});
</script>
</head>
<body>
	[#assign current = "receiverList" /]
	[#include "/shop/${theme}/include/header.ftl" /]
	<div class="container member">
		<div class="row">
			[#include "/shop/${theme}/member/include/navigation.ftl" /]
			<div class="span10">
				<div class="input">
					<div class="title">${message("shop.member.receiver.add")}</div>
					<form id="inputForm" action="save.jhtml" method="post">
						<table class="input">
							<tr>
								<th>
									${message("Receiver.consignee")}:
								</th>
								<td>
									<input type="text" name="consignee" class="text" maxlength="200" />
								</td>
							</tr>
							<tr>
								<th>
									${message("Receiver.area")}:
								</th>
								<td>
									<span class="fieldSet">
										<input type="hidden" id="areaId" name="areaId" />
									</span>
								</td>
							</tr>
							<tr>
								<th>
									${message("Receiver.address")}:
								</th>
								<td>
									<input type="text" name="address" class="text" maxlength="200" />
								</td>
							</tr>
							<tr>
								<th>
									${message("Receiver.zipCode")}:
								</th>
								<td>
									<input type="text" name="zipCode" class="text" maxlength="200" />
								</td>
							</tr>
							<tr>
								<th>
									${message("Receiver.phone")}:
								</th>
								<td>
									<input type="text" name="phone" class="text" maxlength="200" />
								</td>
							</tr>
							<tr>
								<th>
									${message("Receiver.isDefault")}:
								</th>
								<td>
									<input type="checkbox" name="isDefault" value="true" />
									<input type="hidden" name="_isDefault" value="false" />
								</td>
							</tr>
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