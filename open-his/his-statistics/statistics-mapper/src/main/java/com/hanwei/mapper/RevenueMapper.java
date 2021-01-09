package com.hanwei.mapper;

import com.hanwei.domain.Income;
import com.hanwei.domain.Refund;
import com.hanwei.dto.RevenueQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RevenueMapper {
    /**
     * 查询收入的数据
     *
     * @param revenueQueryDto
     * @return
     */
    List<Income> queryIncome(@Param("revenue") RevenueQueryDto revenueQueryDto);

    /**
     * 查询退费的数据
     *
     * @param revenueQueryDto
     * @return
     */
    List<Refund> queryRefund(@Param("revenue") RevenueQueryDto revenueQueryDto);
}
