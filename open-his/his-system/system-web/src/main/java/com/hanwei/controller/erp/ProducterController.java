package com.hanwei.controller.erp;

import com.hanwei.aspectj.annotation.Log;
import com.hanwei.aspectj.enums.BusinessType;
import com.hanwei.controller.BaseController;
import com.hanwei.dto.ProducterDto;
import com.hanwei.service.ProducterService;
import com.hanwei.util.ShiroSecurityUtils;
import com.hanwei.vo.AjaxResult;
import com.hanwei.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author hanwei
 * @ClassName ProducterController
 * @date 2020/11/16
 */
@RestController
@RequestMapping("erp/producter")
public class ProducterController extends BaseController {

    @Reference
    private ProducterService producterService;

    /**
     * 分页查询
     */
    @HystrixCommand
    @GetMapping("listProducterForPage")
    public AjaxResult listProducterForPage(ProducterDto producterDto){
        DataGridView gridView = this.producterService.listProducterPage(producterDto);
        return AjaxResult.success("查询成功",gridView.getData(),gridView.getTotal());
    }
    /**
     * 添加
     */
    @HystrixCommand
    @PostMapping("addProducter")
    @Log(title = "添加生产厂家",businessType = BusinessType.INSERT)
    public AjaxResult addProducter(@Validated ProducterDto producterDto) {
        producterDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.producterService.addProducter(producterDto));
    }

    /**
     * 修改
     */
    @HystrixCommand
    @PutMapping("updateProducter")
    @Log(title = "修改生产厂家",businessType = BusinessType.UPDATE)
    public AjaxResult updateProducter(@Validated ProducterDto producterDto) {
        producterDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.producterService.updateProducter(producterDto));
    }


    /**
     * 根据ID查询一个生产厂家信息
     */
    @HystrixCommand
    @GetMapping("getProducterById/{producterId}")
    public AjaxResult getProducterById(@PathVariable @Validated @NotNull(message = "生产厂家ID不能为空") Long producterId) {
        return AjaxResult.success(this.producterService.getOne(producterId));
    }

    /**
     * 删除
     */
    @HystrixCommand
    @DeleteMapping("deleteProducterByIds/{producterIds}")
    @Log(title = "删除生产厂家",businessType = BusinessType.DELETE)
    public AjaxResult deleteProducterByIds(@PathVariable @Validated @NotEmpty(message = "要删除的ID不能为空") Long[] producterIds) {
        return AjaxResult.toAjax(this.producterService.deleteProducterByIds(producterIds));
    }

    /**
     * 查询所有可用的生产厂家
     */
    @HystrixCommand
    @GetMapping("selectAllProducter")
    public AjaxResult selectAllProducter() {
        return AjaxResult.success(this.producterService.selectAllProducter());
    }

}

