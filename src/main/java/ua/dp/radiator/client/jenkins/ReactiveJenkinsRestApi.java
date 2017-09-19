package ua.dp.radiator.client.jenkins;

import java.nio.charset.Charset;
import java.util.Base64;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import reactor.core.publisher.Mono;
import ua.dp.radiator.client.jenkins.api.BuildDetails;
import ua.dp.radiator.config.properties.RadiatorProperties;
import ua.dp.radiator.utils.TypeUtils;

@Component
public class ReactiveJenkinsRestApi implements JenkinsRestApi {

	private RadiatorProperties properties;
	private WebClient webClient;


	public ReactiveJenkinsRestApi(RadiatorProperties properties, WebClient webClient) {
		this.properties = properties;
//		this.webClient =  WebClient.create();
		this.webClient = webClient;
	}

	public Mono<Integer> loadLastBuildNumber(Mono<String> baseUrl) {
		return baseUrl.flatMap(
				urlPrefix -> loadInteger(urlPrefix + "/lastBuild/buildNumber")
						.onErrorReturn(0)
		);
	}

	public Mono<Integer> loadLastSuccessfulBuildNumber(Mono<String> baseUrl) {
		return baseUrl.flatMap(
				urlPrefix -> loadInteger(urlPrefix + "/lastSuccessfulBuild/buildNumber")
						.onErrorReturn(0)
		);
	}

	public Mono<Integer> loadLastFailedBuildNumber(Mono<String> baseUrl) {
		return baseUrl.flatMap(
				urlPrefix -> loadInteger(urlPrefix + "/lastFailedBuild/buildNumber")
						.onErrorReturn(0)
		);
	}

	private ResponseSpec load(String fullUrl) {
		return webClient
                    .get()
                    .uri(fullUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + new String(Base64.getEncoder().encode((properties.buildState.auth.username + ":" + properties.buildState.auth.password).getBytes())))
                    .accept(MediaType.TEXT_PLAIN)
                    .acceptCharset(Charset.forName("US-ASCII"))
                    .retrieve();
	}

	private Mono<Integer> loadInteger(String fullUrl) {
		return load(fullUrl)
				.bodyToMono(String.class)
				.onErrorReturn("0") //TODO
				.log("loadInteger: ")
				.map(TypeUtils::toIntegerOrNull);
	}



	public Mono<BuildDetails> loadBuildDetails(String daseUrl, Integer lastFailedBuild) {
		return load(daseUrl + "/" + lastFailedBuild + "/api/json")
				.bodyToMono(BuildDetails.class)
				.log("loadBuildDetails: ")
				.onErrorReturn(null);
	}
}

