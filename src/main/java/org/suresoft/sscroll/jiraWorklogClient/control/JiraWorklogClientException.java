package org.suresoft.sscroll.jiraWorklogClient.control;

public abstract class JiraWorklogClientException {
	public static final class MakeSessionFailedException extends RuntimeException {
		public MakeSessionFailedException(final String message) {
			super(message);
		}
	}	
}


