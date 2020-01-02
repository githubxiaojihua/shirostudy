package com.xiaojihua.service;

import com.xiaojihua.dao.PermissionDao;
import com.xiaojihua.entity.Permission;


public class PermissionServiceImpl implements PermissionService {

    //依赖注入
    private PermissionDao permissionDao;

    public void setPermissionDao(PermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }

    public Permission createPermission(Permission permission) {
        return permissionDao.createPermission(permission);
    }

    public void deletePermission(Long permissionId) {
        permissionDao.deletePermission(permissionId);
    }
}
