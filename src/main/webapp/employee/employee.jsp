<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
.add_employee_table{
	margin: 0px auto;
}

.add_employee_table tr{
	line-height: 45px;
}

.add_employee_table tr td:nth-child(2) {
	text-align: left;
	width: 70%;
}

.dialog-button {
	text-align: center;
}
</style>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false">
		<!-- Begin of toolbar -->
		<div id="employee-toolbar">
			<div class="my-toolbar-button">
				<a href="#" class="easyui-linkbutton" iconCls="icon-add"
					id="adduser" onclick="openAddEmp(-1)" plain="true">添加</a>  <a href="#"
					class="easyui-linkbutton" iconCls="icon-reload"
					onclick="searchEmpGrid()" plain="true">刷新</a> <a href="#"
					class="easyui-linkbutton" iconCls="icon-back" onclick="removeTab()"
					plain="true">返回</a>
			</div>
			<div class="my-toolbar-search">
				<label>员工姓名：</label><input class="easyui-textbox" type="text"
					name="employeeName" data-options="prompt:'员工姓名'" style="width: 240px;height:35px;"></input>
				<a href="#" class="easyui-linkbutton" iconCls="icon-search"
					 onclick="searchEmpGrid()" style="height:35px;">查&nbsp;&nbsp;询&nbsp;&nbsp;</a>
			</div>
		</div>
		<!-- End of toolbar -->
		<table id="employee-datagrid" toolbar="#employee-toolbar">
		</table>
	</div>
</div>
<!-- Begin of easyui-dialog -->
<div id="employee-dialog" class="easyui-dialog"
	data-options="closed:true,iconCls:'icon-save'"
	style="width: 400px; padding: 10px;">
	<form id="employee-add-form" method="post">
		<table class="add_employee_table">
			<tr>
				<td width="60">手机:</td>
				<td><input type="text" name="add_tel" class="easyui-textbox"
					data-options="prompt:'手机'" style="width: 160px; height: 35px;" /></td>
			</tr>
			<tr>
				<td width="60">姓名:</td>
				<td><input type="text" name="add_username"
					class="easyui-textbox" data-options="prompt:'姓名'"
					style="width: 160px; height: 35px;" /></td>
			</tr>
			<tr>
				<td>密码:</td>
				<td><input type="password" name="password"
					class="easyui-textbox" data-options="prompt:'密码'"
					style="width: 160px; height: 35px;" /></td>
			</tr>
			<tr>
				<td>确认密码:</td>
				<td><input type="password" name="confirmpwd"
					class="easyui-textbox" data-options="prompt:'确认密码'"
					style="width: 160px; height: 35px;" /></td>
			</tr>
		</table>
	</form>
</div>
<!-- End of easyui-dialog -->
<script src="${pageContext.request.contextPath}/js/employee.js"></script>