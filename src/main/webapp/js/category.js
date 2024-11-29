// 声明一个全局变量 category_id 用于存储当前选中的分类 ID
let category_id = '';
let sortDirection = 'asc'; // 初始排序方向为升序
// 当文档加载完成后执行以下代码
$(function () {
    // 初始化分类数据表格
    $('#category-datagrid').datagrid({
        // 数据请求 URL
        url: "category_/listAll",
        // 显示行号列
        rownumbers: true,
        // 显示分页工具栏
        pagination: true,
        // 自动调整列宽以适应表格宽度
        fitColumns: true,
        // 自适应大小，填充容器
        fit: true,
        // 每页显示的记录数
        pageSize: 10,
        // 分页大小选择列表
        pageList: [5, 10, 20, 50],
        // 从服务器对数据进行排序
        remoteSort: true,
        // 只允许选择一行
        singleSelect: true,
        // 允许多列排序
        multiSort: true,
        // 加载数据时的提示消息
        loadMsg: "数据加载中...",
        // 定义表格列
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
                // 格式化类型字段的值
                formatter: categoryOpt
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
                // 格式化操作列，显示操作按钮
                formatter: showCategoryOptBtn
            }
        ]],
        // 查询参数
        queryParams: {
            categoryName: "",
            categoryType: -1,
        },
        // 双击行时触发的事件
        onDblClickRow: function (index, row) {
            editCategory(row);
        },
        // 数据加载成功后触发的事件
        onLoadSuccess: function (data) {
            if (!(data instanceof Object)) {
                data = JSON.parse(data);
            }
            if (data.code == 0) {
                // 初始化编辑按钮样式
                initOperationButtons();

                // 如果没有数据，显示提示信息
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
        },
        // 数据加载失败触发事件
        success: function (response) {
            $('#category-datagrid').datagrid('loadData', response.data);
        },
        error: function (error) {
            console.error('Error fetching data:', error);
        }
    });
    // 绑定排序按钮的点击事件
    $('#sort').click(() => {
        sortCategory();
    });
});

/**
 * 根据类别类型生成类别名称
 * @param value 初始值
 * @param row 列
 * @param index 行数的索引
 * @returns {string} 未知类型
 */
function categoryOpt(value, row, index) {
    // 根据value 判断返回对应的类别名称
    if (value == 1) {
        return '菜品分类';
    } else if (value == 2) {
        return '套餐分类';
    } else {
        // 如果value值既不是1也不是2，则返回未知类型
        return '未知类型';
    }
}


/**
 * 对类别数据进行排序
 *
 * 此函数通过改变数据的排序来对数据进行排序首先获取数据网格中的所有数据
 * 然后根据当前排序方向（升序或者降序）对数据进行排序完成后，更新数据网格可以反映新的排序结果
 * 并重新初始化操作按钮意以适应新的数据排序
 *
 * @returns {void} 无返回值，但会更新数据网格和操作按钮
 */
function sortCategory() {
    // 获取数据网格中的数据
    let data = $('#category-datagrid').datagrid('getData').rows;

    // 切换排序方向
    sortDirection = sortDirection === 'asc' ? 'desc' : 'asc';

    // 根据排序方向对数据进行排序
    if (sortDirection === 'asc') {
        data.sort((a, b) => a.name.localeCompare(b.name));
    } else {
        data.sort((a, b) => b.name.localeCompare(a.name));
    }

    // 更新数据网格的数据
    $('#category-datagrid').datagrid('loadData', data);

    initOperationButtons()
}


// 操作样式丢失的问题
function initOperationButtons() {
    // 初始化编辑按钮样式
    $("a[name='editCategory']").linkbutton({
        plain: true,
        iconCls: 'icon-edit'
    });
    // 初始化启用/禁用按钮样式
    $("a[name='enableOrDisable']").linkbutton({
        plain: true,
        iconCls: 'icon-cancel'
    });
}


/**
 * 查询类别信息
 * 
 * 本函数从用户界面收集查询参数，包括类别名称和类别类型，并使用这些参数重新加载数据网格，以显示过滤后的数据
 */
function queryCategory() {
    // 获取数据网格的查询参数
    let queryParams = $('#category-datagrid').datagrid('options').queryParams;
    // 更新查询参数中的类别名称，使用文本框中的值，并去除前后空格
    queryParams.categoryName = $.trim($('#categoryName').textbox('getValue'));
    // 更新查询参数中的类别类型，使用下拉框中的值
    queryParams.categoryType = $('#categoryType').combobox('getValue');
    // 重新加载数据网格，应用新的查询参数
    $('#category-datagrid').datagrid('reload');
}


/**
 * 生成类别管理页面的操作按钮
 * 该函数用于为每个类别项生成编辑和删除的按钮
 * 
 * @param {any} value - 不使用，占位参数，可能代表当前单元格的值
 * @param {object} row - 当前行的数据对象
 * @param {number} index - 当前行的索引
 * @returns {string} 返回包含编辑和删除按钮的HTML字符串
 */
function showCategoryOptBtn(value, row, index) {
    // 返回编辑和删除按钮的HTML字符串
    // 使用onclick事件调用相应的函数，并传入当前行的索引作为参数
    return "<a href='#' onclick='categoryEditClick(" + index + ")' class='easyui-linkbutton' " +
        "id='editCategory' name='editCategory'>编辑</a>&nbsp;&nbsp;" +
        "<a href='#' onclick='enableOrDisable(" + index + ")' class='easyui-linkbutton' " +
        "id='enableOrDisable' name='enableOrDisable'>删除</a>";
}

/**
 * 根据给定的索引启用或禁用分类
 * 此函数旨在删除用户确认后的分类记录
 * @param {number} index - 分类在数据网格中的索引
 */
function enableOrDisable(index) {
    // 获取数据网格中指定索引的行数据
    row = $('#category-datagrid').datagrid('getData').rows[index];
    // 定义删除请求的URL
    reqUrl = "category_/delete";
    
    // 显示确认对话框以确保用户想要删除分类
    $.messager.confirm('系统提示', '确认是否删除当前分类?', function (r) {
        // 如果用户确认删除，则发送异步请求
        if (r) {
            $.ajax({
                url: reqUrl,
                type: "post",
                dataType: "json",
                // 将要删除的分类的ID作为数据发送
                data: 'id=' + row["id"],
                success: function (result) {
                    // 解析结果，如果结果不是对象，则尝试将其解析为JSON
                    if (!(result instanceof Object)) {
                        result = JSON.parse(result);
                    }
                    // 显示系统提示消息
                    $.messager.show({
                        title: '系统提示',
                        msg: result.message
                    });
                    // 重新加载数据网格以反映数据变化
                    $('#category-datagrid').datagrid('reload');
                },
                // 错误处理函数留空，可根据需要进行扩展
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

/**
 * 编辑分类对话框的初始化函数
 * 该函数用于填充分类信息，并显示编辑分类的对话框
 * @param {Object} row - 包含分类信息的对象，应包括id、name和type属性
 */
function editCategory(row) {
    // 检查row对象是否存在，并确保其包含id属性
    if (row && row['id'] != undefined) {
        // 将分类ID存储在全局变量中
        category_id = row['id'];
        // 为分类名称文本框设置值
        $('#name').textbox('setValue', row['name']);
        // 为分类类型下拉框设置值
        $('#type').combobox('setValue', row['type']);
        $('#sort').textbox('setValue', row['sort']);
        // 初始化编辑分类对话框
        $('#category-dialog').dialog({
            closed: false,
            modal: true,
            collapsible: true,
            minimizable: true,
            title: "编辑分类",
            buttons: [{
                text: '保存',
                iconCls: 'icon-save',
                // 保存分类信息的事件处理函数
                handler: saveCategory
            }, {
                text: '关闭',
                iconCls: 'icon-bullet-cross',
                // 关闭对话框的事件处理函数
                handler: function () {
                    $('#category-dialog').dialog('close');
                }
            }]
        });
        // 在分类名称文本框中设置焦点
        $('#name').textbox().next('span').find('input').focus();
    }
}


/**
 * 编辑类别按钮点击事件处理函数
 * 此函数的目的是在用户点击编辑按钮时，检查用户是否恰好选择了一行数据，然后编辑该行数据
 * @param {number} rowIndex - 行索引，表示用户尝试编辑的行的位置
 */
function categoryEditClick(rowIndex) {
    // 获取当前选中的所有行
    let rows = $('#category-datagrid').datagrid('getSelections');
    // 如果选中的行数不是恰好一行，则不执行任何操作
    if (rows.length != 1) {
        return;
    }
    // 获取当前选中行的索引
    let selectedRowIndex = $('#category-datagrid').datagrid('getRowIndex', $('#category-datagrid').datagrid('getSelected'));
    // 如果点击编辑的行索引与当前选中行的索引相等，则尝试编辑该行数据
    if (rowIndex == selectedRowIndex) {
        // 获取当前选中的行数据
        let row = $('#category-datagrid').datagrid('getSelected');
        // 如果成功获取到行数据，则调用编辑函数
        if (row) {
            editCategory(row);
        }
    }
}


/**
 * 保存分类信息
 * 此函数在用户提交分类信息时被调用，它会验证用户输入，确保分类名称和类型被正确选择，
 * 并通过Ajax提交表单数据到服务器进行保存
 */
function saveCategory() {
    // 检查分类名称是否为空
    if ($.trim($('#name').textbox('getValue')) == "") {
        $.messager.show({
            title: '系统提示',
            msg: "请填写分类名称"
        });
        $('#name').textbox().next('span').find('input').focus();
        return;
    }
    // 检查分类类型是否被选择
    if ($('#type').combobox('getValue') == "-1") {
        $.messager.show({
            title: '系统提示',
            msg: "请选择分类类型"
        });
        $('#type').combobox().next('span').find('input').focus();
        return;
    }
    // 通过Ajax提交表单
    $("#category-form").ajaxSubmit({
        url: "category_/save",
        type: "post",
        dataType: "json",
        // 在提交前添加分类ID到请求参数中
        beforeSubmit: function (arr, $form, options) {
            arr.push({
                'name': 'id',
                'value': category_id,
                'type': 'hidden',
                'required': false
            });
        },
        // 处理服务器返回的成功响应
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
            // 根据服务器返回的结果进行相应处理
            if (result.code == 0) {
                if (category_id == '') {
                    // 如果是新增分类，清空输入框并重新加载分类数据网格
                    $('#name').textbox('setValue', '');
                    $('#type').combobox('setValue', '-1');
                    $('#name').textbox().next('span').find('input').focus();
                    $('#category-datagrid').datagrid('reload');
                } else {
                    // 如果是编辑分类，关闭对话框并重新加载分类数据网格
                    $('#category-datagrid').datagrid('reload');
                }
                // 关闭弹出框
                $('#category-dialog').dialog('close');
            }

        },
        // 处理服务器返回的错误响应
        error: function (xhr, status, error, $form) {
        },
        // 请求完成后的处理
        complete: function (xhr, status, $form) {
        }
    });
}

