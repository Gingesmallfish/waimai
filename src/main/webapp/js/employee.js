var employee_id = '';
$(function() {
    $('#employee-datagrid').datagrid({
        //请求数据url
        url:"employee_/list",
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
                field: 'name',
                title: '员工姓名',
                width: 30,
                align: 'center'
            },
            {
                field: 'username',
                title: '账号',
                width: 30,
                align: 'center'
            },
            {
                field: 'sex',
                title: '性别',
                width: 20,
                align: 'center',
                /**
                 * 单元格formatter(格式化器)函数
                 * @param value : 字段值
                 * @param row : 行记录数据
                 * @param index : 行索引
                 */
                formatter: function(value, row, index) {
                    if (value == "女") {
                        return '<span style="color:red;font-weight:bold;">' + value + '</span>';
                    } else {
                        return '<span style="color:blue;font-weight:bold;">' + value + '</span>';
                    }
                }
            },
            {
                field: 'id_number',
                title: '身份证号',
                width: 50,
                align: 'center'
            },
            {
                field: 'phone',
                title: '手机号',
                width: 40,
                align: 'center'
            },
            {
                field: 'status',
                title: '员工状态',
                width: 30,
                align: 'center',
                formatter: function(value, row, index) {
                    if (value == 0) {
                        return '<span style="color:red;font-weight:bold;">禁用</span>';
                    } else {
                        return "正常";
                    }
                }
            },
            {
                field: 'operation',
                title: '操作',
                width: 50,
                align: 'center',
                formatter: showEmployeeOptBtn
            }
        ]],
        //在请求远程数据的时候发送额外的参数
        queryParams: {
            employeeName: "",
            employeeStatus:-1
        },
        /**
         * 在用户双击一行的时候触发
         * @param index : 点击的行的索引值，该索引值从0开始。
         * @param row : 对应于点击行的记录。
         */
        onDblClickRow: function(index, row) {
            editEmployee(row);
        },
        //在数据加载成功的时候触发
        onLoadSuccess: function(data) {
            if (!(data instanceof Object)) {
                data = JSON.parse(data);
            }
            if (data.code == 0) {
                $("a[name='editEmployee']").linkbutton({
                    plain: true,
                    iconCls: 'icon-edit'
                });
                $("a[name='enableOrDisable']").linkbutton({
                    plain: true,
                    iconCls: 'icon-lock'
                });
                if (data.total == 0) {
                    $(this).datagrid('appendRow', {
                        name: '<div style="text:align:center;color:gray">暂无相关数据</div>'
                    }).datagrid('mergeCells', {
                        index: 0,
                        field: 'name',
                        colspan: 7
                    });
                }
            }
        }
    });
});

function queryEmployee() {
    var queryParams = $('#employee-datagrid').datagrid('options').queryParams;
    queryParams.employeeName = $.trim($('#employeeName').textbox('getValue'));
    queryParams.employeeStatus = $('#employeeStatus').combobox('getValue');
    $('#employee-datagrid').datagrid('reload');
}

function showEmployeeOptBtn(value, row, index) {
    value = row["status"] == 0 ? "启用" : "禁用";
    return "<a href='#' onclick='employeeEditClick(" + index + ")' class='easyui-linkbutton' " +
        "id='editEmployee' name='editEmployee'>编辑</a>&nbsp;&nbsp;" +
        "<a href='#' onclick='enableOrDisable(" + index + ")' class='easyui-linkbutton' " +
        "id='enableOrDisable' name='enableOrDisable'>" + value +"</a>";
}

function enableOrDisable(index){
    row = $('#employee-datagrid').datagrid('getData').rows[index];
    reqUrl = row["status"] == 0 ? "employee_/enable" : "employee_/disable";
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
                    $('#employee-datagrid').datagrid('reload');
                },
                error: function () {
                }
            });
        }
    });
}

function addEmployee(employeeid) {
    $('#emplopyee-form').form('clear');
    $('#name').textbox('setValue','');
    $('#sex').combobox('setValue','');
    $('#username').textbox('setValue', '');
    $('#id_number').textbox('setValue', '');
    $('#phone').textbox('setValue', '');
    $('#sex').combobox('setValue',"-1");
    employee_id = employeeid;
    $('#employee-dialog').dialog({
        //设置初始状态为打开
        closed: false,
        //用于控制对话框是否为模态窗口
        modal: true,
        //定义是否显示可折叠按钮
        collapsible:true,
        //定义是否显示最小化按钮
        minimizable:true,
        //对话框窗口标题文本
        title: "添加员工",
        /**
         * 对话框窗口底部按钮，可用值有：
         * 1) 一个数组，每一个按钮的属性都和 linkbutton 相同。
         * 2) 一个选择器指定按钮栏。
         * 按钮可以声明在<div>标签里面：
         */
        buttons: [{
            text: '保存',
            iconCls: 'icon-save',
            handler: saveEmployee
        }, {
            text: '关闭',
            iconCls: 'icon-bullet-cross',
            handler: function() {
                $('#employee-dialog').dialog('close');
            }
        }]
    });
    $('#name').textbox().next('span').find('input').focus();
}

function editEmployee(row) {
    if (row && row['id'] != undefined) {
        employee_id = row['id'];
        $('#name').textbox('setValue', row['name']);
        $('#sex').combobox('setValue',row['sex']);
        $('#username').textbox('setValue', row['username']);
        $('#id_number').textbox('setValue', row['id_number']);
        $('#phone').textbox('setValue', row['phone']);
        $('#employee-dialog').dialog({
            closed: false,
            modal: true,
            collapsible:true,
            minimizable:true,
            title: "编辑员工",
            buttons: [{
                text: '保存',
                iconCls: 'icon-save',
                handler: saveEmployee
            }, {
                text: '关闭',
                iconCls: 'icon-bullet-cross',
                handler: function() {
                    $('#employee-dialog').dialog('close');
                }
            }]
        });
        $('#name').textbox().next('span').find('input').focus();
    }
}


function employeeEditClick(rowIndex) {
    var rows = $('#employee-datagrid').datagrid('getSelections');
    if (rows.length != 1) {
        return;
    }
    /*	获取当前选中行的索引	*/
    var selectedRowIndex = $('#employee-datagrid').datagrid('getRowIndex', $('#employee-datagrid').datagrid('getSelected'));
    /*	判断当前按钮所在行与当前选中行是否一致	*/
    if (rowIndex == selectedRowIndex) {
        var row = $('#employee-datagrid').datagrid('getSelected');
        if (row) {
            editEmployee(row);
        }
    }
}

function validateIdNumber(idNumber) {
    var reg = /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
    return reg.test(idNumber);
}

function validatePhone(phone) {
    var reg = /^1[3-9]\d{9}$/;
    return reg.test(phone);
}

function saveEmployee() {
    if ($.trim($('#name').textbox('getValue')) == "") {
        $.messager.show({
            title: '系统提示',
            msg: "请填写员工姓名"
        });
        $('#name').textbox().next('span').find('input').focus();
        return;
    }
    if($('#sex').combobox('getValue') == "-1"){
        $.messager.show({
            title: '系统提示',
            msg: "请选择员工性别"
        });
        return;
    }
    if ($.trim($('#username').textbox('getValue')) == "") {
        $.messager.show({
            title: '系统提示',
            msg: "请填写登录名"
        });
        $('#username').textbox().next('span').find('input').focus();
        return;
    }
    if ($.trim($('#id_number').textbox('getValue')) == "") {
        $.messager.show({
            title: '系统提示',
            msg: "请填写身份证号"
        });
        $('#id_number').textbox().next('span').find('input').focus();
        return;
    }
    if(!validateIdNumber($('#id_number').textbox('getValue'))){
        $.messager.show({
            title: '系统提示',
            msg: "身份证号错误"
        });
        $('#id_number').textbox().next('span').find('input').focus();
        return;
    }
    if ($.trim($('#phone').textbox('getValue')) == "") {
        $.messager.show({
            title: '系统提示',
            msg: "请填写手机号"
        });
        $('#phone').textbox().next('span').find('input').focus();
        return;
    }
    if(!validatePhone($('#phone').textbox('getValue'))){
        $.messager.show({
            title: '系统提示',
            msg: "手机号错误"
        });
        $('#phone').textbox().next('span').find('input').focus();
        return;
    }
    $("#employee-form").ajaxSubmit({
        url: "employee_/save",
        type: "post",
        dataType: "json",
        beforeSubmit: function(arr, $form, options) {
            arr.push({
                'name': 'id',
                'value': employee_id,
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
                if (employee_id == '') {
                    $('#name').textbox('setValue', '');
                    $('#sex').combobox('setValue','-1');
                    $('#username').textbox('setValue', '');
                    $('#id_number').textbox('setValue', '');
                    $('#phone').textbox('setValue', '');
                    $('#name').textbox().next('span').find('input').focus();
                    $('#employee-datagrid').datagrid('reload');
                } else {
                    $('#employee-datagrid').datagrid('reload');
                    $('#employee-dialog').dialog('close');
                }
            }
        },
        error: function(xhr, status, error, $form) {
        },
        complete: function(xhr, status, $form) {
        }
    });
}