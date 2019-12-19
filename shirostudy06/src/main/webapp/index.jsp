<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>首页</title>
</head>
<body>
<!-- 普通的登录验证 使用shiro.ini -->
普通的登录验证 使用shiro.ini：<br/>
<a href="${pageContext.request.contextPath}/login">登录</a><br/>
<a href="${pageContext.request.contextPath}/authenticated">已身份认证</a><br/><br/>

<!-- 内置的登录（ 身份验证）实现------基于basic的 使用shiro-basicfilterlogin.ini -->
内置的登录（身份验证）实现------基于basic的 使用shiro-basicfilterlogin.ini：<br/>
<a href="${pageContext.request.contextPath}/role">角色授权</a><br/><br/>

<!-- 内置的登录（身份验证）实现------基于表单拦截器 使用shiro-formfilterlogin.ini
     当访问role没有登录的时候会先定向到formfilterlogin的登录页面，登录成功后会自动跳到
     原来请求的role请求上。
     这与上面普通的登录验证不一样，上面普通的登录验证登录成功后总会跳转到success页面，如需
     达到同样的效果，需要额外编码，比如记录原始请求登录成功后跳转等。
     具体的说明可逻辑可以查看shiro-formfilterlogin.ini中的备注
-->
内置的登录（身份验证）实现------基于表单拦截器 使用shiro-formfilterlogin.ini：<br/>
<a href="${pageContext.request.contextPath}/role">角色授权</a><br/>
<a href="${pageContext.request.contextPath}/formfilterlogin">formLogin</a><br/><br/>

授权（角色/权限验证）用zhang/123登录可访问role/permission两个请求，用wang/123访问则提示无授权
<a href="${pageContext.request.contextPath}/permission">权限授权</a><br/>

</body>
</html>