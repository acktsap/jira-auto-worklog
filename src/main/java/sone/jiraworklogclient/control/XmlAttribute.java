package sone.jiraworklogclient.control;

public enum XmlAttribute {
	
	ID("id"),
	NAME("name"),
	SELECTED("selected");
	
	private String name;
	
	XmlAttribute(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
