package com.hanwei.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hanwei.dto.SchedulingFormDto;
import com.hanwei.dto.SchedulingQueryDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.hanwei.mapper.SchedulingMapper;
import com.hanwei.domain.Scheduling;
import com.hanwei.service.SchedulingService;

@Service
public class SchedulingServiceImpl implements SchedulingService{

    @Autowired
    private SchedulingMapper schedulingMapper;

    @Override
    public List<Scheduling> queryScheduling(SchedulingQueryDto schedulingDto) {
        QueryWrapper<Scheduling> qw=new QueryWrapper<>();
        qw.eq(null!=schedulingDto.getUserId(),Scheduling.COL_USER_ID,schedulingDto.getUserId());
        qw.eq(null!=schedulingDto.getDeptId(),Scheduling.COL_DEPT_ID,schedulingDto.getDeptId());
        qw.ge(StringUtils.isNotBlank(schedulingDto.getBeginDate()),Scheduling.COL_SCHEDULING_DAY,schedulingDto.getBeginDate());
        qw.le(StringUtils.isNotBlank(schedulingDto.getEndDate()),Scheduling.COL_SCHEDULING_DAY,schedulingDto.getEndDate());
        return this.schedulingMapper.selectList(qw);
    }

    @Override
    public int saveScheduling(SchedulingFormDto schedulingFormDto) {
        if (null != schedulingFormDto.getData() && schedulingFormDto.getData().size() > 0) {
            DateTime dateTime = DateUtil.parse(schedulingFormDto.getBeginDate(), "yyyy-MM-dd");
            DateTime date = DateUtil.beginOfWeek(dateTime);
            String beginDate = DateUtil.format(date, "yyyy-MM-dd");
            String endDate = DateUtil.format(DateUtil.endOfWeek(dateTime), "yyyy-MM-dd");
            SchedulingFormDto.SchedulingData schedulingData = schedulingFormDto.getData().get(0);
            Long userId = schedulingData.getUserId();
            Long deptId = schedulingData.getDeptId();
            if (null != userId) {
                //删除原来的排班
                QueryWrapper<Scheduling> qw = new QueryWrapper<>();
                qw.eq(Scheduling.COL_USER_ID, userId);
                qw.eq(Scheduling.COL_DEPT_ID, deptId);
                qw.between(Scheduling.COL_SCHEDULING_DAY, beginDate, endDate);
                this.schedulingMapper.delete(qw);
                //添加新的排班
                List<String> schedulingDays = initSchedulingDay(date);
                for (SchedulingFormDto.SchedulingData d : schedulingFormDto.getData()) {
                    Scheduling scheduling;
                    int i = 0;
                    for (String s : d.getSchedulingType()) {
                        if (StringUtils.isNotBlank(s)) {
                            scheduling = new Scheduling(userId, deptId, schedulingDays.get(i), d.getSubsectionType(), s, new Date(), schedulingFormDto.getSimpleUser().getUserName());
                            this.schedulingMapper.insert(scheduling);
                        }
                        i++;
                    }
                }
                //受影响的行数
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }
    /**
     * 初始化值班记录
     * @param startDate 当周第一天
     * @return
     */
    private static List<String> initSchedulingDay(Date startDate){
        List<String> list = new ArrayList<>();
        for (int i=0;i<7;i++){
            DateTime d = DateUtil.offsetDay(startDate,i);
            String key = DateUtil.format(d, "yyyy-MM-dd");
            list.add(key);
        }
        return  list;
    }

    @Override
    public List<Long> queryHasSchedulingDeptIds(Long deptId, String schedulingDay, String schedulingType, String subsectionType) {
        return this.schedulingMapper.queryHasSchedulingDeptIds(deptId,schedulingDay,schedulingType,subsectionType);
    }


}
