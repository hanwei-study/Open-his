package com.hanwei.controller.system;

import com.hanwei.aspectj.annotation.Log;
import com.hanwei.aspectj.enums.BusinessType;
import com.hanwei.dto.DictDataDto;
import com.hanwei.service.DictDataService;
import com.hanwei.util.ShiroSecurityUtils;
import com.hanwei.vo.AjaxResult;
import com.hanwei.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author hanwei
 * @ClassName DictTypeController
 * @date 2020/9/27
 */
@RestController
@RequestMapping("system/dict/data")
public class DictDataController {

    @Autowired
    private DictDataService dictDataService;

    
    /**
     * 分页查询
     */
    @GetMapping("listForPage")
    public AjaxResult listForPage(DictDataDto dictDataDto) {
        DataGridView dataGridView = dictDataService.listPage(dictDataDto);
        return AjaxResult.success("查询成功", dataGridView.getData(), dataGridView.getTotal());
    }
    /**
     * 添加
     */
    @PostMapping("addDictData")
    @Log(title = "添加字典数据",businessType = BusinessType.INSERT)
    public AjaxResult addDictData(@Validated DictDataDto dictDataDto) {
        dictDataDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.dictDataService.insert(dictDataDto));
    }

    /**
     * 修改
     */
    @PutMapping("updateDictData")
    @Log(title = "修改字典数据",businessType = BusinessType.UPDATE)
    public AjaxResult updateDictData(@Validated DictDataDto dictDataDto) {
        dictDataDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.dictDataService.update(dictDataDto));
    }


    /**
     * 根据ID查询一个字典数据信息
     */
    @GetMapping("getOne/{dictId}")
    public AjaxResult getDictData(@PathVariable @Validated @NotNull(message = "字典数据ID不能为空") Long dictId) {
        return AjaxResult.success(this.dictDataService.selectDictDataById(dictId));
    }

    /**
     * 删除
     */
    @DeleteMapping("deleteDictDataByIds/{dictIds}")
    @Log(title = "删除字典数据",businessType = BusinessType.DELETE)
    public AjaxResult updateDictData(@PathVariable @Validated @NotEmpty(message = "要删除的数据ID不能为空") Long[] dictIds) {
        return AjaxResult.toAjax(this.dictDataService.deleteDictDataByIds(dictIds));
    }

    /**
     * 查询所有可用的字典类型
     */
    @GetMapping("getDataByType/{dictData}")
    public AjaxResult getDataByType(@PathVariable @Validated @NotEmpty(message = "字典类型不能为空") String dictData){
        return AjaxResult.success(this.dictDataService.selectDictDataByDictType(dictData));
    }
}
