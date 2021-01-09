package com.hanwei.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@ApiModel(value="com-hanwei-dto-OrderBackfeeFormDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderBackfeeFormDto extends BaseDto {

    private static final long serialVersionUID = 8438800112522541177L;
    /**
     * 主订单
     */
    private OrderBackfeeDto orderBackfeeDto;

    /**
     * 订单详情
     */
    @NotEmpty(message = "退费订单详情不能为空")
    private List<OrderBackfeeItemDto> orderBackfeeItemDtoList;

}

