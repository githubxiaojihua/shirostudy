package com.xiaojihua.test;

import org.junit.Assert;
import org.junit.Test;

/**
 * 授权判断
 *
 */
public class C02AuthorizerTest extends BaseTest{

    /**
     * 测试自定义授权逻辑和规则.使用自定的Myrealm
     */
    @Test
    public void testIsPermitted(){
        login("classpath:shiro-authorizer.ini", "zhang", "123");
        //判断拥有权限：user:create
        /**
         * 配置文件中的自定义realm的doGetAuthorizationInfo方法返回了subject所具有的
         * 角色和权限，并且其抽象父类的isPermitted方法通过doGetAuthorizationInfo返回
         * 的角色和权限与传入的进行匹配.
         *
         * 由BitAndWildPermissionResolver来转换成对应的Permission
         */
        Assert.assertTrue(subject().isPermitted("user1:update"));//内部转化成具体的Permission对象
        Assert.assertTrue(subject().isPermitted("user2:update"));
        //通过二进制位的方式表示权限，由BitAndWildPermissionResolver来转换成对应的Permission
        Assert.assertTrue(subject().isPermitted("+user1+2"));//新增权限
        Assert.assertTrue(subject().isPermitted("+user1+8"));//查看权限
        Assert.assertTrue(subject().isPermitted("+user2+10"));//新增及查看

        Assert.assertFalse(subject().isPermitted("+user1+4"));//没有删除权限

        //通过MyRolePermissionResolver解析得到的权限
        Assert.assertTrue(subject().isPermitted("menu:view"));

    }

    /**
     * 测试基于JDBC的自定义授权逻辑和规则，没有使用自定的Myrealm而是shiro自带的
     * JdbcRealm.
     * 但是也是根据数据库中获取的字符串权限规则调用不同的Permission，逻辑跟
     * testIsPermitted2中的转换一样
     */
    @Test
    public void testIsPermitted2() {
        login("classpath:shiro-jdbc-authorizer.ini", "zhang", "123");
        //判断拥有权限：user:create
        Assert.assertTrue(subject().isPermitted("user1:update"));
        Assert.assertTrue(subject().isPermitted("user2:update"));
        //通过二进制位的方式表示权限
        Assert.assertTrue(subject().isPermitted("+user1+2"));//新增权限
        Assert.assertTrue(subject().isPermitted("+user1+8"));//查看权限
        Assert.assertTrue(subject().isPermitted("+user2+10"));//新增及查看

        Assert.assertFalse(subject().isPermitted("+user1+4"));//没有删除权限

        Assert.assertTrue(subject().isPermitted("menu:view"));//通过MyRolePermissionResolver解析得到的权限
    }
}
