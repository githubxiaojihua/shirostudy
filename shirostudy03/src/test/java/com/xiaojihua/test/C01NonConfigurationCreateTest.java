package com.xiaojihua.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * 测试不使用任何配置文件来创建SecurityManager，并且使用shiro.
 * 纯java代码实现
 *
 */
public class C01NonConfigurationCreateTest {

    @Test
    public void test(){
        //1、创建SecurityManager
        DefaultSecurityManager manager = new DefaultSecurityManager();
        //2、设置authenticator
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        manager.setAuthenticator(authenticator);
        //3、设置authorizer
        ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
        authorizer.setPermissionResolver(new WildcardPermissionResolver());
        manager.setAuthorizer(authorizer);

        //4、设置Realm
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/shiro");
        ds.setUsername("root");
        ds.setPassword("root");

        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(ds);
        jdbcRealm.setPermissionsLookupEnabled(true);

        manager.setRealms(Arrays.asList((Realm) jdbcRealm));

        //5、将securityManager设置到SecurityUtils全局使用
        SecurityUtils.setSecurityManager(manager);

        //6、使用
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang","123");
        subject.login(token);
        Assert.assertTrue(subject.isAuthenticated());
    }
}
