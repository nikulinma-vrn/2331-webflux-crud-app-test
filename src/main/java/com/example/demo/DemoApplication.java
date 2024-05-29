package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.util.List;

@EnableAutoConfiguration
@SpringBootApplication
public class DemoApplication {

	@Autowired
	private static BaseDocRepository bdr;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
