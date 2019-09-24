package com.andcreations.ae.studio.plugins.adb;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.andcreations.ae.studio.plugins.console.ConsoleComponent;
import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.ae.studio.plugins.ui.common.UIFonts;
import com.andcreations.android.adb.ADBDevice;
import com.andcreations.android.adb.ADBLogCatMessage;
import com.andcreations.android.adb.ADBLogCatMessage.Level;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class LogCatViewComponent extends JPanel { 
    /** */
    private class LogCatDeviceItem {
        /** */
        private String name;
        
        /** */
        private ADBDevice device;
        
        /** */
        private LogCatDeviceItem(ADBDevice device) {
            this.name = device.getSerialNumber();
            this.device = device;
        }
        
        /** */
        private LogCatDeviceItem(String name) {
            this.name = name;
        }
        
        /** */
        @Override
        public String toString() {
            return name;
        }
    }
    
    /** */
    private static final String NON_PROCESS_STYLE = "nonprocess";
    
    /** */
    private static final String LOG_CAT_STYLE = "logcat";
    
    /** */
    private final BundleResources res =
        new BundleResources(LogCatViewComponent.class); 
        
    /** */
    private LogCatViewComponentListener listener;
        
    /** */
    private JComboBox<LogCatDeviceItem> deviceCombo;
    
    /** */
    private JLabel processNameLabel;
    
    /** */
    private ConsoleComponent console;
    
    /** */
    LogCatViewComponent(LogCatViewComponentListener listener) {
        this.listener = listener;
        create();
    }
    
    /** */
    private void create() {
        setLayout(new BorderLayout());
        createTopBar();
        createConsole();
    }
    
    /** */
    private void createTopBar() {
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setBorder(null);
        add(topBar,BorderLayout.NORTH);
        
    // device label
        JLabel deviceLabel = new JLabel(res.getStr("device"));
        topBar.add(deviceLabel);
        
    // device combo
        deviceCombo = new JComboBox<LogCatDeviceItem>();
        deviceCombo.addItemListener(new ItemListener() {
            /** */
            @Override
            public void itemStateChanged(ItemEvent event) {
                deviceSelectionChanged(event);
            }
        });
        topBar.add(deviceCombo);
        
    // no device item
        deviceCombo.addItem(new LogCatDeviceItem(res.getStr("no.device")));
        
    // process name
        processNameLabel = new JLabel();
        processNameLabel.setFont(
            UIFonts.getSmallFont(processNameLabel.getFont()));
        topBar.add(processNameLabel);
    }
    
    /** */
    private void createConsole() {
        console = new ConsoleComponent();
        add(console,BorderLayout.CENTER);
        
    // styles
        console.addStyle(Level.VERBOSE.name(),UIColors.fromHex("888888"));
        console.addStyle(Level.DEBUG.name(),UIColors.fromHex("b8b8b8"));
        console.addStyle(Level.INFO.name(),UIColors.fromHex("d8d8d8"));
        console.addStyle(Level.WARNING.name(),UIColors.fromHex("dc9656"));
        console.addStyle(Level.ERROR.name(),UIColors.fromHex("df5f5a"));    
        console.addStyle(Level.ASSERT.name(),UIColors.fromHex("df5f5a"));    
        console.addStyle(NON_PROCESS_STYLE,UIColors.fromHex("b9925f"));
        console.addStyle(LOG_CAT_STYLE,UIColors.fromHex("b9925f"));
    }
    
    /** */
    private void deviceSelectionChanged(ItemEvent event) {
        if (event.getStateChange() != ItemEvent.SELECTED) {
            return;
        }
        
        LogCatDeviceItem item = (LogCatDeviceItem)deviceCombo.getSelectedItem();
        listener.adbDeviceSelected(item.device);
    }
    
    /** */
    private LogCatDeviceItem findItem(ADBDevice device) {
        for (int index = 0; index < deviceCombo.getItemCount(); index++) {
            LogCatDeviceItem item =
                (LogCatDeviceItem)deviceCombo.getItemAt(index);
            if (device.equals(item.device) == true) {
                return item;
            }
        }
        
        return null;
    }
    
    /** */
    void setProcessName(String processName) {
        if (processName == null || processName.length() == 0) {
            processNameLabel.setText(res.getStr("no.process.name"));
            return;
        }
        processNameLabel.setText(res.getStr("process.name",processName));
    }
    
    /** */
    void deviceConnected(ADBDevice device) {
        LogCatDeviceItem item = new LogCatDeviceItem(device);
        deviceCombo.addItem(item);
    }
    
    /** */
    void deviceDisconnected(ADBDevice device) {
        LogCatDeviceItem item = findItem(device);
        if (item == null) {
            // TODO Handle null device.
            return;
        }
        deviceCombo.removeItem(item);
    }
    
    /** */
    void log(ADBLogCatMessage message) {
        console.append(res.getStr(message.getLevel().name()),
            message.getLevel().name());
        
        String msg = res.getStr("log.msg",
            message.getTag(),message.getMessage());
        if (message.processMatch() == true) {
            console.append(msg);
        }
        else {
            console.append(msg,NON_PROCESS_STYLE);
        }
    }
    
    /** */
    void logCatLog(String msg) {
        console.append(msg,LOG_CAT_STYLE);
    }
    
    /** */
    void clear() {
        console.clear();
    }
    
    /** */
    void setScrollLock(boolean lock) {
        console.setScrollLock(lock);
    }
    
    /** */
    void setFindPanelVisible(boolean visible) {
        console.setFindPanelVisible(visible);
    }
}