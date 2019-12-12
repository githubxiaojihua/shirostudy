package com.xiaojihua.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

/**
 * 测试基于ini配置文件的Shiro
 * 与C01纯java代码对应
 */
public class C02ConfigurationCreateTest {

    @Test
    public void test(){
        //1、创建SecurityManager工厂实例，基于ini配置文件
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-config.ini");
        //2、创建SecurityManager并进行全局绑定
        SecurityManager manager = factory.getInstance();
        SecurityUtils.setSecurityManager(manager);
        //3、创建subject，构建token
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang","123");
        //4、登录认证
        subject.login(token);
        Assert.assertTrue(subject.isAuthenticated());
    }
}
