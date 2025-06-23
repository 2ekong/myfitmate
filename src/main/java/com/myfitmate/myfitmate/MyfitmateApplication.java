package com.myfitmate.myfitmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyfitmateApplication {

	public static void main(String[] args) {

		System.out.println("=== 앱 시작됨 ===");
		SpringApplication.run(MyfitmateApplication.class, args);
	}

}
