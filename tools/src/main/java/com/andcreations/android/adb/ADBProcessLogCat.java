package com.andcreations.android.adb;

import java.util.ArrayList;
import java.util.List;

import com.andcreations.util.Timer;

/**
 * @author Mikolaj Gucki
 */
public class ADBProcessLogCat {
    /** */
    private class ListProcessesTimer extends Timer {
        /** */
        private static final int PERIOD = 1000;
        
        /** */
        private ListProcessesTimer() {
            super(PERIOD);
        }
        
        /** */
        @Override
        protected void tick() {
            listProcesses();
        }
    }
    
    /** */
    private ADBDevice device;
    
    /** */
    private String processName;
    
    /** */
    private ADBLogCatListener listener;
    
    /** */
    private List<String> pids = new ArrayList<>();
    
    /** */
    private List<ADBLogCatMessage> cachedMessages = new ArrayList<>();
    
    /** */
    private ListProcessesTimer listProcessesTimer;
    
    /** */
    private List<ADBProcessLogCatListener> listeners = new ArrayList<>();
    
    /** */
    public ADBProcessLogCat(ADBDevice device,String processName,
        ADBLogCatListener listener) {
    //
        this.device = device;
        this.processName = processName;
        this.listener = listener;
        
        create();
    }
    
    /** */
    private void create() {
    // process timer
        listProcessesTimer = new ListProcessesTimer();
    }

    /** */
    public void addADBProcessLogCatListener(ADBProcessLogCatListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    private void notifyLogCatFailed(Exception exception) {
        synchronized (listeners) {
            for (ADBProcessLogCatListener listener:listeners) {
                listener.logCatFailed(processName,exception);
            }
        }        
    }
    
    /** */
    private void filterMessages(List<ADBLogCatMessage> messages) {
        for (ADBLogCatMessage msg:messages) {
            msg.setProcessMatch(pids.contains(msg.getPid()));
        }
        listener.log(messages);
    }
    
    /** */
    private void listProcesses() {
        ADBProcessReceiverTask task = new ADBProcessReceiverTask(device);
        ADBProcess[] processes = null;
        try {
            processes = task.listProcesses();
        } catch (ADBException exception) {
            notifyLogCatFailed(exception);
            return;
        }
        
        synchronized (pids) {
            boolean hadPids = pids.isEmpty() == false;
            pids.clear();
            
        // nothing to do here if the process name is not set
            if (processName == null) {
            	return;
            }
            
        // get the pids of all the process of the set name
            for (ADBProcess process:processes) {
                if (process.isZombie() == true ||
                    processName.equals(process.getName()) == false) {
                //
                    continue;
                }
                pids.add(process.getPid());
            }
            
        // if process died
            if (hadPids == true && pids.isEmpty() == true) {
            // notify
                synchronized (listeners) {
                    for (ADBProcessLogCatListener listener:listeners) {
                        listener.processDied(processName);
                    }
                }
            }
            
        // if process became alive
            if (hadPids == false && pids.isEmpty() == false) {
                filterMessages(cachedMessages);
            }
            cachedMessages.clear();
        }
    }
    
    /** */
    public void startLogCat() {
    // start process timer
        Thread listProcessesTimerThread = new Thread(listProcessesTimer,
            String.format("LogCatProcesses-%s",device.getSerialNumber()));
        listProcessesTimerThread.start();
        
    // start logcat
        Thread logCatThread = new Thread(new Runnable() {
            /** */
            @Override
            public void run() {
                device.startLogCat(new ADBLogCatListener() {
                    /** */
                    @Override
                    public void log(List<ADBLogCatMessage> messages) {
                        synchronized (pids) {
                            filterMessages(messages);
                            cachedMessages.addAll(messages);
                        }
                    }
                });                
            }
        },String.format("LogCat-%s",device.getSerialNumber()));
        logCatThread.start();
    }
    
    /** */
    public void setProcessName(String processName) {
        synchronized (pids) {
            this.processName = processName;
        }
    }
    
    /** */
    public void stopLogCat() {   
        listProcessesTimer.stop();
        device.stopLogCat();
        
    // notify
        synchronized (listeners) {
            for (ADBProcessLogCatListener listener:listeners) {
                listener.logCatStopped(processName);
            }
        }        
    }
}