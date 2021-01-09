package com.hanwei.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel(value="com-hanwei-dto-PurchaseFromDto")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseFormDto extends  BaseDto {

    private static final long serialVersionUID = -5915526295992981660L;

    //存放采购单主表数据

    private PurchaseDto purchaseDto;

    //存放采购单详情数据

    private List<PurchaseItemDto> purchaseItemDtos;
}
