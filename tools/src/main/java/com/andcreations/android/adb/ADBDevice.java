package com.andcreations.android.adb;

import java.util.ArrayList;
import java.util.List;

import com.android.ddmlib.Client;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.logcat.LogCatListener;
import com.android.ddmlib.logcat.LogCatMessage;
import com.android.ddmlib.logcat.LogCatReceiverTask;

/**
 * @author Mikolaj Gucki
 */
public class ADBDevice {
    /** */
    private IDevice device;
    
    /** */
    private LogCatReceiverTask logCatTask;
    
    /** */
    ADBDevice(IDevice device) {
        this.device = device;
    }
    
    /** */
    IDevice getDevice() {
        return device;
    }
    
    /** */
    @Override
    public String toString() {
        return device.toString();
    }
 
    /** */
    public String getManufacturer() {
        return device.getProperty(IDevice.PROP_DEVICE_MANUFACTURER);
    }
    
    /** */
    public String getModel() {
        return device.getProperty(IDevice.PROP_DEVICE_MODEL);
    }
    
    /** */
    public String getVersion() {
        return device.getProperty(IDevice.PROP_BUILD_VERSION);
    }
    
    /** */
    public String getAPILevel() {
        return device.getProperty(IDevice.PROP_BUILD_API_LEVEL);
    }
    
    /** */
    public String getCodename() {
        return device.getProperty(IDevice.PROP_BUILD_CODENAME);
    }
    
    /** */
    public String getSerialNumber() {
        return device.getSerialNumber();
    }
    
    /** */
    public String getClientName(int pid) {
        return device.getClientName(pid);
    }
    
    /** */
    public ADBClient[] getClients() {
        Client[] clients = device.getClients();
        ADBClient[] adbClients = new ADBClient[clients.length];
        
        for (int index = 0; index < clients.length; index++) {
            adbClients[index] = new ADBClient(clients[index]);
        }
        
        return adbClients;
    }
    
    /** */
    public void startLogCat(final ADBLogCatListener listener) {
        if (logCatTask != null) {
            throw new IllegalStateException("LogCat already running");
        }
        logCatTask = new LogCatReceiverTask(device);
        logCatTask.addLogCatListener(new LogCatListener() {
            @Override   
            public void log(List<LogCatMessage> messages) {
                List<ADBLogCatMessage> adbLogCatMessages = new ArrayList<>();
                for (LogCatMessage message:messages) {
                    adbLogCatMessages.add(new ADBLogCatMessage(message));
                }
                listener.log(adbLogCatMessages);
            }
        });
        logCatTask.run();
    }
    
    /** */
    public void stopLogCat() {
        if (logCatTask == null) {
            throw new IllegalStateException("LogCat not running");
        }
        logCatTask.stop();
        logCatTask = null;        
    }
    
    /** */
    @Override
    public boolean equals(Object obj) {
        if ((obj instanceof ADBDevice) == false) {
            return false;
        }
        ADBDevice device = (ADBDevice)obj;
        return getSerialNumber().equals(device.getSerialNumber());
    }
}