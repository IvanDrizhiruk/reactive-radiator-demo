package ua.dp.radiator.jobs.buildstate;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dp.radiator.config.properties.RadiatorProperties;
import ua.dp.radiator.domain.BuildState;

@Component
public class BuildStateStreamController {

	private RadiatorProperties properties;
	private BuildStateExecutor buildStateExecutor;

	private DirectProcessor<BuildState> buildStatusesProcessor = DirectProcessor.create();


	public BuildStateStreamController(RadiatorProperties properties, BuildStateExecutor buildStateExecutor) {
		this.properties = properties;
		this.buildStateExecutor = buildStateExecutor;
	}

	@PostConstruct
	private void init() {

//		Flux<BuildState> newBuildStatuses = Flux.fromIterable(properties.buildState.instances)
//				.flatMap(executor::loadState)
//				.filter(not(this::isLastFromDB));

		Mono<BuildState> data = buildStateExecutor
				.loadState(properties.buildState.instances.iterator().next());

		data.subscribe(buildStatusesProcessor);
	}


	public Flux<BuildState> getBuildStatusesSteram() {
		return buildStatusesProcessor;
	}
}
