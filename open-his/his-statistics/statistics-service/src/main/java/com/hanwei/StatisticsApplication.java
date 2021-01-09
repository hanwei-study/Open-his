package com.hanwei;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hanwei
 * @ClassName StatisticsApplication
 * @date 2020/9/23
 */
@EnableDubbo
@SpringBootApplication
@MapperScan(basePackages = {"com.hanwei.mapper"})
public class StatisticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(StatisticsApplication.class);
    }
}
