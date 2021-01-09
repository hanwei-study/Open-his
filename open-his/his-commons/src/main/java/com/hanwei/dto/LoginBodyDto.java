package com.hanwei.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author hanwei
 * @ClassName LoginBodyDto
 * @date 2020/9/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginBodyDto {
    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    private String username;
    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    private String password;
    /**
     * 验证码
     */
    private String code;
}
