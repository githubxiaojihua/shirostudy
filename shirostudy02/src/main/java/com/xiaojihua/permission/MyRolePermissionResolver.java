package com.xiaojihua.permission;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

import java.util.Arrays;
import java.util.Collection;

/**
 * 自定义RolePermissionResolver
 * 用于role和对应的permission进行对应
 * 注册了rolePermissionResolver那么其转换的permission也会聚合到subject的permission列表中
 */
public class MyRolePermissionResolver implements RolePermissionResolver {

    public Collection<Permission> resolvePermissionsInRole(String roleString) {
        /**
         * 如果用户拥有role1，那么就返回一个“menu:*”的权限
         * 并且会聚合到subject的permission列表中
         */
        if("role1".equals(roleString)){
            return Arrays.asList((Permission) new WildcardPermission("menu:*"));
        }
        return null;
    }
}
