package ua.dp.radiator.jobs.buildstate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.time.ZonedDateTime;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.springframework.web.reactive.function.client.WebClientException;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ua.dp.radiator.client.jenkins.JenkinsRestApi;
import ua.dp.radiator.client.jenkins.api.BuildDetails;
import ua.dp.radiator.config.properties.RadiatorProperties.BuildStateInstance;
import ua.dp.radiator.domain.BuildState;
import ua.dp.radiator.utils.time.DataTimeUtils;

public class BuildStateLoaderTest {

	@Test
	public void successesBuildStateShouldBeLoaded() {
		//given
		JenkinsRestApi restClient = mockJenkinsRestApi(
				Mono.just(8),
				Mono.just(7),
				Mono.just(4),
				newBuildDetails(1506141316L));

		DataTimeUtils dataTime = mockDataTimeUtils(ZonedDateTime.parse("2017-09-23T08:00:00Z"));

		BuildStateInstance instance = new BuildStateInstance();
		instance.configUrl = "http://localhost:8080";
		instance.name = "test";
		instance.errorMessage = "Tests error";

		BuildState expected = new BuildState();
		expected.setInstancesName("test");
		expected.setState(BuildStates.SUCCESS.name());
		expected.setBuildId(7);
		expected.setExtractingDate(ZonedDateTime.parse("2017-09-23T08:00:00Z"));
		expected.setBuildInProgress(true);
		expected.setLastRunTimestemp(1506141316L);

		//when
		Mono<BuildState> actual = new BuildStateLoader(restClient,dataTime)
				.loadState(instance);

		//then
		StepVerifier.create(actual)
				.expectNextMatches(data -> {
					assertReflectionEquals(expected, data);
					return true;
				})
				.expectComplete()
				.verify();
	}

	@Test
	public void failedBuildStateShouldBeLoaded() throws Exception {
		//given
		JenkinsRestApi restClient = mockJenkinsRestApi(
				Mono.just(7),
				Mono.just(6),
				Mono.just(7),
				newBuildDetails(1506141316L));

		DataTimeUtils dataTime = mockDataTimeUtils(ZonedDateTime.parse("2017-09-23T08:00:00Z"));

		BuildStateInstance instance = new BuildStateInstance();
		instance.configUrl = "http://localhost:8080";
		instance.name = "test";
		instance.errorMessage = "Tests error";

		BuildState expected = new BuildState();
		expected.setInstancesName("test");
		expected.setState(BuildStates.FAILED.name());
		expected.setBuildId(7);
		expected.setExtractingDate(ZonedDateTime.parse("2017-09-23T08:00:00Z"));
		expected.setBuildInProgress(false);
		expected.setLastRunTimestemp(1506141316L);

		//when
		Mono<BuildState> actual = new BuildStateLoader(restClient,dataTime)
				.loadState(instance);

		//then
		StepVerifier.create(actual)
				.expectNextMatches(data -> {
					assertReflectionEquals(expected, data);
					return true;
				})
				.expectComplete()
				.verify();
	}

	@Test
	public void buildStateShouldBeLoadedIf404ErrorForGetFailedBuildNumber() {
		//given
		JenkinsRestApi restClient = mockJenkinsRestApi(
				Mono.just(8),
				Mono.just(7),
				Mono.error(new WebClientException("ClientResponse has erroneous status code: 404 Not Found")),
				newBuildDetails(1506141316L));

		DataTimeUtils dataTime = mockDataTimeUtils(ZonedDateTime.parse("2017-09-23T08:00:00Z"));

		BuildStateInstance instance = new BuildStateInstance();
		instance.configUrl = "http://localhost:8080";
		instance.name = "test";
		instance.errorMessage = "Tests error";

		BuildState expected = new BuildState();
		expected.setInstancesName("test");
		expected.setState(BuildStates.SUCCESS.name());
		expected.setBuildId(7);
		expected.setExtractingDate(ZonedDateTime.parse("2017-09-23T08:00:00Z"));
		expected.setBuildInProgress(true);
		expected.setLastRunTimestemp(1506141316L);

		//when
		Mono<BuildState> actual = new BuildStateLoader(restClient,dataTime)
				.loadState(instance);

		//then
		StepVerifier.create(actual)
				.expectNextMatches(data -> {
					assertReflectionEquals(expected, data);
					return true;
				})
				.expectComplete()
				.verify();
	}

	@Test
	public void buildStateShouldBeLoadedIf404ErrorForGetSuccessfulBuildNumber() {
		//given
		JenkinsRestApi restClient = mockJenkinsRestApi(
				Mono.just(8),
				Mono.error(new WebClientException("ClientResponse has erroneous status code: 404 Not Found")),
				Mono.just(7),
				newBuildDetails(1506141316L));

		DataTimeUtils dataTime = mockDataTimeUtils(ZonedDateTime.parse("2017-09-23T08:00:00Z"));

		BuildStateInstance instance = new BuildStateInstance();
		instance.configUrl = "http://localhost:8080";
		instance.name = "test";
		instance.errorMessage = "Tests error";

		BuildState expected = new BuildState();
		expected.setInstancesName("test");
		expected.setState(BuildStates.FAILED.name());
		expected.setBuildId(7);
		expected.setExtractingDate(ZonedDateTime.parse("2017-09-23T08:00:00Z"));
		expected.setBuildInProgress(true);
		expected.setLastRunTimestemp(1506141316L);

		//when
		Mono<BuildState> actual = new BuildStateLoader(restClient,dataTime)
				.loadState(instance);

		//then
		StepVerifier.create(actual)
				.expectNextMatches(data -> {
					assertReflectionEquals(expected, data);
					return true;
				})
				.expectComplete()
				.verify();
	}

	@Test
	public void errorShouldHappendIf404ErrorForGetLastBuildNumber() {
		//given
		JenkinsRestApi restClient = mockJenkinsRestApi(
				Mono.error(new WebClientException("ClientResponse has erroneous status code: 404 Not Found")),
				Mono.just(8),
				Mono.just(7),
				newBuildDetails(1506141316L));

		DataTimeUtils dataTime = mockDataTimeUtils(ZonedDateTime.parse("2017-09-23T08:00:00Z"));

		BuildStateInstance instance = new BuildStateInstance();
		instance.configUrl = "http://localhost:8080";
		instance.name = "test";
		instance.errorMessage = "Tests error";

		BuildState expected = new BuildState();
		expected.setInstancesName("test");
		expected.setState(BuildStates.FAILED.name());
		expected.setBuildId(7);
		expected.setExtractingDate(ZonedDateTime.parse("2017-09-23T08:00:00Z"));
		expected.setBuildInProgress(true);
		expected.setLastRunTimestemp(1506141316L);

		//when
		Mono<BuildState> actual = new BuildStateLoader(restClient,dataTime)
				.loadState(instance);

		//then
		StepVerifier.create(actual)
				.expectError(WebClientException.class)
				.verify();
	}

	@Test
	public void detailsShouldNotBeLoadedIf404ForLoadDetails() {
		//given
		JenkinsRestApi restClient = mockJenkinsRestApi(
				Mono.just(8),
				Mono.just(7),
				Mono.just(4),
				Mono.error(new TimeoutException()));

		DataTimeUtils dataTime = mockDataTimeUtils(ZonedDateTime.parse("2017-09-23T08:00:00Z"));

		BuildStateInstance instance = new BuildStateInstance();
		instance.configUrl = "http://localhost:8080";
		instance.name = "test";
		instance.errorMessage = "Tests error";

		BuildState expected = new BuildState();
		expected.setInstancesName("test");
		expected.setState(BuildStates.SUCCESS.name());
		expected.setBuildId(7);
		expected.setExtractingDate(ZonedDateTime.parse("2017-09-23T08:00:00Z"));
		expected.setBuildInProgress(true);

		//when
		Mono<BuildState> actual = new BuildStateLoader(restClient,dataTime)
				.loadState(instance);

		//then
		StepVerifier.create(actual)
				.expectNextMatches(data -> {
					assertReflectionEquals(expected, data);
					return true;
				})
				.expectComplete()
				.verify();
	}

	private DataTimeUtils mockDataTimeUtils(ZonedDateTime nowTime) {
		DataTimeUtils dataTime = mock(DataTimeUtils.class);
		when(dataTime.nowZonedDateTime()).thenReturn(nowTime);
		return dataTime;
	}

	private Mono<BuildDetails> newBuildDetails(long lastBuildTimestamp) {
		BuildDetails buildDetailsData = new BuildDetails();
		buildDetailsData.timestamp = lastBuildTimestamp;

		return Mono.just(buildDetailsData);
	}

	private JenkinsRestApi mockJenkinsRestApi(
			Mono<Integer> lastBuildNumber, Mono<Integer> lastSuccessfulBuildNumber, Mono<Integer> lastFailedBuildNumber,
			Mono<BuildDetails> buildDetails) {
		JenkinsRestApi restClient = mock(JenkinsRestApi.class);
		when(restClient.loadLastBuildNumber(any())).thenReturn(lastBuildNumber);
		when(restClient.loadLastSuccessfulBuildNumber(any())).thenReturn(lastSuccessfulBuildNumber);
		when(restClient.loadLastFailedBuildNumber(any())).thenReturn(lastFailedBuildNumber);
		when(restClient.loadBuildDetails(any(),any())).thenReturn(buildDetails);
		return restClient;
	}
}