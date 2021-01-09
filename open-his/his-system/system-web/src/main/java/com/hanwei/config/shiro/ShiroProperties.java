package com.hanwei.config.shiro;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author hanwei
 * @ClassName ShiroProperties
 * @date 2020/9/26
 */
@Data
@ConfigurationProperties(prefix = "shiro")
public class ShiroProperties {
    /**
     * 密码加密方式
     */
    private String hashAlgorithmName="md5";
    /**
     * 密码散列次数
     */
    private Integer hashIterations=2;

    /**
     * 不拦击的路径
     */
    private String [] anonUrls;

    /**
     * 拦截的路径
     */
    private String [] authcUrls;

}
