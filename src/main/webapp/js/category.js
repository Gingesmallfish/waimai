var category_id = '';
$(function() {
    $('#category-datagrid').datagrid({
        //请求数据url
        url:"category_/list",
        //是否显示一个行号列
        rownumbers: true,
        //在DataGrid控件底部是否显示分页工具栏
        pagination: true,
        //真正的自动展开/收缩列的大小，以适应网格的宽度，防止水平滚动。默认false
        fitColumns: true,
        //自适应大小,填充容器
        fit: true,
        //在设置分页属性的时候初始化页面大小（每页显示记录数量）
        pageSize: 10,
        //在设置分页属性的时候初始化页面大小选择列表
        pageList: [5, 10, 20, 50],
        //定义从服务器对数据进行排序
        remoteSort: true,
        //设置只允许选择一行
        singleSelect: true,
        //是否允许多列排序
        multiSort: true,
        //在从远程站点加载数据的时候显示提示消息
        loadMsg : "数据加载中...",
        columns: [[
            {
                //列字段名称
                field: 'id',
                //列标题文本
                title: 'ID',
                //指明如何对齐列数据。可以使用的值有：'left','right','center'
                align: 'center',
                //列宽
                width: 30,
                //如果为true，则隐藏列
                hidden: "true"
            },
            {
                field: 'categoryName',
                title: '分类名称',
                width: 30,
                align: 'center'
            },
            {
                field: 'categoryOrder',
                title: '分类排序',
                width: 40,
                align: 'center'
            },
            {
                field: 'operation',
                title: '操作',
                width: 50,
                align: 'center',
                formatter: showCategoryOptBtn
            }
        ]],
        //在请求远程数据的时候发送额外的参数
        queryParams: {
            categoryName: "",
            categoryStatus:-1
        },
        /**
         * 在用户双击一行的时候触发
         * @param index : 点击的行的索引值，该索引值从0开始。
         * @param row : 对应于点击行的记录。
         */
        onDblClickRow: function(index, row) {
            editCategory(row);
        },
        //在数据加载成功的时候触发
        onLoadSuccess: function(data) {
            if (!(data instanceof Object)) {
                data = JSON.parse(data);
            }
            if (data.code == 0) {
                $("a[name='editCategory']").linkbutton({
                    plain: true,
                    iconCls: 'icon-edit'
                });
                $("a[name='enableOrDisable']").linkbutton({
                    plain: true,
                    iconCls: 'icon-lock'
                });
                if (data.total == 0) {
                    $(this).datagrid('appendRow', {
                        categoryName: '暂无相关数据'
                    }).datagrid('mergeCells', {
                        index: 0,
                        field: 'categoryName',
                        colspan: 7
                    });
                }
            }
        }
    });
});

function queryCategory() {
    var queryParams = $('#category-datagrid').datagrid('options').queryParams;
    queryParams.categoryName = $.trim($('#categoryName').textbox('getValue'));
    queryParams.categoryStatus = $('#categoryStatus').combobox('getValue');
    $('#category-datagrid').datagrid('reload');
}

function showCategoryOptBtn(value, row, index) {
    value = row["categoryStatus"] == 0 ? "启用" : "禁用";
    return "编辑" + "" + value +"";
}

function enableOrDisable(index){
    row = $('#category-datagrid').datagrid('getData').rows[index];
    reqUrl = row["categoryStatus"] == 0 ? "category_/enable" : "category_/disable";
    $.messager.confirm('系统提示', '确认是否修改当前用户状态?', function (r) {
        if (r) {
            $.ajax({
                url: reqUrl,
                type: "post",
                dataType: "json",
                data: 'id=' + row["id"],
                success: function (result) {
                    if (!(result instanceof Object)) {
                        result = JSON.parse(result);
                    }
                    $.messager.show({
                        title: '系统提示',
                        msg: result.message
                    });
                    $('#category-datagrid').datagrid('reload');
                },
                error: function () {
                }
            });
        }
    });
}

function addCategory(categoryid) {
    $('#category-form').form('clear');
    $('#categoryName').textbox('setValue','');
    $('#categoryCode').textbox('setValue','');
    $('#categoryDesc').textbox('setValue', '');
    $('#categoryStatus').combobox('setValue',"-1");
    $('#categoryOrder').textbox('setValue', '');
    category_id = categoryid;
    $('#category-dialog').dialog({
        //设置初始状态为打开
        closed: false,
        //用于控制对话框是否为模态窗口
        modal: true,
        //定义是否显示可折叠按钮
        collapsible:true,
        //定义是否显示最小化按钮
        minimizable:true,
        //对话框窗口标题文本
        title: "添加分类",
        /**
         * 对话框窗口底部按钮，可用值有：
         * 1) 一个数组，每一个按钮的属性都和 linkbutton 相同。
         * 2) 一个选择器指定按钮栏。
         * 按钮可以声明在标签里面：
         */
        buttons: [{
            text: '保存',
            iconCls: 'icon-save',
            handler: saveCategory
        }, {
            text: '关闭',
            iconCls: 'icon-bullet-cross',
            handler: function() {
                $('#category-dialog').dialog('close');
            }
        }]
    });
    $('#categoryName').textbox().next('span').find('input').focus();
}


function editCategory(row) {
    if (row && row['id'] != undefined) {
        category_id = row['id'];
        $('#categoryName').textbox('setValue', row['categoryName']);
        $('#categoryCode').textbox('setValue',row['categoryCode']);
        $('#categoryDesc').textbox('setValue', row['categoryDesc']);
        $('#categoryStatus').combobox('setValue',row['categoryStatus']);
        $('#categoryOrder').textbox('setValue', row['categoryOrder']);
        $('#category-dialog').dialog({
            closed: false,
            modal: true,
            collapsible:true,
            minimizable:true,
            title: "编辑分类",
            buttons: [{
                text: '保存',
                iconCls: 'icon-save',
                handler: saveCategory
            }, {
                text: '关闭',
                iconCls: 'icon-bullet-cross',
                handler: function() {
                    $('#category-dialog').dialog('close');
                }
            }]
        });
        $('#categoryName').textbox().next('span').find('input').focus();
    }
}


function categoryEditClick(rowIndex) {
    var rows = $('#category-datagrid').datagrid('getSelections');
    if (rows.length != 1) {
        return;
    }
    /*	获取当前选中行的索引	*/
    var selectedRowIndex = $('#category-datagrid').datagrid('getRowIndex', $('#category-datagrid').datagrid('getSelected'));
    /*	判断当前按钮所在行与当前选中行是否一致	*/
    if (rowIndex == selectedRowIndex) {
        var row = $('#category-datagrid').datagrid('getSelected');
        if (row) {
            editCategory(row);
        }
    }
}

function validateCategoryCode(categoryCode) {
    var reg = /^[a-zA-Z0-9_-]{4,16}$/;
    return reg.test(categoryCode);
}

function validateCategoryDesc(categoryDesc) {
    var reg = /^.{1,50}$/;
    return reg.test(categoryDesc);
}

function validateCategoryOrder(categoryOrder) {
    var reg = /^\d{1,3}$/;
    return reg.test(categoryOrder);
}

function saveCategory() {
    if ($.trim($('#categoryName').textbox('getValue')) == "") {
        $.messager.show({
            title: '系统提示',
            msg: "请填写分类名称"
        });
        $('#categoryName').textbox().next('span').find('input').focus();
        return;
    }
    if ($.trim($('#categoryCode').textbox('getValue')) == "") {
        $.messager.show({
            title: '系统提示',
            msg: "请填写分类编码"
        });
        $('#categoryCode').textbox().next('span').find('input').focus();
        return;
    }
    if(!validateCategoryCode($('#categoryCode').textbox('getValue'))){
        $.messager.show({
            title: '系统提示',
            msg: "分类编码错误"
        });
        $('#categoryCode').textbox().next('span').find('input').focus();
        return;
    }
    if ($.trim($('#categoryDesc').textbox('getValue')) == "") {
        $.messager.show({
            title: '系统提示',
            msg: "请填写分类描述"
        });
        $('#categoryDesc').textbox().next('span').find('input').focus();
        return;
    }
    if(!validateCategoryDesc($('#categoryDesc').textbox('getValue'))){
        $.messager.show({
            title: '系统提示',
            msg: "分类描述错误"
        });
        $('#categoryDesc').textbox().next('span').find('input').focus();
        return;
    }
    if ($.trim($('#categoryOrder').textbox('getValue')) == "") {
        $.messager.show({
            title: '系统提示',
            msg: "请填写分类排序"
        });
        $('#categoryOrder').textbox().next('span').find('input').focus();
        return;
    }
    if(!validateCategoryOrder($('#categoryOrder').textbox('getValue'))){
        $.messager.show({
            title: '系统提示',
            msg: "分类排序错误"
        });
        $('#categoryOrder').textbox().next('span').find('input').focus();
        return;
    }
    $("#category-form").ajaxSubmit({
        url: "category_/save",
        type: "post",
        dataType: "json",
        beforeSubmit: function(arr, $form, options) {
            arr.push({
                'name': 'id',
                'value': category_id,
                'type': 'hidden',
                'required': false
            });
        },
        success: function(result, status, xhr, $form) {
            if (!(result instanceof Object)) {
                result = JSON.parse(result);
            }
            $.messager.show({
                title: '系统提示',
                msg: result.message,
                timeout: 500,
                showType: 'slide'
            });
            if (result.code == 0) {
                if (category_id == '') {
                    $('#categoryName').textbox('setValue', '');
                    $('#categoryCode').textbox('setValue','-1');
                    $('#categoryDesc').textbox('setValue', '');
                    $('#categoryStatus').combobox('setValue', '');
                    $('#categoryOrder').textbox('setValue', '');
                    $('#categoryName').textbox().next('span').find('input').focus();
                    $('#category-datagrid').datagrid('reload');
                } else {
                    $('#category-datagrid').datagrid('reload');
                    $('#category-dialog').dialog('close');
                }
            }
        },
        error: function(xhr, status, error, $form) {
        },
        complete: function(xhr, status, $form) {
        }
    });
}
