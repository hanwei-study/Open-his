package com.hanwei.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel(value = "com-hanwei-dto-RegistrationQueryDto")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationQueryDto extends BaseDto {

    private static final long serialVersionUID = 2224750559534327451L;

    private Long deptId;
    /**
     * 挂号类型
     */
    private String schedulingType;
    /**
     * 挂号时段
     */
    private String subsectionType;
    /**
     * 查询时间
     */
    private String schedulingDay;
}
