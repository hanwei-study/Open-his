package com.hanwei.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hanwei
 * @ClassName ActiveUser
 * @date 2020/9/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveUser implements Serializable {
    private static final long serialVersionUID = -2810489668594552779L;

    private User user;
    /**
     * 角色
     */
    private List<String> roles = new ArrayList<>();

    /**
     * 权限
     */
    private List<String> permissions = new ArrayList<>();
}
