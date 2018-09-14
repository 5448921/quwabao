<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/commons/include.inc.jsp" %>
<%@ include file="/WEB-INF/view/commons/include.head.jsp" %>

<body>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center',collapsible:false, split:false">
    <div id="query_toolbar" class="search-div">
        <form id="queryForm" action="${ctx}/admin/query/doQuery" method="post" >
            <div>
                <font color="#ff7f50">${message}</font>
            </div>
            <div>

                    <textarea  rows="5" cols="100" id="queryStr" name="queryStr" style="width: 900px">${queryStr}</textarea>
                     <label>只能查询</label>
            </div>

            <div class="search-toolbar" >
                <span style="float:left;">
                        <a  class="easyui-linkbutton"  icon="icon-reload" plain="true"
                            href="javascript:exportData()" >导出</a>
                        
                </span>

                <span style="float:left">
                    <button class="btn btn-primary btn-small" type="button" onclick="loadQuery()">查询</button>&nbsp;
                    <button class="btn btn-small" type="button" onclick="clearSearchQuery()">清空</button>&nbsp;
                </span>
            </div>
        </form>
    </div>

    <table id="query_datagrid" data-toggle="table">
        <thead>
        <c:forEach items="${data}" var="item" varStatus="status" begin="0" end="0">
            <tr>
                    <th></th>
                <c:forEach items="${item}" var="i" varStatus="istatus">
                    <th>${i.key}</th>
                </c:forEach>
            </tr>
        </c:forEach>
        </thead>
        <tbody>
        <c:forEach items="${data}" var="item" varStatus="status">
            <tr>
                    <td>${status.index + 1}</td>
                <c:forEach items="${item}" var="i" varStatus="istatus">
                    <td>${i.value}</td>
                </c:forEach>
            </tr>
        </c:forEach>
        </tbody>
    </table>


        <form id="exportForm" method="post" action="${ctx}/admin/query/export">
            <input type="hidden" id="hiddenQueryStr" name="queryStr" >
        </form>

</div>



<script type="text/javascript" >
    <!--
    $(function() {
        // loadQuery();
    });

    function loadQuery(){
        $('#queryForm').submit();
    }

    function exportData(){
        $("#hiddenQueryStr").val($("#queryStr").val());

        $('#exportForm').submit();
    }


   

    

    function clearSearchQuery(){
        $("#queryStr").val("");
    }

    //-->
</script>
</body>
