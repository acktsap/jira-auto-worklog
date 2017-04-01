package org.suresoft.sscroll.jiraWorklogClient.entity;

public class ServerInfo implements EntityInterface {
	private String ip;
	private String port;

	public String getIP() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	@Override
	public String getSummary() {
		StringBuffer summaryBuffer = new StringBuffer();

		summaryBuffer.append("Server : " + getIP() + ":" + getPort() + "\n");

		return summaryBuffer.toString();
	}
}
