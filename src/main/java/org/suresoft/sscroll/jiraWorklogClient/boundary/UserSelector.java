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

import org.suresoft.sscroll.jiraWorklogClient.entity.User;

public class UserSelector extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3134145354912847645L;
	
	private List<User> users;
	private JTextArea selectedUsersTextArea;
	private JButton button;
	
	public UserSelector(final JFrame parent) {
//		super(new FlowLayout(FlowLayout.LEFT, 0, 0));	// flowlayout with no gap
		super(new BorderLayout());
		
		selectedUsersTextArea = new JTextArea(2, 20);
		selectedUsersTextArea.setEditable(false);
		selectedUsersTextArea.setLineWrap(true);
		selectedUsersTextArea.setBackground(super.getBackground()); 
		selectedUsersTextArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		add(selectedUsersTextArea, BorderLayout.WEST);
		
		button = new JButton("Edit");
		button.setMargin(new Insets(1, 5, 1, 5));
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new UserSelectDialog(parent);
			}
			
		});
		add(button, BorderLayout.EAST);
	}
	
	public void setUsers(final List<User> users) {
		this.users = users;
		updateSelectedUsersTextArea();
	}

	public List<User> getUsers() {
		return users;
	}
	
	public List<String> getSelectedUsers() {
		List<String> selectedUsers = new ArrayList<String>();
		
		for (final User user : users) {
			if( user.isSelected() ) {
				selectedUsers.add(user.getName());
			}
		}
		
		return selectedUsers;
	}
	
	private void updateSelectedUsersTextArea() {
		StringBuilder stringBuilder = new StringBuilder();
		
		for (final User userInfo : users) {
			if( userInfo.isSelected() ) {
				stringBuilder.append(userInfo.getName());
				stringBuilder.append(" ");
			}
		}
		stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		
		selectedUsersTextArea.setText(stringBuilder.toString());
	}

}
