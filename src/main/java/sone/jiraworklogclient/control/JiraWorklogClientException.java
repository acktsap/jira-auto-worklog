package sone.jiraworklogclient.control;

public abstract class JiraWorklogClientException {
	
	public static final class MakeSessionFailedException extends RuntimeException {
		public MakeSessionFailedException(final String message) {
			super(message);
		}
	}	
	
	public static final class XmlFileParsingException extends RuntimeException {
		public XmlFileParsingException(final String message) {
			super(message);
		}
	}	
}


