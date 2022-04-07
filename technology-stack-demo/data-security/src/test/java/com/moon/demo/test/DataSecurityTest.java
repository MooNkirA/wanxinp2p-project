package com.moon.demo.test;

import com.moon.demo.RSAEncrypt;
import com.moon.demo.SHA256;
import org.junit.Test;

import java.util.Map;

/**
 * 数据加解密测试
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-21 15:44
 * @description
 */
public class DataSecurityTest {

    /**
     * 测试RSA加解密
     *
     * @throws Exception
     */
    @Test
    public void testRSAEncrypt() throws Exception {
        Map<Integer, String> keyMap = RSAEncrypt.genKeyPair();
        String content = "天锁斩月";
        System.out.println("随机生成的公钥为:" + keyMap.get(0));
        System.out.println("随机生成的私钥为:" + keyMap.get(1));
        String messageEn = RSAEncrypt.encrypt(content, keyMap.get(0));
        System.out.println("加密后的字符串为:" + messageEn);
        String messageDe = RSAEncrypt.decrypt(messageEn, keyMap.get(1));
        System.out.println("解密后的字符串为:" + messageDe);
    }

    /**
     * 测试SHA256摘要算法
     */
    @Test
    public void testSHA256() {
        String content = "死亡笔记";
        System.out.println(content + ": 第一次摘要后的字符串为:" + SHA256.getSHA256(content));
        content = "言叶之庭"; // 模拟数据被窜改，两次加密后结果不一样的
        System.out.println(content + ": 第二次摘要后的字符串为:" + SHA256.getSHA256(content));
    }

    /**
     * 测试签名、验证签名
     */
    @Test
    public void testSHA1WithRSA() throws Exception {
        Map<Integer, String> akeyMap = RSAEncrypt.genKeyPair(); // a方密钥对
        System.out.println("随机生成的a方公钥为:" + akeyMap.get(0));
        System.out.println("随机生成的a方私钥为:" + akeyMap.get(1));

        String content = "金田一少年之事件簿";

        System.out.println("------------ a向b发送数据，使用a的私钥生成签名 -----------");
        String signature = RSAEncrypt.sign(content, akeyMap.get(1), "utf-8");
        System.out.println("原文:'" + content + "'生成签名：" + signature);

        System.out.println("---------- b接收到a发的数据，使用a的公钥验证签名 -----------");
        if (RSAEncrypt.verify(content, signature, akeyMap.get(0), "utf-8")) {
            System.out.println("验证签名成功：" + signature);
        } else {
            System.out.println("验证签名失败！");
        }
    }

}
