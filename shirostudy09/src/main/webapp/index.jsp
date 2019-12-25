<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@ page import="java.io.Serializable" %>
<%@ page import="com.xiaojihua.session.dao.MySessionDAO" %>
<%@ page import="org.apache.shiro.session.Session" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<body>
<shiro:guest>
    欢迎游客访问，<a href="${pageContext.request.contextPath}/login.jsp">点击登录</a><br/>
</shiro:guest>
<shiro:user>
    欢迎[<shiro:principal/>]登录，<a href="${pageContext.request.contextPath}/logout">点击退出</a><br/>
</shiro:user>


<shiro:user>
    <%
        SecurityUtils.getSubject().getSession().setAttribute("key", 123);
        out.print(SecurityUtils.getSubject().getSession().getAttribute("key"));
    %>
    <br/>
    <%
        MySessionDAO sessionDAO = new MySessionDAO();
        Serializable sessionId = SecurityUtils.getSubject().getSession().getId();
        Session onlineSession = sessionDAO.readSession(sessionId);
        out.print(onlineSession.getHost());
    %>
</shiro:user>

</body>
</html>
