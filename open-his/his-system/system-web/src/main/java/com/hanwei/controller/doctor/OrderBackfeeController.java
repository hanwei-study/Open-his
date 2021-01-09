package com.hanwei.controller.doctor;

import cn.hutool.core.bean.BeanUtil;
import com.hanwei.constants.Constants;
import com.hanwei.controller.BaseController;
import com.hanwei.domain.*;
import com.hanwei.dto.OrderBackfeeDto;
import com.hanwei.dto.OrderBackfeeFormDto;
import com.hanwei.service.CareService;
import com.hanwei.service.OrderBackfeeService;
import com.hanwei.service.OrderChargeService;
import com.hanwei.util.ShiroSecurityUtils;
import com.hanwei.utils.IdGeneratorSnowflake;
import com.hanwei.vo.AjaxResult;
import com.hanwei.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("doctor/backfee")
public class OrderBackfeeController extends BaseController {

    @Reference
    private CareService careService;

    @Reference
    private OrderChargeService orderChargeService;

    @Reference
    private OrderBackfeeService orderBackfeeService;

    /**
     * 根据挂号ID查询已支付的处方信息及详情
     */
    @GetMapping("getChargedCareHistoryByRegId/{regId}")
    @HystrixCommand
    public AjaxResult getChargedCareHistoryByRegId(@PathVariable String regId){
        //声明返回的Map对象
        Map<String,Object> res=new HashMap<>();
        //根据挂号单ID查询病例信息
        CareHistory careHistory=this.careService.queryCareHistoryByRegId(regId);
        if(null==careHistory){
            return AjaxResult.fail("【"+regId+"】的挂号单没有对应的病例信息，请核对后再查询");
        }
        //放入默认值
        res.put("careHistory",careHistory);
        res.put("careOrders", Collections.EMPTY_LIST);
        //声明一个可以存放careOrders的集合
        List<Map<String,Object>> mapList=new ArrayList<>();
        //根据病例编号查询处方
        List<CareOrder> careOrders=this.careService.queryCareOrdersByChId(careHistory.getChId());
        if(careOrders.isEmpty()){
            return AjaxResult.fail("【"+regId+"】的挂号单没相关的处方信息，请核对后再查询");
        }
        for (CareOrder careOrder : careOrders) {
            Map<String,Object> beanToMap= BeanUtil.beanToMap(careOrder);
            beanToMap.put("careOrderItems",Collections.EMPTY_LIST);
            BigDecimal allAmount=new BigDecimal("0");
            //根据处方ID查询未支付的处方详情列表
            List<CareOrderItem> careOrderItems=this.careService.queryCareOrderItemsByCoId(careOrder.getCoId(), Constants.ORDER_DETAILS_STATUS_1);
            //如果当前处方未支付的详情不为空，计算总额
            if(!careOrderItems.isEmpty()) {
                //重新计算总价
                for (CareOrderItem careOrderItem : careOrderItems) {
                    allAmount=allAmount.add(careOrderItem.getAmount());
                }
                beanToMap.put("careOrderItems",careOrderItems);
                beanToMap.put("allAmount",allAmount);
                mapList.add(beanToMap);
            }
        }
        if(mapList.isEmpty()){
            return AjaxResult.fail("【"+regId+"】的挂号单没已支付的处方信息，请核对后再查询");
        }else{
            res.put("careOrders",mapList);
            return AjaxResult.success(res);
        }
    }

    /**
     * 创建现金退费订单
     */
    @PostMapping("createOrderBackfeeWithCash")
    public AjaxResult createOrderBackfeeWithCash(@RequestBody @Validated OrderBackfeeFormDto orderBackfeeFormDto){
        //保存订单
        orderBackfeeFormDto.getOrderBackfeeDto().setBackType(Constants.PAY_TYPE_0);
        orderBackfeeFormDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        //生成退费单号
        String backId= IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PREFIX_ODB);
        orderBackfeeFormDto.getOrderBackfeeDto().setBackId(backId);
        //找到当前退费单之前的收费单的ID
        String itemId = orderBackfeeFormDto.getOrderBackfeeItemDtoList().get(0).getItemId();
        OrderChargeItem orderChargeItem=this.orderChargeService.queryOrderChargeItemByItemId(itemId);
        orderBackfeeFormDto.getOrderBackfeeDto().setOrderId(orderChargeItem.getOrderId());
        this.orderBackfeeService.saveOrderAndItems(orderBackfeeFormDto);
        //因为是现金退费，所以直接更新详情状态
        this.orderBackfeeService.backSuccess(backId,null,Constants.PAY_TYPE_0);
        return AjaxResult.success("创建现在退费订单成功");

    }
    /**
     * 分页查询所有退费单
     */
    @GetMapping("queryAllOrderBackfeeForPage")
    @HystrixCommand
    public AjaxResult queryAllOrderChargeForPage(OrderBackfeeDto orderBackfeeDto){
        DataGridView dataGridView=this.orderBackfeeService.queryAllOrderBackfeeForPage(orderBackfeeDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 根据退费单的ID查询退费详情信息
     */
    @GetMapping("queryOrderBackfeeItemByBackId/{backId}")
    @HystrixCommand
    public AjaxResult queryOrderBackfeeItemByBackId(@PathVariable String backId){
        List<OrderBackfeeItem> list=this.orderBackfeeService.queryBackfeeItemByBackId(backId);
        return AjaxResult.success(list);
    }



}
