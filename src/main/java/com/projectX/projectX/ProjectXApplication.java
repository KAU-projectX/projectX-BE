package com.projectX.projectX;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class ProjectXApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectXApplication.class, args);
	}

}

@Configuration
@EnableJpaAuditing
class ApplicationConfig {}