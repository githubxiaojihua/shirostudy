package com.xiaojihua.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="loginServlet",urlPatterns="/login")
public class LoginServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request,response);
    }

    protected  void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
        //1、获取前台提交的用户名和密码
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //2、获取subject，组装token
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        //3、登录验证
        String error = null;
        try{
            subject.login(token);
        }catch(UnknownAccountException e){
            error = "用户名或者密码错误！";
        }catch(IncorrectCredentialsException e){
            error = "用户名或者密码错误！";
        }catch(AuthenticationException e){
            error = "其他错误！";
        }

        if(error != null){
            //5、失败跳转，提示错误信息
            request.setAttribute("error",error);
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request,response);
        }else{
            //4、成功跳转
            request.getRequestDispatcher("/WEB-INF/jsp/loginSuccess.jsp").forward(request,response);
        }



    }
}
