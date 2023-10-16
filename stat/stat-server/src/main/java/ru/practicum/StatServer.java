package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ru.practicum")
public class StatServer {
    public static void main(String[] args) {
        SpringApplication.run(StatServer.class, args);
    }
}
