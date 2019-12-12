package com.xiaojihua.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 测试密码加密以及验证
 */
public class MyRealm extends AuthorizingRealm {

    /**
     * 这里使用PasswordServivce用来加密密码的
     * 一般是在service层使用，并存储到数据库
     * 这里是为了方便所以这么用
     */
    private PasswordService passwordService;

    public void setPasswordService(PasswordService passwordService) {
        this.passwordService = passwordService;
    }


    /**
     * 暂时不实现授权认证
     * @param principalCollection
     * @return
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 返回认证信息后，进行比较验证。
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //System.out.println(passwordService.encryptPassword("123"));
        //這裡的用戶名和密碼都是模擬從數據庫中加載出來的，使用的是passWordService加密後存儲的
        return new SimpleAuthenticationInfo("wu",passwordService.encryptPassword("123"),getName());
    }
}
