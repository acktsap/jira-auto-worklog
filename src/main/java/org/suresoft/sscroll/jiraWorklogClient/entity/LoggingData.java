package org.suresoft.sscroll.jiraWorklogClient.entity;

import java.util.List;

import org.suresoft.sscroll.jiraWorklogClient.boundary.TimeFormatter;

public class LoggingData implements EntityInterface {

	private String issuekey; // (ex. TP-1)
	private int remainingEstimateSeconds = 0;

	private List<String> userList;

	private String dateStarted;
	private int timeSpentSeconds;
	private int billedSeconds; // what's this? does it used?
	private String comment;

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

	public List<String> getUserList() {
		return userList;
	}

	public void setUserList(final List<String> userList) {
		this.userList = userList;
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

	@Override
	public String getSummary() {
		StringBuffer summaryBuffer = new StringBuffer();

		summaryBuffer.append("Issue key : " + getIssuekey() + "\n");
		summaryBuffer.append("Name list : ");
		for (final String name : getUserList()) {
			summaryBuffer.append(name + " ");
		}
		summaryBuffer.append("\n");
		summaryBuffer.append("Date : " + getDateStarted() + "\n");
		summaryBuffer.append("Time spent : " + TimeFormatter.secondToTime(getTimeSpentSeconds()) + "\n");
		summaryBuffer.append("Comment : " + getComment() + "\n");

		return summaryBuffer.toString();
	}

}