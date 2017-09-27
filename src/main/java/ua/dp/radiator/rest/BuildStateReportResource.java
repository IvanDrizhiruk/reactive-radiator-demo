package ua.dp.radiator.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import ua.dp.radiator.domain.BuildState;
import ua.dp.radiator.jobs.buildstate.BuildStateStreamController;

/**
 * REST controller for managing BuildState.
 */
@RestController
@RequestMapping("/api")
public class BuildStateReportResource {

    private final Logger log = LoggerFactory.getLogger(BuildStateReportResource.class);

    private BuildStateStreamController buildStateStreamController;


    public BuildStateReportResource(BuildStateStreamController buildStateStreamController) {
        this.buildStateStreamController = buildStateStreamController;
    }


    @GetMapping(path = "/report/stream/build-states", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<BuildState> getBuildStatesStream() {
		log.debug("REST request to get stream of BuildStates");

		return buildStateStreamController.getBuildStatusesSteram();
	}
}
