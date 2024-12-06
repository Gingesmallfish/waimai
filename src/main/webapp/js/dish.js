var dish_id = '';
var editor;
$(function() {
    loadCategory();
    editor = KindEditor.create('#description', {
        resizeType: 1,
        items: ['bold', 'italic', 'strikethrough', 'underline', '-',
            'justifyleft', 'justifycenter', 'justifyright', 'justifyfull', '-',
            'insertorderedlist', 'insertunorderedlist', 'outdent', 'indent', '-',
            'forecolor', 'backcolor', '-',
            'fontname', 'fontsize'],
        height: true
    });

    $('#imageUpload').filebox({
        buttonText: '选择文件',
        buttonAlign: 'right',
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

/**
 * 当选择图片时触发的函数
 * @param {Event} e - 触发的事件对象
 */
function onImageSelected(e) {
    // 获取下一个元素中的文件输入框
    var f = $(this).next().find('input[type=file]')[0];
    // 检查是否有文件被选中
    if(f.files && f.files[0]){
        // 创建一个文件读取对象
        var reader = new FileReader();
        // 定义读取文件后的操作
        reader.onload = function (e){
            // 设置图片预览的src属性为读取到的文件内容
            $("#previewImage").attr("src",e.target.result);
            // 显示图片预览
            $("#previewImage").css(
                {"display":"block"}
            );
            // 延时500毫秒后执行淡入效果
            setTimeout(function () {
                $("#previewImage").css(
                    {"opacity":"1"}
                );// 淡入效果
            }, 500);
        }
        // 读取选中的文件为Data URL格式
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

/**
 * 加载类别数据到下拉菜单中
 * 此函数通过Ajax请求从服务器获取类别列表，并将其填充到具有ID为'category_id'的下拉菜单中
 */
function loadCategory(){
    // 发起Ajax请求获取类别列表
    $.ajax({
        url: "category_/list",
        dataType: 'json',
        type: 'get',
        success: function(data) {
            // 检查返回数据是否为对象，如果不是，则解析为JSON
            if (!(data instanceof Object)) {
                data = JSON.parse(data);
            }
            // 根据返回数据的状态码处理结果
            if (data.code == 0) {
                // 如果状态码为0，表示成功，清除下拉菜单中现有的选项（保留默认选项）
                $('#category_id option:gt(0)').remove();
                // 遍历返回的类别列表，为每个类别创建一个新的选项，并添加到下拉菜单中
                $.each(data.rows, function(index, value) {
                    $('#category_id').append('<option value=' + value.id + '>' + value.name + '</option>')
                });
            }
            else {
                // 如果状态码不为0，显示错误消息
                $.messager.show({
                    title: '系统提示',
                    msg: data.message,
                    timeout: 500,
                    showType: 'slide'
                });
            }
        },
        // 设置请求为同步，这通常不推荐使用，因为它会阻塞浏览器，导致用户界面不响应
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
    $("#previewImage").css({"display":"none"});
    $('#category_id').combobox('setValue',"-1");
    dish_id = dishid;  // 赋值，而不是重新声明
    $('#dish-dialog').dialog({
        closed: false,
        modal: true,
        collapsible:true,
        minimizable:true,
        title: "添加菜品",
        onBeforeClose: function(event, ui) {
            editor.html('');
        },
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

/**
 * 编辑菜品信息
 * @param {Object} row - 包含菜品信息的对象
 * 该函数根据提供的菜品信息填充表单字段，并显示菜品的详细信息以供编辑
 */
function editDish(row) {
    // 确保row对象存在且具有id属性
    if (row && row['id'] != undefined) {
        // 提取菜品ID
        dish_id = row['id'];
        // 设置菜品名称
        $('input[textboxname=_dishname]').textbox('setValue', row['name']);
        // 设置菜品分类
        $('#category_id').combobox('setValue',row['category_id']);
        // 设置价格
        $('input[textboxname=price]').textbox('setValue', row['price']);
        // 设置菜品代码
        $('input[textboxname=code]').textbox('setValue', row['code']);
        // 根据是否有图片，设置图片显示或隐藏
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
        // 设置菜品描述
        $('#description').texteditor('setValue', row['description']);
        // 设置菜品排序
        $('input[textboxname=dishSort]').textbox('setValue', row['sort']);
        // 初始化菜品编辑对话框
        $('#dish-dialog').dialog({
            closed: false,
            modal: true,
            collapsible:true,
            minimizable:true,
            title: "编辑菜品",
            // 在对话框关闭前清空描述字段
            onBeforeClose: function(event, ui) {
                $('#description').texteditor('setValue', '');
            },
            // 添加工具栏按钮
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
            // 添加底部按钮
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
        // 聚焦到菜品名称字段
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