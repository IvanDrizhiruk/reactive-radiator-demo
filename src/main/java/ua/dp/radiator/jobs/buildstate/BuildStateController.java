package ua.dp.radiator.jobs.buildstate;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import ua.dp.radiator.config.properties.RadiatorProperties;
import ua.dp.radiator.domain.BuildState;
import ua.dp.radiator.repository.BuildStateRepository;

import static com.google.common.base.Predicates.not;

@Component
public class BuildStateController {

    private BuildStateLoader executor;
    private BuildStateRepository buildStateRepository;
    private RadiatorProperties properties;

    public BuildStateController(
            BuildStateLoader executor,
            BuildStateRepository buildStateRepository,
            RadiatorProperties properties) {

        this.executor = executor;
        this.buildStateRepository = buildStateRepository;
        this.properties = properties;
    }

    public void execute() {
        Flux<BuildState> newBuildStatuses = Flux.fromIterable(properties.buildState.instances)
                .flatMap(executor::loadState)
                .filter(not(this::isLastFromDB));


        //TODO ISD blocked
        buildStateRepository.saveAll(newBuildStatuses.toIterable());
    }

    private boolean isLastFromDB(BuildState buildState) {
        return buildStateRepository.findOneByInstancesNameAndStateAndLastRunTimestemp(
                buildState.getInstancesName(),
                buildState.getState(),
                buildState.getLastRunTimestemp())
            .isPresent();
    }
}
