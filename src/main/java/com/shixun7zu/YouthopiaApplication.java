package com.shixun7zu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.shixun7zu.mapper")
public class YouthopiaApplication {

    public static void main(String[] args) {
        SpringApplication.run(YouthopiaApplication.class, args);
    }

}
