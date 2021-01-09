package com.hanwei.controller.statistics;

import cn.hutool.core.date.DateUtil;
import com.hanwei.controller.BaseController;
import com.hanwei.dto.RevenueQueryDto;
import com.hanwei.service.RevenueService;
import com.hanwei.vo.AjaxResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("statistics/revenue")
public class RevenueController extends BaseController {


    @Reference
    private RevenueService revenueService;

    @GetMapping("queryAllRevenueData")
    @HystrixCommand
    public AjaxResult queryAllRevenueData(RevenueQueryDto revenueQueryDto){
        //如果没有选择开始日期和结果日期，就查询当天的数据
        if(revenueQueryDto.getBeginTime()==null){
            revenueQueryDto.setQueryDate(DateUtil.format(DateUtil.date(),"yyyy-MM-dd"));
        }
        Map<String,Object> res = this.revenueService.queryAllRevenueData(revenueQueryDto);
        return AjaxResult.success(res);
    }
}
