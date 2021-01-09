package com.hanwei.service;

import com.hanwei.domain.Scheduling;
import com.hanwei.dto.SchedulingFormDto;
import com.hanwei.dto.SchedulingQueryDto;

import java.util.List;

/**
 * 排班相关信息
 */
public interface SchedulingService {
    /**
     * 查询排班的数据
     * @param schedulingDto
     * @return
     */
    List<Scheduling> queryScheduling(SchedulingQueryDto schedulingDto);

    /**
     * 保存排班的数据
     * @param schedulingFormDto
     * @return
     */
    int saveScheduling(SchedulingFormDto schedulingFormDto);

    /**
     * 查询有排班的部门信息
     * @param deptId
     * @param schedulingDay
     * @param schedulingType
     * @param subsectionType
     * @return
     */
    List<Long> queryHasSchedulingDeptIds(Long deptId, String schedulingDay, String schedulingType, String subsectionType);



}
