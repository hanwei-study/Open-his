package com.hanwei.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class Income  extends BaseEntity {

    private static final long serialVersionUID = 38646072537811470L;
    private Double orderAmount;

    private String payType;
}
