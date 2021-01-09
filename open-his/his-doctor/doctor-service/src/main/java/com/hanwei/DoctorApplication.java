package com.hanwei;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hanwei
 * @ClassName com.hanwei.DoctorApplication
 * @date 2020/9/23
 */
@SpringBootApplication
@EnableDubbo
@MapperScan("com.hanwei.mapper")
public class DoctorApplication {
    public static void main(String[] args) {
        SpringApplication.run(DoctorApplication.class);
    }
}
