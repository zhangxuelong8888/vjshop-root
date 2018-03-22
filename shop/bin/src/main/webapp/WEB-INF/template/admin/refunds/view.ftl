[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.refunds.view")} - Powered By VJSHOP</title>
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
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.refunds.view")}
	</div>
	<table class="input">
		<tr>
			<th>
				${message("Refunds.sn")}:
			</th>
			<td>
				${refunds.sn}
			</td>
			<th>
				${message("admin.common.createDate")}:
			</th>
			<td>
				${refunds.createDate?string("yyyy-MM-dd HH:mm:ss")}
			</td>
		</tr>
		<tr>
			<th>
				${message("Refunds.method")}:
			</th>
			<td>
				${message("Refunds.Method." + refundsMethods[refunds.method])}
			</td>
			<th>
				${message("Refunds.paymentMethod")}:
			</th>
			<td>
				${refunds.paymentMethod!"-"}
			</td>
		</tr>
		<tr>
			<th>
				${message("Refunds.bank")}:
			</th>
			<td>
				${refunds.bank!"-"}
			</td>
			<th>
				${message("Refunds.account")}:
			</th>
			<td>
				${refunds.account!"-"}
			</td>
		</tr>
		<tr>
			<th>
				${message("Refunds.amount")}:
			</th>
			<td>
				${currency(refunds.amount, true)}
			</td>
			<th>
				${message("Refunds.payee")}:
			</th>
			<td>
				${refunds.payee!"-"}
			</td>
		</tr>
		<tr>
			<th>
				${message("Refunds.order")}:
			</th>
			<td>
				${refunds.orderVO.sn}
			</td>
			<th>
				${message("Refunds.operator")}:
			</th>
			<td>
				${refunds.operator!"-"}
			</td>
		</tr>
		<tr>
			<th>
				${message("Refunds.memo")}:
			</th>
			<td colspan="3">
				${refunds.memo!"-"}
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