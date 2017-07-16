package ua.dp.radiator.jobs.buildstate;

import org.springframework.stereotype.Component;
import ua.dp.radiator.config.properties.RadiatorProperties;
import ua.dp.radiator.domain.BuildState;
import ua.dp.radiator.repository.BuildStateRepository;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Predicates.not;

@Component
public class BuildStateController {

    private BuildStateExecutor executor;
    private BuildStateRepository buildStateRepository;
    private RadiatorProperties properties;

    public BuildStateController(
            BuildStateExecutor executor,
            BuildStateRepository buildStateRepository,
            RadiatorProperties properties) {

        this.executor = executor;
        this.buildStateRepository = buildStateRepository;
        this.properties = properties;
    }

    public void execute() {
        List<BuildState> newBuildStatuses = properties.buildState.instances.stream()
                .map(executor::loadState)
                .filter(not(this::isLastFromDB))
                .collect(Collectors.toList());

        buildStateRepository.saveAll(newBuildStatuses);
    }

    private boolean isLastFromDB(BuildState buildState) {
        return buildStateRepository.findOneByInstancesNameAndStateAndLastRunTimestemp(
                buildState.getInstancesName(),
                buildState.getState(),
                buildState.getLastRunTimestemp())
            .isPresent();
    }
}
