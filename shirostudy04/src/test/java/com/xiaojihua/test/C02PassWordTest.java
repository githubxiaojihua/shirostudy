package com.xiaojihua.test;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.converters.AbstractConverter;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.junit.Test;

public class C02PassWordTest extends BaseTest{

    /**
     * 基本逻辑：
     * 这里提交的username和password会通过realm组装成token，
     * 然后realm会返回SimpleAuthenticationInfo，里面的密码是
     * 模拟从数据库中取出来的加过密的密码。
     * 然后进行比对，PasswordService內部将token中的密码按照指定的hashService进行加密
     * 最后进行比对
     * 因此service层的加密密碼和shiro中配置的加密类一定要一直，这里都是用的是
     * PasswordService(内部使用了hashService）
     *
     * 这种方式通过INI设置了新的DefaultHashService，所以不带盐（公盐和私盐）
     * 只是单纯的使用算法将密码哈希
     * 可以参考相关顺序图
     */
    @Test
    public void testPasswordServiceByMyRealm(){
        login("classpath:shiro-passwordservice.ini", "wu", "123");
    }


    /**
     * 基于JDBC的简单加密密码认证，需要先使用SQL初始化数据库
     */
    @Test
    public void testPasswordServiceByJDBCRealm(){
        login("classpath:shiro-jdbc-passwordservice.ini","wu","123");
    }


    /**
     * 使用相关算法加密password
     * 此处使用两个salt，主要是增加安全性。
     * 两个salt可以存储到不同的地方，对于破解来说要破解就要获取
     * 两个salt难度较大
     */
    @Test
    public void testGeneratePassword(){
        String algorithmName = "md5";//加密算法
        String username = "liu";
        String password = "123";
        String salt1 = username;
        String salt2 = new SecureRandomNumberGenerator().nextBytes().toHex();
        int hashIterations = 2;

        SimpleHash hash = new SimpleHash(algorithmName,password,salt1 + salt2,hashIterations);
        String encodedPassword = hash.toHex();
        System.out.println(salt2);
        System.out.println(encodedPassword);
    }


    /**
     * 通过使用defaultPassword和HashedCredentialsMatcher组合，采用加盐的方式
     * 进行哈希，盐由自己制定。
     * 另外需要注意的是这种方式需要手写加密算法用于存储用户的密码，加密算法参考testGeneratePassword就行，
     * 但是在配置INI的HashedCredentialsMatcher的时候要注意与自己制定的算法相匹配
     * 比如都采用MD5算法，都哈希2次，并且salt要一致。
     *
     * 可参考相关顺序图，在教程里面
     */
    @Test
    public void testHashedCredentialsMatcherWithMyRealm2() {
        //使用testGeneratePassword生成的散列密码
        login("classpath:shiro-hashedCredentialsMatcher.ini", "liu", "123");
    }

    /**
     * 测试使用JdbcRealm来验证密码登录，数据库中存储了加密token中密码的salt,
     * 经过加密的token中的密码与数据库中已加密的密码进行匹配
     */
    @Test
    public void testHashedCredentialsMatcherWithJdbcRealm() {
        //必须注册一个Enum转换器，用于转换INI文件中的jdbcRealm.saltStyle=COLUMN属性设置
        //由于COLUMN是Enum类型，Shiro默认使用了apache commons BeanUtils，但是BeanUtils默认不支持Enum类型转换。
        BeanUtilsBean.getInstance().getConvertUtils().register(new EnumConverter(), JdbcRealm.SaltStyle.class);
        //使用testGeneratePassword生成的散列密码
        login("classpath:shiro-jdbc-hashedCredentialsMatcher.ini", "liu", "123");
    }

    /**
     * 自定义Enum类型转换
     */
    private class EnumConverter extends AbstractConverter {
        @Override
        protected String convertToString(final Object value) throws Throwable {
            return ((Enum) value).name();
        }
        @Override
        protected Object convertToType(final Class type, final Object value) throws Throwable {
            return Enum.valueOf(type, value.toString());
        }

        @Override
        protected Class getDefaultType() {
            return null;
        }

    }

    /**
     * 测试登录失败超过5次就抛出异常
     * 通过自定义的CredentialsMatcher来实现，通过Ehcache来记录失败次数
     */
    @Test(expected = ExcessiveAttemptsException.class)
    public void testRetryLimitHashedCredentialsMatcherWithMyRealm() {
        for(int i = 1; i <= 5; i++) {
            try {
                login("classpath:shiro-retryLimitHashedCredentialsMatcher.ini", "liu", "234");
            } catch (Exception e) {/*ignore*/}
        }
        login("classpath:shiro-retryLimitHashedCredentialsMatcher.ini", "liu", "234");
    }
}
