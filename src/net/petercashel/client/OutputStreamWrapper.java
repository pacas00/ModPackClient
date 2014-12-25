package net.petercashel.client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by Peter on 25/12/2014.
 */
public class OutputStreamWrapper extends OutputStream {

   public OutputStreamWrapper ()
    {
        // TODO Create Log File

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
                launcher.print(text);

                // TODO Write to Log File
            }
        });
    }

    @Override
    public void write(int b) throws IOException
    {
        write (new byte [] {(byte)b}, 0, 1);
    }
}
