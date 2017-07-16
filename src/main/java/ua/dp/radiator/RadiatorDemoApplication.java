package ua.dp.radiator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
public class RadiatorDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RadiatorDemoApplication.class, args);
	}

	@Bean
	public RouterFunction<ServerResponse> route() {
		//It is hacked for fix problem with mapping of static-resources
		return RouterFunctions.resources("/**", new ClassPathResource("static/"));
	}

}
