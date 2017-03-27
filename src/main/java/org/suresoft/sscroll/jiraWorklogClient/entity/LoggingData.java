package org.suresoft.sscroll.jiraWorklogClient.entity;

import java.util.List;


public class LoggingData {

	private List<String> nameList;
	
	private String issuekey; // (ex. TP-1)
	private int remainingEstimateSeconds = 0;
	
	private String dateStarted;
	private int timeSpentSeconds;
	private int billedSeconds;	// what's this? does it used?
	private String comment;

	public List<String> getNameList() {
		return nameList;
	}

	public void setNameList(List<String> nameList) {
		this.nameList = nameList;
	}

	public String getIssuekey() {
		return issuekey;
	}

	public void setIssuekey(String issuekey) {
		this.issuekey = issuekey;
	}

	public int getRemainingEstimateSeconds() {
		return remainingEstimateSeconds;
	}

	public void setRemainingEstimateSeconds(int remainingEstimateSeconds) {
		this.remainingEstimateSeconds = remainingEstimateSeconds;
	}

	public String getDateStarted() {
		return dateStarted;
	}

	public void setDateStarted(String dateStarted) {
		this.dateStarted = dateStarted;
	}

	public int getTimeSpentSeconds() {
		return timeSpentSeconds;
	}

	public void setTimeSpentSeconds(int timeSpentSeconds) {
		this.timeSpentSeconds = timeSpentSeconds;
	}

	public int getBilledSeconds() {
		return billedSeconds;
	}

	public void setBilledSeconds(int billedSeconds) {
		this.billedSeconds = billedSeconds;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	

}