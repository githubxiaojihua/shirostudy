package com.xiaojihua.web.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 模拟FormAuthenticationFilter基于表单登录验证的filter
 * 类似于shirostudy06中的，shiro-formfilterlogin.ini中所描述的内置登录验证
 */
public class FormLoginFilter extends PathMatchingFilter {
    private String loginUrl = "/login.jsp";
    private String successUrl = "/";

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    /**
     * 返回true继续走filterchain
     * 返回false不再走filterchain
     * @param request
     * @param response
     * @param mappedValue
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = this.getSuject();
        /**
         * 判断是否已经登录成功，是则继续filterchain
         * 否则
         * 判断是否是登录请求
         *    是？
         *      是POST? 执行登录，登录成功后如果之前有保存的请求则sendRedirect到保存的请求，不再走剩下的
         *              过滤器，没有保存的请求则直接跳转到loginURL
         *      非POST? 继续执行filterchain
         *    否？
         *      保存当前请求的地址，然后重定向到loginurl，不再走剩下的过滤器
         */
        if(subject.isAuthenticated()){
            return true;
        }

        if (this.isLoginRequest(request, response)) {
            if (this.isLoginSubmission(request, response)) {
                return this.executeLogin(request, response);
            } else {
                return true;
            }
        } else {
            //保存请求并登录到loginurl
            this.saveRequestAndRedirectToLogin(request, response);
            return false;
        }
    }

    /**
     * 判断是否是loginurl请求
     * @param request
     * @param response
     * @return
     */
    protected boolean isLoginRequest(ServletRequest request, ServletResponse response) {
        //调用原来实现比对请求的地址是否是loginurl
        return this.pathsMatch(this.getLoginUrl(), request);
    }

    /**
     * 判断是否是post请求
     * @param request
     * @param response
     * @return
     */
    protected boolean isLoginSubmission(ServletRequest request, ServletResponse response) {
        return request instanceof HttpServletRequest && WebUtils.toHttp(request).getMethod().equalsIgnoreCase("POST");
    }

    /**
     * 执行登录操作
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        AuthenticationToken token = new UsernamePasswordToken(userName,password);

        try {
            Subject subject = this.getSuject();
            subject.login(token);
            //如果成功则根据是否有保存的请求进行跳转
            return this.onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException var5) {
            return false;
        }
    }

    /**
     * 调用WebUtils工具类，进行登录后的跳转
     * @param token
     * @param subject
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        //如果有保存的请求则跳转到保存的请求，没有则跳转到第三个参数指定的url
        WebUtils.redirectToSavedRequest(request, response, this.getSuccessUrl());
        return false;
    }

    /**
     * 保存请求并跳转到loginurl
     * @param request
     * @param response
     * @throws IOException
     */
    protected void saveRequestAndRedirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        WebUtils.saveRequest(request);
        /**
         * 重定向到指定的url
         * 内部使用了sendRedirect，并不是使用了
         * ((HttpServletRequest)request).getRequestDispatcher("//dsdf").forward(request,respoonse);
         * 因为filter集成了OncePerRequestFilter，forword的话不会再走filter了
         * sendRedirect使得浏览器的地址也进行改变
         */
        WebUtils.issueRedirect(request, response, loginUrl);
    }


    private Subject getSuject(){
        return SecurityUtils.getSubject();
    }
}
