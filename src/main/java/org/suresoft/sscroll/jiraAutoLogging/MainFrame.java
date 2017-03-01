package org.suresoft.sscroll.jiraAutoLogging;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.json.simple.parser.ParseException;
import org.suresoft.sscroll.jiraAutoLogging.control.CharsetDetector;
import org.suresoft.sscroll.jiraAutoLogging.control.ServerArbiter;
import org.suresoft.sscroll.jiraAutoLogging.entity.LoggerInfo;
import org.suresoft.sscroll.jiraAutoLogging.entity.LoggingData;

public class MainFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3279630073848170157L;
	private static final String TITLE = "Jira logging";
	
	private ServerArbiter serverArbiter;

	private JTextField ipTextField;
	private JTextField portTextField;
	private JTextField autherTextField;
	private JPasswordField passwordTextField;

	private JTextField issueKeyTextField;
	private JTextArea nameListTextArea;
	private JTextField dateTextField;
	private JTextField timeSpentTextField;
	private JTextArea commentTextArea;
	
	public MainFrame() {
		super(TITLE);

		// basic setting
		setSize(400, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(buildServerField());
		contentPane.add(buildLoggerField());
		contentPane.add(buildLoggingDataField());
		contentPane.add(buildButtonField());

		setVisible(true);
		
		serverArbiter = new ServerArbiter();
	}

	private JComponent buildServerField() {
		ipTextField = new JTextField(9);
		portTextField = new JTextField(4);
		
		JPanel panel = new JPanel();
		panel.setBorder(buildTitleBorder("Server Information"));
		panel.add(buildPanelWithLabel("IP : ", ipTextField));
		panel.add(buildPanelWithLabel("Port : ", portTextField));

		return panel;
	}


	private Component buildLoggerField() {
		autherTextField = new JTextField(8);
		passwordTextField = new JPasswordField(10);
		
		JPanel panel = new JPanel();
		panel.setBorder(buildTitleBorder("Logger Information"));
		panel.add(buildPanelWithLabel("Author : ", autherTextField));
		panel.add(buildPanelWithLabel("Password : ", passwordTextField));
		
		return panel;
	}

	private Component buildLoggingDataField() {
		issueKeyTextField = new JTextField(8);
		nameListTextArea = new JTextArea(2, 13);
		dateTextField = new JTextField(8);
		timeSpentTextField = new JTextField(8);
		commentTextArea = new JTextArea(3, 15);
		
		JPanel loggingDataPanel = new JPanel(new GridLayout(0, 1));
		loggingDataPanel.setBorder(buildTitleBorder("Logging Data"));
		loggingDataPanel.add(buildPanelWithLabel("Issue key : ", issueKeyTextField));
		loggingDataPanel.add(buildPanelWithLabel("Name List(split by \" \") : ", nameListTextArea));
		loggingDataPanel.add(buildPanelWithLabel("Date : ", dateTextField));
		loggingDataPanel.add(buildPanelWithLabel("Time Spent : ", timeSpentTextField));
		loggingDataPanel.add(buildPanelWithLabel("Comment : ", commentTextArea));
		
		return loggingDataPanel;
	}

	private Component buildButtonField() {
		JButton logWorkButton = new JButton("Log Work");
		logWorkButton.addActionListener(this);
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainFrame.this.dispose();
			}
		});
		
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.add(logWorkButton);
		panel.add(exitButton);
		
		return panel;
	}

	private TitledBorder buildTitleBorder(final String title) {
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		TitledBorder titleBorder = BorderFactory.createTitledBorder(border, title);
		titleBorder.setTitleJustification(TitledBorder.LEFT);
		return titleBorder;
	}
	
	private JPanel buildPanelWithLabel(final String labelText, final Component... components) {
		JPanel panel = new JPanel();
		panel.add(new JLabel(labelText));
		for( Component component : components) {
			panel.add(component);
		}
		
		return panel;
	}

	
	// log work
	public void actionPerformed(final ActionEvent event) {
		try {
			String ip = ipTextField.getText();
			String password = portTextField.getText();
			serverArbiter.setJiraServer(ip, password);
			
			System.out.println("ip : " + ip + " , password : " + password);
			
			LoggerInfo loggerInfo = getLoggerInfo();
//			serverArbiter.makeSession(loggerInfo);
			
			LoggingData loggingData = getLoggingData();
//			serverArbiter.sendPost(loggingData);
			
		} /*catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	private LoggerInfo getLoggerInfo() {
		LoggerInfo loggerInfo = new LoggerInfo();
		
		loggerInfo.setId(autherTextField.getText());
		loggerInfo.setPassword(passwordTextField.getText());
		
		System.out.println("author : " + loggerInfo.getId() + " , logger pw : " + loggerInfo.getPassword());
		
		return loggerInfo;
	}

	private LoggingData getLoggingData() {
		LoggingData loggingData = new LoggingData();
		
		loggingData.setIssuekey(issueKeyTextField.getText());
//		loggingData.setRemainingEstimateSeconds(0);
		
		String[] nameListArray = nameListTextArea.getText().split(" ");
		List<String> nameList = new ArrayList<String>(Arrays.asList(nameListArray));
		loggingData.setNameList(nameList);
		
		loggingData.setDateStarted(dateTextField.getText());
		String timeSpent = timeSpentTextField.getText();
		loggingData.setTimeSpentSeconds(makeTimeBySeconds(timeSpent));
		loggingData.setComment(commentTextArea.getText());
		
		System.out.println("Issue key : " + loggingData.getIssuekey());
		System.out.print("Name list : ");
		for( String name : loggingData.getNameList() ) {
			System.out.print(name + " ");
		}
		System.out.println();
		System.out.println("Date : " + loggingData.getDateStarted());
		System.out.println("Time spent seconds : " + loggingData.getTimeSpentSeconds());
		System.out.println("Comment : " + loggingData.getComment());
		
		return loggingData;
	}
	
	private int makeTimeBySeconds(final String time) {
		String[] splits = time.split(" ");
		
		int timeBySeconds = 0;
		for( String split : splits ) {
			if (split.charAt(split.length() - 1) == 'h'
					|| split.charAt(split.length() - 1) == 'H') {
				timeBySeconds += Integer.valueOf(split.substring(0, split.length() - 1)) * 3600;
			} else if (split.charAt(split.length() - 1) == 'm'
					|| split.charAt(split.length() - 1) == 'M') {
				timeBySeconds += Integer.valueOf(split.substring(0, split.length() - 1)) * 60;
			} else {
				System.err.println("Wrong Input. ex) 6h 10m\n");
			}
		}
		return timeBySeconds;
	}
	
	public static void main(String[] args) throws Exception {
		new MainFrame();
	}
}
