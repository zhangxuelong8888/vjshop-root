[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:c="http://www.w3.org/1999/html">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("shop.cart.title")}[#if showPowered] - Powered By VJSHOP[/#if]</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/shop/${theme}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/shop/${theme}/css/cart.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/common.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $quantity = $("#cartTable input[name='quantity']");
	var $increase = $("#cartTable span.increase");
	var $decrease = $("#cartTable span.decrease");
	var $delete = $("#cartTable a.delete");
	var $gift = $("#gift");
	var $promotion = $("#promotion");
	var $effectiveRewardPoint = $("#effectiveRewardPoint");
	var $effectivePrice = $("#effectivePrice");
	var $clear = $("#clear");
	var $submit = $("#submit");
	var $selectItems = $("input[name=sid]");
	var $selectAll = $("input[name=selectAll]");
	var timeouts = {};

	// 初始数量
	$quantity.each(function() {
		var $this = $(this);
		$this.data("value", $this.val());
	});
	
	// 数量
	$quantity.keypress(function(event) {
		return (event.which >= 48 && event.which <= 57) || event.which == 8;
	});
	
	// 增加数量
	$increase.click(function() {
		var $quantity = $(this).parent().siblings("input");
		var quantity = $quantity.val();
		if (/^\d*[1-9]\d*$/.test(quantity)) {
			$quantity.val(parseInt(quantity) + 1);
		} else {
			$quantity.val(1);
		}
		edit($quantity);
	});
	
	// 减少数量
	$decrease.click(function() {
		var $quantity = $(this).parent().siblings("input");
		var quantity = $quantity.val();
		if (/^\d*[1-9]\d*$/.test(quantity) && parseInt(quantity) > 1) {
			$quantity.val(parseInt(quantity) - 1);
		} else {
			$quantity.val(1);
		}
		edit($quantity);
	});
	
	// 编辑数量
	$quantity.on("input propertychange change", function(event) {
		if (event.type != "propertychange" || event.originalEvent.propertyName == "value") {
			edit($(this));
		}
	});
	
	// 编辑数量
	function edit($quantity) {
		var quantity = $quantity.val();
		if (/^\d*[1-9]\d*$/.test(quantity)) {
			var $tr = $quantity.closest("tr");
			var id = $tr.find("input[name='id']").val();
			clearTimeout(timeouts[id]);
			timeouts[id] = setTimeout(function() {
				$.ajax({
					url: "edit.jhtml",
					type: "POST",
					data: {id: id, quantity: quantity},
					dataType: "json",
					cache: false,
					beforeSend: function() {
						$submit.prop("disabled", true);
					},
					success: function(data) {
						if (data.message.type == "success") {
							$quantity.data("value", quantity);
							$tr.find("span.subtotal").text(currency(data.subtotal, true));
							if (data.giftNames != null && data.giftNames.length > 0) {
								$gift.html('<dt>${message("Cart.gifts")}:<\/dt>');
								$.each(data.giftNames, function(i, giftName) {
									$gift.append('<dd title="' + escapeHtml(giftName) + '">' + escapeHtml(abbreviate(giftName, 50)) + ' &times; 1<\/dd>');
								});
								"opacity" in document.documentElement.style ? $gift.fadeIn() : $gift.show();
							} else {
								"opacity" in document.documentElement.style ? $gift.fadeOut() : $gift.hide();
							}
							$promotion.text(data.promotionNames.join(", "));
							if (!data.isLowStock) {
								$tr.find("span.lowStock").remove();
							}
							$effectiveRewardPoint.text(data.effectiveRewardPoint);
							$effectivePrice.text(currency(data.effectivePrice, true, true));
						} else if (data.message.type == "warn") {
							$.message(data.message);
							$quantity.val($quantity.data("value"));
						} else if (data.message.type == "error") {
							$.message(data.message);
							$quantity.val($quantity.data("value"));
							setTimeout(function() {
								location.reload(true);
							}, 3000);
						}
					},
					complete: function() {
						$submit.prop("disabled", false);
					}
				});
			}, 500);
		} else {
			$quantity.val($quantity.data("value"));
		}
	}
	
	// 删除
	$delete.click(function() {
		if (confirm("${message("shop.dialog.deleteConfirm")}")) {
			var $this = $(this);
			var $tr = $this.closest("tr");
			var id = $tr.find("input[name='id']").val();
			clearTimeout(timeouts[id]);
			$.ajax({
				url: "delete.jhtml",
				type: "POST",
				data: {id: id},
				dataType: "json",
				cache: false,
				beforeSend: function() {
					$submit.prop("disabled", true);
				},
				success: function(data) {
					if (data.message.type == "success") {
						if (data.quantity > 0) {
							$tr.remove();
							if (data.giftNames != null && data.giftNames.length > 0) {
								$gift.html('<dt>${message("Cart.gifts")}:<\/dt>');
								$.each(data.giftNames, function(i, giftName) {
									$gift.append('<dd title="' + escapeHtml(giftName) + '">' + escapeHtml(abbreviate(giftName, 50)) + ' &times; 1<\/dd>');
								});
								"opacity" in document.documentElement.style ? $gift.fadeIn() : $gift.show();
							} else {
								"opacity" in document.documentElement.style ? $gift.fadeOut() : $gift.hide();
							}
							$promotion.text(data.promotionNames.join(", "));
							$effectiveRewardPoint.text(data.effectiveRewardPoint);
							$effectivePrice.text(currency(data.effectivePrice, true, true));
						} else {
							location.reload(true);
						}
					} else {
						$.message(data.message);
						setTimeout(function() {
							location.reload(true);
						}, 3000);
					}
				},
				complete: function() {
					$submit.prop("disabled", false);
				}
			});
		}
		return false;
	});
	
	// 清空
	$clear.click(function() {
		if (confirm("${message("shop.dialog.clearConfirm")}")) {
			$.each(timeouts, function(i, timeout) {
				clearTimeout(timeout);
			});
			$.ajax({
				url: "clear.jhtml",
				type: "POST",
				dataType: "json",
				cache: false,
				success: function(data) {
					location.reload(true);
				}
			});
		}
		return false;
	});
	
	// 提交
	$submit.click(function() {
        var checked = false;
        $selectItems.each(function() {
            if (this.checked) {
                checked = true;
            }
        });
        if (!checked) {
            $.message("warn", "请至少选择一件商品");
            return false;
        }
		if (!$.checkLogin()) {
			$.redirectLogin("${base}/cart/list.jhtml", "${message("shop.cart.accessDenied")}");
			return false;
		}
	});

    $selectItems.click(function(v) {
		var checkbox = v.target;
        var isSelected;
        if (checkbox.checked) {
            isSelected = true;
        } else {
            isSelected = false;
		}
        $.ajax({
            url: "selected_cartitem.jhtml",
            type: "POST",
            dataType: "json",
            cache: false,
			data: {cartItemId: checkbox.value, isSelected: isSelected},
            success: function() {
                location.reload(true);
            }
        });
        toggleAll();
    });

    //全选状态
    function toggleAll(){
        var hasAllChecked = true;
        $selectItems.each(function() {
            if (!this.checked) {
				hasAllChecked = false;
            }
        });
		$selectAll.attr("checked", hasAllChecked);
    }

    //全选按钮
    $selectAll.click(function(v) {
        var isSelected = false;
        var ids = '';
        if (v.target.checked) {
            isSelected = true;
        }
        $selectItems.each(function () {
            $(this).attr("checked" ,isSelected);
            ids += this.value + ',';
        });
        ids = ids.substring(0, ids.length - 1);
        $.ajax({
            url: "selected_cartitem_all.jhtml",
            type: "POST",
            dataType: "json",
            cache: false,
            data: {cartItemIds: ids, isSelected: isSelected},
            success: function() {
                location.reload(true);
            }
        });
	});

    toggleAll();
});
</script>
</head>
<body>
	[#include "/shop/${theme}/include/header.ftl" /]
	<div class="container cart">
		<div class="row">
			<div class="span12">
				<div class="step">
					<ul>
						<li class="current">${message("shop.cart.step1")}</li>
						<li>${message("shop.cart.step2")}</li>
						<li>${message("shop.cart.step3")}</li>
					</ul>
				</div>
				[#if cart?? && cart.cartItems?has_content]
					<table id="cartTable" class="cartTable">
						<tr>
                            <th><input type="checkbox" name="selectAll"/>${message("shop.cart.selectAll")}</th>
							<th>${message("shop.cart.image")}</th>
							<th>${message("shop.cart.product")}</th>
							<th>${message("shop.cart.price")}</th>
							<th>${message("shop.cart.quantity")}</th>
							<th>${message("shop.cart.subtotal")}</th>
							<th>${message("shop.cart.action")}</th>
						</tr>
						[#list cart.cartItems as cartItem]
							<tr>
                                <td width="40" align="center">
                                    <input type="checkbox" name="sid" value="${cartItem.id}" [#if cartItem.isSelected]checked[/#if]/>
                                </td>
								<td width="60">
									<input type="hidden" name="id" value="${cartItem.id}" />
									<img src="${cartItem.productVO.thumbnail!setting.defaultThumbnailProductImage}" alt="${cartItem.productVO.name}" />
								</td>
								<td>
									<a href="${cartItem.productVO.url}" title="${cartItem.productVO.name}" target="_blank">${abbreviate(cartItem.productVO.name, 50, "...")}</a>
									[#if cartItem.productVO.specifications?has_content]
										<span class="silver">[${cartItem.productVO.specifications?join(", ")}]</span>
									[/#if]
									[#if !cartItem.isMarketable]
										<span class="red">[${message("shop.cart.notMarketable")}]</span>
									[/#if]
									[#if cartItem.isLowStock]
										<span class="red lowStock">[${message("shop.cart.lowStock")}]</span>
									[/#if]
								</td>
								<td>
									${currency(cartItem.productVO.goodsVO.price, true)}
								</td>
								<td class="quantity" width="60">
									<input type="text" name="quantity" value="${cartItem.quantity}" maxlength="4" onpaste="return false;" />
									<div>
										<span class="increase">&nbsp;</span>
										<span class="decrease">&nbsp;</span>
									</div>
								</td>
								<td width="140">
                                    [#--<span class="subtotal">${currency(cartItem.subtotal, true)}</span>--]
									<span class="subtotal">${currency(cartItem.productVO.goodsVO.price * cartItem.quantity, true)}</span>
								</td>
								<td>
									<a href="javascript:;" class="delete">${message("shop.cart.delete")}</a>
								</td>
							</tr>
						[/#list]
					</table>
				[#else]
					<p>
						<a href="${base}/">${message("shop.cart.empty")}</a>
					</p>
				[/#if]
			</div>
		</div>
		[#if cart?? && cart.cartItems?has_content]
            <div class="row">
                <div class="span6">
					<dl id="gift" class="gift clearfix[#if !cart.giftNames?has_content] hidden[/#if]">
						[#if cart.giftNames?has_content]
							<dt>${message("Cart.gifts")}:</dt>
							[#list cart.giftNames as giftName]
								<dd title="${giftName}">${abbreviate(giftName, 50)} &times; 1</dd>
							[/#list]
						[/#if]
					</dl>
				</div>
                <div class="span6">
                    <div class="total">
                        <em id="promotion">${cart.promotionNames?join(", ")}</em>
						[@current_member]
							[#if !currentMember??]
                                <em>${message("shop.cart.promotionTips")}</em>
							[/#if]
						[/@current_member]
					${message("shop.cart.effectiveRewardPoint")}: <em id="effectiveRewardPoint">${cart.effectiveRewardPoint}</em>
					[#--${message("shop.cart.effectivePrice")}: <strong id="effectivePrice">${currency(cart.getEffectivePrice(true), true, true)}</strong>--]
					${message("shop.cart.effectivePrice")}: <strong id="effectivePrice">${currency(cart.getEffectivePrice(true), true, true)}</strong>
					</div>
				</div>
			</div>
            <div class="row">
				<div class="span12">
					<div class="bottom">
						<a href="javascript:;" id="clear" class="clear">${message("shop.cart.clear")}</a>
						<a href="${base}/order/checkout.jhtml" id="submit" class="submit">${message("shop.cart.submit")}</a>
					</div>
				</div>
			</div>
		[/#if]
	</div>
	[#include "/shop/${theme}/include/footer.ftl" /]
</body>
</html>
[/#escape]