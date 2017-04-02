package org.suresoft.sscroll.jiraWorklogClient.boundary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class UserSelector extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3134145354912847645L;
	
	private JTextArea selectedUsers;
	private JButton button;
	
	public UserSelector(final JFrame parent) {
//		super(new FlowLayout(FlowLayout.LEFT, 0, 0));	// flowlayout with no gap
		super(new BorderLayout());
		
		selectedUsers = new JTextArea(2, 20);
		selectedUsers.setEditable(false);
		selectedUsers.setLineWrap(true);
		selectedUsers.setBackground(super.getBackground()); 
		selectedUsers.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
//		add(selectedUsers);
		add(selectedUsers, BorderLayout.WEST);
		
		button = new JButton("Edit");
		button.setMargin(new Insets(1, 5, 1, 5));
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new UserSelectDialog(parent);
			}
		});
//		add(button);
		add(button, BorderLayout.EAST);
	}
	
	public void addUsers(final List<String> users) {
		// TODO just a stub
		StringBuilder stringBuilder = new StringBuilder();
		for( String user : users ) {
			stringBuilder.append(user + " ");
		}
		selectedUsers.setText(stringBuilder.toString());
	}

	public List<String> getUsers() {
		// TODO just a stub
		List<String> users = new ArrayList<String>();
		users.add("fuck");
		users.add("suck");
		users.add("ssuck");
		return users;
	}

}
