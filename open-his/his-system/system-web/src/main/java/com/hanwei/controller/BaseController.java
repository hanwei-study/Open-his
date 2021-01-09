package com.hanwei.controller;

import com.hanwei.vo.AjaxResult;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;

/**
 * @author hanwei
 * @ClassName BaseController
 * @date 2020/11/16
 */
@DefaultProperties(defaultFallback = "fallback") // 配置默认的失败回调方法
public class BaseController {
    /**
     * 如远程服务不可用，或者出现异常，回调的方法
     * @return
     */
    public AjaxResult fallback(){
        return AjaxResult.fail("服务器内部异常，请联系管理员");
    }
}
