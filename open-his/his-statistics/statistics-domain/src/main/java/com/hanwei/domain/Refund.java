package com.hanwei.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class Refund extends BaseEntity {

    private static final long serialVersionUID = -6717301009287130789L;
    private Double backAmount;

    private String backType;
}
