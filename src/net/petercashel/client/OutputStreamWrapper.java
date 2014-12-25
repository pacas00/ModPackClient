package net.petercashel.client;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.logging.Logger;

/**
 * Created by Peter on 25/12/2014.
 */
public class OutputStreamWrapper extends OutputStream {

    private final PrintStream ps;

    public OutputStreamWrapper ()
    {
        // TODO Create Log File
        File file = new File("Log.txt");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ps = new PrintStream(fos);

    }

    @Override
    public void write(byte[] buffer, int offset, int length) throws IOException
    {
        final String text = new String (buffer, offset, length);
        SwingUtilities.invokeLater(new Runnable ()
        {
            @Override
            public void run()
            {
                //Write to consoleTab
                launcher.print(text);
                //Write to Log File
                ps.print(text);
            }
        });
    }

    @Override
    public void write(int b) throws IOException
    {
        write (new byte [] {(byte)b}, 0, 1);
    }
}
