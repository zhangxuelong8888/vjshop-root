[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.payment.view")} - Powered By VJSHOP</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript">
$().ready(function() {

	[@flash_message /]

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.payment.view")}
	</div>
	<table class="input">
		<tr>
			<th>
				${message("Payment.sn")}:
			</th>
			<td>
				${payment.sn}
			</td>
			<th>
				${message("admin.common.createDate")}:
			</th>
			<td>
				${payment.createDate?string("yyyy-MM-dd HH:mm:ss")}
			</td>
		</tr>
		<tr>
			<th>
				${message("Payment.method")}:
			</th>
			<td>
				${message("Payment.Method." + paymentMethods[payment.method])}
			</td>
			<th>
				${message("Payment.paymentMethod")}:
			</th>
			<td>
				${payment.paymentMethod!"-"}
			</td>
		</tr>
		<tr>
			<th>
				${message("Payment.bank")}:
			</th>
			<td>
				${payment.bank!"-"}
			</td>
			<th>
				${message("Payment.account")}:
			</th>
			<td>
				${payment.account!"-"}
			</td>
		</tr>
		<tr>
			<th>
				${message("Payment.amount")}:
			</th>
			<td>
				${currency(payment.amount, true)}
				[#if payment.fee > 0]
					<span class="silver">${message("Payment.fee")}: (${currency(payment.fee, true)})</span>
				[/#if]
			</td>
			<th>
				${message("Payment.payer")}:
			</th>
			<td>
				${payment.payer!"-"}
			</td>
		</tr>
		<tr>
			<th>
				${message("Payment.order")}:
			</th>
			<td>
				${payment.order.sn}
			</td>
			<th>
				${message("Payment.operator")}:
			</th>
			<td>
				${payment.operator!"-"}
			</td>
		</tr>
		<tr>
			<th>
				${message("Payment.memo")}:
			</th>
			<td colspan="3">
				${payment.memo!"-"}
			</td>
		</tr>
		<tr>
			<th>
				&nbsp;
			</th>
			<td colspan="3">
				<input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
			</td>
		</tr>
	</table>
</body>
</html>
[/#escape]