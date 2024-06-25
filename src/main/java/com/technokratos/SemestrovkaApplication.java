package com.technokratos;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class SemestrovkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SemestrovkaApplication.class, args);
    }

}
