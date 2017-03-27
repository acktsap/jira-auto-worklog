package org.suresoft.sscroll.jiraWorklogClient.control;

public abstract class JiraAutoLoggingException {
	public static final class MakeSessionFailedException extends RuntimeException {
		public MakeSessionFailedException(final String message) {
			super(message);
		}
	}	
}


