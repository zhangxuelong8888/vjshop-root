[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.log.list")} - Powered By VJSHOP</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $clearButton = $("#clearButton");
	var $resultRow = $("#listTable tr:gt(0)");
	
	[@flash_message /]
	
	$clearButton.click(function() {
		var $this = $(this);
		$.dialog({
			type: "warn",
			content: "${message("admin.dialog.clearConfirm")}",
			onOk: function() {
				$.ajax({
					url: "clear.jhtml",
					type: "POST",
					dataType: "json",
					cache: false,
					success: function(message) {
						if (message.type == "success") {
							$resultRow.remove();
						}
						$.message(message);
						setTimeout(function() {
							location.reload(true);
						}, 3000);
					}
				});
			}
		});
		return false;
	});

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.log.list")} <span>(${message("admin.page.total", page.total)})</span>
	</div>
	<form id="listForm" action="list.jhtml" method="get">
		<div class="bar">
			<div class="buttonGroup">
				<a href="javascript:;" id="deleteButton" class="iconButton disabled">
					<span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
				</a>
				<a href="javascript:;" id="clearButton" class="iconButton">
					<span class="clearIcon">&nbsp;</span>${message("admin.common.clear")}
				</a>
				<a href="javascript:;" id="refreshButton" class="iconButton">
					<span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
				</a>
				<div id="pageSizeMenu" class="dropdownMenu">
					<a href="javascript:;" class="button">
						${message("admin.page.pageSize")}<span class="arrow">&nbsp;</span>
					</a>
					<ul>
						<li[#if page.pageSize == 10] class="current"[/#if] val="10">10</li>
						<li[#if page.pageSize == 20] class="current"[/#if] val="20">20</li>
						<li[#if page.pageSize == 50] class="current"[/#if] val="50">50</li>
						<li[#if page.pageSize == 100] class="current"[/#if] val="100">100</li>
					</ul>
				</div>
			</div>
			<div id="searchPropertyMenu" class="dropdownMenu">
				<div class="search">
					<span class="arrow">&nbsp;</span>
					<input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200" />
					<button type="submit">&nbsp;</button>
				</div>
				<ul>
					<li[#if page.searchProperty == "operation"] class="current"[/#if] val="operation">${message("Log.operation")}</li>
					<li[#if page.searchProperty == "operator"] class="current"[/#if] val="operator">${message("Log.operator")}</li>
				</ul>
			</div>
		</div>
		<table id="listTable" class="list">
			<tr>
				<th class="check">
					<input type="checkbox" id="selectAll" />
				</th>
				<th>
					<a href="javascript:;" class="sort" name="operation">${message("Log.operation")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="operator">${message("Log.operator")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="ip">${message("Log.ip")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="content">${message("Log.content")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="createDate">${message("admin.common.createDate")}</a>
				</th>
				<th>
					<span>${message("admin.common.action")}</span>
				</th>
			</tr>
			[#list page.content as log]
				<tr>
					<td>
						<input type="checkbox" name="ids" value="${log.id}" />
					</td>
					<td>
						${log.operation}
					</td>
					<td>
						${log.operator}
					</td>
					<td>
						${log.ip}
					</td>
					<td>
						[#if log.content??]
							<span title="${log.content}">${abbreviate(log.content, 50, "...")}</span>
						[/#if]
					</td>
					<td>
						<span title="${log.createDate?string("yyyy-MM-dd HH:mm:ss")}">${log.createDate}</span>
					</td>
					<td>
						<a href="view.jhtml?id=${log.id}">[${message("admin.common.view")}]</a>
					</td>
				</tr>
			[/#list]
		</table>
		[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
			[#include "/admin/include/pagination.ftl"]
		[/@pagination]
	</form>
</body>
</html>
[/#escape]