package org.suresoft.sscroll.jiraAutoLogging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.suresoft.sscroll.jiraAutoLogging.control.CharsetDetector;
import org.suresoft.sscroll.jiraAutoLogging.control.ServerArbiter;
import org.suresoft.sscroll.jiraAutoLogging.entity.LoggerInfo;
import org.suresoft.sscroll.jiraAutoLogging.entity.LoggingData;

public class Starter {

	private static final String[] CHARSETS_CONCERNS = new String[] { "UTF-8" };
	private static final String FILE_NAME = "sample.txt";
	
	public static void main(String[] args) throws Exception {
		
//		UserData userData = new UserData();
//
//		List<String> inputData = Input(); // 데이터를 입력받음
//		LoggingData loggingData = makeData(inputData, userData); // 필요한 정보를 입력
//		
//		
//		
//		ServerArbiter serverArbiter = new ServerArbiter();
//		serverArbiter.setJiraServer(userData.getAddress(), userData.getPort());
//		serverArbiter.getAuth(userData.getId(), userData.getPw());
//		
//		JSONArray jArray = JsonParser.makeJsonArray(loggingData);
//
//		for (int i = 0; i < jArray.size(); i++) {
//			JSONObject jObj = (JSONObject) jArray.get(i);
//			serverArbiter.sendPost(jObj.toJSONString());
//		}
		
		ServerArbiter serverArbiter = new ServerArbiter();
		serverArbiter.setJiraServer("211.116.223.80", "8080");
		
		LoggerInfo userData = getUserData();
		serverArbiter.makeSession(userData);
		
		LoggingData loggingData = getLoggingData();
		serverArbiter.sendPost(loggingData);
	}

	private static LoggerInfo getUserData() {
		LoggerInfo sessionData = new LoggerInfo();
		
		// TODO, do something, it's just a stub
		sessionData.setId("tilim");
		sessionData.setPassword("sure4rl1!");
		
		return sessionData;
	}

	/*
	 * Res 1h 
	 * Key TP-17 
	 * Name dskim/최현진
	 * DateStarted 2016-12-20 
	 * TimeSpent 1h
	 * Comment 된당
	 */
	private static LoggingData getLoggingData() {
		LoggingData loggingData = new LoggingData();
		
		// TODO, do something, it's just a stub
		loggingData.setIssuekey("KEPRIPRJ-253");
//		loggingData.setRemainingEstimateSeconds(0);
		
		ArrayList<String> nameList = new ArrayList<String>();
		nameList.add("tilim");
		loggingData.setNameList(nameList);
		loggingData.setDateStarted("2017-02-22");
		loggingData.setTimeSpentSeconds(7200);
		loggingData.setComment("logging test");
		
		return loggingData;
	}
	

	private static List<String> Input() {
		List<String> inputdata = new ArrayList<String>();
		
		try {
			File inFile = new File(FILE_NAME); // input.txt 입력 받음
			CharsetDetector cd = new CharsetDetector();
			Charset charset = cd.detectCharset(inFile, CHARSETS_CONCERNS);
			
			BufferedReader bufferedReader = null;
			if(charset != null) { // UTF-8일때
				bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), charset));
			} else { // MS949일때
				bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "MS949"));
			}
			
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				inputdata.add(line);
			}
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		return inputdata;
	}
	
	private static LoggingData makeData(List<String> inputdata, LoggerInfo userData) {
		LoggingData loggingData = new LoggingData();
		for (int i = 0; i < inputdata.size(); ++i) {
			String[] line = inputdata.get(i).split("\t"); // split by tab

			if (line[0].equals("Pw")) {
//				userData.setPw(line[1]);
			} else if (line[0].equals("Admin")) {
//				userData.setId(line[1]);
			} else if (line[0].equals("Address")) {
//				userData.setAddress(line[1]);
			} else if (line[0].equals("Port")) {
//				userData.setPort(line[1]);
			} else if (line[0].equals("Res")) {
				String[] split = line[1].split(" ");
				int time = makeTimeBySeconds(split);
				loggingData.setRemainingEstimateSeconds(time);
			} else if (line[0].equals("Key")) {
				loggingData.setIssuekey(line[1]);
			} else if (line[0].equals("Name")) {
				List<String> author = new ArrayList<String>();
				String[] Nlist = line[1].split("/");
				for (int j = 0; j < Nlist.length; ++j) {
					author.add(Nlist[j]);
				}
				loggingData.setNameList(author);
			} else if (line[0].equals("DateStarted")) {
				loggingData.setDateStarted(line[1]);
			} else if (line[0].equals("TimeSpent")) {
				String[] split = line[1].split(" ");
				int time = makeTimeBySeconds(split);
				loggingData.setTimeSpentSeconds(time);
			} else if (line[0].equals("Comment")) {
				loggingData.setComment(line[1]);
			}
		}
		return loggingData;
	}

	private static int makeTimeBySeconds(String[] split) {
		int time = 0;
		// 시간과 분을 초단위로 변환
		for (int j = 0; j < split.length; ++j) {
			if (split[j].charAt(split[j].length() - 1) == 'h'
					|| split[j].charAt(split[j].length() - 1) == 'H') {
				time += Integer.valueOf(split[j].substring(0, split[j].length() - 1)) * 3600;
			} else if (split[j].charAt(split[j].length() - 1) == 'm'
					|| split[j].charAt(split[j].length() - 1) == 'M') {
				time += Integer.valueOf(split[j].substring(0, split[j].length() - 1)) * 60;
			} else {
				System.err.println("Wrong Input. ex) 6h 10m\n");
			}
		}
		return time;
	}

}
