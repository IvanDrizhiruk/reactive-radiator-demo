package ua.dp.radiator.jobs.buildstate;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dp.radiator.client.jenkins.JenkinsRestApi;
import ua.dp.radiator.config.properties.RadiatorProperties.BuildStateInstance;
import ua.dp.radiator.domain.BuildState;
import ua.dp.radiator.utils.DataTimeUtils;

@Component
public class BuildStateExecutor {

    protected JenkinsRestApi jenkinsApi;


    public BuildStateExecutor(JenkinsRestApi restClient) {
        this.jenkinsApi = restClient;
    }


    public Mono<BuildState> loadState(BuildStateInstance instances) {
        return loadBuildState(instances)
                .flatMap(buildState -> loadBuildStateDetails(instances, buildState));
    }

    private  Mono<BuildState> loadBuildState(BuildStateInstance instances) {
        return Flux.zip(
                        jenkinsApi.loadLastBuildNumber(instances.configUrl),
                        jenkinsApi.loadLastSuccessfulBuildNumber(instances.configUrl),
                        jenkinsApi.loadLastFailedBuildNumber(instances.configUrl))
                .map(
                        buildNumbers -> prepareBuildState(instances, buildNumbers.getT1(), buildNumbers.getT2(), buildNumbers.getT3()))
                .last();
    }


    private BuildState prepareBuildState(BuildStateInstance instance, Integer lastBuild, Integer lastSuccessfulBuild, Integer lastFailedBuild) {

        BuildState buildState = new BuildState();


        buildState.setInstancesName(instance.name);
        buildState.setState(calculateStatus(instance, lastSuccessfulBuild, lastFailedBuild).toString());
        buildState.setBuildId(Math.max(lastSuccessfulBuild,lastFailedBuild));
        buildState.setExtractingDate(DataTimeUtils.nowZonedDateTime());
        buildState.setBuildInProgress(lastBuild > lastSuccessfulBuild && lastBuild > lastFailedBuild);

        return buildState;
    }

    private BuildStates calculateStatus(BuildStateInstance instance, Integer lastSuccessfulBuild, Integer lastFailedBuild) {
        if (lastSuccessfulBuild > lastFailedBuild) {
            return BuildStates.SUCCESS;
        } else if (instance.isConfigurationIssue) {
            return BuildStates.CONFIGURATION_FAILED;
        }

        return BuildStates.BUILD_FAILED;
    }

    private Mono<BuildState> loadBuildStateDetails(BuildStateInstance instances, BuildState buildState) {
        return jenkinsApi.loadBuildDetails(instances.configUrl, buildState.getBuildId())
                .map(buildDetails -> {

                    buildState.setLastRunTimestemp(buildDetails.timestamp);

                    return buildState;
                });
    }
}
