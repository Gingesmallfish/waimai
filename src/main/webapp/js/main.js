var treeData;
var flag = true;
$(function() {
	InitTree();
});

function InitTree() {
	$.ajax({
		url: "json/menu.json",
		type: 'post',
		datatype: 'json',
		async: false,
		success: function(result) {
			if (!(result instanceof Object)) {
				result = JSON.parse(result);
			}
			if (result.status) {
				treeData = InitTreeData(result.data);
			} else {
				$.messager.show({
					title: '系统提示',
					msg: result.message,
					timeout: 500,
					showType: 'slide'
				});
				flag = false;
			}
		}
	});
	if (!flag) {
		return;
	}
	//初始化树形菜单
	$("#tree").tree({
		data: treeData,
		lines: true,
		onClick: function(node) {
			if (node.attributes.url != null && node.attributes.url != '') {
				addTab(node.text, node.attributes.url, node.iconCls);
			}
		},
		onLoadSuccess: function(node, data) {
			if (data) {
				$(this).tree('expand', $(this).tree('getRoot').target);
				$(".tree-icon,.tree-file").removeClass("tree-icon tree-file");
				$(".tree-icon,.tree-folder").removeClass("tree-icon tree-folder tree-folder-open tree-folder-closed");
			}
		}
	});
}

function InitTreeData(data) {
	if (!data)
		return [];
	var treeData = []; /*   最终返回结果  */
	var treeArray = {}; /*  记录一级节点  */
	var root = 0; /*    最顶层节点的父id   */
	var idKey = "id"; /*    主键的键名   */
	var pidKey = "pid"; /*  父ID的键名    */
	getChildren(root);

	function getChildren($root) {
		var $children = [];
		for (var i in data) {
			if ($root == data[i][pidKey]) {
				data[i]["children"] = getChildren(data[i][idKey]);
				$children.push(data[i]);
			}
			if (root == data[i][pidKey] && !treeArray[data[i][idKey]]) {
				treeArray[data[i][idKey]] = data[i];
				treeData.push(data[i]);
			}
		}
		return $children;
	}

	return treeData;
}

function editpassword() {
	addTab('修改密码', "password.jsp", 'icon-edit');
}

function addTab(title, href, iconCls) {
	var tabPanel = $('#my-tabs');
	if (!tabPanel.tabs('exists', title)) {
		tabPanel.tabs('add', {
			title: title,
			href: href,
			iconCls: iconCls,
			fit: true,
			cls: 'pd3',
			closable: true
		});
	} else {
		tabPanel.tabs('select', title);
	}
}

/*   移除菜单选项   */
function removeTab() {
	var tabPanel = $('#my-tabs');
	var tab = tabPanel.tabs('getSelected');
	if (tab) {
		var index = tabPanel.tabs('getTabIndex', tab);
		tabPanel.tabs('close', index);
	}
}

/*  移除所有菜单项 */
function closeAll() {
	$.messager.confirm('系统提示', '确认是否关闭所有选项卡清除缓存?', function(r) {
		if (r) {
			$("#my-tabs li").each(function(index, obj) {
				/*  获取所有可关闭的选项卡 */
				var tab = $(".tabs-closable", this).text();
				$(".easyui-tabs").tabs('close', tab);
			});
			$("#close").remove(); /*    同时把此按钮关闭    */
		}
	});
}

function logout() {
	$.messager.confirm('系统提示', '确认是否注销当前用户?', function(r) {
		if (r) {
			$.ajax({
				url: "user_/logout",
				type: 'post',
				datatype: 'json',
				async: false,
				success: function(result) {
					if (!(result instanceof Object)) {
						result = JSON.parse(result);
					}
					$.messager.alert('系统提示', result.message, 'info', function(r) {
						if (result.code == 0) {
							window.location.href = "../login.jsp";
						}
					});
				}
			})
		}
	});
}

/*  将时间戳转换成日期   */
function toDateString(d, format) {
	var date = new Date(d || new Date()),
		ymd = [
			this.digit(date.getFullYear(), 4), this.digit(date.getMonth() + 1), this.digit(date.getDate()),
			this.digit(date.getHours()), this.digit(date.getMinutes()), this.digit(date.getSeconds())
		];
	format = format || 'yyyy年MM月dd日 HH:mm:ss';
	return format.replace(/yyyy/g, ymd[0]).replace(/MM/g, ymd[1]).replace(/dd/g, ymd[2]).replace(/HH/g, ymd[3]).replace(/mm/g, ymd[4]).replace(/ss/g, ymd[5]);
}

/*  数字前置加零  */
function digit(num, length, end) {
	var str = '';
	num = String(num);
	length = length || 2;
	for (var i = num.length; i < length; i++) {
		str += '0';
	}
	return num < Math.pow(10, length) ? str + (num | 0) : num;
}

/*  弹出加载层   */
function load(msgtext) {
	$("<div class=\"datagrid-mask\" id='bodymask' style='z-index:100001'></div>").css({
		display: "block",
		width: "100%",
		height: $("body").height()
	}).appendTo("body");
	$("<div class=\"datagrid-mask-msg\" id='bodymask-msg'  style='z-index:100001' ></div>").html(msgtext).appendTo("body").css({
		display: "block",
		position: "fixed",
		height: "auto",
		left: ($(document.body).outerWidth(true) - 190) / 2,
		top: ($(window).height() - 45) / 2
	});
}

/*  取消加载层   */
function disLoad() {
	$(".datagrid-mask").remove();
	$(".datagrid-mask-msg").remove();
}

function showTime() {
	var dt = new Date();
	var show_day = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");
	var year = dt.getFullYear();
	var month = dt.getMonth() + 1;
	var date = dt.getDate();
	var day = dt.getDay();
	var hours = dt.getHours();
	hours = hours < 10 ? "0" + hours : hours;
	var minutes = dt.getMinutes();
	minutes = minutes < 10 ? "0" + minutes : minutes;
	var seconds = dt.getSeconds();
	seconds = seconds < 10 ? "0" + seconds : seconds;
	var dtime = year + "-" + month + "-" + date + "  " + show_day[day] + "  " + hours + ":" + minutes + ":" + seconds;
	$("#nowTime").html(dtime);
	setTimeout("showTime()", 1000);
}

function _clearFormat(s) {
	s = s.replace(/(\n)/g, '');
	s = s.replace(/(\t)/g, '');
	s = s.replace(/(\r)/g, '');
	s = s.replace(/<\/?[^>]*>/g, '');
	s = s.replace(/\s*/g, '');
	s = s.replace(/ /ig, '');
	s = s.replace(/[ ]|[&nbsp;]/g, '');
	return s;
}
function isEmpty(s) {
	return "" === _clearFormat(s);
}
