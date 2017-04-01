package org.suresoft.sscroll.jiraWorklogClient.control;

public enum XmlTag {
	ROOT("information"),
	IP("ip"),
	PORT("port"),
	AUTHOR("author"),
	PASSWORD("password"),	// not used
	ISSUE_KEY_LIST("issuekeylist", "issueKey"),
	ID_LIST("idlist", "id"),
	DATE("date"),	// not used
	TIME_SPENT("timespent"),
	COMMENT("comment");
	
	private String name;
	private String child;
	
	XmlTag(final String name) {
		this.name = name;
		child = null;
	}
	
	XmlTag(final String name, final String child) {
		this.name = name;
		this.child = child;
	}

	public String getName() {
		return name;
	}

	public String getChildName() {
		return child;
	}
	
	public boolean hasChild() {
		boolean result = true;
		if( child == null ) {
			return false;
		}
		return result;
	}

}