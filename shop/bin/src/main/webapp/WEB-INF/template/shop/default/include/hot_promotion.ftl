[#escape x as x?html]
[@promotion_list productCategoryId = productCategory.id hasEnded = false count = 4]
	[#if promotions?has_content]
		<div class="hotPromotion">
			<dl>
				<dt>${message("shop.goods.hotPromotion")}</dt>
				[#list promotions as promotion]
					<dd>
						<a href="${base}${promotion.path}" title="${promotion.title}">
							[#if promotion.image?has_content]
								<img src="${promotion.image}" alt="${promotion.title}" />
							[#else]
								${abbreviate(promotion.title, 26)}
							[/#if]
						</a>
						[#if promotion.beginDate?? && promotion.endDate??]
							${message("shop.goods.hotPromotionDate")}：${promotion.beginDate?string("MM-dd")} - ${promotion.endDate?string("MM-dd")}
						[/#if]
					</dd>
				[/#list]
			</dl>
		</div>
	[/#if]
[/@promotion_list]
[/#escape]