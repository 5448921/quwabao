<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<link href="${ctx }/resources/js/bootstrap/css/bootstrap.css" rel="stylesheet" media="screen">
<link href="${ctx }/resources/js/bootstrap/css/bootstrap-table.css" rel="stylesheet" media="screen">
<link rel="stylesheet" href="${ctx }/resources/css/main.css" type="text/css"/>
<link rel="stylesheet" href="${ctx }/resources/js/ui/css/ui.css" type="text/css"/>
<link rel="stylesheet" href="${ctx }/resources/js/ui/css/icon.css" type="text/css"/>
<%-- jquery --%>
<script type="text/javascript" src="${ctx }/resources/js/jquery/jquery-1.10.2.js"></script>
<script type="text/javascript" src="${ctx }/resources/js/jquery/jquery.cookie.js"></script>
<script type="text/javascript" src="${ctx }/resources/js/jquery/jquery-expand.js"></script>
<%-- easyui  --%>
<link href="${ctx }/resources/js/easyui-1.5.2/themes/icon.css"  rel="stylesheet" type="text/css" media="screen" />
<link id="easyuiTheme" href="${ctx }/resources/js/easyui-1.5.2/themes/default/easyui.css"  rel="stylesheet" type="text/css" media="screen" />
<script type="text/javascript" src="${ctx }/resources/js/easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx }/resources/js/easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>

<script type="text/javascript" src="${ctx }/resources/js/bootstrap/js/bootstrap-table.js"></script>

<script type="text/javascript" src="${ctx }/resources/js/ui/ui.core.js"></script>
<script type="text/javascript" src="${ctx }/resources/js/ui/ui.navTab.js"></script>
<script type="text/javascript" src="${ctx }/resources/js/ui/ui.ajax.js"></script>
<script type="text/javascript" src="${ctx }/resources/js/ui/ui.database.js"></script>
<script type="text/javascript" src="${ctx }/resources/js/ui/plugins/zTree_plugin.js"></script>

<script type="text/javascript" src="${ctx }/resources/js/ui/ui.util.js"></script>

<script type="text/javascript" src="${ctx }/resources/js/ui/easyui.defaults.js"></script>
<script type="text/javascript" src="${ctx }/resources/js/ui/easyui.validate.js"></script>
<script type="text/javascript" src="${ctx }/resources/js/ui/ui.msg.js"></script>
<script type="text/javascript" src="${ctx }/resources/js/util.js"></script>
<script type="text/javascript" src="${ctx }/resources/js/init.js"></script>
<script type="text/javascript" src="${ctx }/resources/js/editor/kindeditor-all.js"></script>
<script type="text/javascript" src="${ctx }/resources/js/editor/lang/zh-CN.js"></script>

<script type="text/javascript" src="${ctx }/resources/js/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="${ctx }/resources/js/util.js" charset="utf-8"></script>

<script>
	(function($) {
		var ajax = $.ajax;
		$.ajax = function(s) {
			var old = s.error;
			var old1 = s.success;
			s.error = function(xhr, status, err) {
				old(xhr, status, err);
			};
			s.success = function(data, textStatus, xhr) {
				if(data.code == "300"){
                    Msg.alert({
                        title:"提示",
                        msg:"请重新登录!",
                        icon:"warning",
                        fn:function () {
                            window.location.href='${ctx}/admin/toLogin';
                        }
                    });
				}else if(data.code == "301"){
					Msg.topCenter({
						title:"提示",
						msg:"没有操作权限!",
						msgType:"warning"
					});
				}else{
					old1(data, textStatus, xhr);
				}
			};
			ajax(s);
		};
	})(jQuery);

	function getUrlParam(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if (r != null)
			return unescape(r[2]);
		return null;
	}



	
</script>