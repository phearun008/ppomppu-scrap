package com.ppomppu;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class PpomppuApplication implements CommandLineRunner{

	
	public static void main(String[] args) {
		SpringApplication.run(PpomppuApplication.class, args);
	}
	
	@Override
	public void run(String... arg0) throws Exception {
		
	}
}
