package org.suresoft.sscroll.jiraWorklogClient.boundary;

import javax.swing.JOptionPane;

import org.suresoft.sscroll.jiraWorklogClient.entity.EntityInterface;

public class ConfirmAlertBox extends JOptionPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = -321624515105080093L;
	
	public static int LOG_WORK_RESPONSE = 0;
	public static int CANCEL_RESPONSE = 1;

	/**
	 * Show worklog confirm dialog.<br/>
	 * Return LOG_WORK_RESPONSE if Log work button is clicked.<br/>
	 * Return CANCEL_RESPONSE if Cancel button is clicked.
	 * @param entityInterfaces
	 * @return LOG_WORK_RESPONSE | CANCEL_RESPONSE
	 */
	public static int showConfirmDialog(final EntityInterface... entityInterfaces) {
		StringBuffer confirmStringBuffer = new StringBuffer();
		for( final EntityInterface entityInterface : entityInterfaces ) {
			confirmStringBuffer.append(entityInterface.getSummary());
		}
		String confirmString = confirmStringBuffer.toString();
		
		Object[] options = new Object[] { "Log work", "Cancel" };
		
		return showOptionDialog(null, confirmString, "Really?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, options[0]);
	}
	
	
}
