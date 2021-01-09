package com.hanwei.controller.doctor;

import cn.hutool.core.date.DateUtil;
import com.hanwei.constants.Constants;
import com.hanwei.controller.BaseController;
import com.hanwei.domain.CareHistory;
import com.hanwei.domain.CareOrder;
import com.hanwei.domain.CareOrderItem;
import com.hanwei.domain.CheckResult;
import com.hanwei.dto.CheckResultDto;
import com.hanwei.dto.CheckResultFormDto;
import com.hanwei.service.CareService;
import com.hanwei.service.CheckResultService;
import com.hanwei.util.ShiroSecurityUtils;
import com.hanwei.vo.AjaxResult;
import com.hanwei.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("doctor/check")
public class CheckResultController extends BaseController {

    @Reference
    private CareService careService;

    @Reference
    private CheckResultService checkResultService;

    /**
     * 根据挂号ID查询已支付的处方信息及详情
     */
    @PostMapping("queryNeedCheckItem")
    @HystrixCommand
    public AjaxResult getNoChargeCareHistoryByRegId(@RequestBody CheckResultDto checkResultDto) {
        //声明返回的对象
        List<CareOrderItem> resCareOrderItems = new ArrayList<>();
        if (StringUtils.isNotBlank(checkResultDto.getRegId())) {
            //查询时带入挂号单
            //根据挂号单ID查询病例信息
            CareHistory careHistory = this.careService.queryCareHistoryByRegId(checkResultDto.getRegId());
            if (null == careHistory) {
                return AjaxResult.success(resCareOrderItems);
            }
            //根据病例ID查询所有的处方信息
            List<CareOrder> careOrders = this.careService.queryCareOrdersByChId(careHistory.getChId());
            for (CareOrder careOrder : careOrders) {
                //只取检查处方
                if (careOrder.getCoType().equals(Constants.CO_TYPE_CHECK)) {
                    List<CareOrderItem> careOrderItems = this.careService.queryCareOrderItemsByCoId(careOrder.getCoId(), Constants.ORDER_DETAILS_STATUS_1);
                    //过滤查询条件
                    for (CareOrderItem careOrderItem : careOrderItems) {
                        if (checkResultDto.getCheckItemIds().contains(Integer.valueOf(careOrderItem.getItemRefId()))) {
                            resCareOrderItems.add(careOrderItem);
                        }
                    }
                }
            }
        } else {
            //查询所有已支付检查的项目
            List<CareOrderItem> careOrderItems = this.careService.queryCareOrderItemsByStatus(Constants.CO_TYPE_CHECK, Constants.ORDER_DETAILS_STATUS_1);
            //过滤查询条件
            for (CareOrderItem careOrderItem : careOrderItems) {
                if (checkResultDto.getCheckItemIds().contains(Integer.valueOf(careOrderItem.getItemRefId()))) {
                    resCareOrderItems.add(careOrderItem);
                }
            }
        }
        return AjaxResult.success(resCareOrderItems);
    }

    /**
     *根据检查单号查询要检查的项目详情
     */
    @GetMapping("queryCheckItemByItemId/{itemId}")
    @HystrixCommand
    public AjaxResult queryCheckItemByItemId(@PathVariable String itemId){
        CareOrderItem careOrderItem = this.careService.queryCareOrderItemByItemId(itemId);
        if(careOrderItem==null){
            return AjaxResult.fail("【"+itemId+"】的检查单号的数据不存在，请核对后再查询");
        }
        if(!careOrderItem.getStatus().equals(Constants.ORDER_DETAILS_STATUS_1)){
            return AjaxResult.fail("【"+itemId+"】的检查单号的没有支付，请支付后再来检查");
        }
        if(!careOrderItem.getItemType().equals(Constants.CO_TYPE_CHECK)){
            return AjaxResult.fail("【"+itemId+"】的单号不是检查项目，请核对后再查询");
        }
        CareOrder careOrder = this.careService.queryCareOrderByCoId(careOrderItem.getCoId());
        CareHistory careHistory = this.careService.queryCareHistoryByChId(careOrder.getChId());
        Map<String,Object> res=new HashMap<>();
        res.put("item",careOrderItem);
        res.put("careOrder", careOrder);
        res.put("careHistory", careHistory);
        return AjaxResult.success(res);
    }

    /**
     * 开始检查
     */
    @PostMapping("startCheck/{itemId}")
    @HystrixCommand
    public AjaxResult startCheck(@PathVariable String itemId){
        CareOrderItem careOrderItem = this.careService.queryCareOrderItemByItemId(itemId);
        if(careOrderItem==null){
            return AjaxResult.fail("【"+itemId+"】的检查单号的数据不存在，请核对后再查询");
        }
        if(!careOrderItem.getStatus().equals(Constants.ORDER_DETAILS_STATUS_1)){
            return AjaxResult.fail("【"+itemId+"】的检查单号的没有支付，请支付后再来检查");
        }
        if(!careOrderItem.getItemType().equals(Constants.CO_TYPE_CHECK)){
            return AjaxResult.fail("【"+itemId+"】的单号不是检查项目，请核对后再查询");
        }
        CareOrder careOrder = this.careService.queryCareOrderByCoId(careOrderItem.getCoId());
        CareHistory careHistory = this.careService.queryCareHistoryByChId(careOrder.getChId());
        CheckResult checkResult=new CheckResult();
        checkResult.setItemId(itemId);
        checkResult.setCheckItemId(Integer.valueOf(careOrderItem.getItemRefId()));
        checkResult.setCheckItemName(careOrderItem.getItemName());
        checkResult.setPatientId(careOrder.getPatientId());
        checkResult.setPatientName(careOrder.getPatientName());
        checkResult.setPrice(careOrderItem.getPrice());
        checkResult.setRegId(careHistory.getRegId());
        //设置状态为，检查中
        checkResult.setResultStatus(Constants.RESULT_STATUS_0);
        checkResult.setCreateTime(DateUtil.date());
        checkResult.setCreateBy(ShiroSecurityUtils.getCurrentUserName());
        return AjaxResult.toAjax(checkResultService.saveCheckResult(checkResult));
    }

    /**
     * 分页查询所有检查中的项目
     */
    @PostMapping("queryAllCheckingResultForPage")
    public AjaxResult queryAllCheckingResultForPage(@RequestBody CheckResultDto checkResultDto){
        //检查中，状态
        checkResultDto.setResultStatus(Constants.RESULT_STATUS_0);
        DataGridView dataGridView=this.checkResultService.queryAllCheckResultForPage(checkResultDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 上传检查结果并完成检查
     */
    @PostMapping("completeCheckResult")
    @HystrixCommand
    public AjaxResult completeCheckResult(@RequestBody @Validated CheckResultFormDto checkResultFormDto){
        checkResultFormDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.checkResultService.completeCheckResult(checkResultFormDto));
    }

    /**
     * 查询所有检查中和检查完成的项目
     */
    @PostMapping("queryAllCheckResultForPage")
    public AjaxResult queryAllCheckResultForPage(@RequestBody CheckResultDto checkResultDto){
        DataGridView dataGridView=this.checkResultService.queryAllCheckResultForPage(checkResultDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

}