package com.hanwei.config.shiro;

import com.alibaba.fastjson.JSON;
import com.hanwei.constants.HttpStatus;
import com.hanwei.vo.AjaxResult;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * 判断用户是否登录
 * @author hanwei
 * @ClassName ShiroLoginFilter
 * @date 2020/9/26
 */
public class ShiroLoginFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        AjaxResult ajaxResult= AjaxResult.fail();
        ajaxResult.put("code", HttpStatus.UNAUTHORIZED);
        ajaxResult.put("msg", "登录认证失效，请重新登录!");
        httpServletResponse.getWriter().write(JSON.toJSON(ajaxResult).toString());
        return false;
    }
}
