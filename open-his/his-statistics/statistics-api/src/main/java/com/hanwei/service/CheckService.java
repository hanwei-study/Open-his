package com.hanwei.service;

import com.hanwei.domain.Check;
import com.hanwei.domain.CheckStat;
import com.hanwei.dto.CheckQueryDto;

import java.util.List;

/**
 * @author hanwei
 * @ClassName CheckService
 * @date 2020/12/17
 */
public interface CheckService {
    /**
     * 查询检查项列表
     * @param checkQueryDto
     * @return
     */
    List<Check> queryCheck(CheckQueryDto checkQueryDto);

    /**
     * 查询检查项统计列表
     * @param checkQueryDto
     * @return
     */
    List<CheckStat> queryCheckStat(CheckQueryDto checkQueryDto);
}
