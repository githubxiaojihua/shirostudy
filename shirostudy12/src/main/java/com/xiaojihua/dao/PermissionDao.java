package com.xiaojihua.dao;

import com.xiaojihua.entity.Permission;


public interface PermissionDao {

    /**
     * 创建权限
     * @param permission
     * @return
     */
    public Permission createPermission(Permission permission);

    /**
     * 删除权限
     * @param permissionId
     */
    public void deletePermission(Long permissionId);

}
