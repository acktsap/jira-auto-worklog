package org.suresoft.sscroll.jiraWorklogClient.boundary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JFormattedTextField.AbstractFormatter;

// Mar 1, 2017 -> 2017-03-01
public class DateLabelFormatter extends AbstractFormatter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String datePattern = "yyyy-MM-dd";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);
	
	@Override
	public Object stringToValue(final String text) throws ParseException {
		if( text != null ) {
			return dateFormatter.parseObject(text);
		}
		return null;
	}

	@Override
	public String valueToString(final Object value) throws ParseException {
		if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }
		return "";
	}
}
