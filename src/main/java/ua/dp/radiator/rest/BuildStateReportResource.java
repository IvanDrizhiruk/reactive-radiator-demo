package ua.dp.radiator.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.dp.radiator.domain.BuildState;
import ua.dp.radiator.repository.BuildStateRepository;

import java.util.List;

/**
 * REST controller for managing BuildState.
 */
@RestController
@RequestMapping("/api")
public class BuildStateReportResource {

    private final Logger log = LoggerFactory.getLogger(BuildStateReportResource.class);

    private BuildStateRepository buildStateRepository;

    public BuildStateReportResource(BuildStateRepository buildStateRepository) {
        this.buildStateRepository = buildStateRepository;
    }

    @GetMapping(value = "/report/build-states/last", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BuildState> getLastBuildStates() {
        log.debug("REST request to get last BuildStates");

        return buildStateRepository.findLastBuildStates();
    }
}
