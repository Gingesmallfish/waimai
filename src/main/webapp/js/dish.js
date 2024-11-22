var dish_id = '';
$(function() {
    loadCategory();
    $('#description').texteditor({
        toolbar: ['bold', 'italic', 'strikethrough', 'underline', '-',
            'justifyleft', 'justifycenter', 'justifyright', 'justifyfull', '-',
            'insertorderedlist', 'insertunorderedlist', 'outdent', 'indent', '-',
            'forecolor','backcolor','-',
            'fontname', 'fontsize']
    });
    $('#imageUpload').filebox({
        buttonText: '选择文件',
        buttonAlign: 'right'
    })
    $('#dish-datagrid').datagrid({
        url:"dish_/list",
        rownumbers: true,
        pagination: true,
        fitColumns: true,
        fit: true,
        pageSize: 10,
        pageList: [5, 10, 20, 50],
        remoteSort: true,
        singleSelect: true,
        multiSort: true,
        loadMsg : "数据加载中...",
        columns: [[
            {
                field: 'id',
                title: 'ID',
                align: 'center',
                width: 30,
                hidden: "true"
            },
            {
                field: 'name',
                title: '菜品名称',
                width: 30,
                align: 'center'
            },
            {
                field: 'image',
                title: '菜品图片',
                width: 60,
                align: 'center',
                formatter: function(value, row, index) {
                    return '<img src="'+ '../upload/dish/' + value +'" style="max-width:100px;max-height:40px;"/>';
                }
            },
            {
                field: 'categoryname',
                title: '菜品分类',
                width: 30,
                align: 'center',
                formatter: function(value, row, index) {
                    if (row.category) {
                        return row.category.name;
                    }
                }
            },
            {
                field: 'price',
                title: '售价',
                width: 20,
                align: 'center'
            },
            {
                field: 'status',
                title: '售卖状态',
                width: 30,
                align: 'center',
                formatter: function(value, row, index) {
                    if (value == 0) {
                        return '<span style="color:red;font-weight:bold;">停售</span>';
                    } else {
                        return "正常";
                    }
                }
            },
            {
                field: 'sort',
                title: '排序',
                width: 20,
                align: 'center'
            },
            {
                field: 'operation',
                title: '操作',
                width: 50,
                align: 'center',
                formatter: showDishOptBtn
            }
        ]],
        queryParams: {
            dishName: "",
            dishStatus:-1
        },
        onDblClickRow: function(index, row) {
            editDish(row);
        },
        onLoadSuccess: function(data) {
            if (!(data instanceof Object)) {
                data = JSON.parse(data);
            }
            if (data.code == 0) {
                createTooltip();
                $("a[name='editDish']").linkbutton({
                    plain: true,
                    iconCls: 'icon-edit'
                });
                $("a[name='enableOrDisableDish']").linkbutton({
                    plain: true,
                    iconCls: 'icon-lock'
                });
                $("a[name='deleteDish']").linkbutton({
                    plain: true,
                    iconCls: 'icon-cancel'
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

/*  设置悬浮显示大图  */
function createTooltip() {
    var tableTd = $('div.datagrid-body td[field="image"]');
    tableTd.each(function() {
        var $this = $(this);
        var index = $this.parent('tr').attr('datagrid-row-index');
        var rows = $('#dish-datagrid').datagrid('getRows');
        var currentRow = rows[index];
        if(currentRow.image != null){
            $(this).tooltip({
                content: $('<div></div>'),
                onUpdate: function(r) {
                    var row = $('#dish-datagrid').datagrid('getRows')[index];
                    var content = '<ul style="list-style-type:none;margin:10px;padding:0px;-webkit-padding-start:0;">';
                    content += '<li style="line-height:200%;word-break:break-all;">' +
                        '<img style="width:375px;height:auto;" src="../upload/dish/'+currentRow.image+'" />' + '</li>';
                    content += '</ul>';
                    r.panel({
                        width: 400,
                        content: content
                    });
                },
                trackMouse: true,
                position: 'right'
            });
        }
    });
}

function onImageSelected(e) {
    var f = $(this).next().find('input[type=file]')[0];
    if(f.files && f.files[0]){
        var reader = new FileReader();
        reader.onload = function (e){
            $("#previewImage").attr("src",e.target.result);
            $("#previewImage").css(
                {"display":"block"}
            );
            setTimeout(function () {
                $("#previewImage").css(
                    {"opacity":"1"}
                );// 淡入效果
            }, 500);
        }
        reader.readAsDataURL(f.files[0]);
    }
}

function queryDish() {
    var queryParams = $('#dish-datagrid').datagrid('options').queryParams;
    queryParams.dishName = $.trim($('input[textboxname=dishName]').textbox('getValue'));
    queryParams.dishStatus = $('#dishStatus').combobox('getValue');
    $('#dish-datagrid').datagrid('reload');
}

function showDishOptBtn(value, row, index) {
    value = row["status"] == 0 ? "启用" : "禁用";
    return "<a href='#' onclick='dishEditClick(" + index + ")' class='easyui-linkbutton' " +
        "id='editDish' name='editDish'>编辑</a>&nbsp;&nbsp;" +
        "<a href='#' onclick='enableOrDisableDish(" + index + ")' class='easyui-linkbutton' " +
        "id='enableOrDisable' name='enableOrDisable'>" + value +"</a>&nbsp;&nbsp;" +
        "<a href='#' onclick='deleteDish(" + index + ")' class='easyui-linkbutton' " +
        "id='deleteDish' name='deleteDish'>删除</a>";
}

function loadCategory(){
    $.ajax({
        url: "category_/list",
        dataType: 'json',
        type: 'get',
        success: function(data) {
            if (!(data instanceof Object)) {
                data = JSON.parse(data);
            }
            if (data.code == 0) {
                $('#category_id option:gt(0)').remove();
                $.each(data.rows, function(index, value) {
                    $('#category_id').append('<option value=' + value.id + '>' + value.name + '</option>')
                });
            }
            else {
                $.messager.show({
                    title: '系统提示',
                    msg: data.message,
                    timeout: 500,
                    showType: 'slide'
                });
            }
        },
        async: false
    });
}

function deleteDish(index){
    row = $('#dish-datagrid').datagrid('getData').rows[index];
    $.messager.confirm('系统提示', '确认是否删除此条记录?', function (r) {
        if (r) {
            $.ajax({
                url: "dish_/delete",
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
                    $('#dish-datagrid').datagrid('reload');
                },
                error: function () {
                }
            });
        }
    });
}

function enableOrDisableDish(index){
    row = $('#dish-datagrid').datagrid('getData').rows[index];
    reqUrl = row["status"] == 0 ? "dish_/startsale" : "dish_/stopsale";
    $.messager.confirm('系统提示', '确认是否修改当前菜单状态?', function (r) {
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
                    $('#dish-datagrid').datagrid('reload');
                },
                error: function () {
                }
            });
        }
    });
}

function addDish(dishid) {
    $('#dish-form').form('clear');
    $("#previewImage").attr("src","#");
    $("#previewImage").css(
        {"display":"none"}
    );
    $('#category_id').combobox('setValue',"-1");
    dish_id = dishid;
    $('#dish-dialog').dialog({
        closed: false,
        modal: true,
        collapsible:true,
        minimizable:true,
        title: "添加菜品",
        onBeforeClose: function(event, ui) {
            $('#description').texteditor('setValue', '');
        },
        toolbar:[{
            text:'保存',
            iconCls:'icon-save',
            handler:saveDish
        },{
            text:'关闭',
            iconCls:'icon-bullet-cross',
            handler: function() {
                $('#dish-dialog').dialog('close');
            }
        }],
        buttons: [{
            text: '保存',
            iconCls: 'icon-save',
            handler: saveDish
        }, {
            text: '关闭',
            iconCls: 'icon-bullet-cross',
            handler: function() {
                $('#dish-dialog').dialog('close');
            }
        }]
    });
    $('input[textboxname=_dishname]').textbox().next('span').find('input').focus();
}

function editDish(row) {
    if (row && row['id'] != undefined) {
        dish_id = row['id'];
        $('input[textboxname=_dishname]').textbox('setValue', row['name']);
        $('#category_id').combobox('setValue',row['category_id']);
        $('input[textboxname=price]').textbox('setValue', row['price']);
        $('input[textboxname=code]').textbox('setValue', row['code']);
        //图片显示
        debugger;
        if(row['image'] == null){
            $("#previewImage").attr("src","#");
            $("#previewImage").css(
                {"display":"none"}
            );
        }else{
            $("#previewImage").attr("src","../upload/dish/" + row['image']);
            $("#previewImage").css(
                {"display":"block"}
            );
        }
        $('#description').texteditor('setValue', row['description']);
        $('input[textboxname=dishSort]').textbox('setValue', row['sort']);
        $('#dish-dialog').dialog({
            closed: false,
            modal: true,
            collapsible:true,
            minimizable:true,
            title: "编辑菜品",
            onBeforeClose: function(event, ui) {
                $('#description').texteditor('setValue', '');
            },
            toolbar:[{
                text:'保存',
                iconCls:'icon-save',
                handler:saveDish
            },{
                text:'关闭',
                iconCls:'icon-bullet-cross',
                handler: function() {
                    $('#dish-dialog').dialog('close');
                }
            }],
            buttons: [{
                text: '保存',
                iconCls: 'icon-save',
                handler: saveDish
            }, {
                text: '关闭',
                iconCls: 'icon-bullet-cross',
                handler: function() {
                    $('#dish-dialog').dialog('close');
                }
            }]
        });
        $('input[textboxname=_dishname]').textbox().next('span').find('input').focus();
    }
}


function dishEditClick(rowIndex) {
    var rows = $('#dish-datagrid').datagrid('getSelections');
    if (rows.length != 1) {
        return;
    }
    /*	获取当前选中行的索引	*/
    var selectedRowIndex = $('#dish-datagrid').datagrid('getRowIndex', $('#dish-datagrid').datagrid('getSelected'));
    /*	判断当前按钮所在行与当前选中行是否一致	*/
    if (rowIndex == selectedRowIndex) {
        var row = $('#dish-datagrid').datagrid('getSelected');
        if (row) {
            editDish(row);
        }
    }
}

function saveDish() {
    if ($.trim($('input[textboxname=_dishname]').textbox('getValue')) == "") {
        $.messager.show({
            title: '系统提示',
            msg: "请填写菜品名称"
        });
        $('input[textboxname=_dishname]').textbox().next('span').find('input').focus();
        return;
    }
    if($('#category_id').combobox('getValue') == "-1"){
        $.messager.show({
            title: '系统提示',
            msg: "请选择菜品分类"
        });
        return;
    }
    if ($.trim($('input[textboxname=price]').textbox('getValue')) == "") {
        $.messager.show({
            title: '系统提示',
            msg: "请填写菜品价格"
        });
        $('input[textboxname=price]').textbox().next('span').find('input').focus();
        return;
    }
    if ($.trim($('input[textboxname=code]').textbox('getValue')) == "") {
        $.messager.show({
            title: '系统提示',
            msg: "请填写菜品编码"
        });
        $('input[textboxname=code]').textbox().next('span').find('input').focus();
        return;
    }
    description = $('#description').texteditor('getValue');
    if (isEmpty(description)) {
        $.messager.show({
            title: '系统提示',
            msg: "请填写菜品描述"
        });
        $('#description').attr('contenteditable', true);
        return;
    }
    if ($.trim($('input[textboxname=dishSort]').textbox('getValue')) == "") {
        $.messager.show({
            title: '系统提示',
            msg: "请填写菜品排序"
        });
        $('input[textboxname=dishSort]').textbox().next('span').find('input').focus();
        return;
    }
    $("#dish-form").ajaxSubmit({
        url: "dish_/save",
        type: "post",
        dataType: "json",
        beforeSubmit: function(arr, $form, options) {
            arr.push({
                'name': 'id',
                'value': dish_id,
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
            if (result.code == 0) {
                if (dish_id == '') {
                    $('input[textboxname=_dishname]').textbox('setValue', '');
                    $('#category_id').combobox('setValue','-1');
                    $('input[textboxname=price]').textbox('setValue', '');
                    $('input[textboxname=code]').textbox('setValue', '');
                    $('#description').texteditor('setValue', '');
                    $('input[textboxname=dishSort]').textbox('setValue', '');
                    //清空图片
                    $('#dish-form').form('clear');
                    $("#previewImage").attr("src","#");
                    $("#previewImage").css(
                        {"display":"none"}
                    );
                    $('input[textboxname=_dishname]').textbox().next('span').find('input').focus();
                    $('#dish-datagrid').datagrid('reload');
                } else {
                    $('#dish-datagrid').datagrid('reload');
                    $('#dish-datagrid').dialog('close');
                }
            }
        },
        error: function(xhr, status, error, $form) {
        },
        complete: function(xhr, status, $form) {
        }
    });
}