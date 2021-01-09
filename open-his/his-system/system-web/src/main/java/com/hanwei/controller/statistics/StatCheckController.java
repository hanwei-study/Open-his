package com.hanwei.controller.statistics;

import cn.hutool.core.date.DateUtil;
import com.hanwei.controller.BaseController;
import com.hanwei.domain.Check;
import com.hanwei.domain.CheckStat;
import com.hanwei.dto.CheckQueryDto;
import com.hanwei.service.CheckService;
import com.hanwei.vo.AjaxResult;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("statistics/check")
public class StatCheckController extends BaseController {

    @Reference
    private CheckService checkService;

    /**
     * 查询检查项列表
     */
    @GetMapping("queryCheck")
    public AjaxResult queryCheck(CheckQueryDto checkQueryDto){
        if(checkQueryDto.getBeginTime()==null){
            checkQueryDto.setQueryDate(DateUtil.format(DateUtil.date(),"yyyy-MM-dd"));
        }
        List<Check> checkList=this.checkService.queryCheck(checkQueryDto);
        return AjaxResult.success(checkList);
    }


    /**
     * 查询检查项统计列表
     */
    @GetMapping("queryCheckStat")
    public AjaxResult queryCheckStat(CheckQueryDto checkQueryDto){
        if(checkQueryDto.getBeginTime()==null){
            checkQueryDto.setQueryDate(DateUtil.format(DateUtil.date(),"yyyy-MM-dd"));
        }
        List<CheckStat> checkList=this.checkService.queryCheckStat(checkQueryDto);
        return AjaxResult.success(checkList);
    }
}
