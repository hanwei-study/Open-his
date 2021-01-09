package com.hanwei.controller.erp;

import com.hanwei.controller.BaseController;
import com.hanwei.dto.InventoryLogDto;
import com.hanwei.service.InventoryLogService;
import com.hanwei.vo.AjaxResult;
import com.hanwei.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hanwei
 * @ClassName InventoryLogController
 * @date 2020/12/6
 */
@RestController
@RequestMapping("erp/inventoryLog")
public class InventoryLogController extends BaseController {


    @Reference
    private InventoryLogService inventoryLogService;

    /**
     * 分页查询
     */
    @GetMapping("listInventoryLogForPage")
    @HystrixCommand
    public AjaxResult listMedicinesForPage(InventoryLogDto inventoryLogDto) {
        DataGridView gridView = this.inventoryLogService.listInventoryLogPage(inventoryLogDto);
        return AjaxResult.success("查询成功", gridView.getData(), gridView.getTotal());
    }
}