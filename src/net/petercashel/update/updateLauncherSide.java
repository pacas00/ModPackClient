package net.petercashel.update;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;

import net.petercashel.client.Configuration;
import net.petercashel.client.OS_Util;

public class updateLauncherSide {

	public updateLauncherSide() {
		// TODO Auto-generated constructor stub
	}


	public static boolean run() throws IOException {
		boolean update = false;
		//Version of this app
		int version = Configuration.VersionOfLauncher;
		int updVersion = Integer.parseInt(updateCore.getTxtRecord("launcherversion.petercashel.net"));
		if (updVersion > version) {
			update = true;
			String url = updateCore.getTxtRecord("launcherjar.petercashel.net");
			if (!(url.contains("http"))) url = "http://" + url;
			URL website = null;
			try {
				website = new URL(url);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			ReadableByteChannel rbc = null;
			try {
				rbc = Channels.newChannel(website.openStream());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return false;
			}
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("updateLauncher.jar");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			try {
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					fos.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return false;
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// At this point, there SHOULD be a file to trigger the update. So lets check that before continuing.
			File f = new File("updateLauncher.jar");
			if (!f.exists()) return false;
			// Right, Go.
			System.out.println(f.getCanonicalPath());
			System.out.println(OS_Util.getPlatform().ordinal());
			switch (OS_Util.getPlatform().ordinal()) {
			case 4: //Unknown
			case 3: //Mac
			case 1: //Solaris
			case 0: //Linux
			default: { 
				ProcessBuilder linpb = new ProcessBuilder("java -cp updateLauncher.jar net.petercashel.update.updateCore");
				linpb = linpb.inheritIO();
				Map<String, String> linenv = linpb.environment();
				linpb.directory(new File(".").getCanonicalFile());
				Process linp = linpb.start();
				System.exit(0);
			}
			case 2: { //Windows
				File dir = new File(new File(".").getCanonicalFile().getPath());
				//"cmd", "/c", "start /WAIT /D " + "\"" + getJarDir() + "\"" + " 
				ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "start /WAIT /D " + "\"" + dir + "\"" + " java -cp updateLauncher.jar  net.petercashel.update.updateCore");
				pb = pb.inheritIO();
				Map<String, String> env = pb.environment();
				pb.directory(dir);
				Process p = pb.start();
				System.exit(0);
			}
			}
		}
		return update;
	}

}
