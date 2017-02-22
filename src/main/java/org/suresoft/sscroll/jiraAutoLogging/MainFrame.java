package org.suresoft.sscroll.jiraAutoLogging;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.suresoft.sscroll.jiraAutoLogging.control.CharsetDetector;
import org.suresoft.sscroll.jiraAutoLogging.entity.LoggerInfo;
import org.suresoft.sscroll.jiraAutoLogging.entity.LoggingData;

public class MainFrame extends JFrame {

	private static final String TITLE = "Jira logging client";
	
	private static final String[] CHARSETS_CONCERNS = new String[] { "UTF-8" };

	

	private JButton button;
	
	public MainFrame() {
		super(TITLE);

		// basic setting
		setLayout(new GridLayout(0, 1));
		setSize(400, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container mainContainer = this.getContentPane();
		mainContainer.add(buildServerField());
		mainContainer.add(buildLoggerField());
		mainContainer.add(buildLoggingDataField());

		setVisible(true);
	}

	private JComponent buildServerField() {
		JPanel panel = new JPanel();

		panel.add(new JLabel("IP : "));
		panel.add(new JTextField(9));
		panel.add(new JLabel("Port : "));
		panel.add(new JTextField(4));

		return panel;
	}

	private Component buildLoggerField() {
		JPanel panel = new JPanel();
		
		JButton makeSessionButton = new JButton("Make Session");
		makeSessionButton.setMnemonic('S');
		
		panel.add(makeSessionButton);
		
		return panel;
	}

	private Component buildLoggingDataField() {
		JPanel panel = new JPanel();
		panel.add(new JButton("3"));
		return panel;
	}
	
	
	
	private LoggerInfo getUserData() {
		LoggerInfo sessionData = new LoggerInfo();
		
		// TODO, do something, it's just a stub
		sessionData.setId("tilim");
		sessionData.setPassword("sure4rl1!");
		
		return sessionData;
	}

	private LoggingData getLoggingData() {
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
			File inFile = new File("input.txt"); // input.txt ?��?�� 받음
			CharsetDetector cd = new CharsetDetector();
			Charset charset = cd.detectCharset(inFile, CHARSETS_CONCERNS);
			
			BufferedReader bufferedReader = null;
			if(charset != null) { // UTF-8?��?��
				bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), charset));
			} else { // MS949?��?��
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
		// ?��간과 분을 초단?���? �??��
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

	public static void main(String[] args) throws Exception {
		
		new MainFrame();
		
//		UserData userData = new UserData();
//
//		List<String> inputData = Input(); // ?��?��?���? ?��?��받음
//		LoggingData loggingData = makeData(inputData, userData); // ?��?��?�� ?��보�?? ?��?��
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
		
/*		ServerArbiter serverArbiter = new ServerArbiter();
		serverArbiter.setJiraServer("211.116.223.80", "8080");
		
		LoggerInfo userData = getUserData();
		serverArbiter.makeSession(userData);
		
		LoggingData loggingData = getLoggingData();
		serverArbiter.sendPost(loggingData);*/
	}
}
