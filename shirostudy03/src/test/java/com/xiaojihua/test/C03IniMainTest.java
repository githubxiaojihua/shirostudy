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
 * 测试ini配置文件的属性注入
 */
public class C03IniMainTest {

    @Test
    public void test(){
        //1、实例化securityManager工厂，通过配置文件
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-config-main.ini");
        //2、获取securityManager，并且绑定securityUtils
        SecurityManager manager = factory.getInstance();
        SecurityUtils.setSecurityManager(manager);
        //3、获取subject,组装token
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang","123");
        //4、登录认证测试
        subject.login(token);
        Assert.assertTrue(subject.isAuthenticated());
    }
}
