package org.suresoft.sscroll.jiraAutoLogging;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

public abstract class InputChecker {
	
	protected boolean isRight = false;
	protected JTextComponent inputComponent = null;
	protected JLabel alertLabel = null;
	
	public InputChecker(final JTextComponent inputComponent) {
		this.inputComponent = inputComponent;
	}
	
	public boolean isRight() {
		return isRight;
	}
	
	public void setAlertLabel(final JLabel alertLabel) {
		this.alertLabel = alertLabel;
	}

	public void updateAlertLabel() {
		String inputText = inputComponent.getText();
		
		if( alertLabel != null ) {
			Pattern pattern = getPattern();
			Matcher matcher = pattern.matcher(inputText);
			if( matcher.find() ) {
				alertLabel.setForeground(new Color(0, 180, 0));	// light green
				alertLabel.setText(getRightSignal());
				isRight = true;
			} else {
				alertLabel.setForeground(Color.RED);
				alertLabel.setText(getRightExample());
				isRight = false;
			}
		}
	}

	protected abstract Pattern getPattern();
	protected abstract String getRightSignal();
	protected abstract String getRightExample();
	
}

class IPChecker extends InputChecker {

	public IPChecker(final JTextComponent inputComponent) {
		super(inputComponent);
	}

	@Override
	protected Pattern getPattern() {
		return Pattern.compile("^\\d{1,3}(\\.\\d{1,3}){3}$");
	}

	@Override
	protected String getRightSignal() {
		return "Yeah";
	}
	
	@Override
	protected String getRightExample() {
		return "x, ex) 211.222.34.22";
	}

}

class PortChecker extends InputChecker {

	public PortChecker(final JTextComponent inputComponent) {
		super(inputComponent);
	}

	@Override
	protected Pattern getPattern() {
		// 0 ~ 65535
		return Pattern.compile("^(?:\\d|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])$");
	}

	@Override
	protected String getRightSignal() {
		return "Wow";
	}
	
	@Override
	protected String getRightExample() {
		return "Range : 0 ~ 65535";
	}

}


class IdChecker extends InputChecker {

	public IdChecker(final JTextComponent inputComponent) {
		super(inputComponent);
	}

	@Override
	protected Pattern getPattern() {
		return Pattern.compile("^\\w+$");
	}

	@Override
	protected String getRightSignal() {
		return "That's you";
	}
	
	@Override
	protected String getRightExample() {
		return "Type yourself";
	}

}

class PasswordChecker extends InputChecker {

	public PasswordChecker(final JTextComponent inputComponent) {
		super(inputComponent);
	}

	@Override
	protected Pattern getPattern() {
		return Pattern.compile("^.+$");
	}

	@Override
	protected String getRightSignal() {
		return "Your secret..";
	}
	
	@Override
	protected String getRightExample() {
		return "Type password";
	}

}

class IssueKeyChecker extends InputChecker {

	public IssueKeyChecker(final JTextComponent inputComponent) {
		super(inputComponent);
	}

	@Override
	protected Pattern getPattern() {
		return Pattern.compile("^\\w+-[1-9][0-9]*$");
	}

	@Override
	protected String getRightSignal() {
		return "Excellent";
	}
	
	@Override
	protected String getRightExample() {
		return "ex) KP-207";
	}

}

class UserIdListChecker extends InputChecker {

	public UserIdListChecker(final JTextComponent inputComponent) {
		super(inputComponent);
	}

	@Override
	protected Pattern getPattern() {
		return Pattern.compile("^\\w+(( )+\\w+)*$"); // id separated by space
	}

	@Override
	protected String getRightSignal() {
		return "Good";
	}
	
	@Override
	protected String getRightExample() {
		return "split by \" \"";
	}

}

class TimeSpentChecker extends InputChecker {

	public TimeSpentChecker(final JTextComponent inputComponent) {
		super(inputComponent);
	}

	@Override
	protected Pattern getPattern() {
		return Pattern.compile("^([1-5]?[0-9]|60)m|([1-9]?\\d+h)( +([1-5]?[0-9]|60)m)?$");
	}

	@Override
	protected String getRightSignal() {
		return "Our time is running out";
	}
	
	@Override
	protected String getRightExample() {
		return "ex) 1h 30m, 2h, 45m";
	}

}

class BlankChecker extends InputChecker {

	public BlankChecker(final JTextComponent inputComponent) {
		super(inputComponent);
	}

	@Override
	protected Pattern getPattern() {
		return Pattern.compile("^.+$");
	}

	@Override
	protected String getRightSignal() {
		return "Ok";
	}
	
	@Override
	protected String getRightExample() {
		return "Fill it!";
	}

}
