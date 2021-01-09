package com.hanwei.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author hanwei
 * @ClassName SimpleUser
 * @date 2020/9/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleUser implements Serializable{

    private static final long serialVersionUID = -8454922594746016691L;

    private Serializable userId;
    private String userName;

}
