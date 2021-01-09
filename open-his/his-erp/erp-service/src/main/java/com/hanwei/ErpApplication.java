package com.hanwei;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author hanwei
 * @ClassName ErpApplication
 * @date 2020/9/23
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.hanwei.mapper"})
@EnableDubbo
@EnableTransactionManagement
public class ErpApplication {
    public static void main(String[] args) {
        SpringApplication.run(ErpApplication.class);
    }
}
