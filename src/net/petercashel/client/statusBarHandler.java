package net.petercashel.client;
import javax.swing.JTextField;

public class statusBarHandler {
	
	//javax.swing.JTextField
	//launcher.statusField;
	
	public static void setStatusBarText(String s) {
		
		launcher.statusField.setText(s);
		launcher.statusField.setEditable(false);
		launcher.statusField.setEnabled(true);
		
	}
	
	public static String getStatusBarText() {
		
		return launcher.statusField.getText();
	}
	
}
