package com.hanwei.controller.system;

import com.hanwei.aspectj.annotation.Log;
import com.hanwei.aspectj.enums.BusinessType;
import com.hanwei.domain.Dept;
import com.hanwei.dto.DeptDto;
import com.hanwei.service.DeptService;
import com.hanwei.util.ShiroSecurityUtils;
import com.hanwei.vo.AjaxResult;
import com.hanwei.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author hanwei
 * @ClassName DeptController
 * @date 2020/11/9
 */
@RestController
@RequestMapping("system/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    /**
     * 分页查询
     */
    @GetMapping("listDeptForPage")
    public AjaxResult listDeptForPage(DeptDto deptDto) {
        DataGridView gridView = this.deptService.listPage(deptDto);
        return AjaxResult.success("查询成功", gridView.getData(), gridView.getTotal());
    }

    /**
     * 不分页面查询有效的
     */
    @GetMapping("selectAllDept")
    public AjaxResult selectAllDept() {
        List<Dept> lists = this.deptService.list();
        return AjaxResult.success(lists);
    }

    /**
     * 查询一个
     */
    @GetMapping("getDeptById/{deptId}")
    public AjaxResult getDeptById(@PathVariable @Validated @NotEmpty(message = "科室ID为空") Long deptId) {
        Dept dept = this.deptService.getOne(deptId);
        return AjaxResult.success(dept);
    }

    /**
     * 添加
     */
    @Log(title = "科室管理", businessType = BusinessType.INSERT)
    @PostMapping("addDept")
    public AjaxResult addDept(@Validated DeptDto deptDto) {
        deptDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.deptService.addDept(deptDto));
    }

    /**
     * 修改
     */
    @Log(title = "科室管理", businessType = BusinessType.UPDATE)
    @PutMapping("updateDept")
    public AjaxResult updateDept(@Validated DeptDto deptDto) {
        deptDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.deptService.updateDept(deptDto));
    }

    /**
     * 删除
     */
    @Log(title = "科室管理", businessType = BusinessType.DELETE)
    @DeleteMapping("deleteDeptByIds/{deptIds}")
    public AjaxResult delete(@PathVariable @Validated @NotEmpty(message = "科室ID为空") Long[] deptIds) {
        return AjaxResult.toAjax(this.deptService.deleteDeptByIds(deptIds));
    }

}
