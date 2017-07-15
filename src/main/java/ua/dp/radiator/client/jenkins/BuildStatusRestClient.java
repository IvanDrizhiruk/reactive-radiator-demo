package ua.dp.radiator.client.jenkins;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ua.dp.radiator.client.auth.BasicAutorisationRestClient;
import ua.dp.radiator.client.auth.UsarnamePasswordAuthTokenGenerator;
import ua.dp.radiator.client.jenkins.api.BuildDetails;
import ua.dp.radiator.config.properties.RadiatorProperties;
import ua.dp.radiator.utils.TypeUtils;

import javax.annotation.PostConstruct;

import static java.lang.String.format;

@Component
public class BuildStatusRestClient {
	private static Logger LOG = Logger.getLogger(BuildStatusRestClient.class.getName());

	private RadiatorProperties properties;

	private RestTemplate restClient;


	public BuildStatusRestClient(RadiatorProperties properties) {
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

	public Integer loadLastBuildNumber(String daseUrl) {
		return loadNumber(format("%s/lastBuild/buildNumber", daseUrl));
	}

	public Integer loadLastSuccessfulBuildNumber(String daseUrl) {
		return loadNumber(format("%s/lastSuccessfulBuild/buildNumber", daseUrl));
	}

	public Integer loadLastSuccessfulBuildNumber(String daseUrl, int defaultValue) {
		Integer LastSuccessfulBuildNumber = loadLastSuccessfulBuildNumber(daseUrl);
		return null == LastSuccessfulBuildNumber
				? defaultValue
				: LastSuccessfulBuildNumber;
	}
	
	public Integer loadLastFailedBuildNumber(String daseUrl) {
		return loadNumber(format("%s/lastFailedBuild/buildNumber", daseUrl));
	}

	public Integer loadLastFailedBuildNumber(String daseUrl, int defaultValue) {
		Integer lastFailedBuildNumber = loadLastFailedBuildNumber(daseUrl);
		return null == lastFailedBuildNumber
				? defaultValue
				: lastFailedBuildNumber;
	}

	private Integer loadNumber(String fullUrl) {
		return TypeUtils.toIntegerOrNull(loadString(fullUrl));
	}

	private String loadString(String fullUrl) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(format("Try load from url ", fullUrl));
		}

		try {
			return restClient.getForObject(fullUrl, String.class);
		} catch (Exception e) {
			return null;
		}
	}


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

