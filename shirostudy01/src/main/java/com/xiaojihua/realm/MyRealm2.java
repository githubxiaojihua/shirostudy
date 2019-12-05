package com.xiaojihua.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;

public class MyRealm2 implements Realm {
    public String getName() {
        return "MyRealm2";
    }

    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof UsernamePasswordToken;
    }

    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String userName = (String)authenticationToken.getPrincipal();
        String passWord = new String((char[])authenticationToken.getCredentials());
        System.out.println("myrealm2进来了");
        if(!"wang".equals(userName)){
            throw new UnknownAccountException();
        }

        if(!"123".equals(passWord)){
            throw new IncorrectCredentialsException();
        }
        return new SimpleAuthenticationInfo(userName,passWord,getName());
    }
}
