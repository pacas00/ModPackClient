package net.petercashel.update;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Map;

import net.petercashel.client.OS_Util;

public class updateCore {

	public static void main(String[] args) throws IOException {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// At this point, there SHOULD be a file to trigger the update. So lets check that before continuing.
		File f = new File("updateLauncher.jar");
		File f2 = new File("ModPackClient.jar");
		
		while (!f2.canWrite()){
			
		}
		// Right, Go.
		f2.delete();
		copyFile(f,f2);

		switch (OS_Util.getPlatform().ordinal()) {
		case 4: //Unknown
		case 3: //Mac
		case 1: //Solaris
		case 0: //Linux
		default: { 
			ProcessBuilder linpb = new ProcessBuilder("java -jar ModPackClient.jar");
			linpb = linpb.inheritIO();
			Map<String, String> linenv = linpb.environment();
			linpb.directory(new File("."));
			Process linp = linpb.start();
			System.exit(0);
		}
		case 2: { //Windows
			File dir = new File(new File(".").getCanonicalFile().getPath());
			//"cmd", "/c", "start /WAIT /D " + "\"" + getJarDir() + "\"" + " 
			ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "start /D " + "\"" + dir + "\"" + " java -jar ModPackClient.jar");
			pb = pb.inheritIO();
			Map<String, String> env = pb.environment();
			pb.directory(dir);
			Process p = pb.start();
			System.exit(0);
		}
		}




	}




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
	
	public static void copyFile(File sourceFile, File destFile) throws IOException {
	    if(!destFile.exists()) {
	        destFile.createNewFile();
	    }

	    FileChannel source = null;
	    FileChannel destination = null;

	    try {
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();
	        destination.transferFrom(source, 0, source.size());
	    }
	    finally {
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	    }
	}
}
