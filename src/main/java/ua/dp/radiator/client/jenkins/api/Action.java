package ua.dp.radiator.client.jenkins.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class Action {
	public List<Causes> causes;
	public List<Parameter> parameters;
}
