package com.hanwei.util;

import com.hanwei.constants.Constants;
import com.hanwei.domain.ActiveUser;
import com.hanwei.domain.SimpleUser;
import com.hanwei.domain.User;
import org.apache.shiro.SecurityUtils;
import java.util.List;

/**
 * @author hanwei
 * @ClassName ShiroSecurityUtils
 * @date 2020/9/27
 */
public class ShiroSecurityUtils {

    /**
     * 获得当前ActiveUser
     */
    public static ActiveUser getCurrentActiveUser() {
        return (ActiveUser) SecurityUtils.getSubject().getPrincipal();
    }

    /**
     * 获取当前User
     */
    public static User getCurrentUser() {
        return getCurrentActiveUser().getUser();
    }

    /**
     * 获取当前SimpleUser
     */
    public static SimpleUser getCurrentSimpleUser() {
        User user = getCurrentUser();
        return new SimpleUser(user.getUserId(), user.getUserName());
    }

    /**
     * 得到当前登陆的用户名称
     */
    public static String getCurrentUserName(){
        return getCurrentActiveUser().getUser().getUserName();
    }

    /***
     * 得到当前登陆对象的角色编码
     */
    public static List<String> getCurrentUserRoles(){
        return getCurrentActiveUser().getRoles();
    }


    /**
     *  得到当前登陆对象的权限编码
     */
    public static List<String> getCurrentUserPermissions(){
        return getCurrentActiveUser().getPermissions();
    }

    /**
     *  判断当前用户是否是Admin
     */
    public static boolean isAdmin(){
        return getCurrentUser().getUserType().equals(Constants.USER_ADMIN);
    }

}
