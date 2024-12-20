<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
.dish_table{
	margin: 0px auto;
}
.dish_table tr{
	line-height: 45px;
}
.dialog-button{
	text-align: center;
}
.modal{
	display: none;
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	width: 100%;
	height: 100%;
	background-color: rgba(0,0,0,0.8);
}
.modal-image{
	display: block;
	max-width: 65%;
	max-height: 65%;
	position: fixed;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
}
</style>
<body>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false">
		<div id="dish-toolbar">
			<div class="my-toolbar-button">
				<a href="#" class="easyui-linkbutton" iconCls="icon-add" id="addDish" onclick="addDish('')" plain="true">添加</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-reload" onclick="queryDish()" plain="true">刷新</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-back" onclick="removeTab()" plain="true">返回</a>
			</div>
			<div class="my-toolbar-search">
				<label for="dishName">菜品名称:</label>
				<input class="easyui-textbox" type="text" id="dishName" name="dishName" data-options="prompt:'菜品名称'" style="width: 240px;height:35px;" />
				<label for="dishStatus">菜品状态:</label>
				<select id="dishStatus" name="dishStatus" class="easyui-combobox" editable="false" style="width:120px;height:35px;">
					<option value="-1">全部</option>
					<option value="0">停售</option>
					<option value="1">正常</option>
				</select>
				<a href="#" class="easyui-linkbutton" iconCls="icon-search"
				   onclick="queryDish()" style="height:35px;">查&nbsp;&nbsp;询&nbsp;&nbsp;</a>
			</div>
		</div>
		<table id="dish-datagrid" toolbar="#dish-toolbar">
		</table>
	</div>
	<!--  点击放大图片图层	-->
	<div id="modal" class="modal" onclick="hideModel()">
		<img id="modal-image" class="modal-image" title="点击关闭大图" style="cursor:pointer;" />
	</div>
</div>
<div id="dish-dialog" class="easyui-dialog"
	 data-options="closed:true,iconCls:'icon-bricks'"
	 style="width: 750px; padding: 10px;">
	<form id="dish-form" method="post" enctype="multipart/form-data">
		<table class="dish_table">
			<tr>
				<td><label for="_dishname">菜品名称:</label></td>
				<td><input type="text" name="_dishname" id="_dishname" class="easyui-textbox"
						   data-options="prompt:'菜品名称'" style="width: 360px; height: 35px;" /></td>
				<td><label for="imageFile">菜品图片:</label></td>
				<td>
					<input class="easyui-filebox" id="imageFile" name="imageFile" accept="image/*"
						   data-options="buttonText:'上传图片'" style="width:230px;height:35px;" />
				</td>
			</tr>
			<tr>
				<td><label for="category_id">菜品分类:</label></td>
				<td>
					<select id="category_id" name="category_id" class="easyui-combobox" editable="false"
							style="width:360px;height:35px;">
						<option value="-1">请选择</option>
					</select>
				</td>
				<td rowspan="4" colspan="2">
					<div style="border:1px #b2dbfb solid;width:285px;height:184px;">
						<img src="../images/none.webp" id="previewImage" style="width:283px;height:182px;"/>
					</div>
				</td>
			</tr>
			<tr>
				<td><label for="price">菜品价格:</label></td>
				<td><input type="number" name="price" id="price" class="easyui-textbox"
						   data-options="prompt:'菜品价格'" style="width: 360px; height: 35px;" /></td>
			</tr>
			<tr>
				<td><label for="code">菜品编码:</label></td>
				<td><input type="text" name="code" id="code" class="easyui-textbox"
						   data-options="prompt:'菜品编码'" style="width:360px; height: 35px;" /></td>
			</tr>
			<tr>
				<td><label for="dishSort">菜品排序:</label></td>
				<td><input type="number" name="dishSort" id="dishSort" class="easyui-textbox"
						   data-options="prompt:'排序'" style="width:360px; height: 35px;" /></td>
			</tr>
			<tr>
				<td><label for="description">菜品描述:</label></td>
				<td colspan="3">
					<textarea id="description" name="description"  style="width:650px; height:300px;"></textarea>
				</td>
			</tr>
		</table>
	</form>
</div>
<script src="${pageContext.request.contextPath}/js/dish.js"></script>
</body>
