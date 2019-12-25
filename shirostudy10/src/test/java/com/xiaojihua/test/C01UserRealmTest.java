package com.xiaojihua.test;

import com.xiaojihua.realm.UserRealm;
import junit.framework.Assert;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.junit.Test;

public class C01UserRealmTest extends BaseTest{

    @Override
    public void tearDown() throws Exception {
        userService.changePassword(u1.getId(), password);
        RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        UserRealm userRealm = (UserRealm) securityManager.getRealms().iterator().next();
        userRealm.clearCachedAuthenticationInfo(subject().getPrincipals());

        super.tearDown();
    }

    /**
     * 首先登录成功，此时会缓存AuthenticationInfo，然后修改密码，
     * 此时密码就改变了，然后调用Realm的clearCachedAuthenticationInfo方法
     * 清空之前缓存的AuthenticationInfo。否则下次登录的时候还是会获取到之前
     * 缓存的AuthenticationInfo
     */
    @Test
    public void testClearCachedAuthenticationInfo() {

        login(u1.getUsername(), password);
        userService.changePassword(u1.getId(), password + "2");

        RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        UserRealm userRealm = (UserRealm) securityManager.getRealms().iterator().next();
        userRealm.clearCachedAuthenticationInfo(subject().getPrincipals());

        login(u1.getUsername(), password + "2");


    }


    /**
     * 清空授权信息
     */
    @Test
    public void testClearCachedAuthorizationInfo() {
        login(u1.getUsername(), password);
        subject().checkRole(r1.getRole());
        userService.correlationRoles(u1.getId(), r2.getId());

        RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        UserRealm userRealm = (UserRealm) securityManager.getRealms().iterator().next();
        userRealm.clearCachedAuthorizationInfo(subject().getPrincipals());

        subject().checkRole(r2.getRole());
    }

    /**
     * 组合调用清空
     */
    @Test
    public void testClearCache() {
        login(u1.getUsername(), password);
        subject().checkRole(r1.getRole());

        userService.changePassword(u1.getId(), password + "1");
        userService.correlationRoles(u1.getId(), r2.getId());

        RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        UserRealm userRealm = (UserRealm) securityManager.getRealms().iterator().next();
        userRealm.clearCache(subject().getPrincipals());

        login(u1.getUsername(), password + "1");
        subject().checkRole(r2.getRole());
    }

}
