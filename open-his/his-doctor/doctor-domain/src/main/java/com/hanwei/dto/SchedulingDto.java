package com.hanwei.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

@ApiModel(value="com-hanwei-dto-SchedulingDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulingDto implements Serializable {

    private static final long serialVersionUID = 739327825426946544L;
    private Long userId;
    private Long deptId;
    /**
     * 记录是上午、下午还是夜晚
     */
    private String subsectionType;

    /**
     * 一个星期的值班类型
     */
    private Collection<String> schedulingType;

    /**
     * 一周的值班记录，key为日期
     */
    @JsonIgnore
    private Map<String,String> record ;


    public SchedulingDto(Long userId, Long deptId, String subsectionType, Map<String,String> map) {
        this.userId = userId;
        this.subsectionType = subsectionType;
        this.record = map;
        this.deptId=deptId;
    }
}

