package com.xiaojihua.web.filter;

import org.apache.shiro.web.filter.PathMatchingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Arrays;

/**
 * PathMatcheringFilter集成了AdviceFilter，提供了URL匹配功能，如果需要对特定的URL进行处理则需要集成
 * PathMatcheringFilter，我们常用的authc过滤器（FormAuthenticationFilter)就集成了它。
 *
 * 流程：调用preHandle进行url模式（ini文件中指定的/**=myFilter3中的/**)与请求的URL进行匹配，如果匹配会调用
 * onPreHandle方法，如果没有配置URL模式或者没有url模式与请求的URL进行匹配，默认直接返回true.
 *
 * 可以通过在ini文件中[config]传递过滤器参数，比如这就是传递了config字符串参数，在filter中可以使用
 * onPreHandle方法的第三个参数来接收shiro会自动传给他
 *
 *
 */
public class MyPathMatchingFilter extends PathMatchingFilter {

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        //打印传过来的过滤器参数
        System.out.println("url matches,config is " + Arrays.toString((String[])mappedValue));
        return true;
    }
}