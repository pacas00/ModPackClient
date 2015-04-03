package net.petercashel.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import net.miginfocom.swing.MigLayout;
import net.petercashel.update.updateLauncherSide;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;


public class launcher {

    public static final JButton btnLaunch = new JButton("Launch");
    final static JFXPanel fxPanel = new JFXPanel();
    private static final Font MONOSPACED = new Font("Monospaced", 0, 13);
    //Development bools. Release with all true
    private static final boolean enableJFX = true;
    public static JTextField statusField;
    public static boolean settingsOpen = false;
    public static long packVersion;
    public static boolean doneUpdate = false;
    static Thread UpdateThread;
    static JTextPane editorPane;
    private static JFrame frame;
    private static JScrollPane Log;
    private static WebView webView;
    private static JDialog settingsDialogHandle;
    //Vars
    private static boolean updateFound = false;
    private static boolean UpdateForced = false;
    private static boolean updateChecked = false;
    private static boolean updateCheckRunning = false;
    private static String version = "1.0.0";
    private static JButton btnSettings;
    ;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

    	//Do Update Check!
    	//
    	boolean upd = false;
		try {
			upd = updateLauncherSide.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if (!upd) System.out.println("No Update");
    	File f = new File("updateLauncher.jar");
    	if (f.exists()) f.delete();
    	f = null;
    	
        //Configuration Prep
        Configuration.initProp();
        UpdateThread = new Thread() {
            @Override
            public void run() {

                //Check if update is done, or running.
                if (updateChecked) {
                    //Nothing to do
                } else {
                    if (updateCheckRunning) {
                        while (updateCheckRunning) {
                            try {
                                Thread.sleep(200L);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    } else {
                        //Need to fire off the update check
                        UpdateCheckRunnerLogic();
                    }
                }

                if (UpdateForced) updateFound = true;
                //Do Update if needed,
                if (updateFound) doUpdate();

                //Then run launchbootsrap
                launchBootstrap();

            }
        };

        //FIRE THE MAIN CANNONS!
        //
        //Err.. i mean initalize()

        new File(OS_Util.getWorkingDirectory().getAbsolutePath() + File.separator + "tmp").mkdirs();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initialize();
            }
        });
    }

    /**
     * Create the application.
     */

    /**
     * Initialize the contents of the frame.
     */
    private static void initialize() {

        frame = new JFrame("HTB3 Launcher");
        frame.addWindowFocusListener(new WindowFocusListener() {
            public void windowGainedFocus(WindowEvent e) {
                try {
                    if (settingsOpen) {
                        settingsDialogHandle.toFront();
                    }

                } catch (NullPointerException noWindow) {

                }
            }

            public void windowLostFocus(WindowEvent e) {
            }
        });
        frame.getContentPane().setBackground(UIManager.getColor("window"));
        frame.setBounds(100, 100, 1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[400.00,grow,top][25px:25px:25px,grow,bottom]"));
        frame.setVisible(true);


        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBorder(null);
        frame.getContentPane().add(tabbedPane, "cell 0 0,grow");
        if (enableJFX) {
            tabbedPane.addTab("News", null, fxPanel, null);
            fxPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getX() < 9 && e.getY() < 9) {
                        System.out.println("EASTER-EGG!");
                        Platform.runLater(new Runnable() { // this will run initFX as JavaFX-Thread
                            @Override
                            public void run() {
                                if (enableJFX) {
                                    webView.getEngine().load("http://www.matmartinez.net/nsfw/");
                                }
                            }
                        });

                    }
                }
            });
            fxPanel.addHierarchyBoundsListener(new HierarchyBoundsAdapter() {
                @Override
                public void ancestorResized(HierarchyEvent e) {
                    if (enableJFX) {
                        try {
                            webView.setMinSize(fxPanel.getWidth(), fxPanel.getHeight());
                            webView.setMaxSize(fxPanel.getWidth(), fxPanel.getHeight());
                        } catch (NullPointerException nu) {

                        }
                    }

                }
            });
        }
        editorPane = new JTextPane() {
            private static final long serialVersionUID = 1L;
        };
        editorPane.setBackground(Color.DARK_GRAY);
        editorPane.setBackground(new Color(70, 130, 180, 255));
        editorPane.setForeground(new Color(0, 0, 0, 255));
        editorPane.setEditable(false);
        editorPane.setFont(MONOSPACED);

        DefaultCaret caret = (DefaultCaret) editorPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        Log = new JScrollPane();
        tabbedPane.addTab("Console", null, Log, null);

        Log.setViewportView(editorPane);

        JPanel panel = new JPanel();
        panel.setBorder(null);
        frame.getContentPane().add(panel, "flowx,cell 0 1,grow");
        panel.setLayout(new BorderLayout(0, 0));

        btnSettings = new JButton("Settings");
        panel.add(btnSettings, BorderLayout.WEST);


        panel.add(btnLaunch, BorderLayout.EAST);

        statusField = new JTextField();
        panel.add(statusField, BorderLayout.CENTER);
        statusField.setBackground(UIManager.getColor("scrollbar"));
        statusField.setEditable(false);
        statusField.setColumns(120);
        btnLaunch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                println("Launch Pressed");
                if (settingsOpen) {
                    new settingsOpenMsg(frame).setVisible(true);
                } else {
                    UpdateThread.start();
                }
            }
        });
        btnLaunch.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (settingsOpen) {
                    new settingsOpenMsg(frame).setVisible(true);
                } else if (e.getButton() == MouseEvent.BUTTON3 || SwingUtilities.isRightMouseButton(e)) {
                    if (updateFound) {
                        btnLaunch.setText("Launching");
                    //Ignore Update, Run launchbootsrap
                    new Thread() {
                        @Override
                        public void run() {
                            launchBootstrap();
                        }
                    }.start();
                    } else {
                        UpdateForced = true;
                        UpdateThread.start();
                    }
                }
            }
        });

        btnSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!settingsOpen) {
                    settingsDialogHandle = new settingsDialog(frame);
                    settingsDialogHandle.setVisible(true);
                    settingsOpen = true;
                    println("Settings Opened");
                }
            }
        });

        Platform.runLater(new Runnable() { // this will run initFX as JavaFX-Thread
            @Override
            public void run() {
                if (enableJFX) {
                    initFX(fxPanel, "http://blog.petercashel.net/");
                }
            }
        });

        println("HTB Launcher");
        println("Version " + launcher.version);

        OutputStreamWrapper out = new OutputStreamWrapper();
        PrintStream consoleTab = new PrintStream(out);
        System.setOut (consoleTab);
        System.setErr(consoleTab);

        new Thread() {
            @Override
            public void run() {
                UpdateCheckRunnerLogic();
            }
        }.start();
    }

    private static void UpdateCheckRunnerLogic() {
        updateCheckRunning = true;
        if (Configuration.getString("InstallDir") != null && Configuration.getString("ServerVersionJSON") != null && Configuration.getString("InstallDir").length() > 4 && Configuration.getString("ServerVersionJSON").length() > 4) {
            boolean result = doUpdateCheck(new File(Configuration.getString("InstallDir")));
            if (result) {
                println("Updates Available");
                statusField.setText("Updates Available");
                btnLaunch.setText("Update");
            } else {
                println("No Updates Available");
                statusField.setText("No Updates Available");
                btnLaunch.setText("Launch");
            }
            updateFound = result;
        }
        updateChecked = true;
        updateCheckRunning = false;
    }

    /* Creates a WebView and fires up google.com */
    private static void initFX(final JFXPanel fxPanel, String URL) {
        if (enableJFX) {
            Group group = new Group();
            Scene scene = new Scene(group);
            fxPanel.setScene(scene);

            webView = new WebView();
            group.getChildren().add(webView);
            webView.autosize();
            webView.setMinSize(fxPanel.getWidth(), fxPanel.getHeight());
            webView.setMaxSize(fxPanel.getWidth(), fxPanel.getHeight());
            final WebEngine webEngine = webView.getEngine();

            webEngine.locationProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String oldLoc, String newLoc) {
                    // check if the newLoc corresponds to a file you want to be downloadable
                    // and if so trigger some code and dialogs to handle the download.

                    //engine.spotscenered.info

                    if (!newLoc.contains("adf.ly/go.php") && !newLoc.contains("/http://") && !newLoc.contains("/https://") && !newLoc.contains("www.mediafire.com/download/") && !newLoc.contains("engine.spotscenered.info")) {
                        System.out.println(newLoc);

                        String downloadableExtension = null;  // todo I wonder how to find out from WebView which documents it could not process so that I could trigger a save as for them?
                        String[] downloadableExtensions = {".zip", ".jar", ".rar", ".7z"};
                        for (String ext : downloadableExtensions) {
                            if (newLoc.endsWith(ext)) {
                                downloadableExtension = ext;
                                break;
                            }
                        }
                        if (downloadableExtension != null) {
                            // create a file save option for performing a download.
                            FileChooser chooser = new FileChooser();
                            chooser.setTitle("Save " + newLoc);
                            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Downloadable File", downloadableExtension));
                            chooser.setInitialFileName(newLoc.substring(newLoc.lastIndexOf("/") + 1, newLoc.length()));
                            chooser.setInitialDirectory(new File(OS_Util.getWorkingDirectory().getAbsolutePath() + File.separator + "tmp"));
                            int filenameIdx = newLoc.lastIndexOf("/") + 1;
                            if (filenameIdx != 0) {
                                File saveFile = chooser.showSaveDialog(webView.getScene().getWindow());

                                if (saveFile != null) {
                                    statusBarHandler.setStatusBarText("Downloading " + saveFile.toString());
                                    BufferedInputStream is = null;
                                    BufferedOutputStream os = null;
                                    try {
                                        is = new BufferedInputStream(new URL(newLoc).openStream());
                                        os = new BufferedOutputStream(new FileOutputStream(saveFile));
                                        int b = is.read();
                                        while (b != -1) {
                                            os.write(b);
                                            b = is.read();
                                        }
                                    } catch (FileNotFoundException e) {
                                        System.out.println("Unable to save file: " + e);
                                    } catch (MalformedURLException e) {
                                        System.out.println("Unable to save file: " + e);
                                    } catch (IOException e) {
                                        System.out.println("Unable to save file: " + e);
                                    } finally {
                                        try {
                                            if (is != null) is.close();
                                        } catch (IOException e) { /** no action required. */}
                                        try {
                                            if (os != null) os.close();
                                        } catch (IOException e) { /** no action required. */}
                                        statusBarHandler.setStatusBarText("Download Complete! " + saveFile.toString());
                                    }
                                }
                            }
                        }
                    }
                }
            });
            webEngine.load(URL);
            webEngine.setJavaScriptEnabled(true);
        }
    }

    public static void println(String string) {
        String s = editorPane.getText();
        editorPane.setText(s + string + "\n");
        editorPane.setFont(MONOSPACED);
    }
    public static void print(String string) {
        String s = editorPane.getText();
        editorPane.setText(s + string);
        editorPane.setFont(MONOSPACED);
    }

    // Functions below here are for mod update threads

    private static boolean doUpdateCheck(File workDir) {

        println("Checking for modpack updates");
        statusField.setText("Checking for modpack updates");
        Boolean modpackNeedsUpdate = false;

        if (new File(workDir.toString() + File.separator + "version.json").exists()) {

            //Check if version of pack is up to date

            @SuppressWarnings("unused")
            String versionJSONFile;
            FileInputStream inputStream;
            try {
                inputStream = new FileInputStream(workDir.toString() + File.separator + "version.json");
                versionJSONFile = IOUtils.toString(inputStream);
                inputStream.close();
                JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(versionJSONFile);

                packVersion = Long.valueOf(jsonObject.getString("version").toString().replace("\"", "").replace("\\", ""));

            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                modpackNeedsUpdate = true;
            } catch (IOException e) {
                e.printStackTrace();
                modpackNeedsUpdate = true;
            } catch (Exception e) {
                e.printStackTrace();
                modpackNeedsUpdate = true;
            }

            long webPackVersion = 0;

            try {

                Util.downloadFile(Configuration.getString("ServerVersionJSON"), workDir.toString() + File.separator , "versionWeb.json");
                inputStream = new FileInputStream(workDir.toString() + File.separator + "versionWeb.json");
                versionJSONFile = IOUtils.toString(inputStream);
                inputStream.close();
                JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(versionJSONFile);

                File json = new File(workDir.toString() + File.separator + "versionWeb.json");
                json.delete();
                webPackVersion = Long.valueOf(jsonObject.getString("version").toString().replace("\"", "").replace("\\", ""));


            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                modpackNeedsUpdate = true;
            } catch (IOException e) {
                e.printStackTrace();
                modpackNeedsUpdate = true;
            } catch (Exception e) {
                e.printStackTrace();
                modpackNeedsUpdate = true;
            }

            if (webPackVersion > packVersion) {
                modpackNeedsUpdate = true;
                println("Has Version " + packVersion + ". Latest is " + webPackVersion);
                packVersion = webPackVersion;
            }


        } else {
            modpackNeedsUpdate = true;
            //Get Version

            long webPackVersion = 0;

            try {

                String versionJSONFile;
                FileInputStream inputStream;
                Util.downloadFile(Configuration.getString("ServerVersionJSON"), workDir.toString() + File.separator , "versionWeb.json");
                inputStream = new FileInputStream(workDir.toString() + File.separator + "versionWeb.json");
                versionJSONFile = IOUtils.toString(inputStream);
                inputStream.close();
                JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(versionJSONFile);

                File json = new File(workDir.toString() + File.separator + "versionWeb.json");
                json.delete();
                webPackVersion = Long.valueOf(jsonObject.getString("version").toString().replace("\"", "").replace("\\", ""));
                packVersion = webPackVersion;


            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                modpackNeedsUpdate = true;
            } catch (IOException e) {
                e.printStackTrace();
                modpackNeedsUpdate = true;
            } catch (Exception e) {
                e.printStackTrace();
                modpackNeedsUpdate = true;
            }
        }
        return modpackNeedsUpdate;
    }

    private static void doUpdate() {

        println("********************");
        println("********************");
        println("********************");
        println("Mod Updater Starting");
        println("********************");
        println("********************");
        println("********************");

        File installDir = new File(Configuration.getString("InstallDir"));
        // Installer functions as it normally does, except it gets called via this method and not the loader

        // This writes path.txt so the code in the installer can find the right directory
        try {
            // Might need to be changed to OS_Util.getWorkingDirectory() depending on what dir it works in.
            FileUtils.writeStringToFile( new File((new File(".").getCanonicalPath()) + File.separator + "path.txt"), installDir.getCanonicalPath() + File.separator);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Download installer as library

        File Installfile = null;
        try {
            Installfile = new File(installDir.getCanonicalPath() + File.separator + "htb3-installer.jar");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!Installfile.exists()) {
            try {
                URL uRL;
                uRL = new URL(Configuration.getTxtRecord("launcher.petercashel.net"));
                org.apache.commons.io.FileUtils.copyURLToFile(uRL, Installfile);
            } catch (IOException e) {
                // Auto-generated catch block
                e.printStackTrace();
                System.err.println("Error Downloading " + "htb3-installer.jar");
            }
        } else {
            Installfile.delete();
            try {
                URL uRL;
                uRL = new URL(Configuration.getTxtRecord("launcher.petercashel.net"));
                org.apache.commons.io.FileUtils.copyURLToFile(uRL, Installfile);
            } catch (IOException e) {
                // Auto-generated catch block
                e.printStackTrace();
                System.err.println("Error Downloading " + "htb3-installer.jar");
            }
        }

        boolean added = false;
        if (Installfile.exists()) {
            //Prepare Installer
            try {
                launcher.classPathRunner(Installfile);
                added = true;
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        if (added) {
            // Call HTB-Installer via its loader
            String[] arg = new String[0];
            try {
                Class<?> clazz = Class.forName("net.petercashel.launch.Loader");
                Method m = clazz.getMethod("main", String[].class);
                m.invoke(null, (Object)arg);
            } catch (ClassNotFoundException e) {
                // Auto-generated catch block
                e.printStackTrace();
                System.out.println("NO UPDATER");
                System.out.println("Cannot Continue");
                System.exit(1);
            } catch (NoSuchMethodException e) {
                // Auto-generated catch block
                e.printStackTrace();
                System.out.println("Cannot Continue");
                System.exit(1);
            } catch (SecurityException e) {
                //  Auto-generated catch block
                e.printStackTrace();
                System.out.println("Cannot Continue");
                System.exit(1);
            } catch (IllegalAccessException e) {
                //  Auto-generated catch block
                e.printStackTrace();
                System.out.println("Cannot Continue");
                System.exit(1);
            } catch (IllegalArgumentException e) {
                //  Auto-generated catch block
                e.printStackTrace();
                System.out.println("Cannot Continue");
                System.exit(1);
            } catch (InvocationTargetException e) {
                //  Auto-generated catch block
                e.printStackTrace();
                System.out.println("Cannot Continue");
                System.exit(1);
            }
        }

        //TODO: write packVersion to JSON as version
        JsonObject jobj = new JsonObject();
        jobj.addProperty("version", String.valueOf(packVersion));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString;
        jsonString = gson.toJson(jobj);

        FileOutputStream fop = null;
        File file;
        String content = jsonString;

        try {

            file = new File(installDir, "version.json");
            fop = new FileOutputStream(file);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // get the content in bytes
            byte[] contentInBytes = content.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        println("********************");
        println("********************");
        println("********************");
        println("Mod Updater Complete");
        println("********************");
        println("********************");
        println("********************");

        doneUpdate = true;

    }

    private static void classPathRunner(File installfile) throws IOException {
        ClassPathHacker.addFile(installfile);
    }

    private static void launchBootstrap() {
        if (!new File(OS_Util.getWorkingDirectory().toString() + File.separator + "Minecraft.jar").exists()) {
            Util.downloadFile("https://s3.amazonaws.com/Minecraft.Download/launcher/Minecraft.jar", OS_Util.getWorkingDirectory().toString() + File.separator, "Minecraft.jar");
        }

        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "Minecraft.Jar");
            Map<String, String> env = pb.environment();
            pb.directory(OS_Util.getWorkingDirectory());
            Process p = pb.start();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.exit(0);

    }
}
