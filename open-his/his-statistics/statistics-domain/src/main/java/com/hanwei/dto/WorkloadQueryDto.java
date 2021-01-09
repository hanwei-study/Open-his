package com.hanwei.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel(value="com-hanwei-dto-WorkloadQueryDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class WorkloadQueryDto extends BaseDto {
    private static final long serialVersionUID = -4144289443534622900L;
    private String doctorName;
    private String queryDate;
}
