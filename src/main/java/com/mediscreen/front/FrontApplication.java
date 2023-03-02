package com.mediscreen.front;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.mediscreen")
public class FrontApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(FrontApplication.class, args);
	}

	@Override
	public void run(String... args) {

		System.out.println(" ");
		System.out.println("  ______                   ");
		System.out.println(" |  ____|              _   ");
		System.out.println(" | |__ _ __ ___  _ __ | |_ ");
		System.out.println(" |  __| '__/ _ \\| '_ \\| __|");
		System.out.println(" | |  | | | (_) | | | | |_ ");
		System.out.println(" |_|  |_|  \\___/|_| |_|\\__|");
		System.out.println(" ==========================");
		System.out.println(" :: WEBAPP ::      (v1.0.0)");
		System.out.println(" ");
	}
}
