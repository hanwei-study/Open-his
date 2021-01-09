package com.hanwei.service;

import com.hanwei.dto.RevenueQueryDto;

import java.util.Map;

/**
 * @author hanwei
 * @ClassName RevenueService
 * @date 2020/12/17
 */
public interface RevenueService {
    /**
     * 查询收支统计的数据
     * @param revenueQueryDto
     * @return
     */
    Map<String, Object> queryAllRevenueData(RevenueQueryDto revenueQueryDto);

}
