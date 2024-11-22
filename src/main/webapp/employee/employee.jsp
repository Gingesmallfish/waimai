<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
.employee_table{
	margin: 0px auto;
}
.employee_table tr{
	line-height: 50px;
}
.employee_table tr td:nth-child(2) {
	text-align: left;
	width: 70%;
}
.dialog-button{
	text-align: center;
}
</style>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false">
		<div id="employee-toolbar">
			<div class="my-toolbar-button">
				<a href="#" class="easyui-linkbutton" iconCls="icon-add"
					id="addEmployee" onclick="addEmployee('')" plain="true">添加</a>  <a href="#"
					class="easyui-linkbutton" iconCls="icon-reload"
					onclick="queryEmployee()" plain="true">刷新</a> <a href="#"
					class="easyui-linkbutton" iconCls="icon-back" onclick="removeTab()"
					plain="true">返回</a>
			</div>
			<div class="my-toolbar-search">
				<label for="employeeName">员工姓名:</label><input class="easyui-textbox" type="text" id="employeeName"
					name="employeeName" data-options="prompt:'员工姓名'" style="width: 240px;height:35px;" />
				<label for="employeeStatus">员工状态:</label>
				<select id="employeeStatus" name="employeeStatus" class="easyui-combobox" editable="false" style="width:120px;height:35px;">
					<option value="-1">全部</option>
					<option value="0">禁用</option>
					<option value="1">正常</option>
				</select>
				<a href="#" class="easyui-linkbutton" iconCls="icon-search"
					 onclick="queryEmployee()" style="height:35px;">查&nbsp;&nbsp;询&nbsp;&nbsp;</a>
			</div>
		</div>
		<table id="employee-datagrid" toolbar="#employee-toolbar"></table>
	</div>
</div>
<div id="employee-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-user'" style="width: 540px; padding: 10px;">
	<form id="employee-form" method="post">
		<table class="employee_table">
			<tr>
				<td><label for="name">员工姓名:</label></td>
				<td><input type="text" name="name" id="name" class="easyui-textbox"
					data-options="prompt:'员工姓名'" style="width: 280px; height: 35px;" /></td>
			</tr>
			<tr>
				<td><label for="sex">员工性别:</label></td>
				<td>
					<select id="sex" name="sex" class="easyui-combobox" editable="false" style="width:150px;height:35px;">
						<option value="-1">请选择</option>
						<option value="男">男</option>
						<option value="女">女</option>
					</select>
				</td>
			</tr>
			<tr>
				<td><label for="username">登录账号:</label></td>
				<td><input type="text" name="username" id="username" class="easyui-textbox"
						   data-options="prompt:'登录账号'" style="width: 280px; height: 35px;" /></td>
			</tr>
			<tr>
				<td><label for="id_number">身份证号:</label></td>
				<td><input type="text" name="id_number" id="id_number" class="easyui-textbox"
						   data-options="prompt:'身份证号'" style="width: 280px; height: 35px;" /></td>
			</tr>
			<tr>
				<td><label for="phone">手机号:</label></td>
				<td><input type="text" name="phone" id="phone" class="easyui-textbox"
						   data-options="prompt:'手机号'" style="width:280px; height: 35px;" /></td>
			</tr>
		</table>
	</form>
</div>
<script src="${pageContext.request.contextPath}/js/employee.js"></script>