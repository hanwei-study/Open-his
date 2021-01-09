package com.hanwei.service;

import com.hanwei.domain.Menu;
import com.hanwei.domain.SimpleUser;
import com.hanwei.dto.MenuDto;

import java.util.List;

public interface MenuService {

    /**
     * 查询菜单信息
     * 如查用户是超级管理员，那么查询所以菜单和权限
     * 如果用户是普通用户，那么根据用户ID关联角色和权限
     * @param isAdmin 是否是超级管理员
     * @param  simpleUser  如果isAdmin=true  simpleUser可以为空
     */
    public List<Menu> selectMenuTree(boolean isAdmin, SimpleUser simpleUser);

    /**
     * 根据条件查询所有菜单
     * @param menuDto
     * @return
     */
    List<Menu> listAllMenus(MenuDto menuDto);

    /**
     * 根据ID查询菜单和权限
     * @param menuId
     * @return
     */
    Menu getOne(Long menuId);

    /**
     * 添加菜单或权限
     * @param menuDto
     * @return
     */
    int addMenu(MenuDto menuDto);

    /**
     * 修改菜单或权限
     * @param menuDto
     * @return
     */
    int updateMenu(MenuDto menuDto);

    /**
     * 根据ID删除菜单或权限
     * @param menuId
     * @return
     */
    int deleteMenuById(Long menuId);

    /**
     * 根据菜单ID判断菜单是否有子节点
     * @param menuId
     * @return
     */
    boolean hasChildByMenuId(Long menuId);

    /**
     * 根据角色ID查询菜单权限ID数据
     * @param roleId
     * @return
     */
    List<Long> getMenusIdsByRoleId(Long roleId);
}
