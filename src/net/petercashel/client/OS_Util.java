package net.petercashel.client;

import java.io.File;
import java.io.IOException;

public class OS_Util {

	private static OS getPlatform() {
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("win")) {
			return OS.windows;
		}
		if (osName.contains("mac")) {
			return OS.macos;
		}
		if (osName.contains("solaris")) {
			return OS.solaris;
		}
		if (osName.contains("sunos")) {
			return OS.solaris;
		}
		if (osName.contains("linux")) {
			return OS.linux;
		}
		if (osName.contains("unix")) {
			return OS.linux;
		}
		return OS.unknown;
	}

	private static enum OS {
		linux, solaris, windows, macos, unknown;
	}

	public static String getJarDirPath() {
		String currentDir;
		try {
			currentDir = new File(".").getCanonicalPath();
			return currentDir + File.separator;
		} catch (IOException e) {
			e.printStackTrace();
			currentDir = new File(".").getAbsolutePath();
		}
		return currentDir + File.separator;
	}

	public static File getJarDir() {
		String currentDir;
		try {
			currentDir = new File(".").getCanonicalPath();
			return new File(currentDir + File.separator);
		} catch (IOException e) {
			e.printStackTrace();
			currentDir = new File(".").getAbsolutePath();
		}
		return new File(currentDir + File.separator);
	}

	public static String getWorkingDirectoryPath() {
		return getWorkingDirectory() + File.separator;
	}

	public static File getWorkingDirectory() {
		String userHome = getJarDirPath();
		File workingDirectory;
		String applicationName = File.separator + "PeterCashel.Net";
		switch (getPlatform().ordinal()) {
		case 0:
		case 1:
			applicationName = File.separator + "PeterCashel.Net";
			workingDirectory = new File(userHome, File.separator + applicationName + '/');
			break;
		case 2:
			String currentDir = System.getProperty("user.dir");
			String applicationData = System.getenv("APPDATA");
			if (applicationData != null)
				workingDirectory = new File(applicationData, applicationName + '/');
			else
				workingDirectory = new File(userHome, applicationName + '/');
			break;
		case 3:
			applicationName = File.separator + "PeterCashel.Net";
			workingDirectory = new File(userHome, File.separator + "Library/Application Support/" + applicationName);
			break;
		default:
			workingDirectory = new File(userHome, File.separator + applicationName + '/');
		}
		if ((!workingDirectory.exists()) && (!workingDirectory.mkdirs()))
			throw new RuntimeException("The working directory could not be created: " + workingDirectory);
		return workingDirectory;
	}

}
