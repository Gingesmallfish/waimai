var verify = "user/verify";
var bclose = false;
$(function() {
	$('#login').panel({
		title : '外卖后台管理系统',
		width : 600,
		height : 340,
		iconCls : 'icon-bricks',
		collapsible : true,
		closable : true,
		collapsed : false,
		footer : '#footer',
		tools : [ {
			iconCls : 'icon-reload',
			handler : function() {
				window.location.href = "login.jsp";
			}
		} ],
		onOpen : function() {
			$('input[textboxname=tel]').textbox().next('span').find('input').focus();
		},
		onExpand : function() {
			$('input[textboxname=tel]').textbox().next('span').find('input').focus();
		},
		onBeforeClose : function() {
			if (bclose) {
				bclose = false;
				return true;
			} else {
				$.messager.confirm('系统提示', '确认是否关闭登录页面?', function(r) {
					if (r) {
						bclose = true;
						$('#login').panel('close');
					}
				});
				return false;
			}
		},
		onClose : closeWindow
	});
	$('#verify-img').attr('src', verify + '?time=' + Math.random());
	$('#verify-img').click(function() {
		$(this).attr('src', verify + '?time=' + Math.random());
	});
	$("#loginForm").get(0).reset();
	$("#loginBtn").click(function() {
		doLogin();
	})
	$("#resetBtn").click(function() {
		$("#loginForm").get(0).reset();
		$('#verify-img').attr('src', verify + '?time=' + Math.random());
	})
	$('#username').textbox({
		inputEvents : $.extend({}, $.fn.textbox.defaults.inputEvents, {
			keyup : function(event) {
				if ($.trim($('input[textboxname=username]').textbox('getValue')) != "") {
					if (event.keyCode == 13) {
						$('input[textboxname=password]').textbox().next('span').find('input').focus();
					}
				}
			}
		})
	});
	$('#password').textbox({
		inputEvents : $.extend({}, $.fn.textbox.defaults.inputEvents, {
			keyup : function(event) {
				if ($.trim($('input[textboxname=password]').textbox('getValue')) != "") {
					if (event.keyCode == 13) {
						$('input[textboxname=verify]').textbox().next('span').find('input').focus();
					}
				}
			}
		})
	});
	$('#verify').textbox({
		inputEvents : $.extend({}, $.fn.textbox.defaults.inputEvents, {
			keyup : function(event) {
				if ($.trim($('input[textboxname=verify]').textbox('getValue')) != "") {
					if (event.keyCode == 13) {
						doLogin();
					}
				}
			}
		})
	});
})
function doLogin() {
	if ($.trim($('input[textboxname=username]').textbox('getValue')) == "") {
		$.messager.alert('登录提示', '用户名不能为空。', 'info', function(r) {
			$('input[textboxname=username]').textbox().next('span').find('input').focus();
		});
		return;
	}
	if ($.trim($('input[textboxname=password]').textbox('getValue')) == "") {
		$.messager.alert('登录提示', '密码不能为空。', 'info', function(r) {
			$('input[textboxname=password]').textbox().next('span').find('input').focus();
		});
		return;
	}
	if ($.trim($('input[textboxname=verify]').textbox('getValue')) == "") {
		$.messager.alert('登录提示', '验证码不能为空。', 'info', function(r) {
			$('input[textboxname=verify]').textbox().next('span').find('input').focus();
		});
		return;
	}
	$("#loginForm").ajaxSubmit({
		url : "user/login",
		type : "post",
		dataType : "json",
		beforeSubmit : function(arr, $form, options) {},
		success : function(result, status, xhr, $form) {
			if (!(result instanceof Object)) {
				result = JSON.parse(result);
			}
			$.messager.alert('登录提示', result.message, 'info', function(r) {
				if (result.code == 0) {
					window.location.href = "../main.jsp";
				} else {
					// 验证码刷新
					$('#verify-img').attr('src', verify + '?time=' + Math.random());
					// 清空我们验证码文本框
					$('input[textboxname=verify]').textbox('setValue', '');
					// 验证码文本框聚焦
					$('input[textboxname=verify]').textbox().next('span').find('input').focus();
				}
			});
		},
		error : function(xhr, status, error, $form) {},
		complete : function(xhr, status, $form) {}
	});
}
function closeWindow() {
	window.location.href = "about:blank";
	window.close();
}
