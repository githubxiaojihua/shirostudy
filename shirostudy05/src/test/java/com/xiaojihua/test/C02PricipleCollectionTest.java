package com.xiaojihua.test;

import com.xiaojihua.entity.User;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Set;

/**
 * 测试PrincipalColletion的相关API
 */
public class C02PricipleCollectionTest extends BaseTest{

    @Test
    public void test(){
        //因为Realm里没有进行验证，所以相当于每个Realm都身份验证成功了
        login("classpath:shiro-multirealm.ini","zhang","123");
        //获取subject
        Subject subject = subject();

        //相关API调用
        //1、获取第一个Principial
        Object principal1 = subject.getPrincipal();//从底层的map随机返回一个
        PrincipalCollection principals = subject.getPrincipals();
        Object principal2 = principals.getPrimaryPrincipal();//从底层的map随机返回一个
        //但是因为多个Realm都返回了Principal，所以此处到底是哪个是不确定的
        Assert.assertEquals(principal1,principal2);
        //2、获取验证成功的realm名称
        Set<String> realmNames = principals.getRealmNames();
        System.out.println(realmNames);

        //3、返回认证成功的principal由于MyRealm1和MyRealm2返回的一样因此排重了。
        Set<Object> set = principals.asSet(); //asList和asSet的结果一样
        System.out.println(set);

        //4、根据realm的名字获取pricipal
        Collection<User> users = principals.fromRealm("c");
        System.out.println(users);

    }
}
