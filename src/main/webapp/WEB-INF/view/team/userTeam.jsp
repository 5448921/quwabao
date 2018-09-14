<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/commons/include.inc.jsp" %>
<%@ include file="/WEB-INF/view/commons/include.head.jsp" %>
<%--
	模块：系统管理--团队管理
--%>
<body>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center',collapsible:false, split:false">
    <div id="sysUser_toolbar" class="search-div">
        <form action="">
            <div  class="search-content " >
                <span>
                    <label>用户名：</label>
                    <input	type="text" id="sysUser_sUsername" class="span2"  />
                    &nbsp;
                </span>
                <span>
                    <label>手机号码：</label>
                    <input	type="text" id="sysUser_sUserMobile" class="span2"  />
                    &nbsp;
                </span>
                <span>
                    <label>用户状态：</label>
                    <select id="team_status" >
                        <option value="-1">全部</option>
                        <option value="1">可用</option>
                        <option value="0">不可用</option>
                    </select>
                    &nbsp;
                </span>
            </div>

            <div class="search-toolbar" >
                <span style="float:left;">
                    <shiro:hasPermission name="team:create">
                        <a class="easyui-linkbutton"  icon="icon-add"	plain="true"
                           href="javascript:toCreateTeam()">添加</a>
                    </shiro:hasPermission>
                        <a class="easyui-linkbutton"  icon="icon-add"	plain="true"
                           href="javascript:toCreateTeam()">添加</a>
                        <a class="easyui-linkbutton"  icon="icon-edit"	plain="true"
                           href="javascript:toUpdateTeam()">修改</a>
                        <a  class="easyui-linkbutton"  icon="icon-reload" plain="true"
                            href="javascript:updateRentInfo()" >批量同步矿机信息</a>
                        <a  class="easyui-linkbutton"  icon="icon-lock" plain="true"
                            href="javascript:unFrozen()" >解冻</a>
                        <a  class="easyui-linkbutton"  icon="icon-man" plain="true"
                            href="javascript:applyReward()" >申请团队奖励</a>
                </span>

                <span style="float:left">
                    <button class="btn btn-primary btn-small" type="button" onclick="loadDataSysUser()">查询</button>&nbsp;
                    <button class="btn btn-small" type="button" onclick="clearSearchSysUser()">清空</button>&nbsp;
                </span>
            </div>
        </form>
    </div>

    <table id="userTeam_datagrid" toolbar="#sysUser_toolbar" height="100%" ></table>

    <div id="sysUser_formWindow" class="easyui-window" style="width:600px;height:500px;display:none"
         data-options="iconCls:'icon-save',modal:true,closed:true">
        <div style="width: 500px;margin: 0 auto;" >
            <form id="sysUser_dataForm" method="post">
                <input type="hidden" id="sysUser_form_method" name="_method">
                <input type="hidden" id="team_id" name="id">
                <table class="table table-nobordered " style="margin-top: 25px;">
                    <tr>
                        <th style="width: 80px">用户类型：</th>
                        <td>
                            <%--<input type="text" id="sysUser_username" name="user.username" class="easyui-validatebox" data-options="required:true" maxlength="15"/>--%>
                            <select id="userType" name="user.userType">
                                <option value="0">普通</option>
                                <option value="1">团队长</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th style="width: 80px">手机号码：</th>
                        <td>
                            <input type="text" id="sysUser_mobilePhone" name="user.mobilePhone" class="easyui-validatebox" data-options="required:true,validType:'mPhone'" maxlength="20"/>
                        </td>
                    </tr>

                    <tr>
                        <th style="width: 80px">业绩基数：</th>
                        <td>
                            <input type="text" id="baseAsset" name="baseAsset" class="easyui-numberbox" data-options="required:true,min:0" maxlength="20"/>
                        </td>
                    </tr>
                    <tr>
                        <th style="width: 80px">已解冻的：</th>
                        <td>
                            <input type="text" id="unFrozenAsset" name="unFrozenAsset" class="easyui-numberbox" data-options="required:true,min:0" maxlength="20"/>
                        </td>
                    </tr>

                    <tr>
                        <th></th>
                        <td>
                            <div  style="margin-top: 10px;margin-bottom: 10px;">
                                <button type="button" class="btn btn-primary" onclick="_submitUserTeam()">保存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            </div>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
    <form id="sysUser_deleteForm" method="post">
        <input type="hidden" id="sysUser_delete_method" name="_method" value="delete">
    </form>
    </div>

    <div data-options="region:'south',collapsible:false, split:false" style="height:230px">
        <div class="easyui-tabs" data-options="fit:true,border:false,plain:true" >
            <div title="团队奖励">
                <div id="sysUserRole_toolbar" class="search-div" >
                    <div class="search-toolbar" >
						<span style="float:left;">
								<%--<a class="easyui-linkbutton"  icon="icon-add"	plain="true"--%>
                                   <%--href="javascript:toCreateSysUserRole()">添加</a>--%>
						</span>
                    </div>
                </div>

                <table id="teamReward_datagrid" toolbar="#sysUserRole_toolbar" height="100%" ></table>
            </div>
        </div>
    </div>

</div>



<script type="text/javascript" >
    <!--
    $(function() {
        loadDataSysUser();
    });

    function loadDataSysUser(){
        $('#userTeam_datagrid').datagrid({
            nowrap:false,
            url : "${ctx}/admin/userTeam/page/",
            queryParams : {
                username : $("#sysUser_sUsername").val(),
                mobilePhone : $("#sysUser_sUserMobile").val(),
                teamStatus : $("#team_status").val()
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
                    field : "user.username",
                    formatter : function(value,row,index){
                        if (row.user){
                            return row.user.username;
                        } else {
                            return value;
                        }},
                    title : "用户名",
                    width : 200,
                    align:"center"

                },
                {
                    field : "user.mobilePhone",
                    formatter : function(value,row,index){
                        if (row.user){
                            return row.user.mobilePhone;
                        } else {
                            return value;
                        }},
                    title : "手机号码",
                    width : 200,
                    align:"center"

                },
                {
                    field : "baseAsset",
                    formatter : function(value,row,index){
                        if (value){
                            return (parseFloat(value).toFixed(5) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
                        } else {
                            return value;
                        }},
                    title : "业绩基数",
                    width : 200,
                    align:"center"
                },
                {
                    field : "unFrozenRate",
                    formatter : function(value,row,index){
                        if (value){
                            return (parseFloat(value*100).toFixed(2) + '') + '%';
                        } else {
                            return value;
                        }},
                    title : "已经申请解冻的比例",
                    width : 200,
                    align:"center"

                },
                {
                    field : "totalRentAsset",
                    formatter : function(value,row,index){
                        if (value){
                            return (parseFloat(value).toFixed(5) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
                        } else {
                            return value;
                        }},
                    title : "总租赁资产",
                    width : 200,
                    align:"center"
                },
                {
                    field : "totalCount",
                    formatter : function(value,row,index){
                        if (value){
                            return (parseFloat(value).toFixed(0) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
                        } else {
                            return value;
                        }},
                    title : "总矿机数量",
                    width : 200,
                    align:"center"
                },
                {
                    field : "user.userType",
                    title : "状态",
                    width : 200,
                    align:"center",
                    formatter : function(value,row,index){
                        if(row.user.userType == 0){
                            return "<font color='red'>普通用户</font>";
                        }else{
                            return "<font color='green'>团队长</font>";
                        }
                    }

                },
                {
                    field : "teamStatus",
                    title : "状态",
                    width : 200,
                    align:"center",
                    formatter : function(value,row,index){
                        if(value == 0){
                            return "<font color='red'>不可用</font>";
                        }else{
                            return "<font color='green'>可用</font>";
                        }
                    }

                }

            ] ],
            onClickRow:function(rowIndex, rowData){
                currRoleId = rowData.id;
                loadDataTeamReward(rowData.id);
            },
            onLoadSuccess:function(data){
                $("#userTeam_datagrid").datagrid("uncheckAll");
            }
        });
    }

    function loadDataTeamReward(teamid){
        $("#teamReward_datagrid").datagrid({
            nowrap:false,
            url : "${ctx}/admin/reward/"+teamid+"/page/",
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
                    field : "createTime",
                    title : "创建时间",
                    width : 200,
                    align:"center"

                },
                {
                    field : "teamRewardType",
                    title : "奖励等级",
                    width : 50,
                    align:"center"

                },
                {
                    field : "reward",
                    title : "奖励",
                    formatter : function(value,row,index){
                        if (value){
                            return (parseFloat(value).toFixed(5) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
                        } else {
                            return value;
                        }},
                    width : "200",
                    align:"center"
                },
                {
                    field : "remainFrozen",
                    title : "未解冻的奖励",
                    formatter : function(value,row,index){
                        if (value){
                            return (parseFloat(value).toFixed(5) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
                        } else {
                            return value;
                        }},
                    width : "200",
                    align:"center"
                },
                {
                    field : "unlockPerDay",
                    title : "每天解冻",
                    formatter : function(value,row,index){
                        if (value){
                            return (parseFloat(value).toFixed(5) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
                        } else {
                            return value;
                        }},
                    width : "200",
                    align:"center"
                },
                {
                    field : "frozenAsset",
                    title : "初始冻结奖励",
                    formatter : function(value,row,index){
                        if (value){
                            return (parseFloat(value).toFixed(5) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
                        } else {
                            return value;
                        }},
                    width : "200",
                    align:"center"
                }
            ] ],
            onLoadSuccess:function(data){
                $("#teamReward_datagrid").datagrid("uncheckAll");
            }
        });
    }

    function toCreateTeam(){
        clearFormSysUser();
        // $("#sysUser_username").attr("readonly", false);
        $("#sysUser_mobilePhone").attr("readonly", false);
        $("#sysUser_formWindow").window("setTitle", "添加团队");
        $("#sysUser_form_method").val("post");
        $("#sysUser_formWindow").window("open");
    }

    function toUpdateTeam(){
        var rows=$("#userTeam_datagrid").datagrid('getChecked');
        if(rows.length != 1){
            Msg.topCenter({
                title:"提示",
                msg:"请勾选一条数据!",
                msgType:"warning"
            });
        }else{
            $.ajax({
                url : "${ctx}/admin/userTeam/"+rows[0]["id"],
                type : "get",
                dataType : "json",
                success : function(data){
                    $("#team_id").val(data.id);
                    if(data.user.userType ==1){
                        $("#userType").find("option[value='1']").attr("selected",true);
                    }else {
                        $("#userType").find("option[value='0']").attr("selected",true);
                    }

                    $("#sysUser_mobilePhone").val(data.user.mobilePhone);
                    $("#sysUser_mobilePhone").attr("readonly", true);
                    $('#baseAsset').numberbox('setValue',data.baseAsset);
                    $("#unFrozenAsset").numberbox('setValue',data.unFrozenAsset);
                    $("#sysUser_formWindow").window("setTitle", "修改团队信息");
                    $("#sysUser_form_method").val("put");
                    $("#sysUser_formWindow").window("open");
                }
            });
        }
    }

    function _submitUserTeam(){
        var url = "${ctx}/admin/userTeam/update/";
        // if($("#team_id").val() != "")
        //     url += $("#team_id").val();

        $("#sysUser_dataForm").form("submit",{
            url : url,
            success:function(data){
                dataJson = $.parseJSON(data);
                if(dataJson.code=='0000'){
                    clearSearchSysUser();
                    loadDataSysUser();
                    $("#sysUser_formWindow").window("close");
                }else{
                    alert(dataJson.message);
                }

            }
        });
    }

    function updateRentInfo(){
        var rows=$("#userTeam_datagrid").datagrid('getChecked');
        if(rows.length>0){
            Msg.confirm("确认同步","是否同步已勾选数据?",function(r){
                if(r){
                    var ids=[];
                    var l=rows.length;
                    for (var i = 0; i <l ; i++) {
                        ids.push(rows[i]["id"]);
                    }
                    ids.join(",");
                    $("#sysUser_deleteForm").form("submit",{
                        url : "${ctx}/admin/userTeam/cal/"+ids,
                        success:function(data){
                            if(data){
                                $('#userTeam_datagrid').datagrid("uncheckAll");
                                $('#userTeam_datagrid').datagrid("reload");
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

    function unFrozen(){
        var rows=$("#userTeam_datagrid").datagrid('getChecked');
        if(rows.length>0){
            Msg.confirm("确认解冻","是否解冻已勾选数据?",function(r){
                if(r){
                    var ids=[];
                    var l=rows.length;
                    for (var i = 0; i <l ; i++) {
                        ids.push(rows[i]["id"]);
                    }
                    ids.join(",");
                    $("#sysUser_deleteForm").form("submit",{
                        url : "${ctx}/admin/userTeam/unFrozen/"+ids,
                        success:function(data){
                            dataJson = $.parseJSON(data);
                            if(dataJson.code=='0000'){
                                $('#userTeam_datagrid').datagrid("uncheckAll");
                                $('#userTeam_datagrid').datagrid("reload");
                            }else {
                                alert(dataJson.message);
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

    function applyReward(){
        var rows=$("#userTeam_datagrid").datagrid('getChecked');
        if(rows.length>0){
            Msg.confirm("确认申请奖励","是否奖励已勾选数据?",function(r){
                if(r){
                    var ids=[];
                    var l=rows.length;
                    for (var i = 0; i <l ; i++) {
                        ids.push(rows[i]["id"]);
                    }
                    ids.join(",");
                    $("#sysUser_deleteForm").form("submit",{
                        url : "${ctx}/admin/userTeam/applyReward/"+ids,
                        success:function(data){
                            dataJson = $.parseJSON(data);
                            if(dataJson.code=='0000'){
                                $('#userTeam_datagrid').datagrid("uncheckAll");
                                $('#userTeam_datagrid').datagrid("reload");
                            }else {
                                alert(dataJson.message);
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

    function clearSearchSysUser(){
        // $("#sysUser_sUsername").val("");
        $("#sysUser_sUserMobile").val("");
        $("#team_status").val("-1");
    }

    function clearFormSysUser(){
        $("#team_id").val("");
        // $("#sysUser_username").val("");
        $("#sysUser_mobilePhone").val("");
        $("#baseAsset").numberbox('setValue',0);
        $("#unFrozenAsset").numberbox('setValue',0);
    }
    //-->
</script>
</body>
