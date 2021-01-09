package com.hanwei.mapper;

import com.hanwei.domain.Check;
import com.hanwei.domain.CheckStat;
import com.hanwei.dto.CheckQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hanwei
 * @ClassName CheckMapper
 * @date 2020/12/17
 */
public interface CheckMapper {
    /**
     * 查询检查项列表
     *
     * @param checkQueryDto
     * @return
     */
    List<Check> queryCheck(@Param("check") CheckQueryDto checkQueryDto);

    /**
     * 查询检查项统计列表
     *
     * @param checkQueryDto
     * @return
     */
    List<CheckStat> queryCheckStat(@Param("check") CheckQueryDto checkQueryDto);
}
