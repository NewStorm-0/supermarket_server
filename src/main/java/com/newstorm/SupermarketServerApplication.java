package com.newstorm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author NewStorm
 */
@MapperScan("com.newstorm.mapper")
@SpringBootApplication
public class SupermarketServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupermarketServerApplication.class, args);
    }

}
