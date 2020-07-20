package com.xiaojihua.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

public class C02AuthenticatorTest {

    /**
     * 将通用的登录逻辑提取出来
     * @param configFile
     */
    private void login(String configFile){
        //1、获取SecirityManagerFactory，通过configFile指定的ini配置文件
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(configFile);
        //2、获取SecurityManager实例并且绑定到SecurityUtils
        SecurityManager manager = factory.getInstance();
        SecurityUtils.setSecurityManager(manager);
        //3、获取Subject，并且构建token
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang","123");
        //4、登录
        subject.login(token);
    }

    /**
     * 测试AllSuccessfulStrategy，当所有realm都成功的时候，则都成功
     */
    @Test
    public void testAllSuccessfulStratgyWithSuccess(){
        this.login("classpath:shiro-authenticator-all-success.ini");
        //正是因为绑定到了当前线程所以才可以这样获取
        Subject subject = SecurityUtils.getSubject();
        //得到一个身份集合，其包含了Realm验证成功的身份信息，此处为2个一个zhang,一个zhang@163.com
        //如果MyRealm3返回的AuthenticationInfo也是zhang，123，那么principals就只有一个
        PrincipalCollection principals = subject.getPrincipals();
        System.out.println(subject.getPrincipal());
        Assert.assertEquals(2,principals.asList().size());
    }

    /**
     * 测试AllSuccessfulStrategy，当有一个realm失败的时候，则都失败
     *
     * expected代表这个test应该抛出的异常，如果没有抛出异常或者抛出的异常
     * 不是声明的则认为测试失败。否则就认为test成功
     *      * 如果去掉expected则会抛出异常并且测试失败
     */
    @Test(expected = UnknownAccountException.class)
    public void testAllSuccessfulStrategyWithFail() {

        login("classpath:shiro-authenticator-all-fail.ini");
    }

    /**
     * 测试atleastOneSuccessfulStrategy
     */
    @Test
    public void testAtLeastOneSuccessfulStrategyWithSuccess() {
        login("classpath:shiro-authenticator-atLeastOne-success.ini");
        Subject subject = SecurityUtils.getSubject();

        //得到一个身份集合，其包含了Realm验证成功的身份信息
        PrincipalCollection principalCollection = subject.getPrincipals();
        junit.framework.Assert.assertEquals(2, principalCollection.asList().size());
    }

    /**
     * 测试firstonesuccessfulstrategy
     * 只要有一个Realm验证成功即可，只返回第一个成功realm返回的认证信息，其他的忽略
     * FirstSuccessfulStrategy内部的实现实际上就是对merge方法进行的更改，如果已经
     * 有验证成功的认证信息的直接返回不再合并了。
     */
    @Test
    public void testFirstOneSuccessfulStrategyWithSuccess() {
        login("classpath:shiro-authenticator-first-success.ini");
        Subject subject = SecurityUtils.getSubject();

        //得到一个身份集合，其包含了第一个Realm验证成功的身份信息
        PrincipalCollection principalCollection = subject.getPrincipals();
        junit.framework.Assert.assertEquals(1, principalCollection.asList().size());
    }

    /**
     * 测试自定义strategy（只有一个realm验证成功，才认为成功）
     * 如果配置的是myRealm1和myRelm4由于其返回的认证身份是一样的因此验证通过
     * 如果配置的是myRealm1和myRealm3由于其返回的认证身份是2个因此验证不通过报错了
     */
    @Test
    public void testOnlyOneStrategyWithSuccess() {
        login("classpath:shiro-authenticator-onlyone-success.ini");
        Subject subject = SecurityUtils.getSubject();

        //得到一个身份集合，因为myRealm1和myRealm4返回的身份一样所以输出时只返回一个
        PrincipalCollection principalCollection = subject.getPrincipals();
        junit.framework.Assert.assertEquals(1, principalCollection.asList().size());
    }

    /**
     * 测试自定义的atlesttwo策略，由于ini中配置了
     * myrealm1,myrealm2,myrealm4
     * 其中myrealm1和myrealm4验证成功，但是由于返回的认证信息是一样的
     * 被认为是一个，因此没有满足atlisttwo策略，因此抛出了异常
     * 验证失败。
     */
    @Test
    public void testAtLeastTwoStrategyWithSuccess() {
        login("classpath:shiro-authenticator-atLeastTwo-success.ini");
        Subject subject = SecurityUtils.getSubject();

        //得到一个身份集合，因为myRealm1和myRealm4返回的身份一样所以输出时只返回一个
        PrincipalCollection principalCollection = subject.getPrincipals();
        junit.framework.Assert.assertEquals(1, principalCollection.asList().size());
    }

}
