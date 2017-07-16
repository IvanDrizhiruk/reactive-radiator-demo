package ua.dp.radiator.reactive;

import com.google.common.base.Predicates;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ua.dp.radiator.config.properties.RadiatorProperties;
import ua.dp.radiator.domain.BuildState;
import ua.dp.radiator.jobs.buildstate.BuildStateExecutor;

import java.util.Objects;

@RestController
@RequestMapping("/api/buildstatus")
public class BuildRestController {


    private final BuildStateExecutor buildStateExecutor;
    private BuildStatusesController buildStatusesController;
    private RadiatorProperties properties;

    public BuildRestController(
            BuildStateExecutor buildStateExecutor,
            BuildStatusesController buildStatusesController,
            RadiatorProperties properties) {
        this.buildStateExecutor = buildStateExecutor;
        this.buildStatusesController = buildStatusesController;
        this.properties = properties;
    }


    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<BuildState> getAllTasks() {

        return Flux.create(sink -> {
            properties.getBuildState().getInstances().stream()
                    .map(buildStateExecutor::loadState)
                    .filter(Predicates.not(Objects::isNull))
                    .forEach(sink::next);
            sink.complete();
        });
    }

    @GetMapping(path = "/jsonstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<BuildState> getAllTaskAsJsonStream() {
        return buildStatusesController.getBuildStatusesProcessor();
    }

    @GetMapping(path = "/eventstream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<BuildState> getAllTaskAsEventStream() {
        return buildStatusesController.getBuildStatusesProcessor();
    }


    @GetMapping(path = "/build4", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<BuildState> getTasksSet4() {
        return buildStatusesController.getBuildStatusesProcessor();
    }
}