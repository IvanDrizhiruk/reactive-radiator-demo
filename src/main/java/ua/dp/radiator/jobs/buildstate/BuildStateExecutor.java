package ua.dp.radiator.jobs.buildstate;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ua.dp.radiator.client.jenkins.BuildStatusRestClient;
import ua.dp.radiator.client.jenkins.api.BuildDetails;
import ua.dp.radiator.client.jenkins.api.Person;
import ua.dp.radiator.config.properties.RadiatorProperties.BuildStateInstance;
import ua.dp.radiator.domain.BuildState;
import ua.dp.radiator.utils.DataTimeUtils;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Component
public class BuildStateExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(BuildStateExecutor.class);

    protected BuildStatusRestClient restClient;

//	@Value("${radiator.buildState.emailFormat}")
//	protected String emailFormat;

    public BuildStateExecutor(BuildStatusRestClient restClient) {
        this.restClient = restClient;
    }

    public BuildState loadState(BuildStateInstance instances) {
        String url = instances.configUrl;
        if (LOG.isDebugEnabled()) {
            LOG.debug(format("url ", url));
        }

        BuildState buildState = null;
        try {
            buildState = calculateState(instances, url);
        } catch (Exception exception) {
            LOG.error("Can not evaluate buildState");
            if (LOG.isDebugEnabled()) {
                LOG.debug("Error", exception);
            }
        }

        if (LOG.isInfoEnabled()) {
            LOG.debug(format("BuildState for url %s : %s", url, buildState));
        }

        return buildState;
    }

    protected BuildState calculateState(BuildStateInstance instances, String url) {
        int lastBuild = restClient.loadLastBuildNumber(url);

        Long lastRunTimestemp = getLastRunTimestemp(url, lastBuild);
        int lastSuccessfulBuild = restClient.loadLastSuccessfulBuildNumber(url, lastBuild);
        int lastFailedBuild = restClient.loadLastFailedBuildNumber(url, 0);


        if (lastSuccessfulBuild > lastFailedBuild) {
            return newSuccessState(instances, lastRunTimestemp);
        }

        if (lastFailedBuild == lastBuild && !instances.isConfigurationIssue) {
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

        BuildDetails buildDetails = restClient.loadBuildDetails(url, lastFailedBuild);

        //List<Person> culprits = extractCulprits(buildDetails);
        //List<Commiter> commiters = BuildStateUtils.calculateCommiters(culprits, emailFormat);

        //buildState.getCommiters().addAll(commiters);

        return buildState;
    }

    private Long getLastRunTimestemp(String url, int lastBuild) {
        BuildDetails buildDetails = restClient.loadBuildDetails(url, lastBuild);

        return buildDetails.timestamp;
    }

    private List<Person> extractCulprits(BuildDetails buildDetails) {
        //		List<Person> culprits = buildDetails.culprits;
        if (null == buildDetails.changeSet || null == buildDetails.changeSet.items) {
            return Lists.newArrayList();
        }

        return buildDetails.changeSet.items.stream()
                .collect(Collectors.toMap(
                        item -> item.author.fullName,
                        item -> item.author,
                        (item1, item2) -> item1))
                .values().stream()
                .collect(Collectors.toList());
    }


    private BuildState newSuccessState(BuildStateInstance instance, Long lastRunTimestemp) {
        BuildState buildState = new BuildState();

        buildState.setInstancesName(instance.name);
        buildState.setState(BuildStates.SUCCESS.toString());
        buildState.setLastRunTimestemp(lastRunTimestemp);
        buildState.setExtractingDate(DataTimeUtils.nowZonedDateTime());

        return buildState;
    }
}
