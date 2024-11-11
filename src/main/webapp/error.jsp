<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
    String codeValue = request.getParameter("code");
    request.setAttribute("codeValue", codeValue);
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <title>${codeValue}</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/error.css">
    <script>
        let codeValue = ${codeValue};
    </script>
    <script src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/error.js"></script>
</head>
<body>
<div class="errors">
    <div class="code">
        <span id="digit">${codeValue}</span>
    </div>
    <div class="text">
        <h4>抱歉，出现错误了！</h4>
        <p><%=request.getParameter("message")%></p>
        <p>将在<span id="downTime">12秒</span>后自动返回到上一页，</p>
        <p>如果您的浏览器没有自动跳转，请<span onclick="turnUrl()">点击此链接</span>。</p>
    </div>
</div>
</body>
</html>