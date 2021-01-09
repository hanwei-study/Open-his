package com.hanwei.dto;

import com.hanwei.domain.SimpleUser;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础数据传输对象
 * @author hanwei
 * @ClassName BaseDto
 * @date 2020/9/24
 */
@Data
public class BaseDto implements Serializable {
    private static final long serialVersionUID = -2628248190256947466L;
    /**
     * 页码 默认1
     */
    private Integer pageNum = 1;

    /**
     * 每页显示条数 默认10
     */
    private Integer pageSize = 10;

    /**
     * 当前操作对象
     */
    private SimpleUser simpleUser;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

}
