package com.hanwei.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel(value="com-hanwei-dto-CheckResultDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class CheckResultDto extends BaseDto {
    /**
     * 检查项目IDS
     */
    private List<Integer> checkItemIds;
    /**
     * 患者姓名
     */
    @ApiModelProperty(value="患者姓名")
    private String patientName;

    /**
     * 是否录入检查结果0 检测中  1 检测完成  字典表 his_check_result_status
     */
    @ApiModelProperty(value="是否录入检查结果0 检测中  1 检测完成  字典表 his_check_result_status")
    private String resultStatus;

    /**
     * 关联挂号单号
     */
    @ApiModelProperty(value="关联挂号单号")
    private String regId;
}
