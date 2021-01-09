package com.hanwei.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hanwei
 * @ClassName MenuTreeVo
 * @date 2020/9/24
 */
@ApiModel(value = "com-hanwei-vo-MenuTreeVo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuTreeVo {

    /**
     * 菜单id
     */
    private String id;

    /**
     * 菜单表的URL
     */
    private String serPath;

    /**
     * 是否显示
     */
    private boolean show = true;

    public MenuTreeVo(String id, String serPath) {
        this.id = id;
        this.serPath = serPath;
    }

}

