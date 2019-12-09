package com.xiaojihua.permission;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

/**
 * permissionResolver用于将字符串标识的权限转换成Permission实例
 */
public class BitAndWildPermissionResolver implements PermissionResolver {

    /**
     * 如果以+开头则转换成BitPermission
     * 否则走默认的WildcardPermission
     * @param permissionStr
     * @return
     */
    public Permission resolvePermission(String permissionStr) {
        if(permissionStr.startsWith("+")){
            return new BitPermission(permissionStr);
        }
        return new WildcardPermission(permissionStr);
    }
}
