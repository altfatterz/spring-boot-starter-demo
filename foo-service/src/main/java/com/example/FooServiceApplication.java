package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class FooServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FooServiceApplication.class, args);
    }

    @RestController
    static class FooController {

        @GetMapping("/api/foo")
        public String foo() {
            return "foo";
        }

    }
}

