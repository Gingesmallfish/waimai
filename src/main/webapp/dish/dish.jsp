<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<style>
    .dish_table {
        margin: 0px auto;
    }

    .dish_table tr {
        line-height: 50px;
    }

    .dish_table tr td:nth-child(2) {
        text-align: left;
        width: 80%;
    }
</style>

<body>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center',border:false">
        <div id="category-toolbar">
            <div class="my-toolbar-button">
                <a href="#" class="easyui-linkbutton" iconCls="icon-add" id="addCategory" onclick="addDish('')"
                   plain="true">添加分类</a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-reload" onclick="queryDish()" plain="true">刷新</a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-back" onclick="removeTab()" plain="true">返回</a>
            </div>
            <div class="my-toolbar-search">
                <label for="DishName">菜品名称:</label>
                <input class="easyui-textbox" type="text" id="DishName" name="DishName" data-options="prompt:'菜品名称'"
                       style="width: 240px;height:35px;"/>
                <label for="dishStatus">菜品分类:</label>
                <select id="dishStatus" name="dishStatus" class="easyui-combobox" editable="false"
                        style="width:120px;height:35px;">
                    <option value="-1">请选择</option>
                    <option value="1">菜品分类</option>
                    <option value="2">套餐分类</option>
                </select>
                <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="queryCategory()"
                   style="height:35px;">查&nbsp;&nbsp;询&nbsp;&nbsp;</a>
            </div>
        </div>
        <table id="dish-datagrid" class="dish-datagrid" toolbar="#category-toolbar" style="cursor: pointer;"></table>
    </div>
</div>

<div id="dish-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-user'"
     style="width: 735px; padding: 10px;">
    <form id="dish-form" method="post">
        <div style="display: flex;">
            <!-- 左侧：菜品信息 -->
            <div style="flex: 1; margin-right: 2px;">
                <table class="dish_table">
                    <tr>
                        <td>
                            <label for="name">菜品名称:</label>
                        </td>
                        <td>
                            <input class="easyui-textbox" name="name" id="name" data-options="prompt:'请输入菜品名称'"
                                   style="width: 280px; height: 35px;"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="category_id">菜品分类:</label>
                        </td>
                        <td>
                            <select id="category_id" name="category_id" class="easyui-combobox" editable="false"
                                    style="width:280px;height:35px;">
                                <option value="-1">请选择</option>
                                <option value="1">菜品套餐</option>
                                <option value="2">套餐套餐</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="price">售价:</label>
                        </td>
                        <td>
                            <input class="easyui-numberbox" name="price" id="price"
                                   data-options="min:0,prompt:'请输入售价'"
                                   style="width: 280px; height: 35px;"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="code">菜品编码:</label>
                        </td>
                        <td>
                            <input class="easyui-textbox" name="code" id="code" data-options="prompt:'请输入菜品编码'"
                                   style="width: 280px; height: 35px;"/>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <label for="dishSort">排序:</label>
                        </td>
                        <td>
                            <input class="easyui-numberbox" name="dishSort" id="dishSort"
                                   data-options="min:0,prompt:'请输入排序值'"
                                   style="width: 280px; height: 35px;"/>
                        </td>
                    </tr>
                </table>
            </div>
            <!-- 右侧：图片上传和预览 -->
            <div style="flex: 1;">
                <div style="display: flex; align-items: center; margin-bottom: 10px;">
                    <label for="imageUpload" style="margin-right: 10px;">菜品图片:</label>
                    <input class="easyui-filebox" id="imageUpload" name="imageFile"
                           style="width: 280px; height: 35px; display: inline-block;"
                           data-options="onChange:onImageSelected"/>
                </div>
                <div>
                    <img id="previewImage" src="#" alt="预览图片"
                         style="display:block;max-width:200px;max-height:300px;">
                </div>
            </div>
        </div>
    </form>
    <textarea id="description" name="description" rows="5" cols="35" style="width:700px;height:300px;"></textarea>
</div>
<script src="${pageContext.request.contextPath}/js/dish.js"></script>
</body>