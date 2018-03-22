[#assign defaultProduct = goods.defaultProduct /]
[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
[@seo type = "goodsContent"]
	<title>[#if goods.seoTitle??]${goods.seoTitle}[#elseif seo.title??][@seo.title?interpret /][/#if][#if showPowered] - Powered By VJSHOP[/#if]</title>
	<meta name="author" content="VJSHOP Team" />
	<meta name="copyright" content="VJSHOP" />
	[#if goods.seoKeywords??]
		<meta name="keywords" content="${goods.seoKeywords}" />
	[#elseif seo.keywords??]
		<meta name="keywords" content="[@seo.keywords?interpret /]" />
	[/#if]
	[#if goods.seoDescription??]
		<meta name="description" content="${goods.seoDescription}" />
	[#elseif seo.description??]
		<meta name="description" content="[@seo.description?interpret /]" />
	[/#if]
[/@seo]
<link href="${base}/resources/shop/${theme}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/shop/${theme}/css/goods.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.jqzoom.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/common.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $headerCart = $("#headerCart");
	var $historyGoods = $("#historyGoods");
	var $clearHistoryGoods = $("#historyGoods a.clear");
	var $zoom = $("#zoom");
	var $thumbnailScrollable = $("#thumbnailScrollable");
	var $thumbnail = $("#thumbnailScrollable a");
	var $dialogOverlay = $("#dialogOverlay");
	var $preview = $("#preview");
	var $previewClose = $("#preview a.close");
	var $previewScrollable = $("#previewScrollable");
	var $price = $("#price");
	var $marketPrice = $("#marketPrice");
	var $rewardPoint = $("#rewardPoint");
	var $exchangePoint = $("#exchangePoint");
	var $specification = $("#specification dl");
	var $specificationTips = $("#specification div");
	var $specificationValue = $("#specification a");
	var $productNotifyForm = $("#productNotifyForm");
	var $productNotify = $("#productNotify");
	var $productNotifyEmail = $("#productNotify input");
	var $addProductNotify = $("#addProductNotify");
	var $quantity = $("#quantity");
	var $increase = $("#increase");
	var $decrease = $("#decrease");
	var $addCart = $("#addCart");
	var $exchange = $("#exchange");
	var $addFavorite = $("#addFavorite");
	var $window = $(window);
	var $bar = $("#bar ul");
	var $introductionTab = $("#introductionTab");
	var $parameterTab = $("#parameterTab");
	var $reviewTab = $("#reviewTab");
	var $consultationTab = $("#consultationTab");
	var $introduction = $("#introduction");
	var $parameter = $("#parameter");
	var $review = $("#review");
	var $addReview = $("#addReview");
	var $consultation = $("#consultation");
	var $addConsultation = $("#addConsultation");
	var barTop = $bar.offset().top;
	var barWidth = $bar.width();
	var productId = ${defaultProduct.id};
	var productData = {};
	
	[#if goods.hasSpecification()]
		[#list goods.products as product]
			productData["${product.specificationValueIds?join(",")}"] = {
				id: ${product.id},
				price: ${product.price},
				marketPrice: ${product.marketPrice},
				rewardPoint: ${product.rewardPoint},
				exchangePoint: ${product.exchangePoint},
				isOutOfStock: ${product.isOutOfStock?string("true", "false")}
			};
		[/#list]
		
		// 锁定规格值
		lockSpecificationValue();
	[/#if]
	
	// 商品图片放大镜
	$zoom.jqzoom({
		zoomWidth: 368,
		zoomHeight: 368,
		title: false,
		preloadText: null,
		preloadImages: false
	});
	
	// 商品缩略图滚动
	$thumbnailScrollable.scrollable();
	
	$thumbnail.hover(function() {
		var $this = $(this);
		if ($this.hasClass("current")) {
			return false;
		}
		
		$thumbnail.removeClass("current");
		$this.addClass("current").click();
	});
	
	var previewScrollable = $previewScrollable.scrollable({
		keyboard: true
	});
	
	// 商品图片预览
	$zoom.click(function() {
		$preview.show().find("img[data-original]").each(function() {
			var $this = $(this);
			$this.attr("src", $this.attr("data-original")).removeAttr("data-original");
		});
		previewScrollable.data("scrollable").seekTo($thumbnail.filter(".current").index(), 0);
		$dialogOverlay.show();
		return false;
	});
	
	$previewClose.click(function() {
		$preview.hide();
		$dialogOverlay.hide();
	});
	
	// 规格值选择
	$specificationValue.click(function() {
		var $this = $(this);
		if ($this.hasClass("locked")) {
			return false;
		}
		
		$this.toggleClass("selected").parent().siblings().children("a").removeClass("selected");
		lockSpecificationValue();
		return false;
	});
	
	// 锁定规格值
	function lockSpecificationValue() {
		var currentSpecificationValueIds = $specification.map(function() {
			$selected = $(this).find("a.selected");
			return $selected.size() > 0 ? $selected.attr("val") : [null];
		}).get();
		$specification.each(function(i) {
			$(this).find("a").each(function(j) {
				var $this = $(this);
				var specificationValueIds = currentSpecificationValueIds.slice(0);
				specificationValueIds[i] = $this.attr("val");
				if (isValid(specificationValueIds)) {
					$this.removeClass("locked");
				} else {
					$this.addClass("locked");
				}
			});
		});
		var product = productData[currentSpecificationValueIds.join(",")];
		if (product != null) {
			productId = product.id;
			$price.text(currency(product.price, true));
			$marketPrice.text(currency(product.marketPrice, true));
			$rewardPoint.text(product.rewardPoint);
			$exchangePoint.text(product.exchangePoint);
			if (product.isOutOfStock) {
				if ($addProductNotify.val() == "${message("shop.goods.productNotifySubmit")}") {
					$productNotify.show();
				}
				$addProductNotify.show();
				$quantity.closest("dl").hide();
				$addCart.hide();
				$exchange.hide();
			} else {
				$productNotify.hide();
				$addProductNotify.hide();
				$quantity.closest("dl").show();
				$addCart.show();
				$exchange.show();
			}
		} else {
			productId = null;
		}
	}
	
	// 判断规格值ID是否有效
	function isValid(specificationValueIds) {
		for(var key in productData) {
			var ids = key.split(",");
			if (match(specificationValueIds, ids)) {
				return true;
			}
		}
		return false;
	}
	
	// 判断数组是否配比
	function match(array1, array2) {
		if (array1.length != array2.length) {
			return false;
		}
		for(var i = 0; i < array1.length; i ++) {
			if (array1[i] != null && array2[i] != null && array1[i] != array2[i]) {
				return false;
			}
		}
		return true;
	}
	
	// 到货通知
	$addProductNotify.click(function() {
		if (productId == null) {
			$specificationTips.fadeIn(150).fadeOut(150).fadeIn(150);
			return false;
		}
		if ($addProductNotify.val() == "${message("shop.goods.addProductNotify")}") {
			$addProductNotify.val("${message("shop.goods.productNotifySubmit")}");
			$productNotify.show();
			$productNotifyEmail.focus();
			if ($.trim($productNotifyEmail.val()) == "") {
				$.ajax({
					url: "${base}/product_notify/email.jhtml",
					type: "GET",
					dataType: "json",
					cache: false,
					success: function(data) {
						$productNotifyEmail.val(data.email);
					}
				});
			}
		} else {
			$productNotifyForm.submit();
		}
		return false;
	});
	
	// 到货通知表单验证
	$productNotifyForm.validate({
		rules: {
			email: {
				required: true,
				email: true
			}
		},
		submitHandler: function(form) {
			$.ajax({
				url: "${base}/product_notify/save.jhtml",
				type: "POST",
				data: {productId: productId, email: $productNotifyEmail.val()},
				dataType: "json",
				cache: false,
				beforeSend: function() {
					$addProductNotify.prop("disabled", true);
				},
				success: function(data) {
					if (data.message.type == "success") {
						$addProductNotify.val("${message("shop.goods.addProductNotify")}");
						$productNotify.hide();
					}
					$.message(data.message);
				},
				complete: function() {
					$addProductNotify.prop("disabled", false);
				}
			});
		}
	});
	
	// 购买数量
	$quantity.keypress(function(event) {
		return (event.which >= 48 && event.which <= 57) || event.which == 8;
	});
	
	// 增加购买数量
	$increase.click(function() {
		var quantity = $quantity.val();
		if (/^\d*[1-9]\d*$/.test(quantity)) {
			$quantity.val(parseInt(quantity) + 1);
		} else {
			$quantity.val(1);
		}
	});
	
	// 减少购买数量
	$decrease.click(function() {
		var quantity = $quantity.val();
		if (/^\d*[1-9]\d*$/.test(quantity) && parseInt(quantity) > 1) {
			$quantity.val(parseInt(quantity) - 1);
		} else {
			$quantity.val(1);
		}
	});
	
	[#if goods.typeName() == "general"]
		// 加入购物车
		$addCart.click(function() {
			if (productId == null) {
				$specificationTips.fadeIn(150).fadeOut(150).fadeIn(150);
				return false;
			}
			var quantity = $quantity.val();
			if (/^\d*[1-9]\d*$/.test(quantity)) {
				$.ajax({
					url: "${base}/cart/add.jhtml",
					type: "POST",
					data: {productId: productId, quantity: quantity},
					dataType: "json",
					cache: false,
					success: function(message) {
						if (message.type == "success" && $headerCart.size() > 0 && window.XMLHttpRequest) {
							var $image = $zoom.find("img");
							var cartOffset = $headerCart.offset();
							var imageOffset = $image.offset();
							$image.clone().css({
								width: 300,
								height: 300,
								position: "absolute",
								"z-index": 20,
								top: imageOffset.top,
								left: imageOffset.left,
								opacity: 0.8,
								border: "1px solid #dddddd",
								"background-color": "#eeeeee"
							}).appendTo("body").animate({
								width: 30,
								height: 30,
								top: cartOffset.top,
								left: cartOffset.left,
								opacity: 0.2
							}, 1000, function() {
								$(this).remove();
							});
						}
						$.message(message);
					}
				});
			} else {
				$.message("warn", "${message("shop.goods.quantityPositive")}");
			}
		});
	[#elseif goods.typeName() == "exchange"]
		// 积分兑换
		$exchange.click(function() {
			if (productId == null) {
				$specificationTips.fadeIn(150).fadeOut(150).fadeIn(150);
				return false;
			}
			var quantity = $quantity.val();
			if (/^\d*[1-9]\d*$/.test(quantity)) {
				$.ajax({
					url: "${base}/order/check_exchange.jhtml",
					type: "GET",
					data: {productId: productId, quantity: quantity},
					dataType: "json",
					cache: false,
					success: function(message) {
						if (message.type == "success") {
							location.href = "${base}/order/checkout.jhtml?type=exchange&productId=" + productId + "&quantity=" + quantity;
						} else {
							$.message(message);
						}
					}
				});
			} else {
				$.message("warn", "${message("shop.goods.quantityPositive")}");
			}
		});
	[/#if]
	
	// 添加商品收藏
	$addFavorite.click(function() {
		$.ajax({
			url: "${base}/member/favorite/add.jhtml",
			type: "POST",
			data: {goodsId: ${goods.id}},
			dataType: "json",
			cache: false,
			success: function(message) {
				$.message(message);
			}
		});
		return false;
	});
	
	$bar.width(barWidth);
	
	$window.scroll(function() {
		var scrollTop = $(this).scrollTop();
		if (scrollTop > barTop) {
			$bar.addClass("fixed");
			var introductionTop = $introduction.size() > 0 ? $introduction.offset().top - 36 : null;
			var parameterTop = $parameter.size() > 0 ? $parameter.offset().top - 36 : null;
			var reviewTop = $review.size() > 0 ? $review.offset().top - 36 : null;
			var consultationTop = $consultation.size() > 0 ? $consultation.offset().top - 36 : null;
			if (consultationTop != null && scrollTop >= consultationTop) {
				$bar.find("li").removeClass("current");
				$consultationTab.addClass("current");
			} else if (reviewTop != null && scrollTop >= reviewTop) {
				$bar.find("li").removeClass("current");
				$reviewTab.addClass("current");
			} else if (parameterTop != null && scrollTop >= parameterTop) {
				$bar.find("li").removeClass("current");
				$parameterTab.addClass("current");
			} else if (introductionTop != null && scrollTop >= introductionTop) {
				$bar.find("li").removeClass("current");
				$introductionTab.addClass("current");
			}
		} else {
			$bar.removeClass("fixed").find("li").removeClass("current");
		}
	});
	
	[#if setting.isReviewEnabled && setting.reviewAuthority != "anyone"]
		// 发表商品评论
		$addReview.click(function() {
			if ($.checkLogin()) {
				return true;
			} else {
				$.redirectLogin("${base}/review/add/${goods.id}.jhtml", "${message("shop.goods.addReviewNotAllowed")}");
				return false;
			}
		});
	[/#if]
	
	[#if setting.isConsultationEnabled && setting.consultationAuthority != "anyone"]
		// 发表商品咨询
		$addConsultation.click(function() {
			if ($.checkLogin()) {
				return true;
			} else {
				$.redirectLogin("${base}/consultation/add/${goods.id}.jhtml", "${message("shop.goods.addConsultationNotAllowed")}");
				return false;
			}
		});
	[/#if]
	
	// 浏览记录
	var historyGoods = getCookie("historyGoods");
	var historyGoodsIds = historyGoods != null ? historyGoods.split(",") : [];
	for (var i = 0; i < historyGoodsIds.length; i ++) {
		if (historyGoodsIds[i] == ${goods.id}) {
			historyGoodsIds.splice(i, 1);
			break;
		}
	}
	historyGoodsIds.unshift(${goods.id});
	if (historyGoodsIds.length > 6) {
		historyGoodsIds.pop();
	}
	addCookie("historyGoods", historyGoodsIds.join(","));
	$.ajax({
		url: "${base}/goods/history.jhtml",
		type: "GET",
		data: {goodsIds: historyGoodsIds},
		dataType: "json",
		cache: true,
		success: function(data) {
			$.each(data, function (i, item) {
				var thumbnail = item.thumbnail != null ? item.thumbnail : "${setting.defaultThumbnailProductImage}";
				$historyGoods.find("dt").after(
					[@compress single_line = true]
						'<dd>
							<img src="' + escapeHtml(thumbnail) + '" \/>
							<a href="' + escapeHtml(item.url) + '" title="' + escapeHtml(item.name) + '">' + escapeHtml(abbreviate(item.name, 30)) + '<\/a>
							<strong>' + currency(item.price, true) + '<\/strong>
						<\/dd>'
					[/@compress]
				);
			});
		}
	});
	
	// 清空浏览记录
	$clearHistoryGoods.click(function() {
		$historyGoods.remove();
		removeCookie("historyGoods");
	});
	
	// 点击数
	$.ajax({
		url: "${base}/goods/hits/${goods.id}.jhtml",
		type: "GET",
		cache: true
	});

});
</script>
</head>
<body>
	<div id="dialogOverlay" class="dialogOverlay"></div>
	[#include "/shop/${theme}/include/header.ftl" /]
	[#assign productCategory = goods.productCategory /]
	<div class="container goodsContent">
		<div class="row">
			<div class="span2">
				[#include "/shop/${theme}/include/hot_product_category.ftl" /]
				[#include "/shop/${theme}/include/hot_brand.ftl" /]
				[#include "/shop/${theme}/include/hot_goods.ftl" /]
				[#include "/shop/${theme}/include/history_goods.ftl" /]
			</div>
			<div class="span10">
				<div class="breadcrumb">
					<ul>
						<li>
							<a href="${base}/">${message("shop.breadcrumb.home")}</a>
						</li>
						[@product_category_parent_list productCategoryId = productCategory.id]
							[#list productCategories as productCategory]
								<li>
									<a href="${base}${productCategory.path}">${productCategory.name}</a>
								</li>
							[/#list]
						[/@product_category_parent_list]
						<li>
							<a href="${base}${productCategory.path}">${productCategory.name}</a>
						</li>
					</ul>
				</div>
				<div class="productImage">
					[#if goods.productImagesList?has_content]
						<a href="${goods.productImagesList[0].large}" id="zoom" rel="gallery">
							<img class="medium" src="${goods.productImagesList[0].medium}" />
						</a>
					[#else]
						<a href="${setting.defaultLargeProductImage}" id="zoom" rel="gallery">
							<img class="medium" src="${setting.defaultMediumProductImage}" />
						</a>
					[/#if]
					<a href="javascript:;" class="prev">&nbsp;</a>
					<div id="thumbnailScrollable" class="scrollable">
						<div class="items">
							[#if goods.productImagesList?has_content]
								[#list goods.productImagesList as productImage]
									<a[#if productImage_index == 0] class="current"[/#if] href="javascript:;" rel="{gallery: 'gallery', smallimage: '${productImage.medium}', largeimage: '${productImage.large}'}">
										<img src="${productImage.thumbnail}" title="${productImage.title}" />
									</a>
								[/#list]
							[#else]
								<a href="javascript:;" class="current">
									<img src="${setting.defaultThumbnailProductImage}" />
								</a>
							[/#if]
						</div>
					</div>
					<a href="javascript:;" class="next">&nbsp;</a>
				</div>
				<div id="preview" class="preview">
					<a href="javascript:;" class="close">&nbsp;</a>
					<a href="javascript:;" class="prev">&nbsp;</a>
					<div id="previewScrollable" class="scrollable">
						<div class="items">
							[#if goods.productImagesList?has_content]
								[#list goods.productImagesList as productImage]
									<img src="${base}/upload/image/blank.gif" data-original="${productImage.large}" title="${productImage.title}" />
								[/#list]
							[#else]
								<img src="${base}/upload/image/blank.gif" data-original="${setting.defaultLargeProductImage}" />
							[/#if]
						</div>
					</div>
					<a href="javascript:;" class="next">&nbsp;</a>
				</div>
				<div class="info">
					<h1>
						${goods.name}
						[#if goods.caption?has_content]
							<em>${goods.caption}</em>
						[/#if]
					</h1>
					<dl>
						<dt>${message("Goods.sn")}:</dt>
						<dd>
							${goods.sn}
						</dd>
					</dl>
					[#if goods.typeName() != "general"]
						<dl>
							<dt>${message("Goods.type")}:</dt>
							<dd>
								${message("Goods.Type." + goods.typeName())}
							</dd>
						</dl>
					[/#if]
					[#if goods.scoreCount > 0]
						<dl>
							<dt>${message("Goods.score")}:</dt>
							<dd>
								<div class="score${(goods.score * 2)?string("0")}"></div>
							</dd>
						</dl>
					[/#if]
					[#if goods.typeName() == "general"]
						<dl>
							<dt>${message("Product.price")}:</dt>
							<dd>
								<strong id="price">${currency(defaultProduct.price, true)}</strong>
							</dd>
							[#if setting.isShowMarketPrice]
								<dd>
									<span>
										(<em>${message("Product.marketPrice")}:</em>
										<del id="marketPrice">${currency(defaultProduct.marketPrice, true)}</del>)
									</span>
								</dd>
							[/#if]
						</dl>
						[#if goods.validPromotions?has_content]
							<dl>
								<dt>${message("Goods.promotions")}:</dt>
								<dd>
									[#list goods.validPromotions as promotion]
										<a href="${base}${promotion.path}" target="_blank" title="${promotion.title}[#if promotion.beginDate?? || promotion.endDate??] (${promotion.beginDate} ~ ${promotion.endDate})[/#if]">${promotion.name}</a>
									[/#list]
								</dd>
							</dl>
						[/#if]
						[#if defaultProduct.rewardPoint > 0]
							<dl>
								<dt>${message("Product.rewardPoint")}:</dt>
								<dd id="rewardPoint">
									${defaultProduct.rewardPoint}
								</dd>
							</dl>
						[/#if]
					[#else]
						[#if goods.typeName() == "exchange"]
							<dl>
								<dt>${message("Product.exchangePoint")}:</dt>
								<dd>
									<strong id="exchangePoint">${defaultProduct.exchangePoint}</strong>
								</dd>
							</dl>
						[/#if]
						[#if setting.isShowMarketPrice]
							<dl>
								<dt>${message("Product.marketPrice")}:</dt>
								<dd id="marketPrice">
									${currency(defaultProduct.marketPrice, true)}
								</dd>
							</dl>
						[/#if]
					[/#if]
				</div>
				[#if goods.typeName() == "general" || goods.typeName() == "exchange"]
					<div class="action">
						[#if goods.hasSpecification()]
							[#assign defaultSpecificationValueIds = defaultProduct.specificationValueIds /]
							<div id="specification" class="specification clearfix">
								<div class="title">${message("shop.goods.specificationTips")}</div>
								[#list goods.specificationItemsList as specificationItem]
									<dl>
										<dt>
											<span title="${specificationItem.name}">${abbreviate(specificationItem.name, 8)}:</span>
										</dt>
										[#list specificationItem.entries as entry]
											[#if entry.isSelected]
												<dd>
													<a href="javascript:;"[#if defaultSpecificationValueIds[specificationItem_index] == entry.id] class="selected"[/#if] val="${entry.id}">
														${entry.value}<span title="${message("shop.goods.selected")}">&nbsp;</span>
													</a>
												</dd>
											[/#if]
										[/#list]
									</dl>
								[/#list]
							</div>
						[/#if]
						<form id="productNotifyForm" action="${base}/product_notify/save.jhtml" method="post">
							<dl id="productNotify" class="productNotify hidden">
								<dt>${message("shop.goods.productNotifyEmail")}:</dt>
								<dd>
									<input type="text" name="email" maxlength="200" />
								</dd>
							</dl>
						</form>
						<dl class="quantity[#if defaultProduct.isOutOfStock] hidden[/#if]">
							<dt>${message("shop.goods.quantity")}:</dt>
							<dd>
								<input type="text" id="quantity" name="quantity" value="1" maxlength="4" onpaste="return false;" />
								<div>
									<span id="increase" class="increase">&nbsp;</span>
									<span id="decrease" class="decrease">&nbsp;</span>
								</div>
							</dd>
							<dd>
								${goods.unit!message("shop.goods.defaultUnit")}
							</dd>
						</dl>
						<div class="buy">
							<input type="button" id="addProductNotify" class="addProductNotify[#if !defaultProduct.isOutOfStock] hidden[/#if]" value="${message("shop.goods.addProductNotify")}" />
							[#if goods.typeName() == "general"]
								<input type="button" id="addCart" class="addCart[#if defaultProduct.isOutOfStock] hidden[/#if]" value="${message("shop.goods.addCart")}" />
							[#else]
								<input type="button" id="exchange" class="exchange[#if defaultProduct.isOutOfStock] hidden[/#if]" value="${message("shop.goods.exchange")}" />
							[/#if]
							<a href="javascript:;" id="addFavorite">${message("shop.goods.addFavorite")}</a>
						</div>
					</div>
				[/#if]
				<div class="share">
					<div id="bdshare" class="bdshare_t bds_tools get-codes-bdshare">
						<a class="bds_qzone"></a>
						<a class="bds_tsina"></a>
						<a class="bds_tqq"></a>
						<a class="bds_renren"></a>
						<a class="bds_t163"></a>
						<span class="bds_more"></span>
						<a class="shareCount"></a>
					</div>
				</div>
				<div id="bar" class="bar">
					<ul>
						[#if goods.introduction?has_content]
							<li id="introductionTab">
								<a href="#introduction">${message("shop.goods.introduction")}</a>
							</li>
						[/#if]
						[#if goods.parameterValuesList?has_content]
							<li id="parameterTab">
								<a href="#parameter">${message("shop.goods.parameter")}</a>
							</li>
						[/#if]
						[#if setting.isReviewEnabled]
							<li id="reviewTab">
								<a href="#review">${message("shop.goods.review")}</a>
							</li>
						[/#if]
						[#if setting.isConsultationEnabled]
							<li id="consultationTab">
								<a href="#consultation">${message("shop.goods.consultation")}</a>
							</li>
						[/#if]
					</ul>
				</div>
				[#if goods.introduction?has_content]
					<div id="introduction" name="introduction" class="introduction">
						<div class="title">
							<strong>${message("shop.goods.introduction")}</strong>
						</div>
						[#noescape]
							<div>${goods.introduction}</div>
						[/#noescape]
					</div>
				[/#if]
				[#if goods.parameterValuesList?has_content]
					<div id="parameter" name="parameter" class="parameter">
						<div class="title">
							<strong>${message("shop.goods.parameter")}</strong>
						</div>
						<table>
							[#list goods.parameterValuesList as parameterValue]
								<tr>
									<th class="group" colspan="2">${parameterValue.group}</th>
								</tr>
								[#list parameterValue.entries as entry]
									<tr>
										<th>${entry.name}</th>
										<td>${entry.value}</td>
									</tr>
								[/#list]
							[/#list]
						</table>
					</div>
				[/#if]
				[#if setting.isReviewEnabled]
					<div id="review" name="review" class="review">
						<div class="title">${message("shop.goods.review")}</div>
						<div class="content clearfix">
							[#if goods.scoreCount > 0]
								<div class="score">
									<strong>${goods.score?string("0.0")}</strong>
									<div>
										<div class="score${(goods.score * 2)?string("0")}"></div>
										<div>${message("Goods.scoreCount")}: ${goods.scoreCount}</div>
									</div>
								</div>
								<div class="graph">
									<span style="width: ${(goods.score * 20)?string("0.0")}%">
										<em>${goods.score?string("0.0")}</em>
									</span>
									<div>&nbsp;</div>
									<ul>
										<li>${message("shop.goods.graph1")}</li>
										<li>${message("shop.goods.graph2")}</li>
										<li>${message("shop.goods.graph3")}</li>
										<li>${message("shop.goods.graph4")}</li>
										<li>${message("shop.goods.graph5")}</li>
									</ul>
								</div>
								<div class="action">
									<a href="${base}/review/add/${goods.id}.jhtml" id="addReview">${message("shop.goods.addReview")}</a>
								</div>
								[@review_list goodsId = goods.id count = 5]
									[#if reviews?has_content]
										<table>
											[#list reviews as review]
												<tr>
													<th>
														${review.content}
														<div class="score${(review.score * 2)?string("0")}"></div>
													</th>
													<td>
														[#if review.memberInfo??]
															${review.memberInfo.username}
														[#else]
															${message("shop.goods.anonymous")}
														[/#if]
														<span title="${review.createDate?string("yyyy-MM-dd HH:mm:ss")}">${review.createDate?string("yyyy-MM-dd")}</span>
													</td>
												</tr>
											[/#list]
										</table>
										<p>
											<a href="${base}/review/content/${goods.id}.jhtml">[${message("shop.goods.viewReview")}]</a>
										</p>
									[/#if]
								[/@review_list]
							[#else]
								<p>
									${message("shop.goods.noReview")} <a href="${base}/review/add/${goods.id}.jhtml" id="addReview">[${message("shop.goods.addReview")}]</a>
								</p>
							[/#if]
						</div>
					</div>
				[/#if]
				[#if setting.isConsultationEnabled]
					<div id="consultation" name="consultation" class="consultation">
						<div class="title">${message("shop.goods.consultation")}</div>
						<div class="content">
							[@consultation_list goodsId = goods.id count = 5]
								[#if consultations?has_content]
									<ul>
										[#list consultations as consultation]
											<li>
												${consultation.content}
												<span>
													[#if consultation.memberInfo??]
														${consultation.memberInfo.username}
													[#else]
														${message("shop.goods.anonymous")}
													[/#if]
													<span title="${consultation.createDate?string("yyyy-MM-dd HH:mm:ss")}">${consultation.createDate?string("yyyy-MM-dd")}</span>
												</span>
												[#if consultation.replyConsultations?has_content]
													<div class="arrow"></div>
													<ul>
														[#list consultation.replyConsultations as replyConsultation]
															<li>
																${replyConsultation.content}
																<span title="${replyConsultation.createDate?string("yyyy-MM-dd HH:mm:ss")}">${replyConsultation.createDate?string("yyyy-MM-dd")}</span>
															</li>
														[/#list]
													</ul>
												[/#if]
											</li>
										[/#list]
									</ul>
									<p>
										<a href="${base}/consultation/add/${goods.id}.jhtml" id="addConsultation">[${message("shop.goods.addConsultation")}]</a>
										<a href="${base}/consultation/content/${goods.id}.jhtml">[${message("shop.goods.viewConsultation")}]</a>
									</p>
								[#else]
									<p>
										${message("shop.goods.noConsultation")} <a href="${base}/consultation/add/${goods.id}.jhtml" id="addConsultation">[${message("shop.goods.addConsultation")}]</a>
									</p>
								[/#if]
							[/@consultation_list]
						</div>
					</div>
				[/#if]
			</div>
		</div>
	</div>
	[#include "/shop/${theme}/include/footer.ftl" /]
	<script type="text/javascript" id="bdshare_js" data="type=tools&amp;uid=0"></script>
	<script type="text/javascript" id="bdshell_js"></script>
	<script type="text/javascript">
		document.getElementById("bdshell_js").src = "http://bdimg.share.baidu.com/static/js/shell_v2.js?cdnversion=" + Math.ceil(new Date() / 3600000)
	</script>
</body>
</html>
[/#escape]