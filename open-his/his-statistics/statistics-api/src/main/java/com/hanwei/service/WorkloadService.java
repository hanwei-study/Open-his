package com.hanwei.service;

import com.hanwei.domain.Workload;
import com.hanwei.domain.WorkloadStat;
import com.hanwei.dto.WorkloadQueryDto;

import java.util.List;

/**
 * @author hanwei
 * @ClassName WorkloadService
 * @date 2020/12/17
 */
public interface WorkloadService {
    /**
     * 医生工作量统计列表
     * @param workloadQueryDto
     * @return
     */
    List<Workload> queryWorkload(WorkloadQueryDto workloadQueryDto);

    /**
     * 总体工作量统计列表
     * @param workloadQueryDto
     * @return
     */
    List<WorkloadStat> queryWorkloadStat(WorkloadQueryDto workloadQueryDto);
}
