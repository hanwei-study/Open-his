package com.hanwei.service;

import com.hanwei.domain.OrderBackfeeItem;
import com.hanwei.dto.OrderBackfeeDto;
import com.hanwei.dto.OrderBackfeeFormDto;
import com.hanwei.vo.DataGridView;

import java.util.List;

public interface OrderBackfeeService {

    /**
     * 保存退费单
     * @param orderBackfeeFormDto
     */
    void saveOrderAndItems(OrderBackfeeFormDto orderBackfeeFormDto);

    /**
     * 退费成功之后更改状态
     * @param backId
     * @param backPlatformId
     * @param backType
     */
    void backSuccess(String backId, String backPlatformId, String backType);

    /**
     * 分页查询所有退费单
     * @param orderBackfeeDto
     * @return
     */
    DataGridView queryAllOrderBackfeeForPage(OrderBackfeeDto orderBackfeeDto);

    /**
     * 根据退费单的ID查询退费详情信息
     * @param backId
     * @return
     */
    List<OrderBackfeeItem> queryBackfeeItemByBackId(String backId);

}
