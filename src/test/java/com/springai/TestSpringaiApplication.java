package com.springai;

import org.springframework.boot.SpringApplication;

public class TestSpringaiApplication {

	public static void main(String[] args) {
		SpringApplication.from(SpringaiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
