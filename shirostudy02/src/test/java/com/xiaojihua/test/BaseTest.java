package com.xiaojihua.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;

public abstract class BaseTest {
    @After
    public void tearDown() throws Exception {
        ThreadContext.unbindSubject();//退出时请解除绑定Subject到线程 否则对下次测试造成影响
    }

    public void login(String config,String userName,String password){
        //1、获取SecurityManager工厂实例，通过inI
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(config);
        //2、获取SecurityManager实例，并且绑定到SecurityUtils
        SecurityManager manager = factory.getInstance();
        SecurityUtils.setSecurityManager(manager);
        //3、获取subject，并且组装token
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName,password);
        //4、登录
        subject.login(token);
    }

    public Subject subject(){
        return SecurityUtils.getSubject();
    }
}
