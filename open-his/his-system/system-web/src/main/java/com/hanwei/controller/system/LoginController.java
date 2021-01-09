package com.hanwei.controller.system;

import cn.hutool.core.date.DateUtil;
import com.hanwei.aspectj.annotation.Log;
import com.hanwei.aspectj.enums.BusinessType;
import com.hanwei.constants.Constants;
import com.hanwei.constants.HttpStatus;
import com.hanwei.domain.ActiveUser;
import com.hanwei.domain.LoginInfo;
import com.hanwei.domain.Menu;
import com.hanwei.domain.SimpleUser;
import com.hanwei.dto.LoginBodyDto;
import com.hanwei.service.LoginInfoService;
import com.hanwei.service.MenuService;
import com.hanwei.util.ShiroSecurityUtils;
import com.hanwei.utils.AddressUtils;
import com.hanwei.utils.IpUtils;
import com.hanwei.vo.AjaxResult;
import com.hanwei.vo.MenuTreeVo;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hanwei
 * @ClassName LoginController
 * @date 2020/9/26
 */
@RestController
@Slf4j
@RequestMapping("login")
public class LoginController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private LoginInfoService loginInfoService;
    /**
     * 登录方法
     */
    @PostMapping("doLogin")
    public AjaxResult doLogin(@RequestBody @Validated LoginBodyDto loginBodyDto, HttpServletRequest request) {
        AjaxResult ajax = AjaxResult.success();
        String username = loginBodyDto.getUsername();
        String password = loginBodyDto.getPassword();
        //构造用户名和密码的token
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        //封装用户的登陆信息
        LoginInfo loginInfo = createLoginInfo(request);
        loginInfo.setLoginAccount(loginBodyDto.getUsername());
        try {
            subject.login(token);
            //得到会话的token==也就是redis里面存的
            Serializable webToken = subject.getSession().getId();
            ajax.put(Constants.TOKEN,webToken);
            loginInfo.setLoginStatus(Constants.LOGIN_SUCCESS);
            loginInfo.setUserName(ShiroSecurityUtils.getCurrentUserName());
            loginInfo.setMsg("登陆成功");
        } catch (Exception e) {
            log.error("用户名或密码不正确", e);
            ajax = AjaxResult.error(HttpStatus.ERROR, "用户名或密码不正确");
            loginInfo.setLoginStatus(Constants.LOGIN_ERROR);
            loginInfo.setMsg("用户名或密码不正确");
        }
        loginInfoService.insertLoginInfo(loginInfo);
        return ajax;

    }
    /**
     * 得到用户的登陆信息
     * @param request
     * @return
     */
    private LoginInfo createLoginInfo(HttpServletRequest request) {
        LoginInfo loginInfo=new LoginInfo();
        final UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr(request);
        String address = AddressUtils.getRealAddressByIp(ip);
        loginInfo.setIpAddr(ip);
        loginInfo.setLoginLocation(address);
        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        loginInfo.setOs(os);
        loginInfo.setBrowser(browser);
        loginInfo.setLoginTime(DateUtil.date());
        loginInfo.setLoginType(Constants.LOGIN_TYPE_SYSTEM);
        return loginInfo;
    }


    /**
     * 获取用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        Subject subject = SecurityUtils.getSubject();
        ActiveUser activeUser = (ActiveUser) subject.getPrincipal();
        AjaxResult ajax = AjaxResult.success();
        ajax.put("username", activeUser.getUser().getUserName());
        ajax.put("picture", activeUser.getUser().getPicture());
        ajax.put("roles", activeUser.getRoles());
        ajax.put("permissions", activeUser.getPermissions());
        return ajax;
    }
    /**
     * 用户退出
     */
    @GetMapping("logout")
    public AjaxResult logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return AjaxResult.success("用户退出成功");
    }

    /**
     * 获取菜单信息
     */
    @GetMapping("getMenus")
    public AjaxResult getMenus() {
        Subject subject = SecurityUtils.getSubject();
        ActiveUser activeUser = (ActiveUser) subject.getPrincipal();
        boolean isAdmin = activeUser.getUser().getUserType().equals(Constants.USER_ADMIN);
        SimpleUser simpleUser = null;
        if(!isAdmin) {
            simpleUser= new SimpleUser(
                    activeUser.getUser().getUserId(),
                    activeUser.getUser().getUserName());
        }
        List<Menu> menus = menuService.selectMenuTree(isAdmin, simpleUser);
        List<MenuTreeVo> menuVos=new ArrayList<>();
        for (Menu menu : menus) {
            menuVos.add(new MenuTreeVo(menu.getMenuId().toString(),menu.getPath()));
        }
        return AjaxResult.success(menuVos);
    }
}
