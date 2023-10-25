package com.wth.wthinterface;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;

@SpringBootApplication
public class WthInterfaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WthInterfaceApplication.class, args);
    }

}
