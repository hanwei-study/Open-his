package com.hanwei.controller.doctor;

import cn.hutool.core.bean.BeanUtil;
import com.hanwei.constants.Constants;
import com.hanwei.controller.BaseController;
import com.hanwei.domain.*;
import com.hanwei.dto.OrderChargeDto;
import com.hanwei.dto.OrderChargeFormDto;
import com.hanwei.service.CareService;
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
@RequestMapping("doctor/charge")
public class OrderChargeController extends BaseController {

    @Reference
    private CareService careService;

    @Reference
    private OrderChargeService orderChargeService;

    /**
     * 根据挂号ID查询未支付的处方信息及详情
     */
    @GetMapping("getNoChargeCareHistoryByRegId/{regId}")
    @HystrixCommand
    public AjaxResult getNoChargeCareHistoryByRegId(@PathVariable String regId) {
        //声明返回的Map对象
        Map<String, Object> res = new HashMap<>();
        //根据挂号单ID查询病例信息
        CareHistory careHistory = this.careService.queryCareHistoryByRegId(regId);
        if (null == careHistory) {
            return AjaxResult.fail("【" + regId + "】的挂号单没有对应的病例信息，请核对后再查询");
        }
        //放入默认值
        res.put("careHistory", careHistory);
        res.put("careOrders", Collections.EMPTY_LIST);
        //声明一个可以存放careOrders的集合
        List<Map<String, Object>> mapList = new ArrayList<>();
        //根据病例编号查询处方
        List<CareOrder> careOrders = this.careService.queryCareOrdersByChId(careHistory.getChId());
        if (careOrders.isEmpty()) {
            return AjaxResult.fail("【" + regId + "】的挂号单没相关的处方信息，请核对后再查询");
        }
        for (CareOrder careOrder : careOrders) {
            Map<String, Object> beanToMap = BeanUtil.beanToMap(careOrder);
            beanToMap.put("careOrderItems", Collections.EMPTY_LIST);
            BigDecimal allAmount = new BigDecimal("0");
            //根据处方ID查询未支付的处方详情列表
            List<CareOrderItem> careOrderItems = this.careService.queryCareOrderItemsByCoId(careOrder.getCoId(), Constants.ORDER_DETAILS_STATUS_0);
            //如果当前处方未支付的详情不为空，计算总额
            if (!careOrderItems.isEmpty()) {
                //重新计算总价
                for (CareOrderItem careOrderItem : careOrderItems) {
                    allAmount = allAmount.add(careOrderItem.getAmount());
                }
                beanToMap.put("careOrderItems", careOrderItems);
                beanToMap.put("allAmount", allAmount);
                mapList.add(beanToMap);
            }
        }
        if (mapList.isEmpty()) {
            return AjaxResult.fail("【" + regId + "】的挂号单没未支付的处方信息，请核对后再查询");
        } else {
            res.put("careOrders", mapList);
            return AjaxResult.success(res);
        }
    }

    /**
     * 创建现金收费订单
     */
    @PostMapping("createOrderChargeWithCash")
    public AjaxResult createOrderChargeWithCash(@RequestBody @Validated OrderChargeFormDto orderChargeFromDto) {
        //1,保存订单
        //现金支付
        orderChargeFromDto.getOrderChargeDto().setPayType(Constants.PAY_TYPE_0);
        orderChargeFromDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        String orderId = IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PREFIX_ODC);
        orderChargeFromDto.getOrderChargeDto().setOrderId(orderId);
        this.orderChargeService.saveOrderAndItems(orderChargeFromDto);
        //2,因为是现金支付，所有直接更新详情状态
        this.orderChargeService.paySuccess(orderId, null, Constants.PAY_TYPE_0);
        return AjaxResult.success("创建订单并现金支付成功");
    }

    /**
     * 分页查询所有收费单
     */
    @GetMapping("queryAllOrderChargeForPage")
    public AjaxResult queryAllOrderChargeForPage(OrderChargeDto orderChargeDto) {
        DataGridView dataGridView = this.orderChargeService.queryAllOrderChargeForPage(orderChargeDto);
        return AjaxResult.success("查询成功", dataGridView.getData(), dataGridView.getTotal());
    }

    /**
     * 根据收费单的ID查询收费详情信息
     */
    @GetMapping("queryOrderChargeItemByOrderId/{orderId}")
    public AjaxResult queryOrderChargeItemByOrderId(@PathVariable String orderId) {
        List<OrderChargeItem> list = this.orderChargeService.queryOrderChargeItemByOrderId(orderId);
        return AjaxResult.success(list);
    }

    /**
     * 订单列表现金支付订单
     */
    @GetMapping("payWithCash/{orderId}")
    public AjaxResult payWithCash(@PathVariable String orderId) {
        OrderCharge orderCharge = this.orderChargeService.queryOrderChargeByOrderId(orderId);
        if (null == orderCharge) {
            return AjaxResult.fail("【" + orderId + "】订单号所在的订单不存在，请核对后再输入");
        }
        if (orderCharge.getOrderStatus().equals(Constants.ORDER_STATUS_1)) {
            return AjaxResult.fail("【" + orderId + "】订单号不是未支付状态，请核对后再输入");
        }
        this.orderChargeService.paySuccess(orderId, null, Constants.PAY_TYPE_0);
        return AjaxResult.success();
    }
    /**
     * 根据订单ID查询订单信息【验证是否支付成功】
     */
    @GetMapping("queryOrderChargeOrderId/{orderId}")
    public AjaxResult queryOrderChargeOrderId(@PathVariable String orderId){
        OrderCharge orderCharge=this.orderChargeService.queryOrderChargeByOrderId(orderId);
        if(null==orderCharge){
            return AjaxResult.fail("【"+orderId+"】订单号所在的订单不存在，请核对后再输入");
        }
        return  AjaxResult.success(orderCharge);
    }



}