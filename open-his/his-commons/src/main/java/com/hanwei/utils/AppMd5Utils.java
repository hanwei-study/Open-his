package com.hanwei.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

import java.util.UUID;

/**
 * @author hanwei
 * @ClassName AppMd5Utils
 * @date 2020/11/15
 */
public class AppMd5Utils {

    /**
     * 生成盐
     */
    public static String createSalt() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    /**
     * 生成加密字符串
     */
    public static String md5(String source, String salt, Integer hashIterations) {
        return new Md5Hash(source, salt, hashIterations).toString();
    }
}
