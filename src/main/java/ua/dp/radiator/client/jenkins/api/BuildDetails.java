package ua.dp.radiator.client.jenkins.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

@JsonAutoDetect
@JsonRootName("actions")
public class BuildDetails {
	@JsonProperty("actions")
	public List<Action> actions;

	public List<String> artifacts;
	public Boolean building;
	public String description;
	public Integer duration;
	public Integer estimatedDuration;
	public String fullDisplayName;
	public String displayName;
	public String id;
	public String queueId;
	public Boolean keepLog;
	public Integer number;
	public String result;
	public Long timestamp;
	public String url;
	public String builtOn;
	public ChangeSet changeSet;
	public List<Person> culprits;
	public List<Runs> runs;
}
