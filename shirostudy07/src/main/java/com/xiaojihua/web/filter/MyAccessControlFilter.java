package com.xiaojihua.web.filter;

import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * AccessControlFilter集成了PathMatchingFilter
 * 扩展了如下两个方法
 * public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
 *         return this.isAccessAllowed(request, response, mappedValue) || this.onAccessDenied(request, response, mappedValue);
 *     }
 */
public class MyAccessControlFilter extends AccessControlFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        System.out.println("access allowed");
        return true;
    }
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        System.out.println("访问拒绝也不自己处理，继续拦截器链的执行");
        return true;
    }
}
