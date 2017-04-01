package org.suresoft.sscroll.jiraWorklogClient.boundary;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JTextField;

public class IssueKeyComboBox extends JComboBox<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1172270761356311297L;
 
	private static final int PREFERRED_WIDTH = 165;
	private static final int PREFERRED_HEIGHT = 25;
	private static final int MAXIMUM_COUNT = 10; 
	
	public IssueKeyComboBox() {
		super();
		setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
		setEditable(true);
		setMaximumRowCount(MAXIMUM_COUNT);
	}
	
	/**
	 * Add current issue to the comboBox with no duplicate key
	 */
	public void addCurrentIssue() {
		// check for duplicate issue key
		String issueKey = getCurrentIssueKey();
		for( final String existIssueKey : getIssueKeys() ) {
			if( existIssueKey.compareTo(issueKey) == 0 ) {	// if it exists
				return;
			}
		}
		
		// add issue to the comboBox 
		insertItemAt(getCurrentIssue(), 0);
		shrinkItemList();
	}

	/**
	 * shrink items if it exceeds MAXIMUM_COUNT
	 */
	private void shrinkItemList() {
		while( getItemCount() > MAXIMUM_COUNT ) {
			removeItemAt(getItemCount() - 1);
		}
	}
	
	public void addItems(final List<String> rawIssueKeys) {
		for( final String issueKey : rawIssueKeys ) {
			addItem(issueKey);
		}
	}

	/**
	 * ComboBox default editor has an internal class BasicComboBoxEditor$BorderlessTextField
	 * @return editor in JTextField
	 */
	public JTextField getTextEditor() {
		JTextField textEditor = null;
		Component component = getEditor().getEditorComponent();
		if (component instanceof JTextField) {
			textEditor = (JTextField) component;
		}
		return textEditor;
	}
	
	/**
	 * Get only issue key(eg. KP-27)
	 * @return issue key
	 */
	public String getCurrentIssueKey() {
		return extractKey(getCurrentIssue());
	}
	
	/**
	 * Get issue of the form "KP-27 - do something"
	 * @return issue
	 */
	public String getCurrentIssue() {
		return (String) getEditor().getItem();
	}
	
	/**
	 * Get "KP-27 - 3¿ù È¸ÀÇ" and returns "KP-27"
	 * @param rawIssueKey
	 * @return issue key
	 */
	private String extractKey(final String rawIssueKey) {
		return rawIssueKey.split(" - ")[0];
	}
	
	/**
	 * Get list of issue key(eg. KP-27)
	 * @return list of issue key
	 */
	public List<String> getIssueKeys() {
		ArrayList<String> issueKeys = new ArrayList<String>();
		for( int i = 0; i < getItemCount(); ++i ) {
			String issueKey = extractKey(getItemAt(i));
			issueKeys.add(issueKey);
		}
		return issueKeys;
	}
	
	/**
	 * Get list of issues of the form "KP-27 - do something"
	 * @return list of issues
	 */
	public List<String> getIssues() {
		ArrayList<String> issues = new ArrayList<String>();
		for( int i = 0; i < getItemCount(); ++i ) {
			issues.add(getItemAt(i));
		}
		return issues;
	}
}
