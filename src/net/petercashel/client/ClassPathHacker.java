package net.petercashel.client;

import java.beans.IntrospectionException;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Created by Peter on 25/12/2014.
 */
public class ClassPathHacker {

    private static final Class[] parameters = new Class[]{URL.class};

    public static void addFile(String s) throws IOException {
        File f = new File(s);
        addFile(f);
    }//end method

    public static void addFile(File f) throws IOException {
        System.out.println(f.getAbsolutePath());
        addURLToSystemClassLoader(f.toURI().toURL());
        try {
            addURLToJFXClassLoader(f.toURI().toURL());
        } catch (IOException e) {
            //Not expecting this to work in dev. therefore catch it.
            e.printStackTrace();
        }
    }//end method


    public static void addURLToSystemClassLoader(URL url) throws IOException {
        URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<URLClassLoader> classLoaderClass = URLClassLoader.class;

        try {
            Method method = classLoaderClass.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true);
            method.invoke(systemClassLoader, new Object[]{url});
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }
    }

    public static void addURLToJFXClassLoader(URL url) throws IOException {
        URLClassLoader systemClassLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        Class<URLClassLoader> classLoaderClass = URLClassLoader.class;

        try {
            Method method = classLoaderClass.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true);
            method.invoke(systemClassLoader, new Object[]{url});
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to JFX classloader");
        }
    }
}
