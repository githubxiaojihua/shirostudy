package com.xiaojihua.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "permissionServlet", urlPatterns = "/permission")
public class PermissionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Subject subject = SecurityUtils.getSubject();
        //虽然是check但是由于在ini配置文件中配置了unauthorizedUrl，因此会跳转到对应的URL中，并非会直接报错
        subject.checkPermission("user:create");
        req.getRequestDispatcher("/WEB-INF/jsp/hasPermission.jsp").forward(req, resp);
    }
}
