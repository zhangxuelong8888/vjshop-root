[#escape x as x?html]
<div class="articleSearch">
	<div class="title">${message("shop.article.search")}</div>
	<div class="content">
		<div>
			<form id="articleSearchForm" action="${base}/article/search.jhtml" method="get">
				<input type="text" name="keyword" value="${articleKeyword}" maxlength="30" />
				<button type="submit">${message("shop.article.searchSubmit")}</button>
			</form>
		</div>
	</div>
</div>
[/#escape]