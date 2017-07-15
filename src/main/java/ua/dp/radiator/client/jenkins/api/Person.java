package ua.dp.radiator.client.jenkins.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class Person {
	public String absoluteUrl;
	public String fullName;
	
	public Person() {}
	
	public Person(String absoluteUrl, String fullName) {
		this.absoluteUrl = absoluteUrl;
		this.fullName = fullName;
	}
}
