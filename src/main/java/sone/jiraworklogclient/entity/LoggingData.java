package sone.jiraworklogclient.entity;

import java.util.List;

import sone.jiraworklogclient.boundary.TimeFormatter;

public class LoggingData implements EntityInterface {

	private String issuekey; // (ex. TP-1)
	private int remainingEstimateSeconds = 0;

	private List<User> userList;

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

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(final List<User> userList) {
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
		StringBuilder summaryBuilder = new StringBuilder();

		summaryBuilder.append("Issue key : " + getIssuekey() + "\n");
		
		final List<User> userList = getUserList();
		summaryBuilder.append("User list(");
		summaryBuilder.append(userList.size());
		summaryBuilder.append(") : ");
		
		for (final User user : userList) {
			summaryBuilder.append(user.getName() + " ");
		}
		
		summaryBuilder.append("\n");
		summaryBuilder.append("Date : " + getDateStarted() + "\n");
		summaryBuilder.append("Time spent : " + TimeFormatter.secondToTime(getTimeSpentSeconds()) + "\n");
		summaryBuilder.append("Comment : " + getComment() + "\n");
		return summaryBuilder.toString();
	}

}