package com.xiaojihua.test;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.*;
import org.apache.shiro.crypto.hash.format.Base64Format;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * 测试编码、哈希和加解密
 */
public class C01CodeAndCryptoTest {

    //===========================编码，解码
    /**
     * base64编码解码
     */
    @Test
    public void testBase64(){
        String str = "hello";
        String base64Encode = Base64.encodeToString(str.getBytes());
        String str1 = Base64.decodeToString(base64Encode);
        Assert.assertEquals(str,str1);
    }

    /**
     * 16进制编码解码
     */
    @Test
    public void testHex(){
       String str = "hello";
       String hexEncode = Hex.encodeToString(str.getBytes());
       String str1 = new String(Hex.decode(hexEncode));
       Assert.assertEquals(str,str1);
    }


    /**
     * CodecSupport类的使用，用于byte数组和String之间进行转换
     */
    @Test
    public void codeSupport(){
        String str = "hello";
        byte[] bytes = CodecSupport.toBytes(str,"utf-8");
        String str1 = CodecSupport.toString(bytes,"utf-8");
        Assert.assertEquals(str,str1);
    }

    //================散列算法，用于生成数据摘要，不可逆，一般适用于存储密码
    /**
     * 生成随机byte数组
     * SecureRandomNumberGenerator是用于生成随机字符串或者byte[]的工具类
     * 并且可以转换成16进制或者进行Base64编码
     */
    @Test
    public void testRandom(){

        SecureRandomNumberGenerator generator = new SecureRandomNumberGenerator();
        generator.setSeed("123".getBytes());
        System.out.println(generator.nextBytes().toHex());
    }

    /**
     * md5算法
     */
    @Test
    public void testMd5(){
        String str = "hello";
        String solt = "123";
        String md5 = new Md5Hash(str,solt,1024).toString();//还可以转换为 toBase64()/toHex()
        System.out.println(md5);
    }

    /**
     * sha1算法
     */
    @Test
    public void testSha1(){
        String str = "hello";
        String salt = "123";
        String sha1 = new Sha1Hash(str,salt,1024).toString();
        System.out.println(sha1);
    }

    @Test
    public void testSha256() {
        String str = "hello";
        String salt = "123";
        String sha1 = new Sha256Hash(str, salt).toString();
        System.out.println(sha1);

    }

    @Test
    public void testSha384() {
        String str = "hello";
        String salt = "123";
        String sha1 = new Sha384Hash(str, salt).toString();
        System.out.println(sha1);

    }

    @Test
    public void testSha512() {
        String str = "hello";
        String salt = "123";
        String sha1 = new Sha512Hash(str, salt).toString();
        System.out.println(sha1);

    }

    @Test
    public void testSimpHash(){
        String str = "hello";
        String salt = "123";
        String simpleHash = new SimpleHash("SHA-1",str,salt,1024).toString();
        System.out.println(simpleHash);
    }

    /**
     * 主要用于复用，shiro源码内部使用了他
     */
    @Test
    public void testHashService(){

        //构造一个hashService
        DefaultHashService hashService = new DefaultHashService(); //默认算法SHA-512
        hashService.setHashAlgorithmName("SHA-512");
        //私盐是service内部使用的盐。公盐和私盐将哈希所用的盐分为两块来存储，目的是提高安全性
        //因为破解者要获得存储在两个不同地方的盐是比较困难的。看DefaultHashService API
        hashService.setPrivateSalt(new SimpleByteSource("123")); //私盐，默认无，不提供默认值是为了提高安全性，否则由于开源的关系将被所有人知道
        //是否生成公盐，默认false
        //公盐值得就是下面request中设置的salt，可以看源码，往往是外部设置的
        hashService.setGeneratePublicSalt(true);
        hashService.setRandomNumberGenerator(new SecureRandomNumberGenerator());//用于生成公盐。默认就这个
        hashService.setHashIterations(1); //生成Hash值的迭代次数

        //构造一个request
        HashRequest request = new HashRequest.Builder()
                .setAlgorithmName("MD5").setSource(ByteSource.Util.bytes("hello"))
                .setSalt(ByteSource.Util.bytes("123")).setIterations(2).build();

        //哈希，返回一個Hash實例，並且調用它的格式化方法
        String hex = hashService.computeHash(request).toHex();
        System.out.println(hex);

    }

    /**
     * 测试HashFormat的使用
     * HashFormat就是将Hash实例（比如Md5Hash），哈希后的byte[]数组
     * 按照指定的格式输出出来，本质上与直接调用Md5Hash.toBase64或者toHex()一样
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testHashFormat() throws UnsupportedEncodingException {
        String str = "hello";
        String solt = "123";
        Md5Hash md5Hash = new Md5Hash(str);//还可以转换为 toBase64()/toHex()
        String zy = md5Hash.toBase64();
        System.out.println(zy);

        Base64Format format = new Base64Format();//HexFormat
        String format1 = format.format(md5Hash);
        System.out.println(format1);
    }
}
