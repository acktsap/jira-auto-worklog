package org.suresoft.sscroll.jiraAutoLogging.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.suresoft.sscroll.jiraAutoLogging.control.JiraAutoLoggingException.MakeSessionFailedException;
import org.suresoft.sscroll.jiraAutoLogging.entity.LoggerInfo;
import org.suresoft.sscroll.jiraAutoLogging.entity.LoggingData;

public class ServerArbiter {
	
	private static final int VALID_RESPONSE = 200;
	private static final int NEED_VERIFICATION_CODE = 403;
	private static final String SESSION_URL = "/rest/auth/1/session";
	private static final String WORKLOG_URL = "/rest/tempo-timesheets/3/worklogs";
	
	private JiraLogJsonParser jiraLogJsonParser;
	private String jiraServer;
	private String sessionName;
	private String sessionValue;

	public ServerArbiter() {
		jiraLogJsonParser = JiraLogJsonParser.getInstance();
	}
	
	public void setJiraServer(final String ip, final String port){
		jiraServer = new String();
		jiraServer += "http://";
		jiraServer += ip;
		jiraServer += ":";
		jiraServer += port;
	}
	
	public String getJiraServer(){
		return jiraServer;
	}
	
	public String getSessionName() {
		return sessionName;
	}
	
	public void setSessionName(final String sessionName) {
		this.sessionName = sessionName;
	}

	public String getSessionValue() {
		return sessionValue;
	}
	
	public void setSessionValue(final String sessionValue) {
		this.sessionValue = sessionValue;
	}
	
	/**
	 * Make session with logger information.
	 * Throws java.net.ConnectException if connection timeout.
	 * Throws MakeSessionFailedException if connection failure.
	 * @param userData
	 */
	public void makeSession(final LoggerInfo userData) throws UnsupportedEncodingException, IOException, ParseException, MakeSessionFailedException {
		HttpURLConnection httpConnection = makeSessionConnection();
		JSONObject jsonSessionData = jiraLogJsonParser.toJsonObject(userData);
		
		sendJsonData(httpConnection, jsonSessionData);
		
		int responseCode = httpConnection.getResponseCode();
		if( responseCode == VALID_RESPONSE ){
			String response = getResponse(httpConnection);
			
			JSONObject responseJson = (JSONObject) jiraLogJsonParser.parse(response);
			setSessionName(jiraLogJsonParser.extractSessionName(responseJson));
			setSessionValue(jiraLogJsonParser.extractSessionValue(responseJson));
		} else if( responseCode == NEED_VERIFICATION_CODE) {
			throw new MakeSessionFailedException("Need verification code.. Log in on site");
		} else {
			throw new MakeSessionFailedException("Logger information error, check it");
		}
	}
	
	/**
	 * Send post request for each logging data.
	 * Returns failed id list(empty if no failure). 
	 * @param loggingData
	 * @return send request failed id list  
	 */
	public List<String> sendPost(final LoggingData loggingData) throws MalformedURLException, ProtocolException, IOException {
		JSONArray loggingJsonArrayData = jiraLogJsonParser.toJsonArray(loggingData);
		
		List<String> failedList = new ArrayList<String>();
		for (int i = 0; i < loggingJsonArrayData.size(); i++) {
			JSONObject loggingJsonData = (JSONObject) loggingJsonArrayData.get(i);
			
			JSONObject author = (JSONObject) loggingJsonData.get(JiraLogJsonParser.JSON_AUTHOR);
			String authorName = (String) author.get(JiraLogJsonParser.JSON_AUTHOR_NAME);
			
			HttpURLConnection httpConnection = makeWorkLogConnection();
			sendJsonData(httpConnection, loggingJsonData);
			
			if( httpConnection.getResponseCode() != VALID_RESPONSE ) {
				failedList.add(authorName);
			}
		}
		return failedList;
	}

	/**
	 * Make HttpURLConnection for session
	 * @return HttpURLConnection
	 */
	private HttpURLConnection makeSessionConnection() throws MalformedURLException, IOException, ProtocolException {
		URL urlObject = new URL(getJiraServer() + SESSION_URL);
		HttpURLConnection httpConnection = (HttpURLConnection) urlObject.openConnection();
		
		// set request header
		httpConnection.setRequestMethod("POST");
		httpConnection.setRequestProperty("Accept", "application/json");
		httpConnection.setRequestProperty("Content-Type", "application/json;");
		httpConnection.setDoOutput(true);
		
		return httpConnection;
	}
	
	/**
	 * 
	 * Make HttpURLConnection for work log
	 * @return HttpURLConnection
	 */
	private HttpURLConnection makeWorkLogConnection() throws MalformedURLException, IOException, ProtocolException {
		URL urlObject = new URL(jiraServer + WORKLOG_URL);
		HttpURLConnection httpConnection = (HttpURLConnection) urlObject.openConnection();         
		
		// set request header
		httpConnection.setRequestMethod("POST");
		httpConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		httpConnection.setRequestProperty("Accept", "application/json");
		httpConnection.setRequestProperty("Content-Type", "application/json;");
		httpConnection.setRequestProperty("Cookie", getSessionName() + "=" + getSessionValue());
		
		httpConnection.setDoOutput(true);
		
		return httpConnection;
	}
	
	/**
	 * Send jsonData on httpConnection with UTF-8 format
	 * @param httpConnection
	 * @param jsonData
	 */
	private void sendJsonData(final HttpURLConnection httpConnection, final JSONObject jsonData)
			throws UnsupportedEncodingException, IOException {
		OutputStreamWriter writer = new OutputStreamWriter(httpConnection.getOutputStream(), "UTF-8");
        writer.write(jsonData.toJSONString());
        writer.close();
	}

	/**
	 * Get response from httpConnection on String
	 * @param httpConnection
	 * @return response on String
	 */
	private String getResponse(final HttpURLConnection httpConnection) throws IOException {
		StringBuffer responseBuffer = new StringBuffer();
		BufferedReader inputBufferReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
		String inputLine = inputBufferReader.readLine();
		while (inputLine != null) {
			responseBuffer.append(inputLine);
			inputLine = inputBufferReader.readLine();
		}
		inputBufferReader.close();
		
		return responseBuffer.toString();
	}
	
}
