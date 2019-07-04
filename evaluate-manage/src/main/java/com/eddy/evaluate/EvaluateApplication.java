package com.eddy.evaluate;

import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


@MapperScan(basePackages = {"com.eddy.evaluate.dao"})
@ServletComponentScan
@Log4j2
@SpringBootApplication
public class EvaluateApplication {

    public static void main(String[] args) {
        log.info("-----启动中-----");
        SpringApplication.run(EvaluateApplication.class, args);
        log.info("-----启动完成-----");
    }

}
