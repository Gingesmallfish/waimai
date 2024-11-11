var college_id = '';
$(function() {
    $('#employee-datagrid').datagrid({
        url:"",
        rownumbers: true,
        pagination: true,
        fitColumns: true,
        fit: true,
        pageSize: 10,
        pageList: [5, 10, 20, 50],
        remoteSort: true,
        singleSelect: true,
        multiSort: true,
        columns: [[
            {
                field: 'id',
                title: 'ID',
                align: 'center',
                width: 40,
                hidden: "true"
            },
            {
                field: 'collegename',
                title: '员工姓名',
                width: 40,
                align: 'center'
            },
            {
                field: 'tel',
                title: '登录用户名',
                width: 40,
                align: 'center'
            },
            {
                field: 'current_number_major',
                title: '性别',
                width: 20,
                align: 'center',
                formatter: function(value, row, index) {
                    if (value > 4) {
                        return '<span style="color:red;font-weight:bold;">' + value + '</span>';
                    } else {
                        return value;
                    }
                }
            },
            {
                field: 'aaa',
                title: '联系电话',
                width: 50,
                align: 'center'
            },
            {
                field: 'current_number_major',
                title: '员工状态',
                width: 40,
                align: 'center',
                formatter: function(value, row, index) {
                    if (value > 4) {
                        return '<span style="color:red;font-weight:bold;">' + value + '</span>';
                    } else {
                        return value;
                    }
                }
            },
            {
                field: 'operation',
                title: '操作',
                width: 60,
                align: 'center',
                formatter: showEmployeeOptBtn
            }
        ]],
        queryParams: {
            employeename: ""
        },
        onDblClickRow: function(index, row) {
            doEditEmployee(row);
        },
        onLoadSuccess: function(data) {
            if (!(data instanceof Object)) {
                data = JSON.parse(data);
            }
            if (data.status) {
                $("a[name='editEmployee']").linkbutton({
                    plain: true,
                    iconCls: 'icon-edit'
                });
                if (data.total == 0) {
                    $(this).datagrid('appendRow', {
                        collegename: '<div style="text:align:center;color:gray">暂无相关数据</div>'
                    }).datagrid('mergeCells', {
                        index: 0,
                        field: 'collegename',
                        colspan: 5
                    });
                }
            } else {
                $.messager.show({
                    title: '系统提示',
                    msg: data.message,
                    timeout: 500,
                    showType: 'slide'
                });
                $(this).datagrid('appendRow', {
                    collegename: '<div style="text:align:center;color:red">因权限原因无法操作</div>'
                }).datagrid('mergeCells', {
                    index: 0,
                    field: 'collegename',
                    colspan: 5
                });
            }
        }
    });
});

function searchEmployeeGrid() {
    var queryParams = $('#employee-datagrid').datagrid('options').queryParams;
    queryParams.employeename = $.trim($('input[textboxname=college]').textbox('getValue'));
    $('#employee-datagrid').datagrid('reload');
}

function showEmployeeOptBtn(value, row, index) {
    return "<a href='#' onclick='editEmployee(" + index + ")' class='easyui-linkbutton' id='editEmployee' name='editEmployee'>编辑</a>";
}

function openAddEmp(collegeid) {
    $('#emplopyee-add-form').form('clear');
    college_id = collegeid;
    $('#employee-dialog').dialog({
        closed: false,
        modal: true,
        title: "添加员工",
        onBeforeClose: function(event, ui) {

        },
        buttons: [{
            text: '确定',
            iconCls: 'icon-ok',
            handler: doSaveEmployee
        }, {
            text: '取消',
            iconCls: 'icon-bullet-cross',
            handler: function() {
                $('#employee-dialog').dialog('close');
            }
        }]
    });
    $('input[textboxname=collegename]').textbox().next('span').find('input').focus();
}

function doEditEmployee(row) {
    if (row && row['id'] != undefined) {
        college_id = row['id'];
        $('input[textboxname=collegename]').textbox('setValue', row['collegename']);
        $('input[textboxname=tel]').textbox('setValue', row['tel']);
        $('#employee-dialog').dialog({
            closed: false,
            modal: true,
            title: "编辑员工",
            onBeforeClose: function(event, ui) {

            },
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: doSaveEmployee
            }, {
                text: '取消',
                iconCls: 'icon-bullet-cross',
                handler: function() {
                    $('#employee-dialog').dialog('close');
                }
            }]
        });
        $('input[textboxname=collegename]').textbox().next('span').find('input').focus();
    }
}


function editEmployee(rowIndex) {
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
            doEditEmployee(row);
        }
    }
}

function doSaveEmployee() {
    if ($.trim($('input[textboxname=collegename]').textbox('getValue')) == "") {
        $.messager.show({
            title: '系统提示',
            msg: "院系名称没有填写"
        });
        $('input[textboxname=collegename]').textbox().next('span').find('input').focus();
        return;
    }
    if ($.trim($('input[textboxname=tel]').textbox('getValue')) == "") {
        $.messager.show({
            title: '系统提示',
            msg: "办公电话没有填写"
        });
        $('input[textboxname=tel]').textbox().next('span').find('input').focus();
        return;
    }
    if (isEmpty(description)) {
        $.messager.show({
            title: '系统提示',
            msg: "院系简介没有填写"
        });
        return;
    }
    $("#employee-add-form").ajaxSubmit({
        url: "",
        type: "post",
        dataType: "json",
        beforeSubmit: function(arr, $form, options) {
            arr.push({
                'name': 'collegeid',
                'value': college_id,
                'type': 'hidden',
                'required': false
            });
            arr.push({
                'name': 'description',
                'value': description,
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
            if (result.status) {
                if (college_id == '') {
                    $('input[textboxname=collegename]').textbox('setValue', '');
                    $('input[textboxname=tel]').textbox('setValue', '');
                    $('#description').texteditor('setValue', '');
                    $('input[textboxname=collegename]').textbox().next('span').find('input').focus();
                    $('#employee-datagrid').datagrid('reload');
                } else {
                    $('#employee-datagrid').datagrid('reload');
                    $('#employee-datagrid').dialog('close');
                }
            }
        },
        error: function(xhr, status, error, $form) {
        },
        complete: function(xhr, status, $form) {
        }
    });
}