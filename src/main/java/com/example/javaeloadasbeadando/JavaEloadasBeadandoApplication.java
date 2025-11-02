package com.example.javaeloadasbeadando;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.javaeloadasbeadando")
public class JavaEloadasBeadandoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaEloadasBeadandoApplication.class, args);
    }

}
