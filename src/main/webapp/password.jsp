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
                    <td>原始密码:</td>
                    <td>
                        <input class="easyui-textbox" type="password" name="oldpassword" style="width:240px;"></input>
                    </td>
                </tr>
                <tr>
                    <td>新密码:</td>
                    <td>
                        <input class="easyui-textbox" type="password" name="newpassword" style="width:240px;"></input>
                    </td>
                </tr>
                <tr>
                    <td>确认密码:</td>
                    <td>
                        <input class="easyui-textbox" type="password" name="repassword" style="width:240px;"></input>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align:center;">
                        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" id="editpwd">修改</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script>
    $(function () {
        $('#editpwd').bind('click', editpwd);
    });

    function editpwd() {
        if ($.trim($('input[textboxname=oldpassword]').textbox('getValue')) == "") {
            $.messager.show({
                title: '系统提示',
                msg: "请先填写原始密码"
            });
            $('input[textboxname=oldpassword]').textbox().next('span').find('input').focus();
            return;
        }
        if ($.trim($('input[textboxname=newpassword]').textbox('getValue')) == "") {
            $.messager.show({
                title: '系统提示',
                msg: "请先填写新密码"
            });
            $('input[textboxname=newpassword]').textbox().next('span').find('input').focus();
            return;
        }
        if ($.trim($('input[textboxname=repassword]').textbox('getValue')) == "") {
            $.messager.show({
                title: '系统提示',
                msg: "请先填写确认密码"
            });
            $('input[textboxname=repassword]').textbox().next('span').find('input').focus();
            return;
        }
        if ($.trim($('input[textboxname=repassword]').textbox('getValue')) != $.trim($('input[textboxname=newpassword]').textbox('getValue'))) {
            $.messager.show({
                title: '系统提示',
                msg: "新密码与确认密码不一致"
            });
            $('input[textboxname=repassword]').textbox().next('span').find('input').focus();
            return;
        }
        $("#editpassword").ajaxSubmit({
            url: editPassword,
            type: "post",
            dataType: "json",
            beforeSubmit: function (arr, $form, options) {
            },
            //提交成功后的回调函数
            success: function (result, status, xhr, $form) {
                $.messager.show({
                    title: '系统提示',
                    msg: result.message,
                    timeout: 500,
                    showType: 'slide'
                });
                if (result.status) {
                    removeTab();
                } else {
                    $('input[textboxname=oldpassword]').textbox().next('span').find('input').focus();
                }
            },
            error: function (xhr, status, error, $form) {
            },
            complete: function (xhr, status, $form) {
            }
        });
    }
</script>