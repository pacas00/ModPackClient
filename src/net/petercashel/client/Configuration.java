package net.petercashel.client;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;


public class Configuration {
	public static SortedProperties prop;
	private static File cfgDir;

	public static void initProp() {
		prop = new SortedProperties();
		OS_Util.getWorkingDirectory().mkdirs();
		
		cfgDir = new File(OS_Util.getWorkingDirectory().getAbsolutePath() + File.separator + "config");
		cfgDir.mkdirs();
		
		try {
			try {
				prop.load(new FileInputStream(cfgDir.getPath() + File.separator + "config.properties"));
			} catch (IOException ex) {
				ex.printStackTrace();
				try {
					URL url = new URL("jar:file:/config.properties");
					InputStream is = url.openStream();
					prop.load(is);
				} catch (Exception ex2) {
					ex2.printStackTrace();
				}

			}
			if (!prop.containsKey("apiServer")) {
				prop.setProperty("apiServer", "");
			}
			if (!prop.containsKey("apiServerPort")) {
				prop.setProperty("apiServerPort", "5984");
			}
			if (!prop.containsKey("apiServerProtocol")) {
				prop.setProperty("apiServerProtocol", "http");
			}
			if (!prop.containsKey("authUser")) {
				prop.setProperty("authUser", "");
			}
			if (!prop.containsKey("authCode")) {
				prop.setProperty("authCode", "");
			}
			if (!prop.containsKey("packName")) {
				prop.setProperty("packName", "");
			}
			prop.store(new FileOutputStream(cfgDir.getPath() + File.separator + "config.properties"), null);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static String getString(String key) {
		try {
			prop.load(new FileInputStream(cfgDir.getPath() + File.separator + "config.properties"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return prop.getProperty(key);
	}

	public static boolean setString(String key, String value) {
		try {
			prop.load(new FileInputStream(cfgDir.getPath() + File.separator + "config.properties"));
			prop.setProperty(key, value);
			prop.store(new FileOutputStream(cfgDir.getPath() + File.separator + "config.properties"), null);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return true;
	}
	
	
	public static Map<Object,Object> getMap() {
		
		try {
			prop.load(new FileInputStream(cfgDir.getPath() + File.separator + "config.properties"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<Object,Object> propertiesMap = new HashMap<Object, Object>();
		prop.putAll(propertiesMap);
		return propertiesMap;
		
	}
	
public static Map<String,String> getStringMap() {
		
		try {
			prop.load(new FileInputStream(cfgDir.getPath() + File.separator + "config.properties"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String,String> propertiesMap = new HashMap<String, String>();
		prop.putAll(propertiesMap);
		return propertiesMap;
		
	}
	
}

class SortedProperties extends Properties {
	public Enumeration keys() {
		Enumeration keysEnum = super.keys();
		Vector<String> keyList = new Vector<String>();
		while(keysEnum.hasMoreElements()){
			keyList.add((String)keysEnum.nextElement());
		}
		Collections.sort(keyList);
		return keyList.elements();
	}

}