<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<style>
    tr {
        height: 60px;
        text-align: left;
    }

    tr td {
        margin-left: 10px;
    }
</style>
<div style="width:700px;height:300px;padding:0px;margin:30px auto;">
    <div class="easyui-panel" title="">
        <form id="editpassword" method="post">
            <table cellpadding="5" style="margin:30px auto;">
                <tr>
                    <td><label for="oldpassword">原始密码:</label></td>
                    <td>
                        <input id="oldpassword" class="easyui-textbox" type="password" name="oldpassword" style="width:240px;"></input>
                    </td>
                </tr>
                <tr>
                    <td><label for="newpassword">新密码:</label></td>
                    <td>
                        <input id="newpassword" class="easyui-textbox" type="password" name="newpassword" style="width:240px;"></input>
                    </td>
                </tr>
                <tr>
                    <td><label for="repassword">确认密码:</label></td>
                    <td>
                        <input id="repassword" class="easyui-textbox" type="password" name="repassword" style="width:240px;"></input>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align:center;">
                        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" id="editpwd">修改</a>
                        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-back'" onclick="removeTab()">返回</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/password.js"></script>