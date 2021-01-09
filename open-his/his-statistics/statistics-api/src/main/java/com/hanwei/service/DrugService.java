package com.hanwei.service;

import com.hanwei.domain.Drug;
import com.hanwei.domain.DrugStat;
import com.hanwei.dto.DrugQueryDto;

import java.util.List;

/**
 * @author hanwei
 * @ClassName DrugService
 * @date 2020/12/17
 */
public interface DrugService {
    /**
     * 查询发药统计列表
     * @param drugQueryDto
     * @return
     */
    List<Drug> queryDrug(DrugQueryDto drugQueryDto);

    /**
     * 查询发药数量统计列表
     * @param drugQueryDto
     * @return
     */
    List<DrugStat> queryDrugStat(DrugQueryDto drugQueryDto);
}
