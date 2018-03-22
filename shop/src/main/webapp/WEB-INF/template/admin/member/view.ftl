[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.member.view")} - Powered By VJSHOP</title>
<meta name="author" content="VJSHOP Team" />
<meta name="copyright" content="VJSHOP" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.member.view")}
	</div>
	<ul id="tab" class="tab">
		<li>
			<input type="button" value="${message("admin.member.base")}" />
		</li>
		[#if memberAttributes?has_content]
			<li>
				<input type="button" value="${message("admin.member.profile")}" />
			</li>
		[/#if]
	</ul>
	<table class="input tabContent">
		<tr>
			<th>
				${message("Member.username")}:
			</th>
			<td>
				${member.username}
				[#if loginPlugin??]
					<span class="silver">[${loginPlugin.name}]</span>
				[/#if]
			</td>
		</tr>
		<tr>
			<th>
				${message("Member.email")}:
			</th>
			<td>
				${member.email}
			</td>
		</tr>
		<tr>
			<th>
				${message("Member.nickname")}:
			</th>
			<td>
				${member.nickname}
			</td>
		</tr>
		<tr>
			<th>
				${message("Member.memberRank")}:
			</th>
			<td>
				${member.memberRankInfo.name}
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.member.status")}:
			</th>
			<td>
				[#if !member.isEnabled]
					<span class="red">${message("admin.member.disabled")}</span>
				[#elseif member.isLocked]
					<span class="red"> ${message("admin.member.locked")} </span>
				[#else]
					<span class="green">${message("admin.member.normal")}</span>
				[/#if]
			</td>
		</tr>
		[#if member.isLocked]
			<tr>
				<th>
					${message("Member.lockedDate")}:
				</th>
				<td>
					${member.lockedDate?string("yyyy-MM-dd HH:mm:ss")}
				</td>
			</tr>
		[/#if]
		<tr>
			<th>
				${message("Member.point")}:
			</th>
			<td>
				${member.point}
				<a href="../point/log.jhtml?memberId=${member.id}">[${message("admin.common.view")}]</a>
			</td>
		</tr>
		<tr>
			<th>
				${message("Member.balance")}:
			</th>
			<td>
				${currency(member.balance, true)}
				<a href="../deposit/log.jhtml?memberId=${member.id}">[${message("admin.common.view")}]</a>
			</td>
		</tr>
		<tr>
			<th>
				${message("Member.amount")}:
			</th>
			<td>
				${currency(member.amount, true)}
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.common.createDate")}:
			</th>
			<td>
				${member.createDate?string("yyyy-MM-dd HH:mm:ss")}
			</td>
		</tr>
		<tr>
			<th>
				${message("Member.loginDate")}:
			</th>
			<td>
				${(member.loginDate?string("yyyy-MM-dd HH:mm:ss"))!"-"}
			</td>
		</tr>
		<tr>
			<th>
				${message("Member.registerIp")}:
			</th>
			<td>
				${member.registerIp}
			</td>
		</tr>
		<tr>
			<th>
				${message("Member.loginIp")}:
			</th>
			<td>
				${member.loginIp!"-"}
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.member.reviewCount")}:
			</th>
			<td>
				${member.reviewCount}
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.member.consultationCount")}:
			</th>
			<td>
				${member.consultationCount}
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.member.favoriteGoodsCount")}:
			</th>
			<td>
				${member.favoriteGoodsCount}
			</td>
		</tr>
	</table>
	[#if memberAttributes?has_content]
		<table class="input tabContent">
			[#list memberAttributes as memberAttribute]
				<tr>
					<th>
						${memberAttribute.name}:
					</th>
					<td>
						[#if memberAttribute.enumType == "name"]
							${member.name}
						[#elseif memberAttribute.enumType == "gender"]
							[#if member.gender??]
								${message("Member.Gender." + member.enumGender)}
							[/#if]
						[#elseif memberAttribute.enumType == "birth"]
							${member.birth}
						[#elseif memberAttribute.enumType == "area"]
							[#if member.areaInfo??]
								${member.areaInfo.fullName}
							[#else]
								${member.areaName}
							[/#if]
						[#elseif memberAttribute.enumType == "address"]
							${member.address}
						[#elseif memberAttribute.enumType == "zipCode"]
							${member.zipCode}
						[#elseif memberAttribute.enumType == "phone"]
							${member.phone}
						[#elseif memberAttribute.enumType == "mobile"]
							${member.mobile}
						[#elseif memberAttribute.enumType == "text"]
							${member.getAttributeValue(memberAttribute.enumType, memberAttribute.propertyIndex)}
						[#elseif memberAttribute.enumType == "select"]
							${member.getAttributeValue(memberAttribute.enumType, memberAttribute.propertyIndex)}
						[#elseif memberAttribute.enumType == "checkbox"]
							[#list member.getAttributeValue(memberAttribute.enumType, memberAttribute.propertyIndex) as option]
								${option}
							[/#list]
						[/#if]
					</td>
				</tr>
			[/#list]
		</table>
	[/#if]
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