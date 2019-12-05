package com.xiaojihua.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;

public class MyRealm3 implements Realm {

    public String getName() {
        return "MyRealm3";
    }

    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof UsernamePasswordToken;
    }

    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String userName = (String) authenticationToken.getPrincipal();
        String passWord = new String((char[])authenticationToken.getCredentials());
        if(!"zhang".equals(userName)){
            throw new UnknownAccountException();
        }
        if(!"123".equals(passWord)){
            throw new IncorrectCredentialsException();
        }

        /**
         * 注意此处返回的SimpleAuthticationInfo是zhang@163.com,123
         *
         */
        return new SimpleAuthenticationInfo(userName + "@163.com",passWord,getName());
    }
}
