package com.hanwei.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author hanwei
 * @ClassName LoginInfoDto
 * @date 2020/11/8
 */
@ApiModel(value="com-hanwei-dto-LoginInfoDto")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfoDto extends BaseDto {

    private static final long serialVersionUID = -3333485632790912964L;
    /**
     * 用户名称
     */
    @ApiModelProperty(value="用户名称")
    private String userName;

    /**
     * 登陆账号
     */
    @ApiModelProperty(value="登陆账号")
    private String loginAccount;

    /**
     * 登录IP地址
     */
    @ApiModelProperty(value="登录IP地址")
    private String ipAddr;

    /**
     * 登录状态（0成功 1失败）字典表
     */
    @ApiModelProperty(value="登录状态（0成功 1失败）字典表")
    private String loginStatus;

    /**
     * 登陆类型0系统用户1患者用户 字典表
     */
    @ApiModelProperty(value="登陆类型 0系统用户 1患者用户 字典表")
    private String loginType;
}
