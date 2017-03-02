package org.suresoft.sscroll.jiraAutoLogging;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.rmi.ConnectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.suresoft.sscroll.jiraAutoLogging.control.ServerArbiter;
import org.suresoft.sscroll.jiraAutoLogging.control.XmlFileController;
import org.suresoft.sscroll.jiraAutoLogging.entity.LoggerInfo;
import org.suresoft.sscroll.jiraAutoLogging.entity.LoggingData;

import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class MainFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3279630073848170157L;
	private static final String FILE_NAME = "information.xml";
	private static final String TITLE = "Jira logging";
	
	private ServerArbiter serverArbiter;

	private JTextField ipTextField;
	private JTextField portTextField;
	private JTextField autherTextField;
	private JPasswordField passwordTextField;

	private JTextField issueKeyTextField;
	private JTextArea nameListTextArea;
	private JDatePickerImpl datePicker;
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
		
		fillDataFromFile();
		
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
		datePicker = buildDatePicker();
		timeSpentTextField = new JTextField(8);
		commentTextArea = new JTextArea(3, 15);
		
		JPanel loggingDataPanel = new JPanel(new GridLayout(0, 1));
		loggingDataPanel.setBorder(buildTitleBorder("Logging Data"));
		loggingDataPanel.add(buildPanelWithLabel("Issue key : ", issueKeyTextField));
		loggingDataPanel.add(buildPanelWithLabel("Name List(split by \" \") : ", nameListTextArea));
		loggingDataPanel.add(buildPanelWithLabel("Date : ", datePicker));
		loggingDataPanel.add(buildPanelWithLabel("Time Spent : ", timeSpentTextField));
		loggingDataPanel.add(buildPanelWithLabel("Comment : ", commentTextArea));
		
		return loggingDataPanel;
	}

	private JDatePickerImpl buildDatePicker() {
		UtilDateModel utilDateModel = new UtilDateModel();
		utilDateModel.setSelected(true);	// set the today

		JDatePanelImpl datePanel = new JDatePanelImpl(utilDateModel);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		
		return datePicker;
	}

	// Mar 1, 2017 -> 2017-03-01
	private class DateLabelFormatter extends AbstractFormatter {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String datePattern = "yyyy-MM-dd";
	    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);
		
		@Override
		public Object stringToValue(String text) throws ParseException {
			return dateFormatter.parseObject(text);
		}

		@Override
		public String valueToString(Object value) throws ParseException {
			if (value != null) {
	            Calendar cal = (Calendar) value;
	            return dateFormatter.format(cal.getTime());
	        }
			return "";
		}
	}
	
	private Component buildButtonField() {
		JButton logWorkButton = new JButton("Log Work");
		logWorkButton.addActionListener(this);
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveDataToFile();
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

	private void fillDataFromFile() {
		XmlFileController xmlFileController = new XmlFileController(FILE_NAME);
		xmlFileController.parse();
		
		ipTextField.setText(xmlFileController.getValue(XmlFileController.Element.IP));
		portTextField.setText(xmlFileController.getValue(XmlFileController.Element.PORT));
		autherTextField.setText(xmlFileController.getValue(XmlFileController.Element.AUTHOR));
//		passwordTextField.setText(xmlFileParser.getValue(XmlFileParser.Element.IP)); // no password for security

		issueKeyTextField.setText(xmlFileController.getValue(XmlFileController.Element.ISSUE_KEY));
		nameListTextArea.setText(xmlFileController.getValue(XmlFileController.Element.NAME_LIST));
		setDate(xmlFileController.getValue(XmlFileController.Element.DATE));
		timeSpentTextField.setText(xmlFileController.getValue(XmlFileController.Element.TIME_SPENT));
		commentTextArea.setText(xmlFileController.getValue(XmlFileController.Element.COMMENT));
	}
	
	private void setDate(final String date) {
		String[] dateInfo = date.split("-");
		
		if( dateInfo.length != 3 ) {	// error 
			return; 
		}
		
		int year = Integer.parseInt(dateInfo[0]);
		int month = Integer.parseInt(dateInfo[1]) - 1;
		int day = Integer.parseInt(dateInfo[2]);
		
		datePicker.getModel().setYear(year);
		datePicker.getModel().setMonth(month);
		datePicker.getModel().setDay(day);
	}

	private void saveDataToFile() {
		XmlFileController xmlFileController = new XmlFileController(FILE_NAME);
		xmlFileController.parse();
		
		xmlFileController.setElementValue(XmlFileController.Element.IP, ipTextField.getText());
		xmlFileController.setElementValue(XmlFileController.Element.PORT, portTextField.getText());
		xmlFileController.setElementValue(XmlFileController.Element.AUTHOR, autherTextField.getText());
//		xmlFileController.setElementValue(XmlFileController.Element.PASSWORD, passwordTextField.getText());

		xmlFileController.setElementValue(XmlFileController.Element.ISSUE_KEY, issueKeyTextField.getText());
		xmlFileController.setElementValue(XmlFileController.Element.NAME_LIST, nameListTextArea.getText());
		xmlFileController.setElementValue(XmlFileController.Element.DATE, getDate());
		xmlFileController.setElementValue(XmlFileController.Element.TIME_SPENT, timeSpentTextField.getText());
		xmlFileController.setElementValue(XmlFileController.Element.COMMENT, commentTextArea.getText());
	}

	// log work
	public void actionPerformed(final ActionEvent event) {
		try {
			String ip = ipTextField.getText();
			String port = portTextField.getText();
			serverArbiter.setJiraServer(ip, port);
			
			System.out.println("ip : " + ip + " , port : " + port);
			
			LoggerInfo loggerInfo = getLoggerInfo();
			serverArbiter.makeSession(loggerInfo);
			
			LoggingData loggingData = getLoggingData();
			serverArbiter.sendPost(loggingData);
			
		} catch (ConnectException e ) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private LoggerInfo getLoggerInfo() {
		LoggerInfo loggerInfo = new LoggerInfo();
		
		loggerInfo.setId(autherTextField.getText());
		loggerInfo.setPassword(new String(passwordTextField.getPassword()));
		
		System.out.println("Author : " + loggerInfo.getId() + " , password : " + loggerInfo.getPassword());
		
		return loggerInfo;
	}

	private LoggingData getLoggingData() {
		LoggingData loggingData = new LoggingData();
		
		loggingData.setIssuekey(issueKeyTextField.getText());
//		loggingData.setRemainingEstimateSeconds(0);
		
		loggingData.setNameList(getNameList());
		loggingData.setDateStarted(getDate());
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

	private List<String> getNameList() {
		String[] nameListArray = nameListTextArea.getText().split(" ");
		List<String> nameList = new ArrayList<String>(Arrays.asList(nameListArray));
		return nameList;
	}

	private String getDate() {
		DateModel<?> dateModel = datePicker.getModel();
		int year = dateModel.getYear();
		int month = dateModel.getMonth() + 1;
		int day = dateModel.getDay();
		
		String date = year + "-" + (month < 10 ? "0" : "") + month + "-" + (day < 10 ? "0" : "") + day;
		
		return date;
	}
	
	private int makeTimeBySeconds(final String time) {
		if( time.equals("") ) {
			return 0;
		}
		
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
