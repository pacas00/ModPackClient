package net.petercashel.client;

import java.io.File;
import java.io.IOException;
import java.net.URL;

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

}

