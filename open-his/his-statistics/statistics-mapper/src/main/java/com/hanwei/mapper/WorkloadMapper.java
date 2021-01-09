package com.hanwei.mapper;

import com.hanwei.domain.Workload;
import com.hanwei.domain.WorkloadStat;
import com.hanwei.dto.WorkloadQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hanwei
 * @ClassName WorkloadMapper
 * @date 2020/12/17
 */
public interface WorkloadMapper {
    /**
     * 医生工作量统计列表
     *
     * @param workloadQueryDto
     * @return
     */
    List<Workload> queryWorkload(@Param("workload") WorkloadQueryDto workloadQueryDto);

    /**
     * 总体工作量统计列表
     *
     * @param workloadQueryDto
     * @return
     */
    List<WorkloadStat> queryWorkloadStat(@Param("workload") WorkloadQueryDto workloadQueryDto);
}
