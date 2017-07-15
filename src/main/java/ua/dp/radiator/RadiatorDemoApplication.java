package ua.dp.radiator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class RadiatorDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RadiatorDemoApplication.class, args);
	}
}
