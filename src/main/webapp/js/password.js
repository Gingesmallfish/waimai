$(function () {
    $('#editpwd').bind('click', editpwd);
});

function editpwd() {
    if ($.trim($('#oldpassword').textbox('getValue')) == "") {
        $.messager.show({
            title: '系统提示',
            msg: "请先填写原始密码"
        });
        $('#oldpassword').textbox().next('span').find('input').focus();
        return;
    }
    if ($.trim($('#newpassword').textbox('getValue')) == "") {
        $.messager.show({
            title: '系统提示',
            msg: "请先填写新密码"
        });
        $('#newpassword').textbox().next('span').find('input').focus();
        return;
    }
    if ($.trim($('#repassword').textbox('getValue')) == "") {
        $.messager.show({
            title: '系统提示',
            msg: "请先填写确认密码"
        });
        $('#repassword').textbox().next('span').find('input').focus();
        return;
    }
    if ($.trim($('#repassword').textbox('getValue')) != $.trim($('#newpassword').textbox('getValue'))) {
        $.messager.show({
            title: '系统提示',
            msg: "新密码与确认密码不一致"
        });
        $('#repassword').textbox().next('span').find('input').focus();
        return;
    }
    $("#editpassword").ajaxSubmit({
        url: 'user_/edit',
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
            if (result.code == 0) {
                removeTab();
            } else {
                $('#oldpassword').textbox().next('span').find('input').focus();
            }
        },
        error: function (xhr, status, error, $form) {
        },
        complete: function (xhr, status, $form) {
        }
    });
}