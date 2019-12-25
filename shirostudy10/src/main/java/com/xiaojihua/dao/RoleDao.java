package com.xiaojihua.dao;

import com.xiaojihua.entity.Role;


public interface RoleDao {

    /**
     * 创建角色
     * @param role
     * @return
     */
    public Role createRole(Role role);
    public void deleteRole(Long roleId);

    public void correlationPermissions(Long roleId, Long... permissionIds);
    public void uncorrelationPermissions(Long roleId, Long... permissionIds);

}
