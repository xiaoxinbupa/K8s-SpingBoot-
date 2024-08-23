package com.tarena.passport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = "com.tarena.passport.auto")
//@EnableAutoConfiguration
//@ComponentScan(basePackages = "com.tarena.passport.auto")
public class AutoTestStarter {
    public static void main(String[] args) {
        SpringApplication.run(AutoTestStarter.class,args);
    }
}
