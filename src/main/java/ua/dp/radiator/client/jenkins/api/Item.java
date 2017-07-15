package ua.dp.radiator.client.jenkins.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

@JsonAutoDetect
public class Item {
	public String date; // TODO ISD change type
	public String timestamp;
	public String msg;
	public List<Path> paths;
	public Integer revision;
	public String commitId;
	public String user;
	public List<String> affectedPaths;
	public Person author;
	public String comment;
	public String id;
}
