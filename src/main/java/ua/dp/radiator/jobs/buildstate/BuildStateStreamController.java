package ua.dp.radiator.jobs.buildstate;

import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import ua.dp.radiator.config.properties.RadiatorProperties;
import ua.dp.radiator.domain.BuildState;

@Component
public class BuildStateStreamController {

	private RadiatorProperties properties;
	private BuildStateLoader buildStateExecutor;
	private Map<String,Integer> lastBuildNumbers = Maps.newHashMap();

	private DirectProcessor<BuildState> buildStatusesStream = DirectProcessor.create();
//	private EmitterProcessor<BuildState> buildStatusesStream = EmitterProcessor.<BuildState>builder().bufferSize(3).build();


	public BuildStateStreamController(RadiatorProperties properties, BuildStateLoader buildStateExecutor) {
		this.properties = properties;
		this.buildStateExecutor = buildStateExecutor;
	}

	@Scheduled(cron="${radiator.buildState.cron}")
	private void  executeTask() {
		Flux.fromIterable(properties.buildState.instances)
				.flatMap(buildStateExecutor::loadState)
				.filter(buildState -> lastBuildNumbers.get(buildState.getInstancesName()) != buildState.getBuildId())
				.doOnNext(buildState -> lastBuildNumbers.put(buildState.getInstancesName(), buildState.getBuildId()))
				.subscribe(data -> buildStatusesStream.sink().next(data));
	}


	public Flux<BuildState> getBuildStatusesSteram() {
		return buildStatusesStream;
	}
}
