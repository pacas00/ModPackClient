package net.petercashel.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;


import org.apache.commons.io.IOUtils;
import org.lightcouch.CouchDbClient;
import org.lightcouch.Document;

public class Util {

	public static void downloadFile(String url, String dir, String filename) {
		try {
			URL URL;
			URL = new URL(url);
			File File = new File(dir + filename);
			org.apache.commons.io.FileUtils.copyURLToFile(URL, File);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error Downloading " + filename);
		}
	}

	public static void downloadAudioFile(String url, String dir, String subdir, String filename) {
		try {
			URL URL;
			URL = new URL(url);
			File path = new File(dir + File.separator + subdir);

			if (!path.exists()) {
				path.mkdirs();
			}
			File file = new File(path + File.separator + filename);
			org.apache.commons.io.FileUtils.copyURLToFile(URL, file);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("MalformedURLException While Downloading " + filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error Downloading " + filename);
		}

	}
	
	@SuppressWarnings("rawtypes")
	static public void unZip(String zipFile, String outputFolder) {
		try {
			launcher.println(zipFile);
			int BUFFER = 2048;
			File file = new File(zipFile);

			ZipFile zip;
			zip = new ZipFile(file);
			// String newPath = zipFile.substring(0, zipFile.length() - 4);
			String newPath = outputFolder;

			new File(newPath).mkdir();
			Enumeration zipFileEntries = zip.entries();

			// Process each entry
			while (zipFileEntries.hasMoreElements()) {
				// grab a zip file entry
				ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
				String currentEntry = entry.getName();
				File destFile = new File(newPath, currentEntry);
				// destFile = new File(newPath, destFile.getName());
				File destinationParent = destFile.getParentFile();

				// create the parent directory structure if needed
				destinationParent.mkdirs();

				if (!entry.isDirectory()) {
					BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
					int currentByte;
					// establish buffer for writing file
					byte data[] = new byte[BUFFER];

					// write the current file to disk
					FileOutputStream fos = new FileOutputStream(destFile);
					BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);

					// read and write until last byte is encountered
					while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
						dest.write(data, 0, currentByte);
					}
					dest.flush();
					dest.close();
					is.close();
				}

				if (currentEntry.endsWith(".zip")) {
					// found a zip file, try to open
					unZip(destFile.getAbsolutePath(), outputFolder);
				}
			}
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("ZipException While Unzipping " + zipFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("IOError Unzipping " + zipFile);
		}

	}

	@SuppressWarnings("unused")
	public static void installForge(File profileDir) {
		if (!profileDir.exists()) {
			profileDir.mkdirs();
		}

		CouchDbClient dbClient = new CouchDbClient(packListContoller.packDB.toString().replace("\"", "").replace("\\", ""), false, 
				Configuration.getString("apiServerProtocol").toLowerCase(), Configuration.getString("apiServer"),
				Integer.parseInt(Configuration.getString("apiServerPort")), Configuration.getString("apiUser"), Configuration.getString("apiPass"));

		org.lightcouch.Document doc = dbClient.find(Document.class, "ForgeInstaller");

		// get attachment

		try {
			InputStream in = dbClient.find("ForgeInstaller/ForgeInstaller.jar");
			OutputStream out = new FileOutputStream(profileDir.toString() + File.separator + "ForgeInstaller.jar");
			IOUtils.copy(in,out);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dbClient.shutdown();

		try {
			ProcessBuilder pb = new ProcessBuilder("java", "-jar", "ForgeInstaller.Jar");
			Map<String, String> env = pb.environment();
			pb.directory(profileDir);
			Process p = pb.start();
			try {
				p.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

