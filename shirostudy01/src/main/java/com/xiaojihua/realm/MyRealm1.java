package com.xiaojihua.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;

public class MyRealm1 implements Realm {
    public String getName() {
        return "MyRealm1";
    }

    /**
     * 判断是否支持传入的token类型，如果返回false则直接忽略本realm
     * @param authenticationToken
     * @return
     */
    public boolean supports(AuthenticationToken authenticationToken) {
        //仅支持UsernamePassword类型的token
        return authenticationToken instanceof UsernamePasswordToken;
    }

    /**
     * 自定义认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
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
        return new SimpleAuthenticationInfo(userName,passWord,getName());
    }
}
