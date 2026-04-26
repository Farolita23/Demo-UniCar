package com.daw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationLauncher {

	/**
	 * Método principal de arranque de la aplicación.
	 *
	 * @param args argumentos de línea de comandos pasados al contexto de Spring
	 */
	public static void main(String[] args) {
		SpringApplication.run(ApplicationLauncher.class, args);
	}

}
