package org.suresoft.sscroll.jiraAutoLogging;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.suresoft.sscroll.jiraAutoLogging.control.ServerArbiter;
import org.suresoft.sscroll.jiraAutoLogging.control.XmlParser;
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
	
	private static final int FRAME_WIDTH = 400;
	private static final int FRAME_HEIGHT = 550;
	
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
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent event) {
				saveDataToFile();
				MainFrame.this.dispose();
			}
		});
		
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
		
		JPanel serverDataPanel = new JPanel();
		serverDataPanel.setLayout(new BoxLayout(serverDataPanel, BoxLayout.Y_AXIS));
		serverDataPanel.setPreferredSize(new Dimension(FRAME_WIDTH, 85));
		serverDataPanel.setBorder(buildTitleBorder("Server Information"));
		serverDataPanel.add(buildPanelWithLabel("IP", ipTextField));
		serverDataPanel.add(buildPanelWithLabel("Port", portTextField));

		return serverDataPanel;
	}

	private Component buildLoggerField() {
		autherTextField = new JTextField(8);
		passwordTextField = new JPasswordField(10);
		
		JPanel loggerDataPanel = new JPanel();
		loggerDataPanel.setLayout(new BoxLayout(loggerDataPanel, BoxLayout.Y_AXIS));
		loggerDataPanel.setPreferredSize(new Dimension(FRAME_WIDTH, 85));
		loggerDataPanel.setBorder(buildTitleBorder("Logger Information"));
		loggerDataPanel.add(buildPanelWithLabel("Author Id", autherTextField));
		loggerDataPanel.add(buildPanelWithLabel("Password", passwordTextField));
		
		return loggerDataPanel;
	}

	private Component buildLoggingDataField() {
		issueKeyTextField = new JTextField(8);
		nameListTextArea = buildJTextArea(2, 20);
		datePicker = buildDatePicker();
		timeSpentTextField = new JTextField(8);
		commentTextArea = buildJTextArea(3, 20);
		
		JPanel loggingDataPanel = new JPanel();
		loggingDataPanel.setLayout(new BoxLayout(loggingDataPanel, BoxLayout.Y_AXIS));
		loggingDataPanel.setBorder(buildTitleBorder("Logging Data"));
		loggingDataPanel.add(buildPanelWithLabel("Issue key", issueKeyTextField));
		loggingDataPanel.add(buildPanelWithLabel("<html><p>User Id List</p><p>(split by \" \")</p></html>", nameListTextArea));
		loggingDataPanel.add(buildPanelWithLabel("Date", datePicker));
		loggingDataPanel.add(buildPanelWithLabel("Time Spent", timeSpentTextField));
		loggingDataPanel.add(buildPanelWithLabel("Comment", commentTextArea));
		
		return loggingDataPanel;
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
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.add(logWorkButton);
		buttonsPanel.add(exitButton);
		
		return buttonsPanel;
	}

	private TitledBorder buildTitleBorder(final String title) {
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		TitledBorder titleBorder = BorderFactory.createTitledBorder(border, title);
		titleBorder.setTitleJustification(TitledBorder.LEFT);
		
		return titleBorder;
	}
	
	// need adjustment
	private JPanel buildPanelWithLabel(final String labelText, final JComponent... components) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		JLabel label = new JLabel(labelText);
		label.setPreferredSize(new Dimension(80, 30));
		label.setHorizontalAlignment(JLabel.RIGHT);
		label.setBorder(new EmptyBorder(0, 0, 0, 7));	// right border
//		label.setVerticalAlignment(JLabel.TOP);
		panel.add(label);
		
		for( Component component : components) {
			panel.add(component);
		}
		return panel;
	}
	
	private JTextArea buildJTextArea(final int rows, final int columns) {
		JTextArea textArea = new JTextArea(rows, columns) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			// tab -> focus next, shift + tab -> focus backward
			@Override
			protected void processComponentKeyEvent(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_TAB) {
					e.consume();
					if (e.isShiftDown()) {
						transferFocusBackward();
					} else {
						transferFocus();
					}
				} else {
					super.processComponentKeyEvent(e);
				}
			}
		};
		textArea.setLineWrap(true);
		textArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		return textArea;
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

	private void fillDataFromFile() {
		XmlParser xmlFileController = new XmlParser();
		xmlFileController.parse(FILE_NAME);
		
		ipTextField.setText(xmlFileController.getValue(XmlParser.Tag.IP));
		portTextField.setText(xmlFileController.getValue(XmlParser.Tag.PORT));
		autherTextField.setText(xmlFileController.getValue(XmlParser.Tag.AUTHOR));
//		passwordTextField.setText(xmlFileParser.getValue(XmlParser.Tag.PASSWORD)); // no password for security

		issueKeyTextField.setText(xmlFileController.getValue(XmlParser.Tag.ISSUE_KEY));
		nameListTextArea.setText(xmlFileController.getValue(XmlParser.Tag.ID_LIST));
//		setDate(xmlFileController.getValue(XmlParser.Tag.DATE));	// don't save date
		timeSpentTextField.setText(xmlFileController.getValue(XmlParser.Tag.TIME_SPENT));
		commentTextArea.setText(xmlFileController.getValue(XmlParser.Tag.COMMENT));
	}
	
	@SuppressWarnings("unused")
	private void setDate(final String date) {
		if( date == null ) {
			return;
		}
		
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
		XmlParser xmlParser = new XmlParser();
		
		xmlParser.setElementValue(XmlParser.Tag.IP, ipTextField.getText());
		xmlParser.setElementValue(XmlParser.Tag.PORT, portTextField.getText());
		xmlParser.setElementValue(XmlParser.Tag.AUTHOR, autherTextField.getText());
//		xmlFileController.setElementValue(XmlParser.Tag.PASSWORD, passwordTextField.getText());

		xmlParser.setElementValue(XmlParser.Tag.ISSUE_KEY, issueKeyTextField.getText());
		xmlParser.setElementValue(XmlParser.Tag.ID_LIST, nameListTextArea.getText());
//		xmlParser.setElementValue(XmlParser.Tag.DATE, getDate());
		xmlParser.setElementValue(XmlParser.Tag.TIME_SPENT, timeSpentTextField.getText());
		xmlParser.setElementValue(XmlParser.Tag.COMMENT, commentTextArea.getText());
		
		xmlParser.save(FILE_NAME);
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
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
