package com.xiaojihua.test;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;

public class C01SessionTest extends BaseTest {

    @Test
    public void testGetSession(){
        login("classpath:C01shiro.ini","zhang","123");
        Subject subject = getSubject();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //获取session
        Session session = subject.getSession();
        //获取sessionID
        System.out.println(session.getId());
        //获取当前登录用户的主机地址
        System.out.println(session.getHost());
        //获取session超时时间
        System.out.println(session.getTimeout());
        //获取session创建时间
        System.out.println(format.format(session.getStartTimestamp()));
        //获取session最后访问时间
        System.out.println(session.getLastAccessTime());

        //会话属性操作
        session.setAttribute("key","123");
        Assert.assertEquals("123",session.getAttribute("key"));
        session.removeAttribute("key");
    }
}
