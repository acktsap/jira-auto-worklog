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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.suresoft.sscroll.jiraAutoLogging.entity.LoggerInfo;
import org.suresoft.sscroll.jiraAutoLogging.entity.LoggingData;

public class ServerArbiter {

	
	private static final int VALID_RESPONSE = 200;
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
	
	public void makeSession(final LoggerInfo userData) throws UnsupportedEncodingException, IOException, ParseException {
		HttpURLConnection httpConnection = makeSessionConnection();
		JSONObject jsonSessionData = jiraLogJsonParser.toJsonObject(userData);
		
		sendJsonData(httpConnection, jsonSessionData);
		
		System.out.println("Response Code : " + httpConnection.getResponseCode());
		
		if( httpConnection.getResponseCode() == VALID_RESPONSE ){
			String response = getResponse(httpConnection);
			System.out.println("Response : " + response);
			
			JSONObject responseJson = (JSONObject) jiraLogJsonParser.parse(response);
			setSessionName(jiraLogJsonParser.extractSessionName(responseJson));
			setSessionValue(jiraLogJsonParser.extractSessionValue(responseJson));
		}
	}
	
	public void sendPost(final LoggingData loggingData) throws MalformedURLException, ProtocolException, IOException {
		JSONArray loggingJsonArrayData = jiraLogJsonParser.toJsonArray(loggingData);
		
		for (int i = 0; i < loggingJsonArrayData.size(); i++) {
			JSONObject loggingJsonData = (JSONObject) loggingJsonArrayData.get(i);
			
			HttpURLConnection httpConnection = makeWorkLogConnection();
			sendJsonData(httpConnection, loggingJsonData);
			
			System.out.println("Response Code : " + httpConnection.getResponseCode());

			if( httpConnection.getResponseCode() == VALID_RESPONSE ) {
				String response = getResponse(httpConnection);
				System.out.println("Response : " + response);
			}
		}
	}

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
	
	private void sendJsonData(final HttpURLConnection httpConnection, final JSONObject jsonData)
			throws UnsupportedEncodingException, IOException {
		OutputStreamWriter writer = new OutputStreamWriter(httpConnection.getOutputStream(), "UTF-8");
        writer.write(jsonData.toJSONString());
        writer.close();
	}

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

/*
	public void sendGet() throws Exception {
		
		String url = "http://211.116.223.43:8080/plugins/servlet/tempo-getWorklog/?dateFrom=2016-12-19&dateTo=2016-12-19&format=xml&diffOnly=false&tempoApiToken=cf372508-994d-4e73-a300-cc4b76f13746";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		//print result
		System.out.println(inputLine);
	}
*/
}
