[#escape x as x?html]
<div class="footer">
	<div class="service clearfix">
		<dl>
			<dt class="icon1">新手指南</dt>
			<dd>
				<a href="#">购物流程</a>
			</dd>
			<dd>
				<a href="#">会员注册</a>
			</dd>
			<dd>
				<a href="#">购买宝贝</a>
			</dd>
			<dd>
				<a href="#">支付货款</a>
			</dd>
			<dd>
				<a href="#">用户协议</a>
			</dd>
		</dl>
		<dl>
			<dt class="icon2">特色服务</dt>
			<dd>
				<a href="#">夺宝岛</a>
			</dd>
			<dd>
				<a href="#">DIY装机</a>
			</dd>
			<dd>
				<a href="#">延保服务</a>
			</dd>
			<dd>
				<a href="#">节能补贴</a>
			</dd>
			<dd>
				<a href="#">用户协议</a>
			</dd>
		</dl>
		<dl>
			<dt class="icon3">支付方式</dt>
			<dd>
				<a href="#">货到付款</a>
			</dd>
			<dd>
				<a href="#">在线支付</a>
			</dd>
			<dd>
				<a href="#">分期付款</a>
			</dd>
			<dd>
				<a href="#">邮局汇款</a>
			</dd>
			<dd>
				<a href="#">公司转账</a>
			</dd>
		</dl>
		<dl>
			<dt class="icon4">配送方式</dt>
			<dd>
				<a href="#">上门自提</a>
			</dd>
			<dd>
				<a href="#">211限时达</a>
			</dd>
			<dd>
				<a href="#">国内配送</a>
			</dd>
			<dd>
				<a href="#">配送查询</a>
			</dd>
			<dd>
				<a href="#">配送收费</a>
			</dd>
		</dl>
		<div class="qrCode">
			<img src="${base}/resources/shop/${theme}/images/qr_code.gif" alt="${message("shop.footer.weixin")}" />
			${message("shop.footer.weixin")}
		</div>
	</div>
	<div class="bottom">
		<div class="bottomNav">
			<ul>
				[@navigation_list position = "2"]
					[#list navigations as navigation]
						<li>
							<a href="${navigation.url}"[#if navigation.isBlankTarget] target="_blank"[/#if]>${navigation.name}</a>
							[#if navigation_has_next]|[/#if]
						</li>
					[/#list]
				[/@navigation_list]
			</ul>
		</div>
		<div class="info">
			<p>${setting.certtext}</p>
                        <!--<p>${message("shop.footer.copyright", setting.siteName)}</p>-->
			<p>${message("Copyright © 2005-现在 Dophy购物商城 版权所有",setting.siteName)}</p>
                         <!--[@friend_link_list type=1 count = 8]
				<ul>
					[#list friendLinks as friendLink]
						<li>
							<a href="${friendLink.url}" target="_blank">
								<img src="${friendLink.logo}" alt="${friendLink.name}" />
							</a>
						</li>
					[/#list]
				</ul>
			[/@friend_link_list]-->
		</div>
	</div>
	[#include "/shop/${theme}/include/statistics.ftl" /]
</div>
[/#escape]