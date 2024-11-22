<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta content="text/html;charset=utf-8" http-equiv="Content-Type"/>
    <title>外卖后台管理系统</title>
    <link href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
    <link href="${pageContext.request.contextPath}/css/my.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/css/icon.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/kindeditor/themes/default/default.css"/>
    <script charset="utf-8" src="${pageContext.request.contextPath}/kindeditor/kindeditor-all.js"></script>
    <script charset="utf-8" src="${pageContext.request.contextPath}/kindeditor/lang/zh-CN.js"></script>
    <script src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
    <script src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script src="${pageContext.request.contextPath}/js/jquery.form.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/echarts.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body class="easyui-layout" onload="showTime()">
<!-- begin of header -->
<div class="my-header"
     data-options="region:'north',border:false,split:true">
    <div style="position:absolute;top:8px;">
        <img src="${pageContext.request.contextPath}/images/favicon.ico" style="max-width:32px;max-height:32px;" alt="ico"/>
    </div>
    <div class="my-header-left">
        <h1>外卖后台管理系统</h1>
    </div>
    <div class="my-header-right">
        <p>
            欢迎您:${user.name}
            <a href="javascript:void(0)" onclick="editpassword();">修改密码</a> |
            <a href="javascript:void(0)" onclick="closeAll()">清除缓存 </a> |
            <a href="javascript:void(0)" onclick="logout()">安全退出 </a>
        </p>
        <p>
            <a id="nowTime"> </a>
        </p>
    </div>
</div>
<!-- end of header -->
<!-- begin of sidebar -->
<div class="my-sidebar"
     data-options="region:'west',split:true,border:true,title:'导航菜单'">
    <div class="easyui-accordion" data-options="border:false,fit:true">
        <ul id="tree"></ul>
    </div>
</div>
<!-- end of sidebar -->
<!-- begin of main -->
<div class="my-main" data-options="region:'center'">
    <div class="easyui-tabs" data-options="border:false,fit:true" id="my-tabs">
        <div data-options="href:'${pageContext.request.contextPath}/info.jsp',closable:false,iconCls:'icon-tip',cls:'pd3'" title="欢迎使用"></div>
    </div>
</div>
<!-- end of main -->
<!-- begin of footer -->
<div class="my-footer"
     data-options="region:'south',border:true,split:false">
    CopyRight © 2023 - 2027 CQEEC GROUP CO., LTD All Rights Reserved.<br/>
</div>
<!-- end of footer -->
</body>
</html>