package com.hanwei.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@ApiModel(value="com-bjsxt-dto-OrderChargeDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderChargeFormDto extends BaseDto {

    private static final long serialVersionUID = 8367761132418464987L;
    /**
     * 主订单
     */
    private OrderChargeDto orderChargeDto;

    /**
     * 订单详情
     */
    @NotEmpty(message = "订单详情不能为空")
    private List<OrderChargeItemDto> orderChargeItemDtoList;

}
