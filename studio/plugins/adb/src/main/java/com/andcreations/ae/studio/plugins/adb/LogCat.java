package com.andcreations.ae.studio.plugins.adb;

import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import com.andcreations.ae.studio.plugins.android.AndroidSettings;
import com.andcreations.ae.studio.plugins.android.AndroidSettingsAdapter;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.common.UIKeys;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.ae.studio.plugins.ui.main.view.DefaultViewProvider;
import com.andcreations.ae.studio.plugins.ui.main.view.SingleViewFactory;
import com.andcreations.ae.studio.plugins.ui.main.view.View;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButton;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButtonListener;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCategory;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCheckButton;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCheckButtonListener;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewDecorator;
import com.andcreations.android.adb.ADBDevice;
import com.andcreations.android.adb.ADBLogCatListener;
import com.andcreations.android.adb.ADBLogCatMessage;
import com.andcreations.android.adb.ADBProcessLogCat;
import com.andcreations.android.adb.ADBProcessLogCatListener;
import com.andcreations.resources.BundleResources;
/**
 * @author Mikolaj Gucki
 */
class LogCat implements LogCatViewComponentListener {
    /** The view identifier. */
    private static final String VIEW_ID =
        "com.andcreations.ae.studio.plugins.adb.logcat"; 
        
    /** */
    private static LogCat instance;
    
    /** */
    private BundleResources res = new BundleResources(LogCat.class);     
    
    /** */
    private LogCatViewComponent component;  
    
    /** */
    private ADBProcessLogCat processLogCat;
    
    /** */
    private ADBProcessLogCatListener processLogCatListener;
    
    /** */
    void init() {
    // android settings
        AndroidSettings.get().addAndroidSettingsListener(
            new AndroidSettingsAdapter() {
                /** */
                @Override
                public void androidPackageChanged(String androidPackage) {
                    synchronized (LogCat.this) {
                        if (processLogCat != null) {
                            processLogCat.setProcessName(androidPackage);
                        }
                        component.setProcessName(androidPackage);
                    }
                }                
            });
        
    // log cat
        processLogCatListener = new ADBProcessLogCatListener() {
            /** */
            public void logCatFailed(String processName,Exception exception) {
                component.logCatLog(res.getStr("log.cat.failed.log",
                    exception.getMessage()));
                CommonDialogs.error(res.getStr("log.cat.failed.title"),
                    res.getStr("log.cat.failed.msg",exception.getMessage()));
            }
            
            /** */
            public void processDied(String processName) {
                component.logCatLog(res.getStr("process.died"));
            }
            
            /** */
            public void logCatStopped(String processName) {
            }            
        };
        
    // UI
        initUI();
        component.setProcessName(AndroidSettings.get().getAndroidPackage());
    }
    
    /** */
    private void initUI() {
    // component
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                component = new LogCatViewComponent(LogCat.this);
            }
        });
        
        String title = res.getStr("view.title");
        ImageIcon icon = Icons.getIcon(ADBIcons.LOG_CAT);
        
    // view factory
        SingleViewFactory factory = new SingleViewFactory(
            VIEW_ID,component,title,icon);
        factory.setViewCategory(ViewCategory.DETAILS);
        MainFrame.get().getViewManager().registerViewFactory(factory);
        
    // view provider
        DefaultViewProvider provider = new DefaultViewProvider(
            title,icon,factory);
        MainFrame.get().getViewManager().addViewProvider(provider);
        
    // clear
        factory.addViewDecorator(new ViewDecorator() {
            /** */
            @Override
            public void decorateView(View view) {
                KeyStroke keyStroke = KeyStroke.getKeyStroke(
                    KeyEvent.VK_X,UIKeys.menuKeyMask());                  
                ViewButton button = view.addButton();
                button.setAccelerator(keyStroke);
                button.setIcon(Icons.getIcon(DefaultIcons.ERASER));
                button.setTooltip(res.getStr("clear.tooltip"));
                button.addViewButtonListener(new ViewButtonListener() {
                    /** */
                    @Override
                    public void actionPerformed(ViewButton button) {
                        component.clear();
                    }
                });                
            }
        });
        
    // lock
        factory.addViewDecorator(new ViewDecorator() {
            /** */
            @Override
            public void decorateView(View view) {
                KeyStroke keyStroke = KeyStroke.getKeyStroke(
                    KeyEvent.VK_L,UIKeys.menuKeyMask());                  
                ViewCheckButton button = view.addCheckButton();
                button.setAccelerator(keyStroke);
                button.setIcon(Icons.getIcon(DefaultIcons.LOCK));
                button.setTooltip(res.getStr("lock.tooltip"));
                button.addViewCheckButtonListener(
                        new ViewCheckButtonListener() {
                        /** */
                        @Override
                        public void actionPerformed(ViewCheckButton button) {
                            component.setScrollLock(button.isChecked());
                        }
                    });                
            }
        });
        
    // find
        factory.addViewDecorator(new ViewDecorator() {
            /** */
            @Override
            public void decorateView(View view) {
                KeyStroke keyStroke = KeyStroke.getKeyStroke(
                    KeyEvent.VK_F,UIKeys.menuKeyMask());                  
                ViewCheckButton button = view.addCheckButton();
                button.setAccelerator(keyStroke);
                button.setIcon(Icons.getIcon(DefaultIcons.SEARCH));
                button.setTooltip(res.getStr("find.tooltip"));
                button.addViewCheckButtonListener(
                    new ViewCheckButtonListener() {
                        /** */
                        @Override
                        public void actionPerformed(ViewCheckButton button) {
                            component.setFindPanelVisible(button.isChecked());
                        }
                    });                
            }
        });    
    }
    
    /** */
    private void stopLogCat() {
        if (processLogCat != null) {
            processLogCat.stopLogCat();
            processLogCat = null;
        }        
    }
    
    /** */
    @Override
    public synchronized void adbDeviceSelected(ADBDevice device) {
    // stop
        stopLogCat();
        if (device == null) {
            return;
        }
        
        
        component.clear();
    // start
        String processName = AndroidSettings.get().getAndroidPackage();
        processLogCat = new ADBProcessLogCat(device,processName,
            new ADBLogCatListener() {
                /** */
                @Override
                public void log(List<ADBLogCatMessage> messages) {
                    for (ADBLogCatMessage msg:messages) {
                        if (msg.processMatch() == true) {
                            component.log(msg);
                        }
                    }
                }
            });
        processLogCat.addADBProcessLogCatListener(processLogCatListener);
        processLogCat.startLogCat();
    }
    
    /** */
    void deviceConnected(final ADBDevice device) {
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                component.deviceConnected(device);
            }
        });        
    }
    
    /** */
    void deviceDisconnected(final ADBDevice device) {
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                component.deviceDisconnected(device);
            }            
        });
    }
    
    /** */
    void stop() {
        stopLogCat();
    }
    
    /** */
    public static LogCat get() {
        if (instance == null) {
            instance = new LogCat();
        }
        
        return instance;
    }
}