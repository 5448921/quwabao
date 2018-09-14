<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/commons/include.inc.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <title>登录</title>
    <link rel="stylesheet" href="${ctx }/resources/css/login.css" />
    <script type="text/javascript" src="${ctx }/resources/js/jquery/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="${ctx }/resources/js/placeholder.js"></script>
</head>

<body>
<div class="login-box">
    <h1 class="logo-img"></h1>
    <ul class="center-box">
        <li class="inp-box">
            <i class="i-name"></i>
            <input id="username" type="text" placeholder="请输入用户名" value="aaa"/>
        </li>
        <li class="inp-box mt20">
            <i class="i-pwd"></i>
            <input id="password" type="password" placeholder="请输入密码" value="111111"/>
        </li>
        <li class="mt50 tips-box">
            <div class="txt-box" style="display:none" id="msg">用户不存在或者密码错误！</div>
            <a href="javascript:login();" class="btn-login">登录</a>
        </li>
    </ul>
    <div class="bottom-txt">Copyright  2010 - 2018 格斗迷 All rights reserved</div>
</div>
</body>

<script>
    function login(){
        $.ajax({
            url : "${ctx }/admin/login",
            type : "post",
            dataType : "json",
            crossDomain: true,
            data : {
                username : $("#username").val(),
                password : $("#password").val()
            },
            success : function(data){
                if(data.code == '0000'){
                    location.href = "${ctx}/admin/main";
                }else{
                    $("#msg").show();
                }
            }
        });
    }

</script>
</html>