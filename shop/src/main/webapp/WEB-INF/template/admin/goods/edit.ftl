[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.goods.edit")} - Powered By VJSHOP</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/webuploader.js"></script>
<script type="text/javascript" src="${base}/resources/admin/ueditor/ueditor.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<style type="text/css">
	.parameterTable table th {
		width: 146px;
	}
	
	.specificationTable span {
		padding: 10px;
	}
	
	.productTable td {
		border: 1px solid #dde9f5;
	}
	
	.productTable .current td {
		background-color: #fafbff;
	}
</style>
<script type="text/javascript">
$().ready(function() {
	var $inputForm = $("#inputForm");
	var $isDefault = $("#isDefault");
	var $productCategoryId = $("#productCategoryId");
	var $price = $("#price");
	var $cost = $("#cost");
	var $marketPrice = $("#marketPrice");
	var $filePicker = $("#filePicker");
	var $rewardPoint = $("#rewardPoint");
	var $exchangePoint = $("#exchangePoint");
	var $stock = $("#stock");
	var $promotionIds = $("input[name='promotionIds']");
	var $introduction = $("#introduction");
	var $productImageTable = $("#productImageTable");
	var $addProductImage = $("#addProductImage");
	var $parameterTable = $("#parameterTable");
	var $addParameter = $("#addParameter");
	var $resetParameter = $("#resetParameter");
	var $attributeTable = $("#attributeTable");
	var $specificationTable = $("#specificationTable");
	var $resetSpecification = $("#resetSpecification");
	var $productTable = $("#productTable");
	var previousProductCategoryId = ${goods.productCategory};
	var productImageIndex = ${(goods.productImagesList?size)!0};
	var parameterIndex = ${(goods.parameterValuesList?size)!0};
	var specificationItemEntryId = ${(goods.specificationItemEntryIds?last + 1)!0};
	var hasSpecification = ${goods.hasSpecification()?string("true", "false")};
	var initProductValues = {};
	
	[@flash_message /]
	
	[#if !goods.parameterValuesList?has_content]
		loadParameter();
	[/#if]
	[#if goods.hasSpecification()]
		[#list goods.products as product]
			initProductValues["${product.specificationValueIds?join(",")}"] = {
				id: ${product.id},
				sn: "${product.sn}",
				price: ${product.price},
				cost: ${product.cost!"null"},
				marketPrice: ${product.marketPrice},
				rewardPoint: ${product.rewardPoint},
				exchangePoint: ${product.exchangePoint},
				stock: ${product.stock},
				allocatedStock: ${product.allocatedStock},
				isDefault: ${product.isDefault?string("true", "false")},
				isEnabled: true
			};
		[/#list]
		buildProductTable(initProductValues);
	[#else]
		loadSpecification();
	[/#if]
	$filePicker.uploader();
	
	$introduction.editor();
	
	// 商品分类
	$productCategoryId.change(function() {
		if ($attributeTable.find("select[value!='']").size() > 0) {
			$.dialog({
				type: "warn",
				content: "${message("admin.goods.productCategoryChangeConfirm")}",
				width: 450,
				onOk: function() {
					if ($parameterTable.find("input.parameterEntryValue[value!='']").size() == 0) {
						loadParameter();
					}
					loadAttribute();
					if ($productTable.find("input:text[value!='']").size() == 0) {
						loadSpecification();
					}
					previousProductCategoryId = $productCategoryId.val();
				},
				onCancel: function() {
					$productCategoryId.val(previousProductCategoryId);
				}
			});
		} else {
			if ($parameterTable.find("input.parameterEntryValue[value!='']").size() == 0) {
				loadParameter();
			}
			loadAttribute();
			if ($productTable.find("input:text[value!='']").size() == 0) {
				loadSpecification();
			}
			previousProductCategoryId = $productCategoryId.val();
		}
	});
	
	// 修改视图
	function changeView() {
		if (hasSpecification) {
			$isDefault.prop("disabled", true);
			$price.add($cost).add($marketPrice).add($rewardPoint).add($exchangePoint).add($stock).prop("disabled", true).closest("tr").hide();
		} else {
			$isDefault.prop("disabled", false);
			$price.add($cost).add($marketPrice).add($rewardPoint).add($exchangePoint).add($stock).prop("disabled", false).closest("tr").show();
		}
	}
	
	// 增加商品图片
	$addProductImage.click(function() {
		$productImageTable.append(
			[@compress single_line = true]
				'<tr>
					<td>
						<input type="file" name="productImagesList[' + productImageIndex + '].file" class="productImageFile" \/>
					<\/td>
					<td>
						<input type="text" name="productImagesList[' + productImageIndex + '].title" class="text" maxlength="200" \/>
					<\/td>
					<td>
						<input type="text" name="productImagesList[' + productImageIndex + '].order" class="text productImageOrder" maxlength="9" style="width: 50px;" \/>
					<\/td>
					<td>
						<a href="javascript:;" class="remove">[${message("admin.common.remove")}]<\/a>
					<\/td>
				<\/tr>'
			[/@compress]
		);
		productImageIndex ++;
	});
	
	// 删除商品图片
	$productImageTable.on("click", "a.remove", function() {
		$(this).closest("tr").remove();
	});
	
	// 增加参数
	$addParameter.click(function() {
		$(
			[@compress single_line = true]
				'<tr>
					<td colspan="2">
						<table>
							<tr>
								<th>
									${message("Parameter.group")}:
								<\/th>
								<td>
									<input type="text" name="parameterValuesList[' + parameterIndex + '].group" class="text parameterGroup" maxlength="200" \/>
								<\/td>
								<td>
									<a href="javascript:;" class="remove group">[${message("admin.common.remove")}]<\/a>
									<a href="javascript:;" class="add">[${message("admin.common.add")}]<\/a>
								<\/td>
							<\/tr>
							<tr>
								<th>
									<input type="text" name="parameterValuesList[' + parameterIndex + '].entries[0].name" class="text parameterEntryName" maxlength="200" style="width: 50px;" \/>
								<\/th>
								<td>
									<input type="text" name="parameterValuesList[' + parameterIndex + '].entries[0].value" class="text parameterEntryValue" maxlength="200" \/>
								<\/td>
								<td>
									<a href="javascript:;" class="remove">[${message("admin.common.remove")}]<\/a>
								<\/td>
							<\/tr>
						<\/table>
					<\/td>
				<\/tr>'
			[/@compress]
		).appendTo($parameterTable).find("table").data("parameterIndex", parameterIndex).data("parameterEntryIndex", 1);
		parameterIndex ++;
	});
	
	// 重置参数
	$resetParameter.click(function() {
		$.dialog({
			type: "warn",
			content: "${message("admin.goods.resetParameterConfirm")}",
			width: 450,
			onOk: function() {
				loadParameter();
			}
		});
	});
	
	// 删除参数
	$parameterTable.on("click", "a.remove", function() {
		var $this = $(this);
		if ($this.hasClass("group")) {
			$this.closest("table").closest("tr").remove();
		} else {
			if ($this.closest("table").find("tr").size() <= 2) {
				$.message("warn", "${message("admin.common.deleteAllNotAllowed")}");
				return false;
			}
			$this.closest("tr").remove();
		}
	});
	
	// 增加参数
	$parameterTable.on("click", "a.add", function() {
		var $table = $(this).closest("table");
		var parameterIndex = $table.data("parameterIndex");
		var parameterEntryIndex = $table.data("parameterEntryIndex");
		$table.append(
			[@compress single_line = true]
				'<tr>
					<th>
						<input type="text" name="parameterValuesList[' + parameterIndex + '].entries[' + parameterEntryIndex + '].name" class="text parameterEntryName" maxlength="200" style="width: 50px;" \/>
					<\/th>
					<td>
						<input type="text" name="parameterValuesList[' + parameterIndex + '].entries[' + parameterEntryIndex + '].value" class="text parameterEntryValue" maxlength="200" \/>
					<\/td>
					<td>
						<a href="javascript:;" class="remove">[${message("admin.common.remove")}]<\/a>
					<\/td>
				<\/tr>'
			[/@compress]
		);
		$table.data("parameterEntryIndex", parameterEntryIndex + 1);
	});
	
	// 加载参数
	function loadParameter() {
		$.ajax({
			url: "parameters.jhtml",
			type: "GET",
			data: {productCategoryId: $productCategoryId.val()},
			dataType: "json",
			success: function(data) {
				parameterIndex = 0;
				$parameterTable.find("tr:gt(0)").remove();
				$.each(data, function(i, parameter) {
					var $parameterGroupTable = $(
						[@compress single_line = true]
							'<tr>
								<td colspan="2">
									<table>
										<tr>
											<th>
												${message("Parameter.group")}:
											<\/th>
											<td>
												<input type="text" name="parameterValuesList[' + parameterIndex + '].group" class="text parameterGroup" value="' + escapeHtml(parameter.group) + '" maxlength="200" \/>
											<\/td>
											<td>
												<a href="javascript:;" class="remove group">[${message("admin.common.remove")}]<\/a>
												<a href="javascript:;" class="add">[${message("admin.common.add")}]<\/a>
											<\/td>
										<\/tr>
									<\/table>
								<\/td>
							<\/tr>'
						[/@compress]
					).appendTo($parameterTable).find("table").data("parameterIndex", parameterIndex);
					
					var parameterEntryIndex = 0;
					$.each(parameter.names, function(i, name) {
						$parameterGroupTable.append(
							[@compress single_line = true]
								'<tr>
									<th>
										<input type="text" name="parameterValuesList[' + parameterIndex + '].entries[' + parameterEntryIndex + '].name" class="text parameterEntryName" value="' + escapeHtml(name) + '" maxlength="200" style="width: 50px;" \/>
									<\/th>
									<td>
										<input type="text" name="parameterValuesList[' + parameterIndex + '].entries[' + parameterEntryIndex + '].value" class="text parameterEntryValue" maxlength="200" \/>
									<\/td>
									<td>
										<a href="javascript:;" class="remove">[${message("admin.common.remove")}]<\/a>
									<\/td>
								<\/tr>'
							[/@compress]
						);
						parameterEntryIndex ++;
					});
					$parameterGroupTable.data("parameterEntryIndex", parameterEntryIndex);
					parameterIndex ++;
				});
			}
		});
	}
	
	// 加载属性
	function loadAttribute() {
		$.ajax({
			url: "attributes.jhtml",
			type: "GET",
			data: {productCategoryId: $productCategoryId.val()},
			dataType: "json",
			success: function(data) {
				$attributeTable.empty();
				$.each(data, function(i, attribute) {
					var $select = $(
						[@compress single_line = true]
							'<tr>
								<th>' + escapeHtml(attribute.name) + ':<\/th>
								<td>
									<select name="attribute_' + attribute.id + '">
										<option value="">${message("admin.common.choose")}<\/option>
									<\/select>
								<\/td>
							<\/tr>'
						[/@compress]
					).appendTo($attributeTable).find("select");
					$.each(attribute.options, function(j, option) {
						$select.append('<option value="' + escapeHtml(option) + '">' + escapeHtml(option) + '<\/option>');
					});
				});
			}
		});
	}
	
	// 重置规格
	$resetSpecification.click(function() {
		$.dialog({
			type: "warn",
			content: "${message("admin.goods.resetSpecificationConfirm")}",
			width: 450,
			onOk: function() {
				hasSpecification = false;
				changeView();
				loadSpecification();
			}
		});
	});
	
	// 选择规格
	$specificationTable.on("change", "input:checkbox", function() {
		if ($specificationTable.find("input:checkbox:checked").size() > 0) {
			hasSpecification = true;
		} else {
			hasSpecification = false;
		}
		changeView();
		buildProductTable();
	});
	
	// 规格
	$specificationTable.on("change", "input:text", function() {
		var $this = $(this);
		var value = $.trim($this.val());
		if (value == "") {
			$this.val($this.data("value"));
			return false;
		}
		if ($this.hasClass("specificationItemEntryValue")) {
			var values = $this.closest("tr").find("input.specificationItemEntryValue").not($this).map(function() {
				return $.trim($(this).val());
			}).get();
			if ($.inArray(value, values) >= 0) {
				$.message("warn", "${message("admin.goods.specificationItemEntryValueRepeated")}");
				$this.val($this.data("value"));
				return false;
			}
		}
		$this.data("value", value);
		buildProductTable();
	});
	
	// 是否默认
	$productTable.on("change", "input.isDefault", function() {
		var $this = $(this);
		if ($this.prop("checked")) {
			$productTable.find("input.isDefault").not($this).prop("checked", false);
		} else {
			$this.prop("checked", true);
		}
	});
	
	// 是否启用
	$productTable.on("change", "input.isEnabled", function() {
		var $this = $(this);
		if ($this.prop("checked")) {
			$this.closest("tr").find("input:not(.isEnabled)").prop("disabled", false);
		} else {
			$this.closest("tr").find("input:not(.isEnabled)").prop("disabled", true).end().find("input.isDefault").prop("checked", false);
		}
		if ($productTable.find("input.isDefault:not(:disabled):checked").size() == 0) {
			$productTable.find("input.isDefault:not(:disabled):first").prop("checked", true);
		}
	});
	
	// 生成商品表
	function buildProductTable(productValues) {
		var specificationItems = [];
		if (!hasSpecification) {
			$productTable.empty()
			return false;
		}
		$specificationTable.find("tr:gt(0)").each(function() {
			var $this = $(this);
			var $checked = $this.find("input:checkbox:checked");
			if ($checked.size() > 0) {
				var specificationItem = {};
				specificationItem.name = $this.find("input.specificationItemName").val();
				specificationItem.entries = $checked.map(function() {
					return {
						id: $(this).siblings("input.specificationItemEntryId").val(),
						value: $(this).siblings("input.specificationItemEntryValue").val()
					};
				}).get();
				specificationItems.push(specificationItem);
			}
		});
		var products = cartesianProductOf($.map(specificationItems, function(specificationItem) {
			return [specificationItem.entries];
		}));
		if (productValues == null) {
			productValues = {};
			$productTable.find("tr:gt(0)").each(function() {
				var $this = $(this);
				productValues[$this.data("ids")] = {
					price: $this.find("input.price").val(),
					cost: $this.find("input.cost").val(),
					marketPrice: $this.find("input.marketPrice").val(),
					rewardPoint: $this.find("input.rewardPoint").val(),
					exchangePoint: $this.find("input.exchangePoint").val(),
					stock: $this.find("input.stock").val(),
					isDefault: $this.find("input.isDefault").prop("checked"),
					isEnabled: $this.find("input.isEnabled").prop("checked")
				};
			});
		}
		$titleTr = $('<tr><\/tr>').appendTo($productTable.empty());
		$.each(specificationItems, function(i, specificationItem) {
			$titleTr.append('<th>' + escapeHtml(specificationItem.name) + '<\/th>');
		});
		$titleTr.append(
			[@compress single_line = true]
				'[#if goods.type == "0"]
					<th>
						${message("Product.price")}
					<\/th>
				[/#if]
				<th>
					${message("Product.cost")}
				<\/th>
				<th>
					${message("Product.marketPrice")}
				<\/th>
				[#if goods.type == "0"]
					<th>
						${message("Product.rewardPoint")}
					<\/th>
				[/#if]
				[#if goods.type == "1"]
					<th>
						${message("Product.exchangePoint")}
					<\/th>
				[/#if]
				<th>
					${message("Product.stock")}
				<\/th>
				<th>
					${message("Product.isDefault")}
				<\/th>
				<th>
					${message("admin.goods.isEnabled")}
				<\/th>'
			[/@compress]
		);
		$.each(products, function(i, entries) {
			var ids = [];
			$productTr = $('<tr><\/tr>').appendTo($productTable);
			$.each(entries, function(j, entry) {
				$productTr.append(
					[@compress single_line = true]
						'<td>
							' + escapeHtml(entry.value) + '
							<input type="hidden" name="productList[' + i + '].specificationValuesList[' + j + '].id" value="' + entry.id + '" \/>
							<input type="hidden" name="productList[' + i + '].specificationValuesList[' + j + '].value" value="' + escapeHtml(entry.value) + '" \/>
						<\/td>'
					[/@compress]
				);
				ids.push(entry.id);
			});
			var initProductValue = initProductValues[ids.join(",")];
			var productValue = productValues[ids.join(",")];
			var price = productValue != null && productValue.price != null ? productValue.price : "";
			var cost = productValue != null && productValue.cost != null ? productValue.cost : "";
			var marketPrice = productValue != null && productValue.marketPrice != null ? productValue.marketPrice : "";
			var rewardPoint = productValue != null && productValue.rewardPoint != null ? productValue.rewardPoint : "";
			var exchangePoint = productValue != null && productValue.exchangePoint != null ? productValue.exchangePoint : "";
			var stock = productValue != null && productValue.stock != null ? productValue.stock : "";
			var isDefault = productValue != null && productValue.isDefault != null ? productValue.isDefault : false;
			var isEnabled = productValue != null && productValue.isEnabled != null ? productValue.isEnabled : false;
			$productTr.append(
				[@compress single_line = true]
					'[#if goods.type == "0"]
						<td>
							<input type="text" name="productList[' + i + '].price" class="text price" value="' + price + '" maxlength="16" style="width: 50px;" \/>
						<\/td>
					[/#if]
					<td>
						<input type="text" name="productList[' + i + '].cost" class="text cost" value="' + cost + '" maxlength="16" style="width: 50px;" \/>
					<\/td>
					<td>
						<input type="text" name="productList[' + i + '].marketPrice" class="text marketPrice" value="' + marketPrice + '" maxlength="16" style="width: 50px;" \/>
					<\/td>
					[#if goods.type == "0"]
						<td>
							<input type="text" name="productList[' + i + '].rewardPoint" class="text rewardPoint" value="' + rewardPoint + '" maxlength="9" style="width: 50px;" \/>
						<\/td>
					[/#if]
					[#if goods.type == "1"]
						<td>
							<input type="text" name="productList[' + i + '].exchangePoint" class="text exchangePoint" value="' + exchangePoint + '" maxlength="9" style="width: 50px;" \/>
						<\/td>
					[/#if]
					<td>
						<input type="text" name="productList[' + i + '].stock" class="text stock" value="' + (initProductValue != null ? initProductValue.stock : stock) + '" maxlength="9"' + (initProductValue != null ? ' title="${message("Product.allocatedStock")}: ' + initProductValue.allocatedStock + '" readonly="readonly"' : '') + ' style="width: 50px;" \/>
						' + (initProductValue != null ? '<a href="..\/stock\/stock_in.jhtml?productId=' + initProductValue.id + '" title="${message("admin.goods.stockIn")}">+<\/a> <a href="..\/stock\/stock_out.jhtml?productId=' + initProductValue.id + '" title="${message("admin.goods.stockOut")}">-<\/a>' : '') + '
					<\/td>
					<td>
						<input type="checkbox" name="productList[' + i + '].isDefault" class="isDefault" value="true"' + (isDefault ? ' checked="checked"' : '') + ' \/>
						<input type="hidden" name="_productList[' + i + '].isDefault" value="false" \/>
					<\/td>
					<td>
						<input type="checkbox" name="isEnabled" class="isEnabled" value="true"' + (isEnabled ? ' checked="checked"' : '') + ' \/>
					<\/td>'
				[/@compress]
			).data("ids", ids.join(","));
			if (initProductValue != null) {
				$productTr.addClass("current").attr("title", "${message("Product.sn")}: " + initProductValue.sn);
			}
			if (!isEnabled) {
				$productTr.find(":input:not(.isEnabled)").prop("disabled", true);
			}
		});
		if ($productTable.find("input.isDefault:not(:disabled):checked").size() == 0) {
			$productTable.find("input.isDefault:not(:disabled):first").prop("checked", true);
		}
	}
	
	// 笛卡尔积
	function cartesianProductOf(array) {
		function addTo(current, args) {
			var i, copy;
			var rest = args.slice(1);
			var isLast = !rest.length;
			var result = [];
			for (i = 0; i < args[0].length; i++) {
				copy = current.slice();
				copy.push(args[0][i]);
				if (isLast) {
					result.push(copy);
				} else {
					result = result.concat(addTo(copy, rest));
				}
			}
			return result;
		}
		return addTo([], array);
	}
	
	// 加载规格
	function loadSpecification() {
		$.ajax({
			url: "specifications.jhtml",
			type: "GET",
			data: {productCategoryId: $productCategoryId.val()},
			dataType: "json",
			success: function(data) {
				$specificationTable.find("tr:gt(0)").remove();
				$productTable.empty();
				$.each(data, function(i, specification) {
					var $td = $(
						[@compress single_line = true]
							'<tr>
								<th>
									<input type="text" name="specificationItemsList[' + i + '].name" class="text specificationItemName" value="' + escapeHtml(specification.name) + '" style="width: 50px;" \/>
								<\/th>
								<td><\/td>
							<\/tr>'
						[/@compress]
					).appendTo($specificationTable).find("input").data("value", specification.name).end().find("td");
					$.each(specification.options, function(j, option) {
						$(
							[@compress single_line = true]
								'<span>
									<input type="checkbox" name="specificationItemsList[' + i + '].entries[' + j + '].isSelected" value="true" \/>
									<input type="hidden" name="specificationItemsList[' + i + '].entries[' + j + '].isSelected" value="false" \/>
									<input type="hidden" name="specificationItemsList[' + i + '].entries[' + j + '].id" class="text specificationItemEntryId" value="' + specificationItemEntryId + '" \/>
									<input type="text" name="specificationItemsList[' + i + '].entries[' + j + '].value" class="text specificationItemEntryValue" value="' + escapeHtml(option) + '" style="width: 50px;" \/>
								<\/span>'
							[/@compress]
						).appendTo($td).find("input.specificationItemEntryValue").data("value", option);
						specificationItemEntryId ++;
					});
				});
			}
		});
	}
	
	$.validator.addClassRules({
		productImageFile: {
			required: function(element) {
				return $(element).siblings("input:hidden").size() == 0;
			},
			extension: "${setting.uploadImageExtension}"
		},
		productImageOrder: {
			digits: true
		},
		parameterGroup: {
			required: true
		},
		price: {
			required: true,
			min: 0,
			decimal: {
				integer: 12,
				fraction: ${setting.priceScale}
			}
		},
		cost: {
			min: 0,
			decimal: {
				integer: 12,
				fraction: ${setting.priceScale}
			}
		},
		marketPrice: {
			min: 0,
			decimal: {
				integer: 12,
				fraction: ${setting.priceScale}
			}
		},
		rewardPoint: {
			digits: true
		},
		exchangePoint: {
			required: true,
			digits: true
		},
		stock: {
			required: true,
			digits: true
		}
	});
	
	// 表单验证
	$inputForm.validate({
		rules: {
			productCategoryId: "required",
			name: "required",
			"product.price": {
				required: true,
				min: 0,
				decimal: {
					integer: 12,
					fraction: ${setting.priceScale}
				}
			},
			"product.cost": {
				min: 0,
				decimal: {
					integer: 12,
					fraction: ${setting.priceScale}
				}
			},
			"product.marketPrice": {
				min: 0,
				decimal: {
					integer: 12,
					fraction: ${setting.priceScale}
				}
			},
			image: {
				pattern: /^(http:\/\/|https:\/\/|\/).*$/i
			},
			weight: "digits",
			"product.rewardPoint": "digits",
			"product.exchangePoint": {
				digits: true,
				required: true
			},
			"product.stock": {
				required: true,
				digits: true
			}
		},
		messages: {
			sn: {
				pattern: "${message("admin.validate.illegal")}",
				remote: "${message("admin.validate.exist")}"
			}
		},
		submitHandler: function(form) {
			if (hasSpecification && $productTable.find("input.isEnabled:checked").size() == 0) {
				$.message("warn", "${message("admin.goods.specificationProductRequired")}");
				return false;
			}
			addCookie("previousProductCategoryId", $productCategoryId.val(), {expires: 24 * 60 * 60});
			$(form).find("input:submit").prop("disabled", true);
			form.submit();
		}
	});

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.goods.edit")}
	</div>
	<form id="inputForm" action="update.jhtml" method="post" enctype="multipart/form-data">
		<input type="hidden" name="id" value="${goods.id}" />
		<input type="hidden" id="isDefault" name="product.isDefault" value="true" />
		<ul id="tab" class="tab">
			<li>
				<input type="button" value="${message("admin.goods.base")}" />
			</li>
			<li>
				<input type="button" value="${message("admin.goods.introduction")}" />
			</li>
			<li>
				<input type="button" value="${message("admin.goods.productImage")}" />
			</li>
			<li>
				<input type="button" value="${message("admin.goods.parameter")}" />
			</li>
			<li>
				<input type="button" value="${message("admin.goods.attribute")}" />
			</li>
			<li>
				<input type="button" value="${message("admin.goods.specification")}" />
			</li>
		</ul>
		<table class="input tabContent">
			<tr>
				<th>
					${message("Goods.productCategory")}:
				</th>
				<td>
					<select id="productCategoryId" name="productCategoryId">
						[#list productCategoryTree as productCategory]
							<option value="${productCategory.id}"[#if productCategory.id == goods.productCategory] selected="selected"[/#if]>
								[#if productCategory.grade != 0]
									[#list 1..productCategory.grade as i]
										&nbsp;&nbsp;
									[/#list]
								[/#if]
								${productCategory.name}
							</option>
						[/#list]
					</select>
				</td>
			</tr>
			<tr>
				<th>
					${message("Goods.type")}:
				</th>
				<td>
					${message("Goods.Type." + types[goods.type])}
				</td>
			</tr>
			<tr>
				<th>
					${message("Goods.sn")}:
				</th>
				<td>
					${goods.sn}
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Goods.name")}:
				</th>
				<td>
					<input type="text" name="name" class="text" value="${goods.name}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Goods.caption")}:
				</th>
				<td>
					<input type="text" name="caption" class="text" value="${goods.caption}" maxlength="200" />
				</td>
			</tr>
			[#if goods.type == "0"]
				<tr[#if goods.hasSpecification()] class="hidden"[/#if]>
					<th>
						<span class="requiredField">*</span>${message("Product.price")}:
					</th>
					<td>
						<input type="text" id="price" name="product.price" class="text" value="${goods.price}" maxlength="16"[#if goods.hasSpecification()] disabled="disabled"[/#if] />
					</td>
				</tr>
			[/#if]
			<tr[#if goods.hasSpecification()] class="hidden"[/#if]>
				<th>
					${message("Product.cost")}:
				</th>
				<td>
					<input type="text" id="cost" name="product.cost" class="text" value="${goods.defaultProduct.cost}" maxlength="16" title="${message("admin.goods.costTitle")}"[#if goods.hasSpecification()] disabled="disabled"[/#if] />
				</td>
			</tr>
			<tr[#if goods.hasSpecification()] class="hidden"[/#if]>
				<th>
					${message("Product.marketPrice")}:
				</th>
				<td>
					<input type="text" id="marketPrice" name="product.marketPrice" class="text" value="${goods.marketPrice}" maxlength="16" title="${message("admin.goods.marketPriceTitle")}"[#if goods.hasSpecification()] disabled="disabled"[/#if] />
				</td>
			</tr>
			<tr>
				<th>
					${message("Goods.image")}:
				</th>
				<td>
					<span class="fieldSet">
						<input type="text" name="image" class="text" value="${goods.image}" maxlength="200" title="${message("admin.goods.imageTitle")}" />
						<a href="javascript:;" id="filePicker" class="button">${message("admin.upload.filePicker")}</a>
						[#if goods.image??]
							<a href="${goods.image}" target="_blank">${message("admin.common.view")}</a>
						[/#if]
					</span>
				</td>
			</tr>
			<tr>
				<th>
					${message("Goods.unit")}:
				</th>
				<td>
					<input type="text" name="unit" class="text" value="${goods.unit}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Goods.weight")}:
				</th>
				<td>
					<input type="text" name="weight" class="text" value="${goods.weight}" maxlength="9" title="${message("admin.goods.weightTitle")}" />
				</td>
			</tr>
			[#if goods.type == "0"]
				<tr[#if goods.hasSpecification()] class="hidden"[/#if]>
					<th>
						${message("Product.rewardPoint")}:
					</th>
					<td>
						<input type="text" id="rewardPoint" name="product.rewardPoint" class="text" value="${goods.defaultProduct.rewardPoint}" maxlength="9" title="${message("admin.goods.rewardPointTitle")}"[#if goods.hasSpecification()] disabled="disabled"[/#if] />
					</td>
				</tr>
			[/#if]
			[#if goods.type == "1"]
				<tr[#if goods.hasSpecification()] class="hidden"[/#if]>
					<th>
						<span class="requiredField">*</span>${message("Product.exchangePoint")}:
					</th>
					<td>
						<input type="text" id="exchangePoint" name="product.exchangePoint" class="text" value="${goods.defaultProduct.exchangePoint}" maxlength="9"[#if goods.hasSpecification()] disabled="disabled"[/#if] />
					</td>
				</tr>
			[/#if]
			[#if goods.hasSpecification()]
				<tr class="hidden">
					<th>
						<span class="requiredField">*</span>${message("Product.stock")}:
					</th>
					<td>
						<input type="text" id="stock" name="product.stock" class="text" value="1" maxlength="9" disabled="disabled" />
					</td>
				</tr>
			[#else]
				<tr>
					<th>
						${message("Product.stock")}:
					</th>
					<td>
						<input type="text" id="stock" name="product.stock" class="text" value="${goods.defaultProduct.stock}" maxlength="9" title="${message("Product.allocatedStock")}: ${goods.defaultProduct.allocatedStock}" readonly="readonly" />
						<a href="../stock/stock_in.jhtml?productId=${goods.defaultProduct.id}" title="${message("admin.goods.stockIn")}">+</a>
						<a href="../stock/stock_out.jhtml?productId=${goods.defaultProduct.id}" title="${message("admin.goods.stockOut")}">-</a>
					</td>
				</tr>
			[/#if]
			<tr>
				<th>
					${message("Goods.brand")}:
				</th>
				<td>
					<select name="brandId">
						<option value="">${message("admin.common.choose")}</option>
						[#list brands as brand]
							<option value="${brand.id}"[#if brand.id == goods.brand] selected="selected"[/#if]>
								${brand.name}
							</option>
						[/#list]
					</select>
				</td>
			</tr>
			[#if goods.type == "0" && promotions?has_content]
				<tr>
					<th>
						${message("Goods.promotions")}:
					</th>
					<td>
						[#list promotions as promotion]
							<label title="${promotion.title}">
								<input type="checkbox" name="promotionIds" value="${promotion.id}"[#if goods.promotions?seq_contains(promotion)] checked="checked"[/#if] />${promotion.name}
							</label>
						[/#list]
					</td>
				</tr>
			[/#if]
			[#if tags?has_content]
				<tr>
					<th>
						${message("Goods.tags")}:
					</th>
					<td>
						[#list tags as tag]
							<label>
								<input type="checkbox" name="tagIds" value="${tag.id}"[#if goods.tags?seq_contains(tag)] checked="checked"[/#if] />${tag.name}
							</label>
						[/#list]
					</td>
				</tr>
			[/#if]
			<tr>
				<th>
					${message("admin.common.setting")}:
				</th>
				<td>
					<label>
						<input type="checkbox" name="isMarketable" value="true"[#if goods.isMarketable] checked="checked"[/#if] />${message("Goods.isMarketable")}
						<input type="hidden" name="_isMarketable" value="false" />
					</label>
					<label>
						<input type="checkbox" name="isList" value="true"[#if goods.isList] checked="checked"[/#if] />${message("Goods.isList")}
						<input type="hidden" name="_isList" value="false" />
					</label>
					<label>
						<input type="checkbox" name="isTop" value="true"[#if goods.isTop] checked="checked"[/#if] />${message("Goods.isTop")}
						<input type="hidden" name="_isTop" value="false" />
					</label>
					<label>
						<input type="checkbox" name="isDelivery" value="true"[#if goods.isDelivery] checked="checked"[/#if] />${message("Goods.isDelivery")}
						<input type="hidden" name="_isDelivery" value="false" />
					</label>
				</td>
			</tr>
			<tr>
				<th>
					${message("Goods.memo")}:
				</th>
				<td>
					<input type="text" name="memo" class="text" value="${goods.memo}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Goods.keyword")}:
				</th>
				<td>
					<input type="text" name="keyword" class="text" value="${goods.keyword}" maxlength="200" title="${message("admin.goods.keywordTitle")}" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Goods.seoTitle")}:
				</th>
				<td>
					<input type="text" name="seoTitle" class="text" value="${goods.seoTitle}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Goods.seoKeywords")}:
				</th>
				<td>
					<input type="text" name="seoKeywords" class="text" value="${goods.seoKeywords}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Goods.seoDescription")}:
				</th>
				<td>
					<input type="text" name="seoDescription" class="text" value="${goods.seoDescription}" maxlength="200" />
				</td>
			</tr>
		</table>
		<table class="input tabContent">
			<tr>
				<td>
					<textarea id="introduction" name="introduction" class="editor" style="width: 100%;">${goods.introduction}</textarea>
				</td>
			</tr>
		</table>
		<table id="productImageTable" class="item tabContent">
			<tr>
				<td colspan="4">
					<a href="javascript:;" id="addProductImage" class="button">${message("admin.goods.addProductImage")}</a>
				</td>
			</tr>
			<tr>
				<th>
					${message("ProductImage.file")}
				</th>
				<th>
					${message("ProductImage.title")}
				</th>
				<th>
					${message("admin.common.order")}
				</th>
				<th>
					${message("admin.common.action")}
				</th>
			</tr>
			[#list goods.productImagesList as productImage]
				<tr>
					<td>
						<input type="hidden" name="productImagesList[${productImage_index}].source" value="${productImage.source}" />
						<input type="hidden" name="productImagesList[${productImage_index}].large" value="${productImage.large}" />
						<input type="hidden" name="productImagesList[${productImage_index}].medium" value="${productImage.medium}" />
						<input type="hidden" name="productImagesList[${productImage_index}].thumbnail" value="${productImage.thumbnail}" />
						<input type="file" name="productImagesList[${productImage_index}].file" class="productImageFile" />
						<a href="${productImage.large}" target="_blank">${message("admin.common.view")}</a>
					</td>
					<td>
						<input type="text" name="productImagesList[${productImage_index}].title" class="text" value="${productImage.title}" maxlength="200" />
					</td>
					<td>
						<input type="text" name="productImagesList[${productImage_index}].order" class="text productImageOrder" value="${productImage.order}" maxlength="9" style="width: 50px;" />
					</td>
					<td>
						<a href="javascript:;" class="remove">[${message("admin.common.remove")}]</a>
					</td>
				</tr>
			[/#list]
		</table>
		<table id="parameterTable" class="parameterTable input tabContent">
			<tr>
				<th>&nbsp;

				</th>
				<td>
					<a href="javascript:;" id="addParameter" class="button">${message("admin.goods.addParameter")}</a>
					<a href="javascript:;" id="resetParameter" class="button">${message("admin.goods.resetParameter")}</a>
				</td>
			</tr>
			[#list goods.parameterValuesList as parameterValue]
				<tr>
					<td colspan="2">
						<table data-parameter-index="${parameterValue_index}" data-parameter-entry-index="${parameterValue.entries?size}">
							<tr>
								<th>
									${message("Parameter.group")}:
								</th>
								<td>
									<input type="text" name="parameterValuesList[${parameterValue_index}].group" class="text parameterGroup" value="${parameterValue.group}" maxlength="200" />
								</td>
								<td>
									<a href="javascript:;" class="remove group">[${message("admin.common.remove")}]</a>
									<a href="javascript:;" class="add">[${message("admin.common.add")}]</a>
								</td>
							</tr>
							[#list parameterValue.entries as entry]
								<tr>
									<th>
										<input type="text" name="parameterValuesList[${parameterValue_index}].entries[${entry_index}].name" class="text parameterEntryName" value="${entry.name}" maxlength="200" style="width: 50px;" />
									</th>
									<td>
										<input type="text" name="parameterValuesList[${parameterValue_index}].entries[${entry_index}].value" class="text parameterEntryValue" value="${entry.value}" maxlength="200" />
									</td>
									<td>
										<a href="javascript:;" class="remove">[${message("admin.common.remove")}]</a>
									</td>
								</tr>
							[/#list]
						</table>
					</td>
				</tr>
			[/#list]
		</table>
		<table id="attributeTable" class="input tabContent">
			[#list goods.productCategoryVO.attributes as attribute]
				<tr>
					<th>${attribute.name}:</th>
					<td>
						<select name="attribute_${attribute.id}">
							<option value="">${message("admin.common.choose")}</option>
							[#list attribute.optionsList as option]
								<option value="${option}"[#if option == goods.getAttributeValue(attribute)] selected="selected"[/#if]>${option}</option>
							[/#list]
						</select>
					</td>
				</tr>
			[/#list]
		</table>
		<div class="tabContent">
			<table id="specificationTable" class="specificationTable input">
				<tr>
					<th>&nbsp;

					</th>
					<td>
						<a href="javascript:;" id="resetSpecification" class="button">${message("admin.goods.resetSpecification")}</a>
					</td>
				</tr>
				[#list goods.specificationItemsList as specificationItem]
					<tr>
						<th>
							<input type="text" name="specificationItemsList[${specificationItem_index}].name" class="text specificationItemName" value="${specificationItem.name}" data-value="${specificationItem.name}" style="width: 50px;" />
						</th>
						<td>
							[#list specificationItem.entries as entry]
								<span>
									<input type="checkbox" name="specificationItemsList[${specificationItem_index}].entries[${entry_index}].isSelected" value="true"[#if entry.isSelected] checked="checked"[/#if] />
									<input type="hidden" name="specificationItemsList[${specificationItem_index}].entries[${entry_index}].isSelected" value="false" />
									<input type="hidden" name="specificationItemsList[${specificationItem_index}].entries[${entry_index}].id" class="text specificationItemEntryId" value="${entry.id}" />
									<input type="text" name="specificationItemsList[${specificationItem_index}].entries[${entry_index}].value" class="text specificationItemEntryValue" value="${entry.value}" data-value="${entry.value}" style="width: 50px;" />
								</span>
							[/#list]
						</td>
					</tr>
				[/#list]
			</table>
			<table id="productTable" class="productTable item"></table>
		</div>
		<table class="input">
			<tr>
				<th>&nbsp;
					
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