package com.hanwei.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel(value="com-bjsxt-dto-DrugQueryDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DrugQueryDto extends BaseDto {
    private static final long serialVersionUID = -1183655855434638023L;
    private String drugName;
    private String queryDate;
}
