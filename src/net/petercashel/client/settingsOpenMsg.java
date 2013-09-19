package net.petercashel.client;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JDesktopPane;

import java.awt.BorderLayout;
import java.awt.SystemColor;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.SwingConstants;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.JSeparator;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class settingsOpenMsg extends JDialog {
	
	public settingsOpenMsg(Frame parent) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				launcher.settingsOpen = false;
			}
		});
		setTitle("Settings Still Open");
		setSize(400, 100);
		setLocationRelativeTo(parent);
		
		JLabel lblPleaseCloseThe = new JLabel("Please close the settings window before trying to launch.");
		lblPleaseCloseThe.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblPleaseCloseThe, BorderLayout.CENTER);
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		getContentPane().add(btnOk, BorderLayout.SOUTH);
		
	}
}
