package ua.dp.radiator.client.jenkins;

import static java.lang.String.format;

import java.nio.charset.Charset;
import java.util.Base64;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import ua.dp.radiator.client.auth.BasicAutorisationRestClient;
import ua.dp.radiator.client.auth.UsarnamePasswordAuthTokenGenerator;
import ua.dp.radiator.client.jenkins.api.BuildDetails;
import ua.dp.radiator.config.properties.RadiatorProperties;
import ua.dp.radiator.utils.TypeUtils;

@Component
public class ReactiveJenkinsRestApi implements JenkinsRestApi {
	private static Logger LOG = Logger.getLogger(ReactiveJenkinsRestApi.class.getName());

	private RadiatorProperties properties;

	private RestTemplate restClient;


	public ReactiveJenkinsRestApi(RadiatorProperties properties) {
		this.properties = properties;
	}

	@PostConstruct
	public void init() {
		if (null == restClient) {
			UsarnamePasswordAuthTokenGenerator generator = new UsarnamePasswordAuthTokenGenerator(
					properties.buildState.auth.username,
					properties.buildState.auth.password);
			restClient = new BasicAutorisationRestClient(generator);
		}
	}

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




//	public Integer loadLastSuccessfulBuildNumber(String daseUrl, int defaultValue) {
//		Mono<Integer> lastSuccessfulBuildNumber = loadLastSuccessfulBuildNumber(daseUrl);
//
//		Integer number = lastSuccessfulBuildNumber.block();
//		return null == number
//				? defaultValue
//				: number;
//	}
//
//	public Integer loadLastFailedBuildNumber(String daseUrl, int defaultValue) {
//		Mono<Integer> lastFailedBuildNumberMono = loadLastFailedBuildNumber(daseUrl);
//
//		Integer lastFailedBuildNumber = lastFailedBuildNumberMono.block();
//
//		return null == lastFailedBuildNumber
//				? defaultValue
//				: lastFailedBuildNumber;
//	}

	private Mono<Integer> loadInteger(String daseUrl, String uri) {
		return WebClient.create(daseUrl)
				.get()
				.uri(uri)
				.header(HttpHeaders.AUTHORIZATION, "Basic " + new String(Base64.getEncoder().encode((properties.buildState.auth.username + ":" + properties.buildState.auth.password).getBytes())))
				.accept(MediaType.TEXT_PLAIN)
				.acceptCharset(Charset.forName("US-ASCII"))
				.retrieve()
				.bodyToMono(String.class)
				.onErrorReturn("0")
				.log("webClient: ")
				.map(TypeUtils::toIntegerOrNull);
	}

//	private Integer loadNumber(String fullUrl) {
//		return TypeUtils.toIntegerOrNull(loadString(fullUrl));
//	}

//	private String loadString(String fullUrl) {
//		if (LOG.isDebugEnabled()) {
//			LOG.debug(format("Try load from url ", fullUrl));
//		}
//
//		try {
//			return jenkinsApi.getForObject(fullUrl, String.class);
//		} catch (Exception e) {
//			return null;
//		}
//	}


	public BuildDetails loadBuildDetails(String daseUrl, Integer lastFailedBuild) {
		String fullUrl = format("%s/%d/api/json", daseUrl, lastFailedBuild);
		if (LOG.isDebugEnabled()) {
			LOG.debug(format("Try load from url ", fullUrl));
		}
		BuildDetails forObject = null;
		try {
			return forObject = restClient.getForObject(fullUrl, BuildDetails.class);
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug(format("BuildDetails from url %s loaded : %s", fullUrl, forObject));
			}
		}
	}
}

