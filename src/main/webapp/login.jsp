<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="0">
<title>外卖后台管理系统-登录页</title>
<link
	href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/icon.css"
	rel="stylesheet" type="text/css" />
<script src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
<script
	src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.form.min.js"></script>
<style type="text/css">
body {
	background: #FFFAF4;
}

table {
	margin: auto;
	margin-top: 20px;
}
tr {
	height: 50px;
	text-align: left;
}

tr td {
	margin-left: 10px;
}
</style>
<script src="${pageContext.request.contextPath}/js/login.js"></script>
</head>
<body>
	<div id="panel_login" style="margin: auto;width: 600px;margin-top: 180px;">
		<!--创建登录面板-->
		<div id="login">
			<form id="loginForm">
				<table>
					<tr>
						<td>用户名:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td><input type="text" name="username" class="easyui-textbox"
							id="username" style="width:200px;height:35px;" value="admin" /></td>
					</tr>
					<tr>
						<td>密&nbsp;&nbsp;&nbsp;码:</td>
						<td><input type="password" name="password" id="password"
							class="easyui-textbox" style="width:200px;height:35px;" value="123456" /></td>
					</tr>
					<tr>
						<td>验证码:</td>
						<td><input type="text" name="verify" class="easyui-textbox"
							id="verify" style="width:92px;height:35px;" /> <span
							style="padding:0px 0px 0px 10px;vertical-align: middle;">
								<img style="cursor:pointer;" id='verify-img' />
						</span></td>
					</tr>
					<tr style="text-align: left;">
						<td colspan="2">
							<input type="checkbox" class="easyui-checkbox"
								   id="rememberMe" name="rememberMe"
								   style="width:18px;height:18px;"
							/>
							<label for="rememberMe">记住我(3天内自动登录)</label>
						</td>
					</tr>
					<tr style="text-align: center;">
						<td colspan="2"><a id="loginBtn" href="javascript:void(0)"
							class="easyui-linkbutton" data-options="iconCls:'icon-ok'">登&nbsp;&nbsp;录&nbsp;&nbsp;</a>&nbsp;
							&nbsp;&nbsp; <a id="resetBtn" href="javascript:void(0)"
							class="easyui-linkbutton" data-options="iconCls:'icon-undo'">重&nbsp;&nbsp;置&nbsp;&nbsp;</a>
						</td>
					</tr>
				</table>
				<input value="login" type="hidden" name="method">
			</form>
		</div>
		<div id="footer" style="padding:5px;text-align:center;font-size:10px;color:gray;">
			CopyRight © 2023 - 2027 CQEEC GROUP CO., LTD All Rights Reserved.
		</div>
	</div>
</body>
</html>