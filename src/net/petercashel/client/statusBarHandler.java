package net.petercashel.client;

public class statusBarHandler {

    //javax.swing.JTextField
    //launcher.statusField;

    public static String getStatusBarText() {

        return launcher.statusField.getText();
    }

    public static void setStatusBarText(String s) {

        launcher.statusField.setText(s);
        launcher.statusField.setEditable(false);
        launcher.statusField.setEnabled(true);

    }

}
