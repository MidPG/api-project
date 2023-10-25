package com.wth.apigateway;

import com.wth.apigateway.filter.CustomGlobalFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class WthApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(WthApiGatewayApplication.class, args);
	}

}
