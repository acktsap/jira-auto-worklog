package org.suresoft.sscroll.jiraWorklogClient.boundary;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class UserSelectDialog extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3229523830660061026L;
	
	
	private static final int BOX_WIDTH = 365;
	private static final String USER_SELECT_DIALOG_TITLE = "Select User";
	
	private JPanel checkBoxPanel;
	
	private JButton addButton;
	private JButton removeButton;
	private JButton modifyButton;
	
	private JButton confirmButton;
	private JButton cancelButton;

	public UserSelectDialog(final JFrame parent) {
		super(parent, USER_SELECT_DIALOG_TITLE, true);

		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(buildCheckboxPanel());
		contentPane.add(buildButtonsField());
		
		setLocationRelativeTo(null);
		pack();
		setVisible(true);
	}
	
	private JComponent buildCheckboxPanel() {
		checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		checkBoxPanel.setBorder(buildTitleBorder(USER_SELECT_DIALOG_TITLE));
		
		// TODO get id, name, selected state from parent
		int nUser = 11;
		for( int i = 0; i < nUser; ++i ) {
			JCheckBox testCheckBox = new JCheckBox();
			testCheckBox.setName("id");
			testCheckBox.setText("name");
			testCheckBox.setSelected(true);
			checkBoxPanel.add(testCheckBox);
		}
		
		// set the height dynamically
		checkBoxPanel.setPreferredSize(new Dimension(BOX_WIDTH, 30 + 32 * (nUser / 5 + 1)));
		
		return checkBoxPanel;
	}

	private TitledBorder buildTitleBorder(final String title) {
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		TitledBorder titleBorder = BorderFactory.createTitledBorder(border, title);
		titleBorder.setTitleJustification(TitledBorder.LEFT);

		return titleBorder;
	}

	private JComponent buildButtonsField() {
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1, 2));
		buttonsPanel.add(buildLeftButtons());
		buttonsPanel.add(buildRightButtons());
		
		return buttonsPanel;
	}

	private JComponent buildLeftButtons() {
		JPanel leftButtonsPanel = new JPanel();
		leftButtonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		Insets insets = new Insets(2, 2, 2, 2);
		
		addButton = new JButton("Add");
		addButton.addActionListener(this);
		addButton.setMargin(insets);
		leftButtonsPanel.add(addButton);
		
		removeButton = new JButton("Remove");
		removeButton.addActionListener(this);
		removeButton.setMargin(insets);
		leftButtonsPanel.add(removeButton);
		
		modifyButton = new JButton("Modify");
		modifyButton.addActionListener(this);
		modifyButton.setMargin(insets);
		leftButtonsPanel.add(modifyButton);
		
		return leftButtonsPanel;
	}

	private JComponent buildRightButtons() {
		JPanel rightButtonsPanel = new JPanel();
		rightButtonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		Insets insets = new Insets(2, 6, 2, 6);

		confirmButton = new JButton("Confirm");
		confirmButton.addActionListener(this);
		confirmButton.setMargin(insets);
		rightButtonsPanel.add(confirmButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		cancelButton.setMargin(insets);
		rightButtonsPanel.add(cancelButton);
		
		return rightButtonsPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if( source == addButton ) {
			addButtonClicked();
		} else if( source == removeButton ) {
			removeButtonClicked();
		} else if( source == modifyButton ) {
			modifyButtonClicked();
		} else if (source == confirmButton) {
			confirmButtonClicked();
		} else if ( source == cancelButton ){
			dispose();
		} 
		
	}

	private void addButtonClicked() {
		// TODO Auto-generated method stub
		
	}

	private void removeButtonClicked() {
		// TODO Auto-generated method stub
		
	}

	private void modifyButtonClicked() {
		// TODO Auto-generated method stub
		
	}

	private void confirmButtonClicked() {
		// TODO
		dispose();
	}
	
}
