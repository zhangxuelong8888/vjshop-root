[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${goods.name} ${message("shop.review.title")}[#if showPowered] - Powered By VJSHOP[/#if]</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/shop/${theme}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/shop/${theme}/css/goods.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/shop/${theme}/css/review.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.rating.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/common.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $reviewForm = $("#reviewForm");
	var $score = $("input.score");
	var $tips = $("#tips");
	var $content = $("#content");
	var $captcha = $("#captcha");
	var $captchaImage = $("#captchaImage");
	var $submit = $("input:submit");
	
	// 评分
	$score.rating({
		callback: function(value, link) {
			$tips.text(message("${message("shop.review.tips")}", value));
		}
	});
	
	// 更换验证码
	$captchaImage.click(function() {
		$captchaImage.attr("src", "${base}/common/captcha.jhtml?captchaId=${captchaId}&timestamp=" + new Date().getTime());
	});
	
	// 表单验证
	$reviewForm.validate({
		rules: {
			content: {
				required: true,
				maxlength: 200
			},
			captcha: "required"
		},
		submitHandler: function(form) {
			$.ajax({
				url: $reviewForm.attr("action"),
				type: "POST",
				data: $reviewForm.serialize(),
				dataType: "json",
				cache: false,
				beforeSend: function() {
					$submit.prop("disabled", true);
				},
				success: function(message) {
					$.message(message);
					if (message.type == "success") {
						setTimeout(function() {
							$submit.prop("disabled", false);
							location.href = "../content/${goods.id}.jhtml";
						}, 3000);
					} else {
						$submit.prop("disabled", false);
						[#if setting.captchaTypes?? && setting.captchaTypes?seq_contains("review")]
							$captcha.val("");
							$captchaImage.attr("src", "${base}/common/captcha.jhtml?captchaId=${captchaId}&timestamp=" + new Date().getTime());
						[/#if]
					}
				}
			});
		}
	});

});
</script>
</head>
<body>
	[#include "/shop/${theme}/include/header.ftl" /]
	<div class="container review">
		<div class="row">
			<div class="span2">
				[#include "/shop/${theme}/include/hot_product_category.ftl" /]
				[#include "/shop/${theme}/include/hot_brand.ftl" /]
				[#include "/shop/${theme}/include/hot_goods.ftl" /]
				[#include "/shop/${theme}/include/hot_promotion.ftl" /]
			</div>
			<div class="span10">
				<div class="breadcrumb">
					<ul>
						<li>
							<a href="${base}/">${message("shop.breadcrumb.home")}</a>
						</li>
						<li>
							<a href="${goods.url}">${abbreviate(goods.name, 30)}</a>
						</li>
						<li>${message("shop.review.title")}</li>
					</ul>
				</div>
				<table class="info">
					<tr>
						<th rowspan="3">
							<img src="${goods.thumbnail!setting.defaultThumbnailProductImage}" alt="${goods.name}" />
						</th>
						<td>
							<a href="${goods.url}">${abbreviate(goods.name, 50, "...")}</a>
						</td>
					</tr>
					<tr>
						<td>
							${message("Goods.price")}: <strong>${currency(goods.price, true, true)}</strong>
						</td>
					</tr>
					<tr>
						<td>
							[#if goods.scoreCount > 0]
								<div>${message("Goods.score")}: </div>
								<div class="score${(goods.score * 2)?string("0")}"></div>
								<div>${goods.score?string("0.0")}</div>
							[#else]
								[#if setting.isShowMarketPrice]
									${message("Goods.marketPrice")}:
									<del>${currency(goods.marketPrice, true, true)}</del>
								[/#if]
							[/#if]
						</td>
					</tr>
				</table>
				<form id="reviewForm" action="${base}/review/save.jhtml" method="post">
					<input type="hidden" name="goodsId" value="${goods.id}" />
					[#if setting.captchaTypes?? && setting.captchaTypes?seq_contains("review")]
						<input type="hidden" name="captchaId" value="${captchaId}" />
					[/#if]
					<div class="add">
						<table>
							<tr>
								<th>
									${message("Review.score")}:
								</th>
								<td>
									<input type="radio" name="score" class="score" value="1" title="${message("shop.review.score1")}" />
									<input type="radio" name="score" class="score" value="2" title="${message("shop.review.score2")}" />
									<input type="radio" name="score" class="score" value="3" title="${message("shop.review.score3")}" />
									<input type="radio" name="score" class="score" value="4" title="${message("shop.review.score4")}" />
									<input type="radio" name="score" class="score" value="5" title="${message("shop.review.score5")}" checked="checked" />
									<strong id="tips" class="tips">${message("shop.review.tips", 5)}</strong>
								</td>
							</tr>
							<tr>
								<th>
									${message("Review.content")}:
								</th>
								<td>
									<textarea id="content" name="content" class="text"></textarea>
								</td>
							</tr>
							[#if setting.captchaTypes?? && setting.captchaTypes?seq_contains("review")]
								<tr>
									<th>
										${message("shop.captcha.name")}:
									</th>
									<td>
										<span class="fieldSet">
											<input type="text" id="captcha" name="captcha" class="text captcha" maxlength="4" autocomplete="off" /><img id="captchaImage" class="captchaImage" src="${base}/common/captcha.jhtml?captchaId=${captchaId}" title="${message("shop.captcha.imageTitle")}" />
										</span>
									</td>
								</tr>
							[/#if]
							<tr>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="submit" class="button" value="${message("shop.review.submit")}" />
								</td>
							</tr>
						</table>
					</div>
				</form>
			</div>
		</div>
	</div>
	[#include "/shop/${theme}/include/footer.ftl" /]
</body>
</html>
[/#escape]