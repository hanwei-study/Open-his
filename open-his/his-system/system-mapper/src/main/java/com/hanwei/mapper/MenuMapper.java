package com.hanwei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hanwei.domain.Menu;
import com.hanwei.domain.SimpleUser;

import java.io.Serializable;
import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 根据菜单ID查询它的子节点个数
     *
     * @param menuId
     * @return
     */
    Long queryChildCountByMenuId(Long menuId);

    /**
     * 查询菜单树的叶子节点
     * @param roleId
     * @return
     */
    List<Long> queryMenuIdsByRoleId(Long roleId);

    List<Menu> selectMenuListByUserId(Serializable userId);
}