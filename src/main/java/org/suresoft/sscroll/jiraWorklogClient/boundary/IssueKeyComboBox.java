package org.suresoft.sscroll.jiraWorklogClient.boundary;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JTextField;

public class IssueKeyComboBox extends JComboBox<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1172270761356311297L;
 
	private static final int MAXIMUM_COUNT = 10; 
	
	public IssueKeyComboBox() {
		super();
		setEditable(true);
	}
	
	public void addIssueKey(final String issueKey) {
		// check for duplicate
		for( final String existIssueKey : getIssueKeys() ) {
			if( existIssueKey.compareTo(issueKey) == 0 ) {	// if it exists
				return;
			}
		}
		insertItemAt(issueKey, 0);
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
	
	public void addItems(final List<String> issueKeys) {
		for( final String issueKey : issueKeys ) {
			addItem(issueKey);
		}
	}

	// ComboBox default editor has an internal class BasicComboBoxEditor$BorderlessTextField 
	public JTextField getTextEditor() {
		JTextField textEditor = null;
		Component component = getEditor().getEditorComponent();
		if (component instanceof JTextField) {
			textEditor = (JTextField) component;
		}
		return textEditor;
	}
	
	public String getIssueKey() {
		return (String) getEditor().getItem();
	}
	
	public List<String> getIssueKeys() {
		ArrayList<String> issueKeys = new ArrayList<String>();
		for( int i = 0; i < getItemCount(); ++i ) {
			issueKeys.add(getItemAt(i));
		}
		return issueKeys;
	}
}
