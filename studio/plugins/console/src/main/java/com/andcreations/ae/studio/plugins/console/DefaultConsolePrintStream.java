package com.andcreations.ae.studio.plugins.console;

import com.andcreations.io.OutputStreamLineReader;

/**
 * @author Mikolaj Gucki
 */
public class DefaultConsolePrintStream extends OutputStreamLineReader {
    /** */
    @Override
    public void lineRead(String line) {
        DefaultConsole.get().println(line);
    }
}
