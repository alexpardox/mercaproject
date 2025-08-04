package com.merca.merca;

import com.merca.merca.config.EnvironmentConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MercaApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(MercaApplication.class);
		app.addListeners(new EnvironmentConfig());
		app.run(args);
	}

}
