package ua.dp.radiator.client.jenkins.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class Revision {
	public String module;
	public Integer revision;
	
	@Override
	public String toString() {
		return "Revision [module=" + module + ", revision=" + revision + "]";
	}
}
