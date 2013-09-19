package net.petercashel.client;
import java.awt.Frame;

import javafx.application.Platform;

import javax.swing.JDialog;
import javax.swing.JDesktopPane;

import java.awt.BorderLayout;
import java.awt.SystemColor;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.SwingConstants;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSeparator;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class settingsDialog extends JDialog {
	public static JTextField serverURLField;
	public static JTextField serverPortField;
	public static JTextField userNameField;
	public static JTextField userCodeField;
	public static JComboBox comboBox;


	public settingsDialog(Frame parent) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				launcher.settingsOpen = false;
				packListContoller.saveSelectedPack();
			}
		});
		setTitle("Settings");
		this.setAutoRequestFocus(true);

		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(SystemColor.control);
		getContentPane().add(desktopPane, BorderLayout.CENTER);

		JLabel lblModPackSettings = new JLabel("Mod Pack Settings");
		lblModPackSettings.setFont(new Font("Monospaced", Font.BOLD, 14));
		lblModPackSettings.setBounds(55, 11, 172, 20);
		desktopPane.add(lblModPackSettings);

		Component horizontalStrut = Box.createHorizontalStrut(20);
		horizontalStrut.setBounds(10, 55, 644, -11);
		desktopPane.add(horizontalStrut);

		JLabel lblSelectModPack = new JLabel("Select Mod Pack");
		lblSelectModPack.setBounds(55, 116, 142, 14);
		desktopPane.add(lblSelectModPack);

		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
	    comboBox = new JComboBox<String>(model);
		comboBox.setBounds(232, 113, 308, 20);
		comboBox.setRenderer(new ComboRenderer());
		desktopPane.add(comboBox);

		serverURLField = new JTextField();
		serverURLField.setBounds(232, 51, 308, 20);
		desktopPane.add(serverURLField);
		serverURLField.setColumns(10);

		JLabel lblNewLabel = new JLabel("Update Server Address");
		lblNewLabel.setBounds(55, 51, 142, 14);
		desktopPane.add(lblNewLabel);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 141, 584, 2);
		desktopPane.add(separator);

		JLabel lblPackSpecificSettings = new JLabel("Pack Specific Settings");
		lblPackSpecificSettings.setFont(new Font("Monospaced", Font.BOLD, 14));
		lblPackSpecificSettings.setBounds(55, 154, 206, 20);
		desktopPane.add(lblPackSpecificSettings);

		JLabel lblMcUserName = new JLabel("MC User Name");
		lblMcUserName.setBounds(55, 200, 100, 14);
		desktopPane.add(lblMcUserName);

		userNameField = new JTextField();
		userNameField.setBounds(232, 197, 308, 20);
		desktopPane.add(userNameField);
		userNameField.setColumns(10);

		userCodeField = new JTextField();
		userCodeField.setBounds(232, 228, 308, 20);
		desktopPane.add(userCodeField);
		userCodeField.setColumns(10);

		JLabel lblUniqueCode = new JLabel("Unique Code");
		lblUniqueCode.setBounds(55, 231, 100, 14);
		desktopPane.add(lblUniqueCode);

		JButton btnRefresh = new JButton("Save");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				new Thread() {
					@Override
					public void run() {
						if (serverURLField.getText().substring(0, 7).toLowerCase().contains("http://")) {
							//we are http
							System.out.println(serverURLField.getText().substring(0, 7).toLowerCase());
							Configuration.setString("apiServerProtocol", serverURLField.getText().substring(0, 4).toLowerCase());
							Configuration.setString("apiServer", serverURLField.getText().substring(7));
							Configuration.setString("apiServerPort", serverPortField.getText());
							statusBarHandler.setStatusBarText("Settings Saved!");

						} else if (serverURLField.getText().substring(0, 8).toLowerCase().contains("https://")) {
							//we are https
							System.out.println(serverURLField.getText().substring(0, 8).toLowerCase());
							Configuration.setString("apiServerProtocol", serverURLField.getText().substring(0, 5).toLowerCase());
							Configuration.setString("apiServer", serverURLField.getText().substring(8));
							Configuration.setString("apiServerPort", serverPortField.getText());
							statusBarHandler.setStatusBarText("Settings Saved!");
						} else {
							// Invalid Address
							// Tell Them. Tell Them Now.
							statusBarHandler.setStatusBarText("Invalid Server Address");

						}

						packListContoller.saveSelectedPack();
						Configuration.setString("authUser",userNameField.getText());
						Configuration.setString("authCode",userCodeField.getText());
						packListContoller.getPackData();
						launcher.settingsOpen = false;
						dispose();
					}
				}.run();			}
		});
		btnRefresh.setBounds(398, 277, 142, 23);
		desktopPane.add(btnRefresh);

		JLabel lblUpdateServerPort = new JLabel("Update Server Port");
		lblUpdateServerPort.setToolTipText("if the start of the address has a colon in it, put the numbers after the colon in this box. Then remove the colon and numbers from the Server Address");
		lblUpdateServerPort.setBounds(55, 85, 142, 14);
		desktopPane.add(lblUpdateServerPort);

		serverPortField = new JTextField();
		serverPortField.setColumns(10);
		serverPortField.setBounds(232, 82, 308, 20);
		desktopPane.add(serverPortField);
		setSize(600, 350);
		setLocationRelativeTo(parent);
		
		serverPortField.setText(Configuration.getString("apiServerPort"));
		serverURLField.setText(Configuration.getString("apiServerProtocol") + "://" + Configuration.getString("apiServer"));
		
		
		userNameField.setText(Configuration.getString("authUser"));
		userCodeField.setText(Configuration.getString("authCode"));
		
		JButton btnReloadPacks = new JButton("Reload Packs");
		btnReloadPacks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					@Override
					public void run() {
						
						if (serverURLField.getText().substring(0, 7).toLowerCase().contains("http://")) {
							//we are http
							System.out.println(serverURLField.getText().substring(0, 7).toLowerCase());
							Configuration.setString("apiServerProtocol", serverURLField.getText().substring(0, 4).toLowerCase());
							Configuration.setString("apiServer", serverURLField.getText().substring(7));
							Configuration.setString("apiServerPort", serverPortField.getText());
							packListContoller.getpackList();

						} else if (serverURLField.getText().substring(0, 8).toLowerCase().contains("https://")) {
							//we are https
							System.out.println(serverURLField.getText().substring(0, 8).toLowerCase());
							Configuration.setString("apiServerProtocol", serverURLField.getText().substring(0, 5).toLowerCase());
							Configuration.setString("apiServer", serverURLField.getText().substring(8));
							Configuration.setString("apiServerPort", serverPortField.getText());
							packListContoller.getpackList();
						} else {
							// Invalid Address
							// Tell Them. Tell Them Now.
							statusBarHandler.setStatusBarText("Invalid Server Address");

						}

						Configuration.setString("authUser",userNameField.getText());
						Configuration.setString("authCode",userCodeField.getText());
					}
				}.run();
			}
		});
		btnReloadPacks.setBounds(232, 277, 156, 23);
		desktopPane.add(btnReloadPacks);
		
		
		Thread getPacks = new Thread() {
			@Override
			public void run() {
				try {
					if (!Configuration.getString("apiServer").isEmpty()) {
						packListContoller.getpackList();
					}	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		getPacks.run();
		if (!Configuration.getString("packName").isEmpty()) {
			comboBox.setSelectedItem(Configuration.getString("packName"));
		}
		
	}
}
