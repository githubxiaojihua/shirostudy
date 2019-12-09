package com.xiaojihua.test;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

import javax.naming.AuthenticationException;
import java.util.Arrays;

public class C01RoleTest extends BaseTest{

    /**
     * 角色判断：测试是否具有一个或者多个role
     */
    @Test
    public void testHasRole(){
        login("classpath:shiro-role.ini","zhang","123");
        //判断拥有角色role1
        Assert.assertTrue(subject().hasRole("role1"));
        //判断拥有角色role1 and role2
        Assert.assertTrue(subject().hasAllRoles(Arrays.asList("role1","role2")));
        //判断拥有角色：role1 and role2 and !role3
        boolean[] results = subject().hasRoles(Arrays.asList("role1", "role2", "role3"));
        Assert.assertEquals(true,results[0]);
        Assert.assertEquals(true,results[1]);
        Assert.assertEquals(false,results[2]);
    }

    /**
     * 角色判断：使用断言，判断是否具有相应角色，失败抛出异常
     */
    @Test(expected=UnauthorizedException.class)
    public void testCheckRole(){
        login("classpath:shiro-role.ini","zhang","123");
        //断言拥有角色：role1
        subject().checkRole("role1");
        //断言拥有角色：role1 and role3 失败抛出异常
        subject().checkRoles("roles1,roles3");
    }

    /**
     * 权限判断：判断是否具有相应权限
     */
    @Test
    public void testPermissions(){
        login("classpath:shiro-permission.ini","zhang","123");
        //判断拥有权限：user:create
        Assert.assertTrue(subject().isPermitted("user:create"));
        //判断拥有权限：user:update and user:delete
        Assert.assertTrue(subject().isPermittedAll("user:delete","user:update"));
        //判断没有权限：user:view
        Assert.assertFalse(subject().isPermitted("user:view"));

    }

    @Test(expected=UnauthorizedException.class)
    public void testCheckPermissions(){
        login("classpath:shiro-permission.ini","zhang","123");
        //判断拥有权限：user:create
        subject().checkPermission("user:create");
        //断言拥有权限：user:delete and user:update
        subject().checkPermissions("user:delete","user:update");
        //断言拥有权限：user:view 失败抛出异常
        subject().checkPermissions("user:view");
    }

}
