package com.hanwei.controller.system;

import com.hanwei.aspectj.annotation.Log;
import com.hanwei.aspectj.enums.BusinessType;
import com.hanwei.domain.CheckItem;
import com.hanwei.dto.CheckItemDto;
import com.hanwei.service.CheckItemService;
import com.hanwei.util.ShiroSecurityUtils;
import com.hanwei.vo.AjaxResult;
import com.hanwei.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author hanwei
 * @ClassName CheckItemController
 * @date 2020/11/15
 */
@RestController
@RequestMapping("system/checkItem")
public class CheckItemController {

    @Autowired
    private CheckItemService checkItemService;

    /**
     * 分页查询
     */
    @GetMapping("listCheckItemForPage")
    public AjaxResult listCheckItemForPage(CheckItemDto checkItemDto){
        DataGridView gridView = this.checkItemService.listCheckItemPage(checkItemDto);
        return AjaxResult.success("查询成功",gridView.getData(),gridView.getTotal());
    }
    /**
     * 添加
     */
    @PostMapping("addCheckItem")
    @Log(title = "添加检查项目",businessType = BusinessType.INSERT)
    public AjaxResult addCheckItem(@Validated CheckItemDto checkItemDto) {
        checkItemDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.checkItemService.addCheckItem(checkItemDto));
    }

    /**
     * 修改
     */
    @PutMapping("updateCheckItem")
    @Log(title = "修改检查项目",businessType = BusinessType.UPDATE)
    public AjaxResult updateCheckItem(@Validated CheckItemDto checkItemDto) {
        checkItemDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.checkItemService.updateCheckItem(checkItemDto));
    }


    /**
     * 根据ID查询一个检查项目信息
     */
    @GetMapping("getCheckItemById/{checkItemId}")
    public AjaxResult getCheckItemById(@PathVariable @Validated @NotNull(message = "检查项目ID不能为空") Long checkItemId) {
        return AjaxResult.success(this.checkItemService.getOne(checkItemId));
    }

    /**
     * 删除
     */
    @DeleteMapping("deleteCheckItemByIds/{checkItemIds}")
    @Log(title = "删除检查项目",businessType = BusinessType.DELETE)
    public AjaxResult deleteCheckItemByIds(@PathVariable @Validated @NotEmpty(message = "要删除的ID不能为空") Long[] checkItemIds) {
        return AjaxResult.toAjax(this.checkItemService.deleteCheckItemByIds(checkItemIds));
    }

    /**
     * 查询所有可用的检查项目
     */
    @GetMapping("selectAllCheckItem")
    public AjaxResult selectAllCheckItem(){
        List<CheckItem> checkItems=this.checkItemService.queryAllCheckItems();
        return AjaxResult.success(checkItems);
    }
}
