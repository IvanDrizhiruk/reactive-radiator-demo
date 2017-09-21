package ua.dp.radiator.client.jenkins;

import static org.mockito.Mockito.verify;
import static ua.dp.radiator.Helper.mockWebClient;

import org.junit.Test;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ua.dp.radiator.config.properties.RadiatorProperties;


public class ReactiveJenkinsRestApiTest {

	@Test
	public void loadLastBuildNumber() throws Exception {
		//given
		Mono<String> response = Mono.just("7");

		WebClient webClientMock = mockWebClient(response);

		Mono<String> baseUrl = Mono.just("http://localhost:8080");

		//when
		Mono<Integer> actual = new ReactiveJenkinsRestApi(webClientMock, newProperties())
				.loadLastBuildNumber(baseUrl);

		//then
		StepVerifier.create(actual)
				.expectNext(7)
				.expectComplete()
				.verify();

		verify(webClientMock.get()).uri("http://localhost:8080/lastBuild/buildNumber");
	}


	private RadiatorProperties newProperties() {
		RadiatorProperties properties = new RadiatorProperties();
		properties.buildState.auth = new RadiatorProperties.BuildState.Authorisation();
		properties.buildState.auth.username = "user";
		properties.buildState.auth.password = "secret";
		return properties;
	}

}