<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>分类页面</title>
    <style>
        .category_table {
            margin: 0px auto;
        }

        .category_table tr {
            line-height: 50px;
        }

        .category_table tr td:nth-child(2) {
            text-align: left;
            width: 70%;
        }

        .dialog-button {
            text-align: center;
        }
    </style>
</head>
<body>
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center',border:false">
            <div id="category-toolbar">
                <div class="my-toolbar-button">
                    <a href="#" class="easyui-linkbutton" iconCls="icon-add" id="addCategory" onclick="addCategory('')"
                        plain="true">添加</a> <a href="#" class="easyui-linkbutton" iconCls="icon-reload"
                        onclick="queryCategory()" plain="true">刷新</a> <a href="#" class="easyui-linkbutton"
                        iconCls="icon-back" onclick="removeTab()" plain="true">返回</a>
                </div>
                <div class="my-toolbar-search">
                    <label for="categoryName">分类名称:</label><input class="easyui-textbox" type="text" id="categoryName"
                        name="categoryName" data-options="prompt:'分类名称'" style="width: 240px;height:35px;" />
                    <label for="categoryStatus">分类状态:</label>
                    <select id="categoryStatus" name="categoryStatus" class="easyui-combobox" editable="false"
                        style="width:120px;height:35px;">
                        <option value="-1">全部</option>
                        <option value="0">禁用</option>
                        <option value="1">正常</option>
                    </select>
                    <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="queryCategory()"
                        style="height:35px;">查&nbsp;&nbsp;询&nbsp;&nbsp;</a>
                </div>
            </div>
            <table id="category-datagrid" toolbar="#category-toolbar"></table>
        </div>
    </div>
    <div id="category-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-category'"
        style="width: 540px; padding: 10px;">
        <form id="category-form" method="post">
            <table class="category_table">
                <tr>
                    <td><label for="name">分类名称:</label></td>
                    <td><input type="text" name="name" id="name" class="easyui-textbox" data-options="prompt:'分类名称'"
                            style="width: 280px; height: 35px;" /></td>
                </tr>
                <tr>
                    <td><label for="description">分类描述:</label></td>
                    <td><input type="text" name="description" id="description" class="easyui-textbox"
                            data-options="prompt:'分类描述'" style="width: 280px; height: 35px;" /></td>
                </tr>
            </table>
        </form>
    </div>
    <script src="${pageContext.request.contextPath}/js/category.js"></script>
</body>
</html>
