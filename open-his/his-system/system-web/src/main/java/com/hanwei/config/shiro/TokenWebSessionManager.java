package com.hanwei.config.shiro;

import com.hanwei.constants.Constants;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.UUID;

/** 生成token
 *  如果没有token生成一个返回到前台，
 *  如果有就从请求头里面取出来
 * @author hanwei
 * @ClassName TokenWebSessionManager
 * @date 2020/9/26
 */
public class TokenWebSessionManager extends DefaultWebSessionManager {

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String header = WebUtils.toHttp(request).getHeader(Constants.TOKEN);
        if(StringUtils.hasText(header)){
            return header;
        }
        return  UUID.randomUUID().toString();
    }
}
