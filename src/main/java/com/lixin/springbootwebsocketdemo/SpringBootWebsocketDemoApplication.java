package com.lixin.springbootwebsocketdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author lixin
 */
@SpringBootApplication
@Slf4j
public class SpringBootWebsocketDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebsocketDemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> log.info("application started");
    }

}
