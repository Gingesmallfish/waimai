var category_id = '';
$(function () {
    $('#category-datagrid').datagrid({
        url: "category_/listAll",  // 请求数据url
        rownumbers: true,  // 是否显示一个行号列
        pagination: true,  // 在DataGrid控件底部是否显示分页工具栏
        fitColumns: true,  // 真正的自动展开/收缩列的大小，以适应网格的宽度，防止水平滚动。默认false
        fit: true,  // 自适应大小,填充容器
        pageSize: 10,  // 在设置分页属性的时候初始化页面大小（每页显示记录数量）
        pageList: [5, 10, 20, 50],  // 在设置分页属性的时候初始化页面大小选择列表
        remoteSort: true,  // 定义从服务器对数据进行排序
        singleSelect: true,  // 设置只允许选择一行
        multiSort: true,  // 是否允许多列排序
        loadMsg: "数据加载中...",  // 在从远程站点加载数据的时候显示提示消息
        columns: [[
            {
                field: 'id',
                title: 'ID',
                align: 'center',
                width: 30,
                hidden: true
            },
            {
                field: 'name',
                title: '分类名称',
                width: 150,
                align: 'center'
            },
            {
                field: 'type',
                title: '类型',
                width: 100,
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == 1) {
                        return '菜品分类';
                    } else if (value == 2) {
                        return '套餐分类';
                    } else {
                        return '未知类型';
                    }
                }
            },
            {
                field: 'sort',
                title: '排序',
                width: 50,
                align: 'center',
               
            },

            {
                field: 'operation',
                title: '操作',
                width: 100,
                align: 'center',
                formatter: showCategoryOptBtn
            }
        ]],
        queryParams: {
            categoryName: "",
            categoryType: -1
        },
        onDblClickRow: function (index, row) {
            editCategory(row);
        },
        onLoadSuccess: function (data) {
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
                        name: '<div style="text-align:center;color:gray">暂无相关数据</div>'
                    }).datagrid('mergeCells', {
                        index: 0,
                        field: 'name',
                        colspan: 4
                    });
                }
            }
        }
    });
});


function queryCategory() {
    var queryParams = $('#category-datagrid').datagrid('options').queryParams;
    queryParams.categoryName = $.trim($('#categoryName').textbox('getValue'));
    queryParams.categoryType = $('#categoryType').combobox('getValue');
    $('#category-datagrid').datagrid('reload');
}

function showCategoryOptBtn(value, row, index) {
    return "<a href='#' onclick='categoryEditClick(" + index + ")' class='easyui-linkbutton' " +
        "id='editCategory' name='editCategory'>编辑</a>&nbsp;&nbsp;" +
        "<a href='#' onclick='enableOrDisable(" + index + ")' class='easyui-linkbutton' " +
        "id='enableOrDisable' name='enableOrDisable'>删除</a>";
}

function enableOrDisable(index) {
    row = $('#category-datagrid').datagrid('getData').rows[index];
    reqUrl = "category_/delete";
    $.messager.confirm('系统提示', '确认是否删除当前分类?', function (r) {
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

/**
 * 添加分类的函数
 * 此函数用于初始化分类表单并设置相关对话框的属性
 * @param {number} categoryid - 分类ID，用于标识特定的分类
 */
function addCategory(categoryid) {
    // 清空分类表单
    $('#category-form').form('clear');
    // 设置名称文本框的值为空
    $('#name').textbox('setValue', '');
    // 设置类型下拉框的值为-1
    $('#type').combobox('setValue', '-1');
    // 设置全局变量category_id为传入的分类ID
    category_id = categoryid;
    // 初始化分类对话框
    $('#category-dialog').dialog({
        closed: false,
        modal: true,
        collapsible: true,
        minimizable: true,
        title: "添加分类",
        buttons: [{
            text: '保存',
            iconCls: 'icon-save',
            handler: saveCategory
        }, {
            text: '关闭',
            iconCls: 'icon-bullet-cross',
            handler: function () {
                // 关闭分类对话框
                $('#category-dialog').dialog('close');
            }
        }]
    });
    // 获取名称文本框的焦点
    $('#name').textbox().next('span').find('input').focus();
}

function editCategory(row) {
    if (row && row['id'] != undefined) {
        category_id = row['id'];
        $('#name').textbox('setValue', row['name']);
        $('#type').combobox('setValue', row['type']);
        $('#category-dialog').dialog({
            closed: false,
            modal: true,
            collapsible: true,
            minimizable: true,
            title: "编辑分类",
            buttons: [{
                text: '保存',
                iconCls: 'icon-save',
                handler: saveCategory
            }, {
                text: '关闭',
                iconCls: 'icon-bullet-cross',
                handler: function () {
                    $('#category-dialog').dialog('close');
                }
            }]
        });
        $('#name').textbox().next('span').find('input').focus();
    }
}

function categoryEditClick(rowIndex) {
    var rows = $('#category-datagrid').datagrid('getSelections');
    if (rows.length != 1) {
        return;
    }
    var selectedRowIndex = $('#category-datagrid').datagrid('getRowIndex', $('#category-datagrid').datagrid('getSelected'));
    if (rowIndex == selectedRowIndex) {
        var row = $('#category-datagrid').datagrid('getSelected');
        if (row) {
            editCategory(row);
        }
    }
}

function saveCategory() {
    if ($.trim($('#name').textbox('getValue')) == "") {
        $.messager.show({
            title: '系统提示',
            msg: "请填写分类名称"
        });
        $('#name').textbox().next('span').find('input').focus();
        return;
    }
    if ($('#type').combobox('getValue') == "-1") {
        $.messager.show({
            title: '系统提示',
            msg: "请选择分类类型"
        });
        $('#type').combobox().next('span').find('input').focus();
        return;
    }
    $("#category-form").ajaxSubmit({
        url: "category_/save",
        type: "post",
        dataType: "json",
        beforeSubmit: function (arr, $form, options) {
            arr.push({
                'name': 'id',
                'value': category_id,
                'type': 'hidden',
                'required': false
            });
        },
        success: function (result, status, xhr, $form) {
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
                    $('#name').textbox('setValue', '');
                    $('#type').combobox('setValue', '-1');
                    $('#name').textbox().next('span').find('input').focus();
                    $('#category-datagrid').datagrid('reload');
                } else {
                    $('#category-datagrid').datagrid('reload');
                    $('#category-dialog').dialog('close');
                }
            }
        },
        error: function (xhr, status, error, $form) {
        },
        complete: function (xhr, status, $form) {
        }
    });
}