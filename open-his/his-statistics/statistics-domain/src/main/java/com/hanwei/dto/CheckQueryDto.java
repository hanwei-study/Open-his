package com.hanwei.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel(value="com-hanwei-dto-CheckQueryDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CheckQueryDto extends BaseDto {
    private static final long serialVersionUID = 689559917462313825L;
    private Long checkItemId;
    private String patientName;
    private String queryDate;
}
