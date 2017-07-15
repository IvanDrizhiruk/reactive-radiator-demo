package ua.dp.radiator.client.jenkins.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

@JsonAutoDetect
public class ChangeSet {
	public List<Item> items;
	public String kind;
	public List<Revision> revisions;
	
	@Override
	public String toString() {
		return "ChangeSet [items=" + items + ", kind=" + kind + ", revisions="
				+ revisions + "]";
	}
}
