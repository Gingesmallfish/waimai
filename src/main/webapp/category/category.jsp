<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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

    <body>
        <div class="easyui-layout" data-options="fit:true">
            <div data-options="region:'center',border:false">
                <div id="category-toolbar">
                    <div class="my-toolbar-button">
                        <a href="#" class="easyui-linkbutton" iconCls="icon-add" id="addCategory"
                            onclick="addCategory('')" plain="true">添加分类</a>
                        <a href="#" class="easyui-linkbutton" iconCls="icon-reload" onclick="queryCategory()"
                            plain="true">刷新</a>
                        <a href="#" class="easyui-linkbutton" iconCls="icon-back" onclick="removeTab()"
                            plain="true">返回</a>
                        <a href="#" class="easyui-linkbutton" iconCls="icon-back" onclick="sortCategory()" plain="true">排序</a>


                    </div>
                    <div class="my-toolbar-search">
                        <label for="categoryName">分类名称:</label>
                        <input class="easyui-textbox" type="text" id="categoryName" name="categoryName"
                            data-options="prompt:'分类名称'" style="width: 240px;height:35px;" />
                        <label for="categoryType">菜品分类:</label>
                        <select id="categoryType" name="categoryType" class="easyui-combobox" editable="false"
                            style="width:120px;height:35px;">
                            <option value="-1">请选择</option>
                            <option value="1">菜品分类</option>
                            <option value="2">套餐分类</option>
                        </select>
                        <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="queryCategory()"
                            style="height:35px;">查&nbsp;&nbsp;询&nbsp;&nbsp;</a>
                    </div>
                </div>
                <table id="category-datagrid" toolbar="#category-toolbar"></table>
            </div>
        </div>
        <div id="category-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-user'"
            style="width: 540px; padding: 10px;">
            <form id="category-form" method="post">
                <table class="category_table">
                    <tr>
                        <td>
                            <label for="name">分类名称:</label>
                        </td>
                        <td>
                            <input type="text" name="name" id="name" class="easyui-textbox" data-options="prompt:'分类名称'"
                                style="width: 280px; height: 35px;" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="type">类型:</label>
                        </td>
                        <td>
                            <select id="type" name="type" class="easyui-combobox" editable="false"
                                style="width:280px;height:35px;">
                                <option value="-1">请选择</option>
                                <option value="1">菜品分类</option>
                                <option value="2">套餐分类</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="sort">排序:</label>
                        </td>
                        <td>
                            <input type="text" name="sort" id="sort" class="easyui-numberbox"
                                data-options="min:0,prompt:'请输入排序值'" style="width: 280px; height: 35px;" />
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <script src="${pageContext.request.contextPath}/js/category.js"></script>
    </body>

    </html>