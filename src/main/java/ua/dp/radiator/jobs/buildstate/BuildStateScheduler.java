package ua.dp.radiator.jobs.buildstate;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.dp.radiator.config.properties.RadiatorProperties;
import ua.dp.radiator.utils.DataTimeUtils;
import ua.dp.radiator.utils.Timer;

import javax.annotation.PostConstruct;

@Service
public class BuildStateScheduler {
	private static Logger LOG = Logger.getLogger(BuildStateScheduler.class.getName());
	
	private BuildStateController buildStateController;
	private RadiatorProperties radiatorProperties;

	public BuildStateScheduler(
			BuildStateController buildStateController,
			RadiatorProperties radiatorProperties) {

		this.buildStateController = buildStateController;
		this.radiatorProperties = radiatorProperties;
	}

	@PostConstruct
	void initialize() {
		try {
			executeTask();
		} catch (Exception e) {
			LOG.info("===========> init exception", e);
			LOG.warn("Unable init Builds states.");
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format("Error: ", e));
			}
		}
	}

	@Scheduled(cron="${radiator.buildState.cron}")
	private void  executeTask() {
		LOG.debug(String.format("Start BuildState calculation %s", DataTimeUtils.currentLongTime()));
		Timer timer = new Timer();
		buildStateController.execute();

		LOG.debug(String.format("BuildState calculation finished %s. Total time: %d miliseconds",
				DataTimeUtils.currentLongTime(), timer.elapsedTimeInMilliseconds()));
	}
}
