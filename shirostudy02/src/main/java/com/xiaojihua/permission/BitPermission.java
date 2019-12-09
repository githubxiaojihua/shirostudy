package com.xiaojihua.permission;

import com.alibaba.druid.util.StringUtils;
import org.apache.shiro.authz.Permission;

/**
 * 自定义授权逻辑
 * 作为一个授权对象，在shiro中不管是字符串标识的授权，还是
 * Permission实例的授权，最终都对应一个Permission
 * 这里以二进制位来标识权限标识：
 *  *  规则
 *  *    +资源字符串+权限位+实例ID
 *  *
 *  *  以+开头 中间通过+分割
 *  *
 *  *  权限：
 *  *     0 表示所有权限
 *  *     1 新增 0001
 *  *     2 修改 0010
 *  *     4 删除 0100
 *  *     8 查看 1000
 *  *
 *  *  如 +user+10 表示对资源user拥有修改/查看权限
 *  *
 *  *  不考虑一些异常情况
 */
public class BitPermission implements Permission {
    private String resourceIdentify;
    private int permissionBit;
    private String instanceId;

    public BitPermission(String permissionString){
        String[] array = permissionString.split("\\+");
        if(array.length > 1){
            resourceIdentify = array[1];
        }
        if(StringUtils.isEmpty(resourceIdentify)){
            resourceIdentify = "*";
        }

        if(array.length > 2){
            permissionBit = Integer.parseInt(array[2]);
        }

        if(array.length > 3){
            instanceId = array[3];
        }
        if(StringUtils.isEmpty(instanceId)){
            instanceId = "*";
        }
    }

    /**
     * 实现接口的方法
     * @param permission
     * @return
     */
    public boolean implies(Permission permission) {
        if(!(permission instanceof BitPermission)){
            return false;
        }
        BitPermission other = (BitPermission)permission;

        if(!("*".equals(this.resourceIdentify) || this.resourceIdentify.equals(other.resourceIdentify))){
            return false;
        }

        //比对二进制权限比如10应该包括2和8。1010 包含 1000和0010
        if(!(this.permissionBit == 0 || (this.permissionBit & other.permissionBit) != 0)){
            return false;
        }

        if(!("*".equals(this.instanceId) || this.instanceId.equals(other.instanceId))){
            return false;
        }

        return true;
    }


}
