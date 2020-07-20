package com.xiaojihua.realm;

import com.xiaojihua.permission.BitPermission;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 从AuthorizingRealm继承
 * AuthorizingRealm扩展了AuthenticatingRealm，因此可同时用于认证和授权.
 * 这种方式的好处是当只需要身份验证时只需要获取身份验证信息而不需要获取授权信息。
 *
 * AuthorizingRealm内部已经实现了根据doGetAuthorizationInfo和doGetAuthenticationInfo
 * 返回的权限角色信息和认证信息进行授权和认证是否通过的逻辑。
 * 比如其内部实现了isPermitted()、hasRole（）方法用于通过AuthorizationInfo来判断是否允许授权的
 * 逻辑。
 * 在这里，我们只需要向shiro提供subject所具有的所有角色和授权，以及如果认证通过的话返回AuthenticationInfo
 *
 */
public class MyRealm extends AuthorizingRealm {

    /**
     * 根据用户身份信息获取授权信息，这里是返回用户的所具有的所有权限，用于对用户权限和角色进行判断。
     * 如果注册了rolePermissionResolver那么其转换的permission也会聚合到subject的permission列表中。
     * 并不对是否用户权限做判断，那是shiro做的工作（在AuthorizingRealm的相应方法
     * 如isPermitted()、hasRole（）等方法中实现，抽象类已经都实现好了）。
     * 其实doGetAuthorizationInfo和doGetAuthenticationInfo都是获取用户
     * 相对应的信息
     * @param principalCollection
     * @return
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //增加subject具有的角色
        authorizationInfo.addRole("role1");
        authorizationInfo.addRole("role2");
        //增加具有的权限----对象权限
        authorizationInfo.addObjectPermission(new BitPermission("+user1+10"));
        authorizationInfo.addObjectPermission(new WildcardPermission("user1:*"));
        //增加具有的权限----字符串权限，将自动调用注册的permissionResolver
        authorizationInfo.addStringPermission("+user2+10");
        authorizationInfo.addStringPermission("user2:*");
        return authorizationInfo;
    }

    /**
     * 自定义认证与shirostudy01中的MyRealm1一样
     * 获取身份验证信息，只有验证成功后才返回AuthorizationInfo
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获得token传过来的userName和passWord
        String userName = (String) authenticationToken.getPrincipal();
        /**
         * 注意，getCridentials方法返回的是Object，但是从源码中可以知道其底层是char[]
         * 要转换成字符串不能使用toStirng方法，而是使用new String（char[]）构造器
         */
        String passWord = new String((char[])authenticationToken.getCredentials());
        System.out.println(authenticationToken.getCredentials().toString());

        /**
         * 这里的zhang和123是模拟的数据库中的用户名和密码
         * 这里进行比较
         */
        if(!"zhang".equals(userName)){
            //抛出用户名异常
            throw new UnknownAccountException();
        }
        if(!"123".equals(passWord)){
            //抛出密码异常
            throw new IncorrectCredentialsException();
        }
        //认证通过返回一个AuthenticationInfo实现
        //返回SimpleAuthenticationInfo供AuthorizingRealm的getAuthenticationInfo方法使用
        //实际上直接使用下面这一句就行了，因为doGetAuthenticationInfo方法的意思是根据principal
        //获得数据库中的认证信息，供shiro对前台传入的和其进行比较
        //return new SimpleAuthenticationInfo("zhang","123",getName());
        return new SimpleAuthenticationInfo(userName,passWord,getName());
    }
}
