package ua.dp.radiator.jobs.buildstate;

import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ua.dp.radiator.client.jenkins.JenkinsRestApi;
import ua.dp.radiator.client.jenkins.api.BuildDetails;
import ua.dp.radiator.config.properties.RadiatorProperties.BuildStateInstance;
import ua.dp.radiator.domain.BuildState;
import ua.dp.radiator.utils.time.DataTimeUtils;

import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class BuildStateLoaderTest {

	@Test
	public void buildStateShouldBeLoaded() throws Exception {
		//given

		JenkinsRestApi restClient = mock(JenkinsRestApi.class);
		when(restClient.loadLastBuildNumber(any())).thenReturn(Mono.just(8));
		when(restClient.loadLastSuccessfulBuildNumber(any())).thenReturn(Mono.just(7));
		when(restClient.loadLastFailedBuildNumber(any())).thenReturn(Mono.just(4));

		BuildDetails buildDetails = new BuildDetails();
		buildDetails.timestamp = 1506141316L;

		when(restClient.loadBuildDetails(any(),any())).thenReturn(Mono.just(buildDetails));

		ZonedDateTime nowTime = ZonedDateTime.parse("2017-09-23T08:00:00Z");

		DataTimeUtils dataTime = mock(DataTimeUtils.class);
		when(dataTime.nowZonedDateTime()).thenReturn(nowTime);

		BuildStateInstance instance = new BuildStateInstance();
		instance.configUrl = "http://localhost:8080";
		instance.name = "test";
		instance.errorMessage = "Tests error";

		BuildState expected = new BuildState();
		expected.setInstancesName("test");
		expected.setState(BuildStates.SUCCESS.name());
		expected.setBuildId(7);
		expected.setExtractingDate(nowTime);
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
}