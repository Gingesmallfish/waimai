<%@ page import="java.net.InetAddress" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<style>
  .info {
    margin: 15px auto;
  }
  .info tr {
    line-height: 35px;
  }
  .info tr td:nth-child(2) {
    text-align: left;
    width: 70%;
  }
</style>
<div style="width:850px;height:70%;padding:10px;margin:20px auto;">
  <div class="easyui-panel" style="height:380px;" title="<center>系统基本参数</center>">
    <table class="info">
      <tr>
        <td width="20%">操作系统</td>
        <td><%=System.getProperty("os.name")%></td>
      </tr>
      <tr>
        <td>WEB服务器</td>
        <td>Apache Tomcat</td>
      </tr>
      <tr>
        <td>数据库服务器</td>
        <td>MySQL</td>
      </tr>
      <tr>
        <td>WEB架构</td>
        <td>JSP + Servlet + JavaBean + EasyUI</td>
      </tr>
      <tr>
        <td>服务器IP</td>
        <td><%=InetAddress.getLocalHost().getHostAddress()%></td>
      </tr>
      <tr>
        <td>服务器端口</td>
        <td><%=request.getServerPort()%></td>
      </tr>
      <tr>
        <td>网站备案号</td>
        <td>渝ICP备02388888号-1</td>
      </tr>
    </table>
  </div>
</div>
