package net.petercashel.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;


public class settingsDialog extends JDialog {
    public static JTextField ServerVersionURL;
    public static JTextField InstallDirPath;
    public static JFileChooser chooser;


    public settingsDialog(Frame parent) {

        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Select Installation Directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                launcher.settingsOpen = false;
            }
        });
        setTitle("Settings");
        this.setAutoRequestFocus(true);

        JDesktopPane desktopPane = new JDesktopPane();
        desktopPane.setBackground(SystemColor.control);
        getContentPane().add(desktopPane, BorderLayout.CENTER);

        JLabel lblSettings = new JLabel("Launcher Settings");
        lblSettings.setFont(new Font("Monospaced", Font.BOLD, 14));
        lblSettings.setBounds(40, 11, 206, 20);
        desktopPane.add(lblSettings);

        JLabel ServerVersionURLName = new JLabel("Server Version URL");
        ServerVersionURLName.setBounds(40, 60, 160, 14);
        desktopPane.add(ServerVersionURLName);

        ServerVersionURL = new JTextField();
        ServerVersionURL.setBounds(170, 60, 370, 20);
        desktopPane.add(ServerVersionURL);
        ServerVersionURL.setColumns(10);

        InstallDirPath = new JTextField();
        InstallDirPath.setBounds(170, 95, 370, 20);
        desktopPane.add(InstallDirPath);
        InstallDirPath.setColumns(10);

        JLabel InstallDirName = new JLabel("Installation Directory");
        InstallDirName.setBounds(40, 95, 160, 14);
        desktopPane.add(InstallDirName);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                new Thread() {
                    @Override
                    public void run() {
                        Configuration.setString("ServerVersionJSON", ServerVersionURL.getText());
                        Configuration.setString("InstallDir", InstallDirPath.getText());
                        launcher.settingsOpen = false;
                        dispose();
                    }
                }.run();
            }
        });
        btnSave.setBounds(398, 126, 142, 23);
        desktopPane.add(btnSave);
        setSize(600, 200);
        setLocationRelativeTo(parent);

        final JButton btnBrowse = new JButton("Browse");
        btnBrowse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    @Override
                    public void run() {
                        int result = chooser.showOpenDialog(null);
                        switch (result) {
                            case JFileChooser.APPROVE_OPTION:
                                String s = null;
                                try {
                                    s = chooser.getSelectedFile().getCanonicalPath();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                InstallDirPath.setText(s);
                                break;
                        }
                    }
                }.run(
                );
            }
        });
        btnBrowse.setBounds(170, 126, 156, 23);
        desktopPane.add(btnBrowse);

        ServerVersionURL.setText(Configuration.getString("ServerVersionJSON"));
        InstallDirPath.setText(Configuration.getString("InstallDir"));

    }
}
