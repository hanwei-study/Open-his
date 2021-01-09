package com.hanwei.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hanwei.constants.Constants;
import com.hanwei.domain.CareOrderItem;
import com.hanwei.domain.OrderCharge;
import com.hanwei.domain.OrderChargeItem;
import com.hanwei.dto.OrderChargeDto;
import com.hanwei.dto.OrderChargeFormDto;
import com.hanwei.dto.OrderChargeItemDto;
import com.hanwei.mapper.CareOrderItemMapper;
import com.hanwei.mapper.OrderChargeItemMapper;
import com.hanwei.mapper.OrderChargeMapper;
import com.hanwei.service.OrderChargeService;
import com.hanwei.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service(methods = {@Method(name = "saveOrderAndItems",retries = 1),
        @Method(name = "paySuccess",retries = 1)})
public class OrderChargeServiceImpl implements OrderChargeService {


    @Autowired
    private OrderChargeMapper orderChargeMapper;

    @Autowired
    private OrderChargeItemMapper orderChargeItemMapper;

    @Autowired
    private CareOrderItemMapper careOrderItemMapper;

    /**
     * 保存订单及详情
     *
     * @param orderChargeFromDto
     */
    @Override
    public void saveOrderAndItems(OrderChargeFormDto orderChargeFromDto) {
        OrderChargeDto orderChargeDto = orderChargeFromDto.getOrderChargeDto();
        List<OrderChargeItemDto> orderChargeItemDtoList = orderChargeFromDto.getOrderChargeItemDtoList();
        OrderCharge orderCharge = new OrderCharge();
        BeanUtil.copyProperties(orderChargeDto, orderCharge);
        orderCharge.setOrderStatus(Constants.ORDER_STATUS_0);
        orderCharge.setCreateTime(DateUtil.date());
        orderCharge.setCreateBy(orderChargeFromDto.getSimpleUser().getUserName());
        int i = this.orderChargeMapper.insert(orderCharge);
        //保存详情
        for (OrderChargeItemDto orderChargeItemDto : orderChargeItemDtoList) {
            OrderChargeItem orderChargeItem = new OrderChargeItem();
            BeanUtil.copyProperties(orderChargeItemDto, orderChargeItem);
            //订单关联订单ID
            orderChargeItem.setOrderId(orderCharge.getOrderId());
            orderChargeItem.setStatus(Constants.ORDER_DETAILS_STATUS_0);
            this.orderChargeItemMapper.insert(orderChargeItem);
        }
    }

    /**
     * 支付成功的回调
     *
     * @param orderId       支付订单ID
     * @param payPlatformId 平台交易ID 如果是现金，则为空
     */
    @Override
    public void paySuccess(String orderId, String payPlatformId, String payType) {
        //根据支付订单ID查询支付订单
        OrderCharge orderCharge = this.orderChargeMapper.selectById(orderId);
        //设置平台交易编号
        orderCharge.setPayPlatformId(payPlatformId);
        //设置支付时间
        orderCharge.setPayTime(DateUtil.date());
        //修改订单状态 已支付
        orderCharge.setOrderStatus(Constants.ORDER_STATUS_1);
        //更新订单状态
        this.orderChargeMapper.updateById(orderCharge);
        //根据支付订单号查询支付订单详情
        QueryWrapper<OrderChargeItem> qw = new QueryWrapper<>();
        qw.eq(OrderChargeItem.COL_ORDER_ID, orderId);
        List<OrderChargeItem> orderChargeItems = this.orderChargeItemMapper.selectList(qw);
        List<String> allItemIds = new ArrayList<>();
        for (OrderChargeItem orderChargeItem : orderChargeItems) {
            allItemIds.add(orderChargeItem.getItemId());
        }
        //更新收费详情的状态
        OrderChargeItem orderItemObj = new OrderChargeItem();
        orderItemObj.setStatus(Constants.ORDER_DETAILS_STATUS_1);
        QueryWrapper<OrderChargeItem> orderItemQw = new QueryWrapper<>();
        orderItemQw.in(OrderChargeItem.COL_ITEM_ID, allItemIds);
        this.orderChargeItemMapper.update(orderItemObj, orderItemQw);
        //更新处方详情的状态
        CareOrderItem careItemObj = new CareOrderItem();
        careItemObj.setStatus(Constants.ORDER_DETAILS_STATUS_1);
        QueryWrapper<CareOrderItem> careItemQw = new QueryWrapper<>();
        careItemQw.in(CareOrderItem.COL_ITEM_ID, allItemIds);
        this.careOrderItemMapper.update(careItemObj, careItemQw);
    }

    @Override
    public DataGridView queryAllOrderChargeForPage(OrderChargeDto orderChargeDto) {
        Page<OrderCharge> page = new Page<>(orderChargeDto.getPageNum(), orderChargeDto.getPageSize());
        QueryWrapper<OrderCharge> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(orderChargeDto.getPatientName()), OrderCharge.COL_PATIENT_NAME, orderChargeDto.getPatientName());
        qw.like(StringUtils.isNotBlank(orderChargeDto.getRegId()), OrderCharge.COL_REG_ID, orderChargeDto.getRegId());
        qw.orderByDesc(OrderCharge.COL_CREATE_TIME);
        this.orderChargeMapper.selectPage(page, qw);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    @Override
    public List<OrderChargeItem> queryOrderChargeItemByOrderId(String orderId) {
        QueryWrapper<OrderChargeItem> qw = new QueryWrapper<>();
        qw.eq(OrderChargeItem.COL_ORDER_ID, orderId);
        return this.orderChargeItemMapper.selectList(qw);
    }

    @Override
    public OrderCharge queryOrderChargeByOrderId(String orderId) {
        return this.orderChargeMapper.selectById(orderId);
    }

    @Override
    public OrderChargeItem queryOrderChargeItemByItemId(String itemId) {
        return this.orderChargeItemMapper.selectById(itemId);
    }


}