package ua.dp.radiator.reactive;

import com.google.common.base.Predicates;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ua.dp.radiator.config.properties.RadiatorProperties;
import ua.dp.radiator.domain.BuildState;
import ua.dp.radiator.jobs.buildstate.BuildStateExecutor;

import java.time.Duration;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BuildStatusesController {
    private static Logger LOG = Logger.getLogger(BuildStatusesController.class.getName());

    private BuildStateExecutor buildStateExecutor;

    private RadiatorProperties properties;


    public BuildStatusesController(BuildStateExecutor buildStateExecutor, RadiatorProperties properties) {
        this.buildStateExecutor = buildStateExecutor;
        this.properties = properties;
    }

    public Flux<BuildState> getBuildStatusesProcessor() {
        return Flux.interval(Duration.ofSeconds(2))
                .map(index -> {
                    LOG.info("ISD build state ==> load");

                    return properties.getBuildState().getInstances().stream()
                            .map(buildStateExecutor::loadState)
                            .filter(Predicates.not(Objects::isNull))
                            .collect(Collectors.toList());

                }).flatMap(buildStatuses -> Flux.fromIterable(buildStatuses));
    }
}
