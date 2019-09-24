package com.andcreations.ae.studio.plugins.simulator;

import java.io.OutputStream;

/**
 * @author Mikolaj Gucki
 */
class ErrorOutputStream extends OutputStream {
    /** */
    private StringBuffer buffer = new StringBuffer();
    
    /** */
    @Override
    public void write(int value) {
        if (value == 10) {
            SimulatorLog.get().engineLog(buffer.toString());
            //DefaultConsole.get().println(buffer.toString());
            buffer.delete(0,buffer.length());
            return;
        }
        buffer.append((char)value);
    }
}