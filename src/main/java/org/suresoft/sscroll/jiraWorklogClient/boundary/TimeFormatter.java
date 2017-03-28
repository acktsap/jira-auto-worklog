package org.suresoft.sscroll.jiraWorklogClient.boundary;

public class TimeFormatter {
	
	/**
	 * Convert time format (eg. 1h 30m) to seconds.<br/>
	 * Eg. 1h 30m -> returns 3600 + 30*60 = 4800.<br/>
	 * If time is in wrong format, return 0.
	 * @param time
	 * @return time by seconds
	 */
	public static int timeToSeconds(final String time) {
		if( time == null || time.equals("") ) {
			System.err.println("time is in wrong format");
			return 0;
		}
		
		String[] splits = time.split(" ");
		
		int timeBySeconds = 0;
		for( final String split : splits ) {
			if (split.charAt(split.length() - 1) == 'h'
					|| split.charAt(split.length() - 1) == 'H') {
				timeBySeconds += Integer.valueOf(split.substring(0, split.length() - 1)) * 3600;
			} else if (split.charAt(split.length() - 1) == 'm'
					|| split.charAt(split.length() - 1) == 'M') {
				timeBySeconds += Integer.valueOf(split.substring(0, split.length() - 1)) * 60;
			} else {
				System.err.println("Wrong Input. ex) 6h 10m");
			}
		}
		return timeBySeconds;
	}
	
	/**
	 * Transform seconds to time format(eg. 1h 30m)
	 * @param seconds
	 * @return formatted time in String
	 */
	public static String secondToTime(final int seconds) {
		StringBuffer timeBuffer = new StringBuffer();
		if( seconds >= 0 ) {
			int hour = seconds / 3600;
			int minute = (seconds % 3600) / 60;
			
			if( hour != 0 ) {
				timeBuffer.append(hour);
				timeBuffer.append("h");
				if( minute != 0 ) {
					timeBuffer.append(" ");
					timeBuffer.append(minute);
					timeBuffer.append("m");
				}
			} else {	// hour == 0
				timeBuffer.append(minute);
				timeBuffer.append("m");
			}
		}
		return timeBuffer.toString();
	}
}
