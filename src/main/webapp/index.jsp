<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" src="${ctx }/resources/js/jquery/jquery-1.10.2.js"></script>
<script>
    window.open("${ctx}/index","_top");

</script>