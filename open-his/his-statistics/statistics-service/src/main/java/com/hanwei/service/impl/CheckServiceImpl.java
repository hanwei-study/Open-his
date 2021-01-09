package com.hanwei.service.impl;

import com.hanwei.domain.Check;
import com.hanwei.domain.CheckStat;
import com.hanwei.dto.CheckQueryDto;
import com.hanwei.mapper.CheckMapper;
import com.hanwei.service.CheckService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CheckServiceImpl implements CheckService {


    @Autowired
    private CheckMapper checkMapper;

    @Override
    public List<Check> queryCheck(CheckQueryDto checkQueryDto) {
        return this.checkMapper.queryCheck(checkQueryDto);
    }

    @Override
    public List<CheckStat> queryCheckStat(CheckQueryDto checkQueryDto) {
        return this.checkMapper.queryCheckStat(checkQueryDto);
    }
}
