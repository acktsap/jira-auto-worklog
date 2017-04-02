package org.suresoft.sscroll.jiraWorklogClient.boundary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UserSelectDialog extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3229523830660061026L;

	private static final String USER_SELECT_DIALOG_TITLE = "Manage author";
	
	private JButton okButton;
	private JButton cancelButton;

	public UserSelectDialog(final JFrame parent) {
		super(parent, USER_SELECT_DIALOG_TITLE, true);

		setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(2, 2, 2, 2);
		JLabel descLabel = new JLabel("Description:");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(descLabel, gbc);

		JTextField descBox = new JTextField(30);
		gbc.gridwidth = 2;
		gbc.gridx = 1;
		gbc.gridy = 0;
		panel.add(descBox, gbc);

		JLabel colorLabel = new JLabel("Choose color:");
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(colorLabel, gbc);

		String[] colorStrings = { "red", "yellow", "orange", "green", "blue" };
		JComboBox colorList = new JComboBox(colorStrings);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 1;
		panel.add(colorList, gbc);

		JLabel spacer = new JLabel(" ");
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(spacer, gbc);

		okButton = new JButton("Ok");
		okButton.addActionListener(this);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 3;
		panel.add(okButton, gbc);

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		gbc.gridx = 1;
		gbc.gridy = 3;
		panel.add(cancelButton, gbc);
		getContentPane().add(panel);
		pack();

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == okButton) {
			// TODO
		} else if ( source == cancelButton ){
			dispose();
		} else {
			// TODO
		}
		
	}

}
