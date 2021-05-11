<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>


	<script type="text/javascript">
		$(function () {

			//为创建按钮绑定事件

			$("#addBtn").click(function () {


				$(".time").datetimepicker({
					minView: "month",
					language:  'zh-CN',
					format: 'yyyy-mm-dd',
					autoclose: true,
					todayBtn: true,
					pickerPosition: "bottom-left"
				});

				$("#createUserModal").modal("show");

			})

			//为保存按钮绑定事件，执行添加操作
			$("#saveBtn").click(function () {

				$.ajax({

					url : "workbench/user/save.do",
					data : {
						"loginAct" : $.trim($("#create-loginAct").val()),
						"name" : $.trim($("#create-name").val()),
						"loginPwd" : $.trim($("#create-loginPwd").val()),
						"email" : $.trim($("#create-email").val()),
						"lockState" : $.trim($("#create-lockState").val()),
						"expireTime" : $.trim($("#create-expireTime").val()),
						"allowIps" : $.trim($("#create-allowIps").val())

					},
					type : "post",
					dataType : "json",
					success : function (data) {

						/*

                            data
                                {"success":true/false}

                         */
						if(data.success){

							//添加成功后

							//做完添加操作后，应该回到第一页，维持每页展现的记录数

							pageList(1,$("#contactsPage").bs_pagination('getOption', 'rowsPerPage'));



							$("#AddForm")[0].reset();

							//关闭添加操作的模态窗口
							$("#createUserModal").modal("hide");

						}else{

							alert("添加失败");

						}

					}

				})


			})




			//为删除按钮绑定事件，执行市场活动删除操作
			$("#deleteBtn").click(function () {

				//找到复选框中所有挑√的复选框的jquery对象
				var $xz = $("input[name=xz]:checked");

				if($xz.length==0){

					alert("请选择需要删除的记录");

					//肯定选了，而且有可能是1条，有可能是多条
				}else{


					if(confirm("确定删除所选中的记录吗？")){

						//url:workbench/activity/delete.do?id=xxx&id=xxx&id=xxx

						//拼接参数
						var param = "";

						//将$xz中的每一个dom对象遍历出来，取其value值，就相当于取得了需要删除的记录的id
						for(var i=0;i<$xz.length;i++){

							param += "id="+$($xz[i]).val();

							//如果不是最后一个元素，需要在后面追加一个&符
							if(i<$xz.length-1){

								param += "&";

							}

						}

						//alert(param);
						$.ajax({

							url : "workbench/user/delete.do",
							data : param,
							type : "post",
							dataType : "json",
							success : function (data) {

								/*

                                    data
                                        {"success":true/false}

                                 */
								if(data.success){

									//删除成功后
									//回到第一页，维持每页展现的记录数
									pageList(1,$("#contactsPage").bs_pagination('getOption', 'rowsPerPage'));


								}else{

									alert("删除失败");

								}

							}
						})
					}

				}
			})


			//页面加载完毕后触发一个方法
			//默认展开列表的第一页，每页展现两条记录
			pageList(1,2);

			//为查询按钮绑定事件，触发pageList方法
			$("#searchBtn").click(function () {

				/*

                    点击查询按钮的时候，我们应该将搜索框中的信息保存起来,保存到隐藏域中


                 */

				$("#hidden-name").val($.trim($("#search-name").val()));
				$("#hidden-lockState").val($.trim($("#search-lockState").val()));
				pageList(1,2);

			})

			//为全选的复选框绑定事件，触发全选操作
			$("#qx").click(function () {

				$("input[name=xz]").prop("checked",this.checked);

			})

			$("#contactsBody").on("click",$("input[name=xz]"),function () {

				$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);

			})


		});

		function pageList(pageNo,pageSize) {

			//将全选的复选框的√干掉
			$("#qx").prop("checked",false);
			//查询前，将隐藏域中保存的信息取出来，重新赋予到搜索框中
			$("#search-name").val($.trim($("#hidden-name").val()));
			$("#search-lockState").val($.trim($("#hidden-lockState").val()));

			$.ajax({
				url:"workbench/user/pageList.do",
				data:{
					"pageNo" : pageNo,
					"pageSize" : pageSize,
					"name" : $.trim($("#search-name").val()),
					"lockState" : $.trim($("#search-lockState").val()),
				},
				type:"get",
				dataType:"json",
				success:function (data) {
					var html = "";
					$.each(data.dataList,function (i,n) {

						html += '<tr class="active">';
						html += '<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
						html += '<td>'+n.loginAct+'</td>';
						html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/user/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
						html += '<td>'+n.email+'</td>';
						html += '<td>'+n.allowIps+'</td>';
						html += '<td>'+n.lockState+'</td>';
						html += '</tr>';


					})

					$("#contactsBody").html(html);

					//计算总页数
					var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;

					//数据处理完毕后，结合分页查询，对前端展现分页信息
					$("#contactsPage").bs_pagination({
						currentPage: pageNo, // 页码
						rowsPerPage: pageSize, // 每页显示的记录条数
						maxRowsPerPage: 20, // 每页最多显示的记录条数
						totalPages: totalPages, // 总页数
						totalRows: data.total, // 总记录条数

						visiblePageLinks: 5, // 显示几个卡片

						showGoToPage: true,
						showRowsPerPage: true,
						showRowsInfo: true,
						showRowsDefaultInfo: true,

						//该回调函数时在，点击分页组件的时候触发的
						onChangePage : function(event, data){
							pageList(data.currentPage , data.rowsPerPage);
						}
					});


				}
			})

		}
	</script>



</head>
<body>

	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-lockState"/>


	<!-- 创建用户的模态窗口 -->
	<div class="modal fade" id="createUserModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">新增用户</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" id="AddForm" role="form">
					
						<div class="form-group">
							<label for="create-loginActNo" class="col-sm-2 control-label">登录帐号<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-loginAct">
							</div>
							<label for="create-username" class="col-sm-2 control-label">用户姓名</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-name">
							</div>
						</div>
						<div class="form-group">
							<label for="create-loginPwd" class="col-sm-2 control-label">登录密码<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="password" class="form-control" id="create-loginPwd">
							</div>
							<label for="create-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-email">
							</div>
						</div>
						<div class="form-group">
							<label for="create-lockStatus" class="col-sm-2 control-label">锁定状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-lockState">
									<option></option>
									<option value="1">启用</option>
									<option value="0">锁定</option>
								</select>
							</div>
							<label for="create-expireTime" class="col-sm-2 control-label">失效时间</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-expireTime">
							</div>
						</div>
						<div class="form-group">
							<label for="create-allowIps" class="col-sm-2 control-label">允许访问的IP</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-allowIps" style="width: 280%" placeholder="多个用逗号隔开">
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	
	<div>
		<div style="position: relative; left: 30px; top: -10px;">
			<div class="page-header">
				<h3>用户列表</h3>
			</div>
		</div>
	</div>
	
	<div class="btn-toolbar" role="toolbar" style="position: relative; height: 80px; left: 30px; top: -10px;">
		<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
		  
		  <div class="form-group">
		    <div class="input-group">
		      <div class="input-group-addon">用户姓名</div>
		      <input class="form-control" type="text" id="search-name">
		    </div>
		  </div>
		  &nbsp;&nbsp;&nbsp;&nbsp;
		  &nbsp;&nbsp;&nbsp;&nbsp;
		  <div class="form-group">
		    <div class="input-group">
		      <div class="input-group-addon">锁定状态</div>
			  <select class="form-control" id="search-lockState">
			  	  <option></option>
			      <option value="0">锁定</option>
				  <option value="1">启用</option>
			  </select>
		    </div>
		  </div>
			&nbsp;&nbsp;&nbsp;&nbsp;
		  <button type="button" id="searchBtn" class="btn btn-default">查询</button>
		  
		</form>
	</div>
	
	
	<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;left: 30px; width: 110%; top: 20px;">
		<div class="btn-group" style="position: relative; top: 18%;">
		  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
		  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
		
	</div>
	
	<div style="position: relative; left: 30px; top: 40px; width: 110%">
		<table class="table table-hover">
			<thead>
				<tr style="color: #B3B3B3;">
					<td><input type="checkbox" id="qx"/></td>
					<td>登录帐号</td>
					<td>用户姓名</td>
					<td>邮箱</td>
					<td>允许访问IP</td>
					<td>锁定状态</td>

				</tr>
			</thead>
			<tbody id="contactsBody">
<%--				<tr class="active">--%>
<%--					<td><input type="checkbox" /></td>--%>
<%--					<td>1</td>--%>
<%--					<td><a  href="settings/qx/user/detail.jsp">zhangsan</a></td>--%>
<%--					<td>张三</td>--%>
<%--					<td>zhangsan@.com</td>--%>
<%--					<td>2021-04-25 10:10:10</td>--%>
<%--					<td>127.0.0.1,192.168.100.2</td>--%>
<%--					<td>启用</td>--%>
<%--					<td>admin</td>--%>
<%--					<td>2021-04-10 10:10:10</td>--%>
<%--					<td>admin</td>--%>
<%--					<td>2021-05-10 20:10:10</td>--%>
<%--				</tr>--%>
<%--				<tr>--%>
<%--					<td><input type="checkbox" /></td>--%>
<%--					<td>2</td>--%>
<%--					<td><a  href="settings/qx/user/detail.jsp">lisi</a></td>--%>
<%--					<td>李四</td>--%>
<%--					<td>市场部</td>--%>
<%--					<td>lisi@.com</td>--%>
<%--					<td>2021-04-25 10:10:10</td>--%>
<%--					<td>127.0.0.1,192.168.100.2</td>--%>
<%--					<td>锁定</td>--%>
<%--					<td>admin</td>--%>
<%--					<td>2021-04-10 10:10:10</td>--%>
<%--					<td>admin</td>--%>
<%--					<td>2021-05-10 20:10:10</td>--%>
<%--				</tr>--%>
			</tbody>
		</table>
	</div>
	
	<div style="height: 50px; position: relative;top: 30px; left: 30px;">
		<div id="contactsPage"></div>
	   </div>
	</div>
			
</body>
</html>