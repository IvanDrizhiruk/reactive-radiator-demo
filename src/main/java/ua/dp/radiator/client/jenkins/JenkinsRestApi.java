package ua.dp.radiator.client.jenkins;

import reactor.core.publisher.Mono;
import ua.dp.radiator.client.jenkins.api.BuildDetails;

import static java.lang.String.format;

public interface JenkinsRestApi {

	Mono<Integer> loadLastBuildNumber(Mono<String> baseUrl);

	Mono<Integer> loadLastSuccessfulBuildNumber(Mono<String> baseUrl);

	Mono<Integer> loadLastFailedBuildNumber(Mono<String> baseUrl);

	Mono<BuildDetails> loadBuildDetails(String url, Integer buildId);
}

