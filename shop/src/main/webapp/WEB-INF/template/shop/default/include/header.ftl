[#escape x as x?html]
<script type="text/javascript">
$().ready(function() {

	var $headerName = $("#headerName");
	var $headerLogin = $("#headerLogin");
	var $headerRegister = $("#headerRegister");
	var $headerLogout = $("#headerLogout");
	var $goodsSearchForm = $("#goodsSearchForm");
	var $keyword = $("#goodsSearchForm input");
	var defaultKeyword = "${message("shop.header.keyword")}";
	
	var username = getCookie("username");
	var nickname = getCookie("nickname");
	if ($.trim(nickname) != "") {
		$headerName.text(nickname).show();
		$headerLogout.show();
	} else if ($.trim(username) != "") {
		$headerName.text(username).show();
		$headerLogout.show();
	} else {
		$headerLogin.show();
		$headerRegister.show();
	}
	
	$keyword.focus(function() {
		if ($.trim($keyword.val()) == defaultKeyword) {
			$keyword.val("");
		}
	});
	
	$keyword.blur(function() {
		if ($.trim($keyword.val()) == "") {
			$keyword.val(defaultKeyword);
		}
	});
	
	$goodsSearchForm.submit(function() {
		if ($.trim($keyword.val()) == "" || $keyword.val() == defaultKeyword) {
			return false;
		}
	});

});
</script>
<div class="header">
	<div class="top">
		<div class="topNav">
			<ul class="left">
				<li>
					<span>${message("shop.header.welcome", setting.siteName)}</span>
					<span id="headerName" class="headerName">&nbsp;</span>
				</li>
				<li id="headerLogin" class="headerLogin">
					<a href="${base}/login.jhtml">${message("shop.header.login")}</a>|
				</li>
				<li id="headerRegister" class="headerRegister">
					<a href="${base}/register.jhtml">${message("shop.header.register")}</a>
				</li>
				<li id="headerLogout" class="headerLogout">
					<a href="${base}/logout.jhtml">[${message("shop.header.logout")}]</a>
				</li>
			</ul>
			<ul class="right">
                [#--<li>
                    <a href="${base}/robots.apk">APP下载</a>|
                </li>--]
				[@navigation_list position = "0"]
					[#list navigations as navigation]
						<li>
							<a href="${navigation.url}"[#if navigation.isBlankTarget] target="_blank"[/#if]>${navigation.name}</a>|
						</li>
					[/#list]
				[/@navigation_list]
				<li id="headerCart" class="headerCart">
					<a href="${base}/cart/list.jhtml">${message("shop.header.cart")}</a>
					(<em></em>)
				</li>
			</ul>
		</div>
	</div>
	<div class="container">
		<div class="row">
			<div class="span3">
				<a href="${base}/">
					<img src="${setting.logo}" alt="${setting.siteName}" />
				</a>
			</div>
			<div class="span6">
				<div class="search">
					<form id="goodsSearchForm" action="${base}/goods/search.jhtml" method="get">
						<input name="keyword" class="keyword" value="${goodsKeyword!message("shop.header.keyword")}" autocomplete="off" x-webkit-speech="x-webkit-speech" x-webkit-grammar="builtin:search" maxlength="30" />
						<button type="submit">&nbsp;</button>
					</form>
				</div>
				<div class="hotSearch">
					[#if setting.hotSearches?has_content]
						${message("shop.header.hotSearch")}:
						[#list setting.hotSearches as hotSearch]
							<a href="${base}/goods/search.jhtml?keyword=${hotSearch?url}">${hotSearch}</a>
						[/#list]
					[/#if]
				</div>
			</div>
			<div class="span3">
				<div class="phone">
					<em>${message("shop.header.phone")}</em>
					${setting.phone}
				</div>
			</div>
		</div>
		<div class="row">
			<div class="span12">
				<dl class="mainNav">
					<dt>
						<a href="${base}/product_category.jhtml">${message("shop.header.allProductCategory")}</a>
					</dt>
					[@navigation_list position = "1"]
						[#list navigations as navigation]
							<dd[#if navigation.url = url] class="current"[/#if]>
								<a href="${navigation.url}"[#if navigation.isBlankTarget] target="_blank"[/#if]>${navigation.name}</a>
							</dd>
						[/#list]
					[/@navigation_list]
				</dl>
			</div>
		</div>
	</div>
</div>
[/#escape]