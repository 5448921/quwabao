<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/commons/include.inc.jsp" %>
<%@ include file="/WEB-INF/view/commons/include.head.jsp" %>
<%--
	模块：系统管理--团队管理
--%>
<body>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center',collapsible:false, split:false">
    <div id="sysRent_toolbar" class="search-div">
        <form action="">
            <div  class="search-content " >
                <span>
                    <label>矿机名称：</label>
                    <input	type="text" id="sysRent_rentname" name="name" class="span2"  />
                    &nbsp;
                </span>
                
            </div>

            <div class="search-toolbar" >
                <span style="float:left;">

                        <a class="easyui-linkbutton"  icon="icon-add"	plain="true"
                           href="javascript:toCreateRent()">添加</a>
                        <a class="easyui-linkbutton"  icon="icon-edit"	plain="true"
                           href="javascript:toUpdateRent()">修改</a>
                        <a  class="easyui-linkbutton"  icon="icon-reload" plain="true"
                            href="javascript:updateRentNumber()" >批量同步矿机信息</a>
                        <a  class="easyui-linkbutton"  icon="icon-reload" plain="true"
                            href="javascript:onTrans()" ><div id="onTrans_div">上线交易所开关</div></a>
                </span>

                <span style="float:left">
                    <button class="btn btn-primary btn-small" type="button" onclick="loadDataSysRent()">查询</button>&nbsp;
                    <button class="btn btn-small" type="button" onclick="clearSearchSysRent()">清空</button>&nbsp;
                </span>
            </div>
        </form>
    </div>

    <table id="rent_datagrid" toolbar="#sysRent_toolbar" height="100%" ></table>

    <div id="sysRent_formWindow" class="easyui-window" style="width:600px;height:500px;display:none"
         data-options="iconCls:'icon-save',modal:true,closed:true">
        <div style="width: 500px;margin: 0 auto;" >
            <form id="sysRent_dataForm" method="post">
                <input type="hidden" id="sysRent_form_method" name="_method">
                <input type="hidden" id="rent_id" name="id">
                <table class="table table-nobordered " style="margin-top: 25px;">
                    <tr>
                        <th style="width: 80px">用户类型：</th>
                        <td>
                            <select id="sys_rentStatus" name="rentStatus">
                                <option value="0">不可用</option>
                                <option value="1">可用</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th style="width: 80px">矿机类型：</th>
                        <td>
                            <input type="text" id="sysRent_name" name="name" class="easyui-validatebox" data-options="required:true" maxlength="20"/>
                        </td>
                    </tr>

                    <tr>
                        <th style="width: 80px">租金：</th>
                        <td>
                            <input type="text" id="rentMoney" name="money" class="easyui-numberbox" data-options="required:true,min:0" maxlength="20"/>
                        </td>
                    </tr>
                    <tr>
                        <th style="width: 80px">code：</th>
                        <td>
                            <input type="text" id="rentCode" name="code" class="easyui-validatebox" data-options="required:true" maxlength="4"/>
                        </td>
                    </tr>
                    <tr>
                        <th style="width: 80px">收益率%：</th>
                        <td>
                            <input type="text" id="rentRate" name="rate" class="easyui-numberbox" data-options="required:true,min:0,precision:2" maxlength="20"/>
                        </td>
                    </tr>
                    <tr>
                        <th style="width: 80px">矿机总数：</th>
                        <td>
                            <input type="text" id="maxNumber" name="maxNumber" class="easyui-numberbox" data-options="required:true,min:0" maxlength="20"/>
                        </td>
                    </tr>
                    <tr>
                        <th style="width: 80px">算力最小：</th>
                        <td>
                            <input type="text" id="digMin" name="digMin" class="easyui-numberbox" data-options="required:true,min:0" maxlength="20"/>
                        </td>
                    </tr>
                    <tr>
                        <th style="width: 80px">算力最大：</th>
                        <td>
                            <input type="text" id="digMax" name="digMax" class="easyui-numberbox" data-options="required:true,min:0" maxlength="20"/>
                        </td>
                    </tr>

                    <tr>
                        <th></th>
                        <td>
                            <div  style="margin-top: 10px;margin-bottom: 10px;">
                                <button type="button" class="btn btn-primary" onclick="submitRent()">保存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            </div>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
    <form id="sysRent_updateForm" method="post">
        <input type="hidden" id="sysUser_update_method" name="_method" value="delete">
    </form>
    </div>


</div>



<script type="text/javascript" >
    <!--
    $(function() {
        loadDataSysRent();
        getOnTrans();
    });

    function loadDataSysRent(){
        $('#rent_datagrid').datagrid({
            nowrap:false,
            url : "${ctx}/admin/rent/page/",
            queryParams : {
                name : $("#sysRent_rentname").val()
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
                    field : "name",
                    title : "类型",
                    width : 200,
                    align:"center"

                },
                {
                    field : "code",
                    title : "code",
                    width : 100,
                    align:"center"

                },
                {
                    field : "money",
                    formatter : function(value,row,index){
                        if (value){
                            return (parseFloat(value).toFixed(2) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
                        } else {
                            return value;
                        }},
                    title : "租金",
                    width : 200,
                    align:"center"
                },
                {
                    field : "rate",
                    formatter : function(value,row,index){
                        if (value){
                            return (parseFloat(value).toFixed(4) + '');
                        } else {
                            return value;
                        }},
                    title : "收益率（30天）",
                    width : 200,
                    align:"center"
                },
                {
                    field : "profitMoney",
                    formatter : function(value,row,index){
                        if (value){
                            return (parseFloat(value).toFixed(2) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
                        } else {
                            return value;
                        }},
                    title : "预计收益",
                    width : 200,
                    align:"center"

                },
                {
                    field : "profitMoneyExt",
                    formatter : function(value,row,index){
                        if (value){
                            return (parseFloat(value).toFixed(2) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
                        } else {
                            return value;
                        }},
                    title : "预计收益（包括本金）",
                    width : 200,
                    align:"center"
                },
                {
                    field : "maxNumber",
                    formatter : function(value,row,index){
                        if (value){
                            return (parseFloat(value).toFixed(0) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
                        } else {
                            return value;
                        }},
                    title : "矿机最大数量",
                    width : 200,
                    align:"center"
                },
                {
                    field : "digMin",
                    title : "算力最低",
                    width : 200,
                    align:"center"

                },
                {
                    field : "digMax",
                    title : "算力最高",
                    width : 200,
                    align:"center"

                },
                {
                    field : "rentNumber",
                    title : "矿机数量",
                    formatter : function(value,row,index){
                        if (value){
                            return (parseFloat(value).toFixed(0) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
                        } else {
                            return value;
                        }},
                    width : 200,
                    align:"center"

                },
                {
                    field : "rentStatus",
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
            onLoadSuccess:function(data){
                $("#rent_datagrid").datagrid("uncheckAll");
            }
        });
    }


    function toCreateRent(){
        clearFormSysRent();
        // $("#sysUser_username").attr("readonly", false);
        $("#sysRent_name").attr("readonly", false);
        $("#sysRent_formWindow").window("setTitle", "添加矿机");
        $("#sysRent_form_method").val("post");
        $("#sysRent_formWindow").window("open");
    }

    function toUpdateRent(){
        var rows=$("#rent_datagrid").datagrid('getChecked');
        if(rows.length != 1){
            Msg.topCenter({
                title:"提示",
                msg:"请勾选一条数据!",
                msgType:"warning"
            });
        }else{
            $.ajax({
                url : "${ctx}/admin/rent/"+rows[0]["id"],
                type : "get",
                dataType : "json",
                success : function(data){
                    $("#rent_id").val(data.id);
                    if(data.rentStatus ==1){
                        $("#sys_rentStatus").find("option[value='1']").attr("selected",true);
                        $("#sys_rentStatus").find("option[value='0']").attr("selected",false);
                    }else {
                        $("#sys_rentStatus").find("option[value='0']").attr("selected",true);
                        $("#sys_rentStatus").find("option[value='1']").attr("selected",false);
                    }

                    $("#sysRent_name").val(data.name);
                    $('#rentMoney').numberbox('setValue',data.money);
                    $('#rentCode').val(data.code);
                    $("#rentRate").numberbox('setValue',data.rate * 100);
                    $("#maxNumber").numberbox('setValue',data.maxNumber);
                    $("#digMax").numberbox('setValue',data.digMax);
                    $("#digMin").numberbox('setValue',data.digMin);
                    $("#sysRent_formWindow").window("setTitle", "修改团队信息");
                    $("#sysRent_form_method").val("put");
                    $("#sysRent_formWindow").window("open");
                }
            });
        }
    }

    function updateRentNumber(){
        var rows=$("#rent_datagrid").datagrid('getChecked');
        if(rows.length>0){
            Msg.confirm("确认同步","是否同步已勾选数据?",function(r){
                if(r){
                    var ids=[];
                    var l=rows.length;
                    for (var i = 0; i <l ; i++) {
                        ids.push(rows[i]["id"]);
                    }
                    ids.join(",");
                    $("#sysRent_updateForm").form("submit",{
                        url : "${ctx}/admin/rent/cal/"+ids,
                        success:function(data){
                            if(data){
                                $('#rent_datagrid').datagrid("uncheckAll");
                                $('#rent_datagrid').datagrid("reload");
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

    function onTrans(){
        $.ajax({
            url : "${ctx}/admin/rent/onTrans",
            type : "get",
            dataType : "json",
            success : function(data){
                if(data.data){
                    $("#onTrans_div").text("交易所开关[开]");
                    $("#onTrans_div").css("background-color","green");
                }else {
                    $("#onTrans_div").text("交易所开关[关]");
                    $("#onTrans_div").css("background-color","red");
                }

            }
        });
    }

    function getOnTrans(){
        $.ajax({
            url : "${ctx}/admin/rent/getOnTrans",
            type : "get",
            dataType : "json",
            success : function(data){
                if(data.data){
                    $("#onTrans_div").text("交易所开关[开]");
                    $("#onTrans_div").css("background-color","green");
                }else {
                    $("#onTrans_div").text("交易所开关[关]");
                    $("#onTrans_div").css("background-color","red");
                }

            }
        });
    }

    function submitRent(){
        var url = "${ctx}/admin/rent/update/";
        // if($("#rent_id").val() != "")
        //     url += $("#rent_id").val();

        $("#sysRent_dataForm").form("submit",{
            url : url,
            success:function(data){
                dataJson = $.parseJSON(data);
                if(dataJson.code=='0000'){
                    clearSearchSysRent();
                    loadDataSysRent();
                    $("#sysRent_formWindow").window("close");
                }else{
                    alert(dataJson.message);
                }

            }
        });
    }


    function clearSearchSysRent(){
        $("#sysRent_rentname").val("");
    }

    function clearFormSysRent(){
        $("#rent_id").val("");
        // $("#sysUser_username").val("");
        $("#sysRent_name").val("");
        $("#rentMoney").numberbox('setValue',0);
        $("#rentRate").numberbox('setValue',0);
        $("#maxNumber").numberbox('setValue',0);
        $("#digMin").numberbox('setValue',0);
        $("#digMax").numberbox('setValue',0);
    }
    //-->
</script>
</body>
