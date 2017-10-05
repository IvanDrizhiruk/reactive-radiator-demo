package ua.dp.radiator.jobs.buildstate;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dp.radiator.client.jenkins.JenkinsRestApi;
import ua.dp.radiator.config.properties.RadiatorProperties.BuildStateInstance;
import ua.dp.radiator.domain.BuildState;
import ua.dp.radiator.utils.time.DataTimeUtils;

@Component
public class BuildStateLoader {
    private JenkinsRestApi jenkinsApi;
    private DataTimeUtils dataTime;


    public BuildStateLoader(JenkinsRestApi restClient, DataTimeUtils dataTime) {
        this.jenkinsApi = restClient;
        this.dataTime = dataTime;
    }


    public Mono<BuildState> loadState(BuildStateInstance instance) {
        return loadBuildState(instance)
                .flatMap(buildState -> loadBuildStateDetails(instance, buildState).retry(3));
    }

    private  Mono<BuildState> loadBuildState(BuildStateInstance instance) {

        Mono<String> configUrl = Mono.just(instance)
                .map(BuildStateInstance::getConfigUrl);

        return Flux.zip(
                        jenkinsApi.loadLastBuildNumber(configUrl),
                        jenkinsApi.loadLastSuccessfulBuildNumber(configUrl)
                                .onErrorReturn(WebClientException.class, -1),
                        jenkinsApi.loadLastFailedBuildNumber(configUrl)
                                .onErrorReturn(WebClientException.class, -1))
                .map(buildNumbers -> prepareBuildState(instance, buildNumbers.getT1(), buildNumbers.getT2(), buildNumbers.getT3()))
                .single();
    }


    private BuildState prepareBuildState(BuildStateInstance instance, Integer lastBuild, Integer lastSuccessfulBuild, Integer lastFailedBuild) {

        BuildState buildState = new BuildState();


        buildState.setInstancesName(instance.name);
        buildState.setState(calculateStatus(instance, lastSuccessfulBuild, lastFailedBuild).toString());
        buildState.setBuildId(Math.max(lastSuccessfulBuild,lastFailedBuild));
        buildState.setExtractingDate(dataTime.nowZonedDateTime());
        buildState.setBuildInProgress(lastBuild > lastSuccessfulBuild && lastBuild > lastFailedBuild);

        return buildState;
    }

    private BuildStates calculateStatus(BuildStateInstance instance, Integer lastSuccessfulBuild, Integer lastFailedBuild) {
        if (lastSuccessfulBuild > lastFailedBuild) {
            return BuildStates.SUCCESS;
        } else if (instance.isConfigurationIssue) {
            return BuildStates.CONFIGURATION_FAILED;
        }

        return BuildStates.FAILED;
    }

    private Mono<BuildState> loadBuildStateDetails(BuildStateInstance instances, BuildState buildState) {
        return jenkinsApi.loadBuildDetails(instances.configUrl, buildState.getBuildId())
                .map(buildDetails -> {

                    buildState.setLastRunTimestemp(buildDetails.timestamp);

                    return buildState;
                })
                .onErrorReturn(buildState);
    }
}
