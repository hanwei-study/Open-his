package com.hanwei.controller.system;

import com.hanwei.domain.Role;
import com.hanwei.dto.RoleDto;
import com.hanwei.service.RoleService;
import com.hanwei.util.ShiroSecurityUtils;
import com.hanwei.vo.AjaxResult;
import com.hanwei.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 分页查询
     */
    @GetMapping("listRoleForPage")
    public AjaxResult listRoleForPage(RoleDto roleDto){
        DataGridView gridView = this.roleService.listRolePage(roleDto);
        return AjaxResult.success("查询成功",gridView.getData(),gridView.getTotal());
    }

    /**
     * 不分页面查询有效的
     */
    @GetMapping("selectAllRoles")
    public AjaxResult selectAllRoles(){
        List<Role> lists=this.roleService.listAllRoles();
        return AjaxResult.success(lists);
    }

    /**
     * 查询一个
     */
    @GetMapping("getRoleById/{roleId}")
    public AjaxResult getRoleById(@PathVariable Long roleId){
        Role role=this.roleService.getOne(roleId);
        return AjaxResult.success(role);
    }

    /**
     * 添加
     */
    @PostMapping("addRole")
    public AjaxResult addRole(@Validated RoleDto roleDto){
        roleDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.roleService.addRole(roleDto));
    }

    /**
     * 修改
     */
    @PutMapping("updateRole")
    public AjaxResult updateRole(@Validated RoleDto roleDto){
        roleDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.roleService.updateRole(roleDto));
    }

    /**
     * 删除
     */
    @DeleteMapping("deleteRoleByIds/{roleIds}")
    public AjaxResult deleteRoleByIds(@PathVariable Long[] roleIds){
        return AjaxResult.toAjax(this.roleService.deleteRoleByIds(roleIds));
    }

    /**
     * 保存角色和菜单之间的关系
     */
    @PostMapping("saveRoleMenu/{roleId}/{menuIds}")
    public AjaxResult saveRoleMenu(@PathVariable Long roleId,@PathVariable Long[] menuIds){
        /**
         * 因为我们用的路径参数，前端可能传过来的menuIds是一个空的，但是为空的话无法匹配上面的路径
         * 所以如果为空，我们让前端传一个-1过来，如果是-1说明当前角色一个权限也没有选择
         */
        if(menuIds.length == 1 && menuIds[0].equals(-1L)){
            menuIds=new Long[]{};
        }
        this.roleService.saveRoleMenu(roleId, menuIds);
        return AjaxResult.success();
    }

    /**
     * 根据用户ID查询用户拥有的角色IDS
     */
    @GetMapping("getRoleIdsByUserId/{userId}")
    public AjaxResult getRoleIdsByUserId(@PathVariable Long userId){
        List<Long> roleIds = this.roleService.getRoleIdsByUserId(userId);
        return AjaxResult.success(roleIds);
    }

    /**
     * 保存角色和用户的关系
     */
    @PostMapping("saveRoleUser/{userId}/{roleIds}")
    public AjaxResult saveRoleUser(@PathVariable Long userId,@PathVariable Long[] roleIds){
        /**
         * 因为我们用的路径参数，前端可能传过来的roleIds是一个空的，但是为空的话无法匹配上面的路径
         * 所以如果为空，我们让前端传一个-1过来，如果是-1说明当前角色一个权限也没有选择
         */
        if(roleIds.length==1&&roleIds[0].equals(-1L)){
            roleIds=new Long[]{};
        }
        this.roleService.saveRoleUser(userId, roleIds);
        return AjaxResult.success();
    }

}

