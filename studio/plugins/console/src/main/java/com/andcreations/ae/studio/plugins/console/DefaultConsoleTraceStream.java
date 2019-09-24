package com.andcreations.ae.studio.plugins.console;

import com.andcreations.io.OutputStreamLineReader;

/**
 * @author Mikolaj Gucki
 */
public class DefaultConsoleTraceStream extends OutputStreamLineReader {
    /** */
    @Override
    public void lineRead(String line) {
        DefaultConsole.get().traceln(line);
    }
}
