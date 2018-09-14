<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/commons/include.inc.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"	+ request.getServerName() + ":" + request.getServerPort()	+ path + "/";
	com.gedoumi.sys.entity.SysUser loginUser = (com.gedoumi.sys.entity.SysUser)org.apache.shiro.SecurityUtils.getSubject().getSession().getAttribute("sysUser");
	if(loginUser == null){
		response.sendRedirect(basePath+"admin/toLogin");
	}
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<META HTTP-EQUIV="Pragma" CONTENT="no-cache"> 
	<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache"> 
	<META HTTP-EQUIV="Expires" CONTENT="0">
	<script type="text/javascript">
	<!--
		appWebRoot='<%=basePath%>';//网站地址
	//-->
	</script>
	<%@ include file="/WEB-INF/view/commons/include.head.jsp"%>
</head>
<body class="easyui-layout">
		<!-- header -->
	    <div data-options="region:'north'"  style="height: 80px; padding: 1px;">
	    <img src="${ctx }/resources/images/banner_logo.png" >
	    <div style="float:right; padding: 40px 20px 0px 0px">
	    欢迎您, <%=loginUser == null ? "" : loginUser.getUsername() %>!
	    <a href="${ctx }/admin/logout" >退出登录</a>
	    </div>
	    </div>
	    <!-- 左侧菜单 -->
	    <div data-options="region:'west',title:'导航菜单',collapsible:false, split:true" style="width:230px; padding: 1px;" >
	    	<div class="easyui-accordion" data-options="fit:true,border:false" id="menuDiv">
	    	</div>
	    </div>
	    <!-- 中间内容 -->
	    <div data-options="region:'center',title:'',border:false" >
	    	<div id="mainTab" class="easyui-tabs" data-options="fit:true,border:false,plain:true" ></div>
	    </div>
	    
<script type="text/javascript">
$(function(){
	$.ajax({
		url : "${ctx}/admin/menu/userMenu",
		dataType : "json",
		success : function(data){
			$.each(data, function(i, v){
				var html = '<ul class="ul-menu">';
				$.each(v.menuList, function(j, w){
					html += '<li><a href="javascript:openMenu(\'${ctx}'+w.menu_url+'\',\''+w.menu_name+'\')" >'+w.menu_name+'</a></li>';
				}); 
				html += '</ul>';
				$("#menuDiv").accordion('add', {
					title: v.superMenu.menuName,
					content : html,
					selected: true
				});
			});
			$("#menuDiv").accordion('select', 0);
			/* html = '<div title="系统管理"><ul class="ul-menu"></ul></div></div>'
			$("#menuDiv").html(html); */
		}
	});
});

function openMenu(url, title){
	var h = $("#mainTab").get(0).offsetHeight - 75;
	var w = $("#mainTab").get(0).offsetWidth - 230;
	if(!$("#mainTab").tabs("exists", title)){
		$("#mainTab").tabs("add",{
			title : title,
			border : false,
			closable : true,
			href : url
		});
	}else{
		$("#mainTab").tabs("select", title);
	}
}
</script>
</body>
</html>