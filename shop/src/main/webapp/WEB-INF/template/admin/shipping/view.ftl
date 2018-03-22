[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.shipping.view")} - Powered By VJSHOP</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
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
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.shipping.view")}
	</div>
	<ul id="tab" class="tab">
		<li>
			<input type="button" value="${message("admin.shipping.base")}" />
		</li>
		<li>
			<input type="button" value="${message("admin.shipping.shippingItem")}" />
		</li>
	</ul>
	<table class="input tabContent">
		<tr>
			<th>
				${message("Shipping.sn")}:
			</th>
			<td>
				${shipping.sn}
			</td>
			<th>
				${message("admin.common.createDate")}:
			</th>
			<td>
				${shipping.createDate?string("yyyy-MM-dd HH:mm:ss")}
			</td>
		</tr>
		<tr>
			<th>
				${message("Shipping.shippingMethod")}:
			</th>
			<td>
				${shipping.shippingMethod!"-"}
			</td>
			<th>
				${message("Shipping.deliveryCorp")}:
			</th>
			<td>
				${shipping.deliveryCorp!"-"}
			</td>
		</tr>
		<tr>
			<th>
				${message("Shipping.trackingNo")}:
			</th>
			<td>
				${shipping.trackingNo!"-"}
			</td>
			<th>
				${message("Shipping.freight")}:
			</th>
			<td>
				${currency(shipping.freight, true)!"-"}
			</td>
		</tr>
		<tr>
			<th>
				${message("Shipping.consignee")}:
			</th>
			<td>
				${shipping.consignee!"-"}
			</td>
			<th>
				${message("Shipping.phone")}:
			</th>
			<td>
				${shipping.phone!"-"}
			</td>
		</tr>
		<tr>
			<th>
				${message("Shipping.area")}:
			</th>
			<td>
				${shipping.area!"-"}
			</td>
			<th>
				${message("Shipping.address")}:
			</th>
			<td>
				${shipping.address!"-"}
			</td>
		</tr>
		<tr>
			<th>
				${message("Shipping.zipCode")}:
			</th>
			<td>
				${shipping.zipCode!"-"}
			</td>
			<th>
				${message("Shipping.order")}:
			</th>
			<td>
				${shipping.orderVO.sn}
			</td>
		</tr>
		<tr>
			
			<th>
				${message("Shipping.operator")}:
			</th>
			<td>
				${shipping.operator!"-"}
			</td>
			<th>
				${message("Shipping.memo")}:
			</th>
			<td>
				${shipping.memo!"-"}
			</td>
		</tr>
	</table>
	<table class="item tabContent">
		<tr>
			<th>
				${message("ShippingItem.sn")}
			</th>
			<th>
				${message("ShippingItem.name")}
			</th>
			<th>
				${message("ShippingItem.quantity")}
			</th>
			<th>
				${message("ShippingItem.isDelivery")}
			</th>
		</tr>
		[#list shipping.shippingItems as shippingItem]
			<tr>
				<td>
					${shippingItem.sn}
				</td>
				<td>
					<span title="${shippingItem.name}">${abbreviate(shippingItem.name, 50, "...")}</span>
				</td>
				<td>
					${shippingItem.quantity}
				</td>
				<td>
					${message(shippingItem.isDelivery?string('admin.common.true', 'admin.common.false'))}
				</td>
			</tr>
		[/#list]
	</table>
	<table class="input">
		<tr>
			<th>
				&nbsp;
			</th>
			<td>
				<input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
			</td>
		</tr>
	</table>
</body>
</html>
[/#escape]