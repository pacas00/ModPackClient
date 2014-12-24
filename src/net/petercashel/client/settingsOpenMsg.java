package net.petercashel.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


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
