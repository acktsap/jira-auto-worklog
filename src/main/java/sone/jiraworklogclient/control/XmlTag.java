package sone.jiraworklogclient.control;

public enum XmlTag {
	
	ROOT("information"),
	
	IP("ip"),
	PORT("port"),
	AUTHOR("author"),
	PASSWORD("password"),	// not used
	
	ISSUE_KEY("issueKey"),
	ISSUE_KEY_LIST("issuekeylist"),
	
	USER("user"),
	USER_LIST("userlist"),
	
	DATE("date"),	// not used
	TIME_SPENT("timespent"),
	COMMENT("comment");
	
	private String name;
	
	XmlTag(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}