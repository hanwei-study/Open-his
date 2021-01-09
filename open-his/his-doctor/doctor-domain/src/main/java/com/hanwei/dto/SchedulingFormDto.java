package com.hanwei.dto;

import com.hanwei.domain.SimpleUser;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@ApiModel(value="com-hanwei-dto-SchedulingFormDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulingFormDto implements Serializable {

    private static final long serialVersionUID = -3663577674255681733L;
    private SimpleUser simpleUser;

    private String beginDate;

    private List<SchedulingData> data;

    @Data
    public static class SchedulingData implements  Serializable{
        private static final long serialVersionUID = -7950644091641287848L;
        private Long userId;
        private Long deptId;
        private String subsectionType; //上午 下午  晚上
        //星期的值班值
        private Collection<String> schedulingType;
    }
}
