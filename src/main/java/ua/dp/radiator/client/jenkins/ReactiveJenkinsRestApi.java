package ua.dp.radiator.client.jenkins;

import static java.lang.String.format;

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


	public ReactiveJenkinsRestApi(RadiatorProperties properties) {
		this.properties = properties;
	}

//	@PostConstruct
//	public void init() {
//		if (null == restClient) {
//			UsarnamePasswordAuthTokenGenerator generator = new UsarnamePasswordAuthTokenGenerator(
//					properties.buildState.auth.username,
//					properties.buildState.auth.password);
//			restClient = new BasicAutorisationRestClient(generator);
//		}
//	}

	public Mono<Integer> loadLastBuildNumber(String daseUrl) {
		return loadInteger(daseUrl, "/lastBuild/buildNumber")
				.onErrorReturn(0);
	}

	public Mono<Integer> loadLastSuccessfulBuildNumber(String daseUrl) {
		return loadInteger(daseUrl, "/lastSuccessfulBuild/buildNumber")
                .onErrorReturn(0);
	}

	public Mono<Integer> loadLastFailedBuildNumber(String daseUrl) {
		return loadInteger(daseUrl, "/lastFailedBuild/buildNumber")
                .onErrorReturn(0);
	}

	private ResponseSpec load(String daseUrl, String uri) {
		return WebClient.create(daseUrl)
                    .get()
                    .uri(uri)
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + new String(Base64.getEncoder().encode((properties.buildState.auth.username + ":" + properties.buildState.auth.password).getBytes())))
                    .accept(MediaType.TEXT_PLAIN)
                    .acceptCharset(Charset.forName("US-ASCII"))
                    .retrieve();
	}

	private Mono<Integer> loadInteger(String daseUrl, String uri) {
		return load(daseUrl, uri)
				.bodyToMono(String.class)
				.onErrorReturn("0") //TODO
				.log("loadInteger: ")
				.map(TypeUtils::toIntegerOrNull);
	}



	public Mono<BuildDetails> loadBuildDetails(String daseUrl, Integer lastFailedBuild) {
		System.out.println("==>  "  + daseUrl + lastFailedBuild + "/api/json");

		return load(daseUrl, "/" + lastFailedBuild + "/api/json")
				.bodyToMono(BuildDetails.class)
				.log("loadBuildDetails: ")
				.onErrorReturn(null);
	}
}

