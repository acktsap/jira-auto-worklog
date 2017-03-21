package org.suresoft.sscroll.jiraAutoLogging.control;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.suresoft.sscroll.jiraAutoLogging.entity.LoggerInfo;
import org.suresoft.sscroll.jiraAutoLogging.entity.LoggingData;

public class JiraLogJsonParser extends JSONParser {
	
	public static final String JSON_USER_ID = "username";
	public static final String JSON_USER_PASSWORD = "password";
	
	public static final String JSON_SESSION = "session";
	public static final String JSON_SESSION_NAME = "name";
	public static final String JSON_SESSION_VALUE = "value";
	
	public static final String JSON_AUTHOR = "author";
	public static final String JSON_AUTHOR_NAME = "name";
	
	public static final String JSON_ISSUE = "issue";
	public static final String JSON_ISSUE_KEY = "key";
	public static final String JSON_ISSUE_RES = "remainingEstimateSeconds";
	
	public static final String JSON_DATE_STARTED = "dateStarted";
	public static final String JSON_TIME_SPENT_SECONDS = "timeSpentSeconds";
	public static final String JSON_BILLED_SECONDS = "billedSeconds";
	public static final String JSON_COMMENT = "comment";
	
	
	private static JiraLogJsonParser jiraLogJsonParser = null;
	
	public static JiraLogJsonParser getInstance() {
		if( jiraLogJsonParser == null ) {
			jiraLogJsonParser = new JiraLogJsonParser();
		}
		return jiraLogJsonParser;
	}

	private JiraLogJsonParser() {
		super();
	}
	
	public JSONObject toJsonObject(final LoggerInfo loggerInfo){
		JSONObject sessionJsonData = new JSONObject();
		sessionJsonData.put(JSON_USER_ID, loggerInfo.getId());
		sessionJsonData.put(JSON_USER_PASSWORD, loggerInfo.getPassword());
		return sessionJsonData;
	}
	
	public JSONArray toJsonArray(final LoggingData loggingData) {
		JSONArray jsonArray = new JSONArray();

		for (int i = 0; i < loggingData.getNameList().size(); i++) {
			JSONObject jsonObject = new JSONObject();

			// author
			JSONObject author = new JSONObject();
			author.put(JSON_AUTHOR_NAME, loggingData.getNameList().get(i));
			jsonObject.put(JSON_AUTHOR, author);
			
			// ISSUE_ISSUETYPE
			JSONObject issue = new JSONObject();
			issue.put(JSON_ISSUE_KEY, loggingData.getIssuekey());
			issue.put(JSON_ISSUE_RES, loggingData.getRemainingEstimateSeconds());
			jsonObject.put(JSON_ISSUE, issue);

			// data
			jsonObject.put(JSON_DATE_STARTED, loggingData.getDateStarted());
			jsonObject.put(JSON_TIME_SPENT_SECONDS, loggingData.getTimeSpentSeconds());
//			jsonObject.put(JSON_BILLED_SECONDS, loggingData.getBilledSeconds());
			
			jsonObject.put(JSON_COMMENT, loggingData.getComment());
			
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}
	
	public String extractSessionName(final JSONObject responseJson) {
		return ((JSONObject)responseJson.get(JSON_SESSION)).get(JSON_SESSION_NAME).toString();
	}

	public String extractSessionValue(final JSONObject responseJson) {
		return ((JSONObject)responseJson.get(JSON_SESSION)).get(JSON_SESSION_VALUE).toString();
	}
}
