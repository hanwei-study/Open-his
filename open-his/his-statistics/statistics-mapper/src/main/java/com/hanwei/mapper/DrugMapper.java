package com.hanwei.mapper;

import com.hanwei.domain.Drug;
import com.hanwei.domain.DrugStat;
import com.hanwei.dto.DrugQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hanwei
 * @ClassName DrugMapper
 * @date 2020/12/17
 */
public interface DrugMapper {
    /**
     * 药品统计列表
     *
     * @param drugQueryDto
     * @return
     */
    List<Drug> queryDrug(@Param("drug") DrugQueryDto drugQueryDto);

    /**
     * 药品数量统计列表
     *
     * @param drugQueryDto
     * @return
     */
    List<DrugStat> queryDrugStat(@Param("drug") DrugQueryDto drugQueryDto);
}
