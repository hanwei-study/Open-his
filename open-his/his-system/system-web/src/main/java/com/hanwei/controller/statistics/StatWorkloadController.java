package com.hanwei.controller.statistics;

import cn.hutool.core.date.DateUtil;
import com.hanwei.controller.BaseController;
import com.hanwei.domain.Workload;
import com.hanwei.domain.WorkloadStat;
import com.hanwei.dto.WorkloadQueryDto;
import com.hanwei.service.WorkloadService;
import com.hanwei.vo.AjaxResult;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("statistics/workload")
public class StatWorkloadController extends BaseController {

    @Reference
    private WorkloadService workloadService;

    /**
     * 医生工作量统计列表
     */
    @GetMapping("queryWorkload")
    public AjaxResult queryWorkload(WorkloadQueryDto workloadQueryDto) {
        if (workloadQueryDto.getBeginTime() == null) {
            workloadQueryDto.setQueryDate(DateUtil.format(DateUtil.date(), "yyyy-MM-dd"));
        }
        List<Workload> workloadList = this.workloadService.queryWorkload(workloadQueryDto);
        return AjaxResult.success(workloadList);
    }


    /**
     * 总体工作量统计列表
     */
    @GetMapping("queryWorkloadStat")
    public AjaxResult queryWorkloadStat(WorkloadQueryDto workloadQueryDto) {
        if (workloadQueryDto.getBeginTime() == null) {
            workloadQueryDto.setQueryDate(DateUtil.format(DateUtil.date(), "yyyy-MM-dd"));
        }
        List<WorkloadStat> workloadList = this.workloadService.queryWorkloadStat(workloadQueryDto);
        return AjaxResult.success(workloadList);
    }
}
