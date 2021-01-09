package com.hanwei.service.impl;

import com.hanwei.domain.Drug;
import com.hanwei.domain.DrugStat;
import com.hanwei.dto.DrugQueryDto;
import com.hanwei.mapper.DrugMapper;
import com.hanwei.service.DrugService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class DrugServiceImpl implements DrugService {


    @Autowired
    private DrugMapper drugMapper;

    @Override
    public List<Drug> queryDrug(DrugQueryDto drugQueryDto) {
        return this.drugMapper.queryDrug(drugQueryDto);
    }

    @Override
    public List<DrugStat> queryDrugStat(DrugQueryDto drugQueryDto) {
        return this.drugMapper.queryDrugStat(drugQueryDto);
    }
}
