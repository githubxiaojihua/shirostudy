package com.xiaojihua.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

public class C01LoginLogoutTest {

    /**
     * 基于shiro默认的realm进行登录和登出验证
     * 在ini文件中写死了usernme和password
     */
    @Test
    public void testLoginAndLogout(){
        //1、获取SecurityManager工厂，此处使用Ini配置文件配置
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        //2、得到SecurityManager实例并且绑定到SecurityUtils
        SecurityManager manager = factory.getInstance();
        SecurityUtils.setSecurityManager(manager);
        //3、得到subject以及组装token
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang","123");
        //4、登录
        try{
            subject.login(token);
        }catch(AuthenticationException e){
            //身份验证失败
        }
        Assert.assertEquals(true,subject.isAuthenticated());
        //5、登出
        subject.logout();
    }

    /**
     * 使用自定义的realm进行身份验证的逻辑
     * 加载了另外的ini，
     * 在ini中只是配置了realm并没有设置用户名和密码
     * 而是在realm中写死了用户名和密码来模拟从数据库中查询出来的
     */
    @Test
    public void testCustomerRealm(){
        //1、获取SecurityManager工厂，此处使用ini配置文件
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-customerRealm.ini");
        //2、获取SecurityManager实例并且绑定到SecurityUtils
        SecurityManager securi = factory.getInstance();
        SecurityUtils.setSecurityManager(securi);
        //3、获取subject，并且组装token
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang","12223");
        //4、登录
        try{
            //最终会调用指定的realm的getAuthenticationInfo方法
            subject.login(token);
        }catch(AuthenticationException e){
            //身份验证失败
            e.printStackTrace();
        }
        Assert.assertEquals(true,subject.isAuthenticated());
        //5、登出
        subject.logout();
    }


    /**
     * 测试多realm的配置，按照ini中声明的real顺序执行
     * 当有多个realm的时候Authticator会委托AuthenticationStrategy
     * 进行身份验证，默认的strategy是找到第一个成功的realm则认证通过
     * 如果没有认证成功的则认为是没有认证通过。
     * 当前例子中shiro-muti-customrealm.ini中配置了两个realm
     * 其中一个的getAuthenticationInfo方法要求用户名为zhang，密码为123
     * 另一个getAuthenticationInfo方法要求用户名为wang，密码为123
     * 下面的验证会按照声明的顺序调用getAuthenticationInfo方法，其中第一个
     * 验证失败，第二个验证成功，按照默认的strategy则任务是验证成功了
     */
    @Test
    public void testCustomerMutiRealm(){
        //1、获取SecrurityManager工厂，此处使用ini配置文件
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-mutil-customrealm.ini");
        //2、获取SecurityManager实例，并且绑定到SecurityUtils
        SecurityManager manager = factory.getInstance();
        SecurityUtils.setSecurityManager(manager);
        //3、获取subject，并且组装token
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("wang","123");
        //4、登录
        try{
            subject.login(token);
        }catch(AuthenticationException e){
            e.printStackTrace();
        }

        Assert.assertEquals(true,subject.isAuthenticated());
        //5、登出
        subject.logout();
    }

    /**
     * 测试JDBCrealm从数据库读取数据进行登录验证
     */
    @Test
    public void testJDBCRealm(){
        //1、获取SecurityFactory，这里使用ini配置文件
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-jdbc-realm.ini");
        //2、获取SecurityFactory实例，并且绑定到SecurityUtils
        SecurityManager manager = factory.getInstance();
        SecurityUtils.setSecurityManager(manager);
        //3、获取Subject，并且组装token
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang","123");
        //4、登录
        try{
            subject.login(token);
        }catch(AuthenticationException e){
            e.printStackTrace();;
        }
        Assert.assertEquals(true,subject.isAuthenticated());
        //5、登出
        subject.logout();
    }
}
