package com.hanwei.service.impl;

import com.hanwei.domain.Workload;
import com.hanwei.domain.WorkloadStat;
import com.hanwei.dto.WorkloadQueryDto;
import com.hanwei.mapper.WorkloadMapper;
import com.hanwei.service.WorkloadService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class WorkloadServiceImpl implements WorkloadService {


    @Autowired
    private WorkloadMapper workloadMapper;

    @Override
    public List<Workload> queryWorkload(WorkloadQueryDto workloadQueryDto) {
        return this.workloadMapper.queryWorkload(workloadQueryDto);
    }

    @Override
    public List<WorkloadStat> queryWorkloadStat(WorkloadQueryDto workloadQueryDto) {
        return this.workloadMapper.queryWorkloadStat(workloadQueryDto);
    }
}
