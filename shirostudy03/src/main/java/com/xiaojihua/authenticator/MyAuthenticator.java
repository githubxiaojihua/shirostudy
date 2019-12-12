package com.xiaojihua.authenticator;

import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import sun.security.krb5.Realm;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * 自定义authenticator用于测试ini配置文件的属性注入
 */
public class MyAuthenticator extends ModularRealmAuthenticator {

    public void setBytes(byte[] bytes){
        System.out.println(new String(bytes));
    }

    public void setArray(int[] ints){
        System.out.println(Arrays.toString(ints));
    }

    public void setSet(Set<Realm> set){
        System.out.println(set);
    }

    public void setMap(Map<Object,Object> map){
        System.out.println(map);
        System.out.println(map.get("1"));
    }
}
