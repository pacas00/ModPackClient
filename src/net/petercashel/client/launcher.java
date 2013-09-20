package net.petercashel.client;
import java.awt.EventQueue;

import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import javax.swing.JTabbedPane;
import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JButton;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import javafx.embed.swing.JFXPanel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.SwingConstants;

import java.awt.BorderLayout;

import javax.swing.JSeparator;

import java.awt.GridLayout;

import javax.swing.JLabel;

import java.awt.FlowLayout;

import com.google.gson.JsonObject;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.BoxLayout;
import javax.swing.UIManager;

import java.awt.SystemColor;

import javax.swing.JFormattedTextField;

import java.awt.Panel;
import java.awt.Label;

import javax.swing.JTextField;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.lightcouch.CouchDbClient;
import org.lightcouch.Document;

import java.awt.event.WindowFocusListener;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class launcher {

	private static JFrame frame;
	private static JScrollPane Log;
	private static final Font MONOSPACED = new Font("Monospaced", 0, 13);
	private static WebView webView;
	final static JFXPanel fxPanel = new JFXPanel();
	Thread UpdateThread;
	static JTextPane editorPane;
	public static JTextField statusField;
	public static boolean settingsOpen = false;
	private static JDialog settingsDialogHandle;
	public static int packVersion;
	public static String forgeVersion;
	public static boolean doneUpdate = false;


	//Development bools. Release with all true
	private static final boolean enableJFX = true;
	private static boolean enableModUpdate = true;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		//Configuration Prep
		Configuration.initProp();

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

		frame = new JFrame("PacLauncher");
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
		editorPane.setBackground( new Color(70, 130, 180, 255) );
		editorPane.setForeground( new Color(0, 0, 0, 255) );
		editorPane.setEditable(false);
		editorPane.setFont(MONOSPACED);

		DefaultCaret caret = (DefaultCaret)editorPane.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		Log = new JScrollPane();
		tabbedPane.addTab("Update Status", null, Log, null);

		Log.setViewportView(editorPane);

		JPanel panel = new JPanel();
		panel.setBorder(null);
		frame.getContentPane().add(panel, "flowx,cell 0 1,grow");
		panel.setLayout(new BorderLayout(0, 0));

		JButton btnSettings = new JButton("Settings");
		panel.add(btnSettings, BorderLayout.WEST);

		JButton btnLaunch = new JButton("Launch");
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
					if (enableModUpdate) {

						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								packListContoller.getpackListLaunch();
								packListContoller.getPackData();
								updateCore();

								if (doneUpdate) {
									launchBootstrap(new File(OS_Util.getWorkingDirectory().getAbsolutePath() + File.separator + "." + Configuration.getString("packName")));
								}
							}
						});
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

		println("Hi There");
		println("No!");
		println(" ");
		println("Scroll Pane Online");





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
				@Override public void changed(ObservableValue<? extends String> observableValue, String oldLoc, String newLoc) {
					// check if the newLoc corresponds to a file you want to be downloadable
					// and if so trigger some code and dialogs to handle the download.

					//engine.spotscenered.info

					if (!newLoc.contains("adf.ly/go.php") && !newLoc.contains("/http://") && !newLoc.contains("/https://") && !newLoc.contains("www.mediafire.com/download/")&& !newLoc.contains("engine.spotscenered.info")) {
						System.out.println(newLoc);

						String downloadableExtension = null;  // todo I wonder how to find out from WebView which documents it could not process so that I could trigger a save as for them?
						String[] downloadableExtensions = { ".doc", ".docx", ".xls", ".xlsx", ".zip", ".tgz", ".tar", ".gz", ".tar.gz", ".lzma", ".jar", ".rar", ".7z", ".pdf" };
						for (String ext: downloadableExtensions) {
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
									BufferedInputStream  is = null;
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
										try { if (is != null) is.close(); } catch (IOException e) { /** no action required. */ }
										try { if (os != null) os.close(); } catch (IOException e) { /** no action required. */ }
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

		editorPane.setText(s  + string + "\n");
		editorPane.setFont(MONOSPACED);
	}

	// Functions below here are for mod update threads

	private static void updateCore() {
		boolean publicPack = packListContoller.packPublic;
		boolean validUser = false;
		boolean newUpdate = false;

		File workDir = new File(OS_Util.getWorkingDirectory().getAbsolutePath() + File.separator + "." + Configuration.getString("packName"));
		workDir.mkdirs();

		CouchDbClient dbClient = new CouchDbClient(packListContoller.packDB.toString().replace("\"", "").replace("\\", ""), false, 
				Configuration.getString("apiServerProtocol").toLowerCase(), Configuration.getString("apiServer"),
				Integer.parseInt(Configuration.getString("apiServerPort")), null, null);

		if (!publicPack) {

			com.google.gson.JsonObject gsonObj = dbClient.find(JsonObject.class, "userAuth");
			String response = gsonObj.toString();
			System.out.println(response);
			dbClient.shutdown();
			JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON( response ); 
			System.out.println(jsonObject.get("pacas00"));
			if(jsonObject.containsKey(Configuration.getString("authUser").toLowerCase())) 
			{
				if (Configuration.getString("authCode").toLowerCase().contentEquals(jsonObject.get(Configuration.getString("authUser")).toString().toLowerCase())) {
					validUser = true;
				}
			} else {
				validUser = false;
			}

		}
		// From here,
		// run the updater always for publicPack = true
		// run the updater for publicPack = false only if validUser = true

		if (!publicPack && validUser) {
			//Run the UpdateCore Thread

			println("Checking for Mod Updates");
			statusBarHandler.setStatusBarText("Checking for Mod Updates");
			newUpdate = doUpdateCheck(workDir);

			if (newUpdate) {
				println("Installing Mod Updates");
				statusBarHandler.setStatusBarText("Installing Mod Updates");
				//Call update Thread
				doUpdate(workDir);
			} else {
				println("No Updates");
				statusBarHandler.setStatusBarText("No Updates");
				doneUpdate = true;
			}	

		} else if (publicPack) {
			//Run the UpdateCore Thread

			println("Checking for Mod Updates");
			statusBarHandler.setStatusBarText("Checking for Mod Updates");
			newUpdate = doUpdateCheck(workDir);

			if (newUpdate) {
				println("Installing Mod Updates");
				statusBarHandler.setStatusBarText("Installing Mod Updates");
				//Call update Thread
				doUpdate(workDir);
			} else {
				println("No Updates");
				statusBarHandler.setStatusBarText("No Updates");
				doneUpdate = true;
			}			
		} else {
			// Check what happened and error appropriately

			doneUpdate = true;
		}

	}

	private static boolean doUpdateCheck(File workDir) {

		println("Checking for modpack updates");
		Boolean modpackNeedsUpdate = false;
		// Download basemods + correct mc jar
		if (new File(workDir.toString() + File.separator + "version.json").exists()) {

			@SuppressWarnings("unused")
			String versionJSONFile;
			FileInputStream inputStream;
			try {
				inputStream = new FileInputStream(workDir.toString() + File.separator  + "version.json");
				versionJSONFile = IOUtils.toString(inputStream);
				inputStream.close();
				JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON( versionJSONFile );

				packVersion = Integer.parseInt(jsonObject.getString("modpack_version").toString().replace("\"", "").replace("\\", ""));
				forgeVersion = jsonObject.getString("forge_version").toString().replace("\"", "").replace("\\", "");


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


			CouchDbClient dbClient = new CouchDbClient(packListContoller.packDB.toString().replace("\"", "").replace("\\", ""), false, 
					Configuration.getString("apiServerProtocol").toLowerCase(), Configuration.getString("apiServer"),
					Integer.parseInt(Configuration.getString("apiServerPort")), null, null);

			com.google.gson.JsonObject gsonObj = dbClient.find(JsonObject.class, "modpackVersion");

			String response = gsonObj.toString();
			dbClient.shutdown();

			if (Integer.parseInt((gsonObj.get("modpack_version")).toString().replace("\"", "").replace("\\", "")) > packVersion) {
				modpackNeedsUpdate = true;
				packVersion = Integer.parseInt((gsonObj.get("modpack_version")).toString().replace("\"", "").replace("\\", ""));
			}
			try {
				if (gsonObj.get("forge_version").toString().replace("\"", "").replace("\\", "").contains(forgeVersion)) {
					// YAY!
				} else {
					// Awe...
					modpackNeedsUpdate = true;
					forgeVersion = gsonObj.get("forge_version").toString().replace("\"", "").replace("\\", "");
				}
			} catch (NullPointerException e) {
				modpackNeedsUpdate = true;
			}

		} else {
			modpackNeedsUpdate = true;
			
			System.out.println((new File(workDir.toString() + File.separator + "version.json").toString()));
			CouchDbClient dbClient = new CouchDbClient(packListContoller.packDB.toString().replace("\"", "").replace("\\", ""), false, 
					Configuration.getString("apiServerProtocol").toLowerCase(), Configuration.getString("apiServer"),
					Integer.parseInt(Configuration.getString("apiServerPort")), null, null);

			com.google.gson.JsonObject gsonObj = dbClient.find(JsonObject.class, "modpackVersion");

			String response = gsonObj.toString();
			dbClient.shutdown();
			packVersion = Integer.parseInt((gsonObj.get("modpack_version")).toString().replace("\"", "").replace("\\", ""));
			forgeVersion = gsonObj.get("forge_version").toString().replace("\"", "").replace("\\", "");
		}
		return modpackNeedsUpdate;
	}

	private static void doUpdate(File workDir) {

		String configDir = workDir.toString() + File.separator + "config" + File.separator;
		String modDir = workDir.toString() + File.separator + "mods" + File.separator;
		String modDirOld = workDir.toString() + File.separator + "mods_Old" + File.separator;
		String assetsDir = workDir.toString() + File.separator + "assets" + File.separator;
		String tmpDir = workDir.toString() + File.separator + "temp" + File.separator;
		String fileInProgress = "";

		println("********************");
		println("********************");
		println("********************");
		println("Mod Updater Starting");
		println("********************");
		println("********************");
		println("********************");

		// Make sure Directories exist
		File dir = new File(configDir);

		if (!dir.exists()) {
			dir.mkdirs();
		}
		//Added old mods folder

		dir = new File(modDirOld);
		FileUtils.deleteQuietly(dir);

		if (!dir.exists()) {
			dir.mkdirs();
		}

		try {
			FileUtils.copyDirectory(new File(modDir), new File(modDirOld));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}		

		dir = new File(modDir);
		FileUtils.deleteQuietly(dir);

		if (!dir.exists()) {
			dir.mkdirs();
		}
		dir = new File(assetsDir);

		if (!dir.exists()) {
			dir.mkdirs();
		}
		dir = new File(tmpDir);

		if (!dir.exists()) {
			dir.mkdirs();
		}

		println("Installing Forge");

		println("Please select 'Install Client' when the 'Minecraft Forge Installer' opens");
		println("Please DO NOT change the path.");

		//Also Downloads!
		Util.installForge(workDir);

		println("Downloading Mods");

		CouchDbClient dbClient = new CouchDbClient(packListContoller.packDB.toString().replace("\"", "").replace("\\", ""), false, 
				Configuration.getString("apiServerProtocol").toLowerCase(), Configuration.getString("apiServer"),
				Integer.parseInt(Configuration.getString("apiServerPort")), null, null);

		com.google.gson.JsonObject gsonObj = dbClient.find(JsonObject.class, "modList");

		String response = gsonObj.toString();
		System.out.println(response);
		dbClient.shutdown();

		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON( response ); 

		JSONObject CommonjsonObject = ( jsonObject.getJSONObject("modList_Common") ); 
		Map<String, String> modListCommon = (Map<String, String>) JSONObject.toBean(CommonjsonObject, Map.class);

		JSONObject ClientjsonObject = ( jsonObject.getJSONObject("modList_Client") ); 
		Map<String, String> modListClient = (Map<String, String>) JSONObject.toBean(ClientjsonObject, Map.class);

		for (Map.Entry<String, String> entry : modListCommon.entrySet()) {
			fileInProgress = entry.getValue();
			File oldFile = new File(modDirOld + entry.getValue());
			File newFile = new File(modDir + entry.getValue());
			if (!oldFile.exists()) {
				println("Downloading " + entry.getValue());
				Util.downloadFile(entry.getKey(), modDir, entry.getValue());
			} else {
				try {
					println("Copying " + entry.getValue());
					FileUtils.copyFile(oldFile, newFile);
				} catch (IOException e) {
					e.printStackTrace();
					System.err.println("Error During Copy Of " + fileInProgress);
					println("Error During Copy Of " + fileInProgress);
					System.exit(1);
				}
			}

		}

		for (Map.Entry<String, String> entry : modListClient.entrySet()) {
			fileInProgress = entry.getValue();
			File oldFile = new File(modDirOld + entry.getValue());
			File newFile = new File(modDir + entry.getValue());
			if (!oldFile.exists()) {
				println("Downloading " + entry.getValue());
				Util.downloadFile(entry.getKey(), modDir, entry.getValue());
			} else {
				try {
					println("Copying " + entry.getValue());
					FileUtils.copyFile(oldFile, newFile);
				} catch (IOException e) {
					e.printStackTrace();
					System.err.println("Error During Copy Of " + fileInProgress);
					println("Error During Copy Of " + fileInProgress);
					System.exit(1);
				}
			}
		}

		// Download config
		println("Downloading mod configs");
		fileInProgress = "config.zip";

		//PATCH

		dbClient = new CouchDbClient(packListContoller.packDB.toString().replace("\"", "").replace("\\", ""), false, 
				Configuration.getString("apiServerProtocol").toLowerCase(), Configuration.getString("apiServer"),
				Integer.parseInt(Configuration.getString("apiServerPort")), null, null);

		org.lightcouch.Document doc = dbClient.find(Document.class, "ForgeInstaller");

		// get attachment

		try {
			InputStream in = dbClient.find("Configzip/Config.zip");
			OutputStream out = new FileOutputStream(tmpDir + File.separator + "Config.zip");
			IOUtils.copy(in,out);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dbClient.shutdown();


		Util.unZip(tmpDir + "config.zip", configDir);


		//		println("Downloading and Installing New Audio");
		//		println("Downloading and Installing New Audio");
		//		try {
		//			fileInProgress = "audio.csv";
		//			InputStream CSV_audio = new URL(Configuration.getString("baseUrl") + "/csv/audio.csv").openStream();
		//			CSVReader reader = new CSVReader(new InputStreamReader(CSV_audio));
		//			String[] nextLine;
		//			while ((nextLine = reader.readNext()) != null) {
		//				if (!nextLine[0].startsWith("#")) {
		//					println("Downloading " + nextLine[2]);
		//					println("Downloading " + nextLine[2]);
		//					fileInProgress = nextLine[2];
		//					println(assetsDir + nextLine[1] + File.separator + nextLine[2]);
		//					println(assetsDir + nextLine[1] + File.separator + nextLine[2]);
		//					File currentaudio = new File(assetsDir + File.separator + nextLine[1] + File.separator + nextLine[2]);
		//					if (!currentaudio.exists()) {
		//						Util.downloadAudioFile(nextLine[0], assetsDir, nextLine[1], nextLine[2]);	
		//					}
		//				}
		//			}
		//			reader.close();
		//			CSV_audio.close();
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//			System.err.println("Error During Download Of " + fileInProgress);
		//			System.exit(1);
		//		}

		//Download server list if its non existant
		File servList = new File(workDir.toString() + File.separator + "servers.dat");
		if (!servList.exists()) {
			//	Util.downloadFile(Configuration.getString("baseUrl") + "/servers.dat", workDir.toString() + File.seperator, "servers.dat");
		}

		Map<String,String> versionJSON = new HashMap<String, String>();
		versionJSON.put("modpack_version", String.valueOf(packVersion));
		versionJSON.put("forge_version", forgeVersion);


		JSONObject versionjsonObject = (JSONObject) JSONSerializer.toJSON( versionJSON ); 
		String versionjsonString = versionjsonObject.toString();
		System.out.println(versionjsonString);
		System.out.println(packVersion);
		System.out.println(forgeVersion);


		try {
			Writer output = null;
			File file = new File(workDir.toString() + File.separator + "version.json");
			output = new BufferedWriter(new FileWriter(file));
			output.write(versionjsonString);
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// feed in your array (or convert your data to an array)

		//Finally remove the old mod dir
		FileUtils.deleteQuietly(new File(modDirOld));

		println("********************");
		println("********************");
		println("********************");
		println("Mod Updater Complete");
		println("********************");
		println("********************");
		println("********************");

		doneUpdate = true;

	}

	private static void launchBootstrap(File workDir) {
		if (!new File(workDir.toString() + File.separator + "Minecraft.jar").exists()) {
			Util.downloadFile("https://s3.amazonaws.com/Minecraft.Download/launcher/Minecraft.jar", workDir.toString() + File.separator, "Minecraft.jar");
		}

		try {
			ProcessBuilder pb = new ProcessBuilder("java", "-jar", "Minecraft.Jar");
			Map<String, String> env = pb.environment();
			pb.directory(workDir);
			Process p = pb.start();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.exit(0);

	}
}
