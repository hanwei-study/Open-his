package com.hanwei.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 表格数据传输对象
 * @author hanwei
 * @ClassName DataGridView
 * @date 2020/9/27
 */
@ApiModel(value = "com-hanwei-vo-DataGridView")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataGridView implements Serializable {

    private static final long serialVersionUID = -2172466658152390061L;

    private Long total;
    private List<?> data;
}
