package org.suresoft.sscroll.jiraWorklogClient.boundary;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

public class IssueKeyComboBox extends JComboBox<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1172270761356311297L;

	public IssueKeyComboBox() {
		super();
		setEditable(true);
	}
	
	public void addItems(final List<String> issueKeys) {
		for( final String issueKey : issueKeys ) {
			addItem(issueKey);
		}
	}
	
	public String getIssueKey() {
		return getEditor().toString();
	}
	
	public List<String> getIssueKeys() {
		ArrayList<String> issueKeys = new ArrayList<String>();
		for( int i = 0; i < getItemCount(); ++i ) {
			issueKeys.add(getItemAt(i));
		}
		return issueKeys;
	}
}
