package ua.dp.radiator.jobs.buildstate;

import static java.lang.String.format;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;
import ua.dp.radiator.client.jenkins.JenkinsRestApi;
import ua.dp.radiator.config.properties.RadiatorProperties.BuildStateInstance;
import ua.dp.radiator.domain.BuildState;
import ua.dp.radiator.utils.DataTimeUtils;

@Component
public class BuildStateExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(BuildStateExecutor.class);

    protected JenkinsRestApi jenkinsApi;

//	@Value("${radiator.buildState.emailFormat}")
//	protected String emailFormat;

    public BuildStateExecutor(JenkinsRestApi restClient) {
        this.jenkinsApi = restClient;
    }

    public Mono<BuildState> loadState(BuildStateInstance instances) {
        String url = instances.configUrl;
        if (LOG.isDebugEnabled()) {
            LOG.debug(format("url ", url));
        }

//        BuildState buildState = null;
        try {
            return calculateState(instances, url);
        } catch (Exception exception) {
            LOG.error("Can not evaluate buildState");
            if (LOG.isDebugEnabled()) {
                LOG.debug("Error", exception);
            }
            return Mono.error(exception);
        }

//        if (LOG.isInfoEnabled()) {
//            LOG.debug(format("BuildState for url %s : %s", url, buildState));
//        }
//
//        return buildState;
    }

    protected Mono<BuildState> calculateState(BuildStateInstance instances, String url) {
        Mono<Integer> lastBuild = jenkinsApi.loadLastBuildNumber(url);
        Mono<Integer> lastSuccessfulBuild = jenkinsApi.loadLastSuccessfulBuildNumber(url);
        Mono<Integer> lastFailedBuild = jenkinsApi.loadLastFailedBuildNumber(url);

        //TODO
//        Long lastRunTimestemp = getLastRunTimestemp(url, lastBuild);

        //TODO
        return Mono.<Integer, BuildState>zip(
                Arrays.asList(lastBuild, lastSuccessfulBuild, lastFailedBuild),
                buildNumbers -> prepareBuildState(instances, (Integer)buildNumbers[0], (Integer)buildNumbers[1], (Integer)buildNumbers[2]));
    }

    private BuildState prepareBuildState(BuildStateInstance instances, Integer lastBuild, Integer lastSuccessfulBuild, Integer lastFailedBuild) {

        //TODO
        Long lastRunTimestemp = null;

        if (lastSuccessfulBuild > lastFailedBuild) {
            return newSuccessState(instances, lastRunTimestemp);
        }

        if (lastFailedBuild == lastBuild && !instances.isConfigurationIssue) {
            //TODO
            String url = null;

            return newBuildFailedState(instances, url, lastFailedBuild, lastRunTimestemp);
        }

        return newConfigurationFailedState(instances, lastRunTimestemp);
    }

    private BuildState newConfigurationFailedState(BuildStateInstance instance, Long lastRunTimestemp) {
        BuildState buildState = new BuildState();

        buildState.setInstancesName(instance.name);
        buildState.setState(BuildStates.CONFIGURATION_FAILED.toString());
        buildState.setLastRunTimestemp(lastRunTimestemp);
        buildState.setExtractingDate(DataTimeUtils.nowZonedDateTime());

        return buildState;
    }

    private BuildState newBuildFailedState(BuildStateInstance instance, String url, Integer lastFailedBuild, Long lastRunTimestemp) {


        BuildState buildState = new BuildState();

        buildState.setInstancesName(instance.name);
        buildState.setState(BuildStates.BUILD_FAILED.toString());
        buildState.setErrorMessage(instance.errorMessage);
        buildState.setLastRunTimestemp(lastRunTimestemp);
        buildState.setExtractingDate(DataTimeUtils.nowZonedDateTime());

        //TODO
//        BuildDetails buildDetails = jenkinsApi.loadBuildDetails(url, lastFailedBuild);
//
        //List<Person> culprits = extractCulprits(buildDetails);
        //List<Commiter> commiters = BuildStateUtils.calculateCommiters(culprits, emailFormat);

        //buildState.getCommiters().addAll(commiters);

        return buildState;
    }

//    private Long getLastRunTimestemp(String url, int lastBuild) {
//        BuildDetails buildDetails = jenkinsApi.loadBuildDetails(url, lastBuild);
//
//        return buildDetails.timestamp;
//    }
//
//    private List<Person> extractCulprits(BuildDetails buildDetails) {
//        //		List<Person> culprits = buildDetails.culprits;
//        if (null == buildDetails.changeSet || null == buildDetails.changeSet.items) {
//            return Lists.newArrayList();
//        }
//
//        return buildDetails.changeSet.items.stream()
//                .collect(Collectors.toMap(
//                        item -> item.author.fullName,
//                        item -> item.author,
//                        (item1, item2) -> item1))
//                .values().stream()
//                .collect(Collectors.toList());
//    }


    private BuildState newSuccessState(BuildStateInstance instance, Long lastRunTimestemp) {
        BuildState buildState = new BuildState();

        buildState.setInstancesName(instance.name);
        buildState.setState(BuildStates.SUCCESS.toString());
        buildState.setLastRunTimestemp(lastRunTimestemp);
        buildState.setExtractingDate(DataTimeUtils.nowZonedDateTime());

        return buildState;
    }
}
