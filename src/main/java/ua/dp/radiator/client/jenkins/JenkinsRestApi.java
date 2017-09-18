package ua.dp.radiator.client.jenkins;

import reactor.core.publisher.Mono;
import ua.dp.radiator.client.jenkins.api.BuildDetails;

import static java.lang.String.format;

public interface JenkinsRestApi {

	Mono<Integer> loadLastBuildNumber(String url);

	Mono<Integer> loadLastSuccessfulBuildNumber(String url);

	Mono<Integer> loadLastFailedBuildNumber(String url);

	Mono<BuildDetails> loadBuildDetails(String url, Integer buildId);
}

