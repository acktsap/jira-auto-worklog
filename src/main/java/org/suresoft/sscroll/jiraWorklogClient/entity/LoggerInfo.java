package org.suresoft.sscroll.jiraWorklogClient.entity;

public class LoggerInfo implements EntityInterface {
	private String id;
	private String password;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getSummary() {
		StringBuffer summaryBuffer = new StringBuffer();
		
		summaryBuffer.append("User id : " + getId() + "\n");
		
		return summaryBuffer.toString();
	}
}
