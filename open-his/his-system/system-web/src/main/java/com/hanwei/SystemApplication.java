package com.hanwei;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


/**
 * @author hanwei
 * @ClassName SystemApplication
 * @date 2020/9/23
 */
@SpringBootApplication
@MapperScan( basePackages = {"com.hanwei.mapper"})
@EnableDubbo
@EnableAspectJAutoProxy
@EnableHystrix //启用hystrix
@EnableCircuitBreaker //启用hystrix的断路保存
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class);
    }
}
