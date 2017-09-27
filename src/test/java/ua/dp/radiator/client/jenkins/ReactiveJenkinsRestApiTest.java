package ua.dp.radiator.client.jenkins;

import static org.mockito.Mockito.verify;
import static ua.dp.radiator.Helper.mockWebClient;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ua.dp.radiator.config.properties.RadiatorProperties;


public class ReactiveJenkinsRestApiTest {

	@Test
	public void lastBuildNumberShouldBeLoaded() throws Exception {
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

	@Test
	public void onErrorShouldBeCalledIfLoadLastBuildNumberReturn404Code() throws Exception {
		//given
		WebClientException error = new WebClientException("ClientResponse has erroneous status code: 404 Not Found");
		Mono<String> response = Mono.error(error);

		WebClient webClientMock = mockWebClient(response);

		Mono<String> baseUrl = Mono.just("http://localhost:8080");

		//when
		Mono<Integer> actual = new ReactiveJenkinsRestApi(webClientMock, newProperties())
				.loadLastBuildNumber(baseUrl);

		//then
		StepVerifier.create(actual)
				.expectError(WebClientException.class)
				.verify();

		verify(webClientMock.get()).uri("http://localhost:8080/lastBuild/buildNumber");
	}

	@Test
	public void onErrorShouldBeCalledIfLoadLastBuildNumberReceiveTimeout() throws Exception {
		//given
		Mono<String> response = Mono.just("7")
				.delayElement(Duration.ofMillis(2000));

		WebClient webClientMock = mockWebClient(response);

		Mono<String> baseUrl = Mono.just("http://localhost:8080");

		//when
		Mono<Integer> actual = new ReactiveJenkinsRestApi(webClientMock, newProperties())
				.loadLastBuildNumber(baseUrl);

		//then
		StepVerifier.create(actual)
				.expectSubscription()
				.expectNoEvent(Duration.ofMillis(900))
				.expectError(TimeoutException.class)
				.verify();

		verify(webClientMock.get()).uri("http://localhost:8080/lastBuild/buildNumber");
	}

	private RadiatorProperties newProperties() {
		RadiatorProperties properties = new RadiatorProperties();
		properties.buildState.auth = new RadiatorProperties.BuildState.Authorisation();
		properties.buildState.auth.username = "user";
		properties.buildState.auth.password = "secret";

		properties.buildState.requestMaxTimeMilliseconds = 1000;

		return properties;
	}

}