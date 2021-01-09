package com.hanwei.config.shiro;

import com.hanwei.domain.ActiveUser;
import com.hanwei.domain.User;
import com.hanwei.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

/**
 * @author hanwei
 * @ClassName UserRealm
 * @date 2020/9/26
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private UserService userService;

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 登录
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String phone = token.getPrincipal().toString();
        User user = userService.queryUserByPhone(phone);
        if(null != user) {
            ActiveUser activeUser = new ActiveUser();
            activeUser.setUser(user);
            //匹配密码
            return new SimpleAuthenticationInfo(
                    activeUser,
                    activeUser.getUser().getPassword(),
                    ByteSource.Util.bytes(user.getSalt()),
                    this.getName());
        } else {
            return null;
        }
    }

    /**
     * 权限挂你
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //身份得到的就是上一个方法的返回值的值 第一个参数activerUser
        ActiveUser activeUser= (ActiveUser) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        return info;

    }




}
