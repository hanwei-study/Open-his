package com.hanwei.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel(value="com-hanwei-dto-RevenueQueryDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RevenueQueryDto extends BaseDto {
    private static final long serialVersionUID = 4389003012302108741L;
    private String queryDate;
}
