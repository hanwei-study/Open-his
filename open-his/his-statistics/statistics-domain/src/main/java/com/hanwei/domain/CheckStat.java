package com.hanwei.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class CheckStat extends BaseEntity {
    private static final long serialVersionUID = -5014889101404048486L;
    /**
     * 检查项目ID
     */
    private Long checkItemId;

    /**
     * 检查项目名称
     */
    private String checkItemName;

    /**
     * 项目总价
     */
    private BigDecimal totalAmount;

    /**
     * 检查次数
     */
    private Integer count;


}
