package com.andcreations.android.adb;

import java.util.ArrayList;
import java.util.List;

import com.android.ddmlib.MultiLineReceiver;

/**
 * @author Mikolaj Gucki
 */
class PSOutputReceiver extends MultiLineReceiver {
    /** */
    private static final int PID_INDEX = 1;
    
    /** */
    private static final int PPID_INDEX = 2;
    
    /** */
    private static final int NAME_INDEX = 8;
    
    /** */
    private String[] header;
    
    /** */
    private List<ADBProcess> processes = new ArrayList<>();
    
    /** */
    @Override
    public boolean isCancelled() {
        return false;
    }
    
    /** */
    private void processLine(String line) {
        if (line.trim().length() == 0) {
            return;
        }
        
    // split
        String[] values = line.split(" +");
        if (header == null) {
            header = values;
            return;
        }
        
    // process
        ADBProcess process = new ADBProcess();
        processes.add(process);
        
    // process values
        process.setPid(values[PID_INDEX]);
        process.setPPid(values[PPID_INDEX]);
        process.setName(values[NAME_INDEX]);
    }
    
    /** */
    ADBProcess[] getProcesses() {
        return processes.toArray(new ADBProcess[]{});
    }
    
    /** */
    @Override
    public void processNewLines(String[] lines) {
        for (String line:lines) {
            processLine(line);
        }
    }
}