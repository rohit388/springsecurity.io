package com.springsecurity.io;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

/*
⚠ Common Mistakes

Agar response me sab null aa raha hai, to:

DTO aur Entity ke field names alag hain (e.g., expenseDate vs date).

Mapper interface me @Mapper(componentModel = "spring") nahi diya.

Maven clean install nahi kiya (annotation processor se generated code compile nahi hua).*/
