<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/commons/include.inc.jsp" %>
<%@ include file="/WEB-INF/view/commons/include.head.jsp" %>

<body>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center',collapsible:false, split:false">
    <div id="user_toolbar" class="search-div">
        <form action="">
            <div  class="search-content " >
                <span>
                    <label>手机号：</label>
                    <input	type="text" id="user_phone" name="mobilePhone" class="span2"  />
                    &nbsp;
                </span>
                
            </div>

            <div class="search-toolbar" >
                <span style="float:left;">
                        <a  class="easyui-linkbutton"  icon="icon-reload" plain="true"
                            href="javascript:updateValStatus()" >通过实名认证</a>
                        
                </span>

                <span style="float:left">
                    <button class="btn btn-primary btn-small" type="button" onclick="loadDatauser()">查询</button>&nbsp;
                    <button class="btn btn-small" type="button" onclick="clearSearchuser()">清空</button>&nbsp;
                </span>
            </div>
        </form>
    </div>

    <table id="user_datagrid" toolbar="#user_toolbar" height="100%" ></table>

    <div id="user_formWindow" class="easyui-window" style="width:600px;height:500px;display:none"
         data-options="iconCls:'icon-save',modal:true,closed:true">
        <div style="width: 500px;margin: 0 auto;" >
            <form id="user_dataForm" method="post">
                <input type="hidden" id="user_form_method" name="_method">
                <input type="hidden" id="user_id" name="id">
                <table class="table table-nobordered " style="margin-top: 25px;">
                    
                </table>
            </form>
        </div>
    </div>
    <form id="user_updateForm" method="post">
        <input type="hidden" id="user_update_method" name="_method" value="delete">
    </form>
    </div>


</div>



<script type="text/javascript" >
    <!--
    $(function() {
        loadDatauser();
    });

    function loadDatauser(){
        $('#user_datagrid').datagrid({
            nowrap:false,
            url : "${ctx}/admin/user/page/",
            queryParams : {
                mobilePhone : $("#user_phone").val()
            },
            method: 'get',
            pagination : true,
            fitColumns: true,
            columns : [ [
                {
                    field:"ck",
                    title : "勾选",
                    checkbox:true
                },
                {
                    field : "username",
                    title : "用户名",
                    width : 200,
                    align:"center"

                },
                {
                    field : "mobilePhone",
                    title : "手机",
                    width : 100,
                    align:"center"

                },
                {
                    field : "realName",
                    title : "真实姓名",
                    width : 200,
                    align:"center"

                },
                {
                    field : "registerTime",
                    title : "注册时间",
                    width : 200,
                    align:"center"

                },
                {
                    field : "inviteCode",
                    title : "邀请码",
                    width : 200,
                    align:"center"

                },
                {
                    field : "regInviteCode",
                    title : "注册使用邀请码",
                    width : 200,
                    align:"center"

                },
                {
                    field : "validateStatus",
                    title : "实名认证状态",
                    width : 200,
                    align:"center",
                    formatter : function(value,row,index){
                        if(value == 0){
                            return "<font color='red'>未认证</font>";
                        }else if(value == 1){
                            return "<font color='green'>已认证</font>";
                        }else{
                            return "<font color='blue'>认证未通过</font>";
                        }
                    }

                }

            ] ],
            onLoadSuccess:function(data){
                $("#user_datagrid").datagrid("uncheckAll");
            }
        });
    }


   

    function updateValStatus(){
        var rows=$("#user_datagrid").datagrid('getChecked');
        if(rows.length>0){
            Msg.confirm("确认更新","是否同步已勾选数据?",function(r){
                if(r){
                    var ids=[];
                    var l=rows.length;
                    for (var i = 0; i <l ; i++) {
                        ids.push(rows[i]["id"]);
                    }
                    ids.join(",");
                    $("#user_updateForm").form("submit",{
                        url : "${ctx}/admin/user/cal/"+ids,
                        success:function(data){
                            if(data){
                                $('#user_datagrid').datagrid("uncheckAll");
                                $('#user_datagrid').datagrid("reload");
                            }
                        }
                    });
                }
            });
        }else{
            Msg.topCenter({
                title:"提示",
                msg:"请至少勾选一条数据!",
                msgType:"warning"
            });
        }
    }

    

    function clearSearchuser(){
        $("#user_phone").val("");
    }

    //-->
</script>
</body>
