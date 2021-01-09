package com.hanwei.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value="com-hanwei-dto-SchedulingQueryDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulingQueryDto implements Serializable {

    private static final long serialVersionUID = 2474134059164259842L;

    private Long userId;
    private Long deptId;

    /**
     * 用于上一周下一周的按钮选择
     */
    private String queryDate;

    private String beginDate;

    private String endDate;
}
