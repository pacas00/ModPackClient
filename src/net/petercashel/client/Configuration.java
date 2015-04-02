package net.petercashel.client;


import java.io.*;
import java.net.URL;
import java.util.*;


public class Configuration {
    public static SortedProperties prop;
    private static File cfgDir;

    public static String getTxtRecord(String hostName) {
	    // Get the first TXT record

	    java.util.Hashtable<String, String> env = new java.util.Hashtable<String, String>();	
	    env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");

	    try {
	        javax.naming.directory.DirContext dirContext 
	            = new javax.naming.directory.InitialDirContext(env);	
	        javax.naming.directory.Attributes attrs 
	            = dirContext.getAttributes(hostName, new String[] { "TXT" });
	        javax.naming.directory.Attribute attr 
	            = attrs.get("TXT");

	        String txtRecord = "";

	        if(attr != null) {
	            txtRecord = attr.get().toString();
	        }

	        return txtRecord;

	    } catch (javax.naming.NamingException e) {

	        e.printStackTrace();
	        return "";
	    }
	}
    
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
            // TODO: Config Here
            if (!prop.containsKey("ServerVersionJSON")) {
                prop.setProperty("ServerVersionJSON", "http://gitlab.petercashel.net:8888/pacas00/launcherconfig/raw/master/version.json");
            }
            if (!prop.containsKey("InstallDir")) {
                prop.setProperty("InstallDir", (new File(".")).getCanonicalPath() + File.separator);
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


    public static Map<Object, Object> getMap() {

        try {
            prop.load(new FileInputStream(cfgDir.getPath() + File.separator + "config.properties"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Map<Object, Object> propertiesMap = new HashMap<Object, Object>();
        prop.putAll(propertiesMap);
        return propertiesMap;

    }

    public static Map<String, String> getStringMap() {

        try {
            prop.load(new FileInputStream(cfgDir.getPath() + File.separator + "config.properties"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Map<String, String> propertiesMap = new HashMap<String, String>();
        prop.putAll(propertiesMap);
        return propertiesMap;

    }

}

class SortedProperties extends Properties {
    public Enumeration keys() {
        Enumeration keysEnum = super.keys();
        Vector<String> keyList = new Vector<String>();
        while (keysEnum.hasMoreElements()) {
            keyList.add((String) keysEnum.nextElement());
        }
        Collections.sort(keyList);
        return keyList.elements();
    }

}