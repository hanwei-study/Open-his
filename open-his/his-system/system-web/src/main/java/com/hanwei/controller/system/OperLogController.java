package com.hanwei.controller.system;

import com.hanwei.dto.OperLogDto;
import com.hanwei.service.OperLogService;
import com.hanwei.vo.AjaxResult;
import com.hanwei.vo.DataGridView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author hanwei
 * @ClassName OperLogController
 * @date 2020/10/3
 */
@Slf4j
@RestController
@RequestMapping("system/operLog")
public class OperLogController {

    @Autowired
    private OperLogService operLogService;

    /**
     * 分页查询
     */
    @GetMapping("listForPage")
    public AjaxResult listForPage(OperLogDto operLogDto) {
        DataGridView gridView = operLogService.listForPage(operLogDto);
        return AjaxResult.success("查询成功", gridView.getData(), gridView.getTotal());
    }

    /**
     * 删除
     */
    @DeleteMapping("deleteOperLogByIds/{infoIds}")
    public AjaxResult deleteOperLogByIds(@PathVariable Long[] infoIds) {

        return AjaxResult.toAjax(this.operLogService.deleteOperLogByIds(infoIds));
    }

    /**
     * 清空删除
     */
    @DeleteMapping("clearAllOperLog")
    public AjaxResult clearAllOperLog() {
        return AjaxResult.toAjax(this.operLogService.clearAllOperLog());
    }
}