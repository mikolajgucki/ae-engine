package com.andcreations.ae.studio.plugins.ui.main;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugin.manager.PluginManager;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewManager;
import com.andcreations.ae.studio.plugins.ui.main.view.impl.ViewManagerImpl;
import com.andcreations.resources.BundleResources;

/**
 * The main frame.
 * 
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {
    /** */
    private final BundleResources res = new BundleResources(MainFrame.class);        
    
    /** The main frame state. */
    private MainFrameState state;
    
    /** The menu bar. */
    private MenuBar menuBar;
    
    /** The view manager. */
    private ViewManager viewManager;
    
    /** The main frame listeners. */
    private List<MainFrameListener> listeners = new ArrayList<>();
    
    /** The frame instance. */
    private static MainFrame instance;
    
    /** */
    private MainFrame() {
    }
    
    /** */
    private void doCreate() {
        setTitle(res.getStr("title"));
        setIconImage(Icons.getIcon(DefaultIcons.AE_STUDIO).getImage());
        
        addComponentListener(new ComponentAdapter() {
            /** */
            @Override
            public void componentShown(ComponentEvent event) {
                notifyMainFrameShown();
            }
            
            /** */
            @Override
            public void componentResized(ComponentEvent event) {
                frameBoundsChanged();            
            }
            
            /** */
            @Override
            public void componentMoved(ComponentEvent event) {
                frameBoundsChanged();
            }            
        });
        
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            /** */
            public void windowClosing(WindowEvent event) {
                close();
            }
        });
        
        addContent();
    }
    
    /** */
    public void addMainFrameListener(MainFrameListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    
    /** */
    public void removeMainFrameListener(MainFrameListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }
    
    /** */
    private void notifyMainFrameShown() {
        synchronized (listeners) {
            for (MainFrameListener listener:listeners) {
                listener.mainFrameShown();
            }
        }
    }
    
    /** */
    private void frameBoundsChanged() {
        if (isVisible() == false) {
            return;
        }
        
        state.setMaximized((getExtendedState() & JFrame.MAXIMIZED_BOTH) != 0);
        state.setBounds(new Rectangle(getX(),getY(),getWidth(),getHeight()));
    }
    
    /** */
    private void close() {
        PluginManager.postStop();
    }
    
    /** */
    private void addViewManager() {
        Log.info("Adding the view manager");
        viewManager = new ViewManagerImpl();
        getContentPane().add(viewManager.getComponent(),BorderLayout.CENTER);
    }
    
    /** */
    private void addMenuBar() {
        menuBar = new MenuBar(viewManager);
        setJMenuBar(menuBar);
    }
    
    /** */
    private void addStatusBar() {
        StatusBar statusBar = StatusBar.get();
        getContentPane().add(statusBar,BorderLayout.SOUTH);
    }
    
    /** */
    private void addContent() {
        getContentPane().setLayout(new BorderLayout());
        addViewManager();
        addMenuBar();
    }
    
    /** */
    private void finalizeBeforeShowing() {
        addStatusBar();
        menuBar.finalizeBeforeShowing();
    }
    
    /** */
    void center() {
        if ((getExtendedState() & JFrame.MAXIMIZED_BOTH) != 0) {
            setExtendedState(JFrame.NORMAL);
        }
        
    // initial bounds
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int screenWidth = toolkit.getScreenSize().width;
        int screenHeight = toolkit.getScreenSize().height;
        
        int width = screenWidth * 3 / 4;
        int height = screenHeight * 3 / 4;
        
        setLocation((screenWidth - width) / 2,(screenHeight - height) / 2);
        setSize(width,height);
    }
    
    /** */
    public void showMainFrame() { 
        finalizeBeforeShowing();
        
    // location, size
        if (state.isMaximized() == true) {
        // Center just to set bounds in case the user would restore from
        // maximized to the normal state.        
            center();
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        else {
            if (state.getBounds() != null) {
                Rectangle bounds = state.getBounds();
                setLocation(bounds.x,bounds.y);
                setSize(bounds.width,bounds.height);
            }
            else {
                center();
            }
        }
        
    // show
        setVisible(true);
    }
    
    /** */
    void stop() {
        dispose();
    }
    
    /**
     * Gets the view manager.
     *
     * @return The view manager.
     */
    public ViewManager getViewManager() {
        return viewManager;
    }
    
    /** */
    public MenuBar getMainFrameMenuBar() {
        return menuBar;
    }
    
    /**
     * Fills the state with the current values. 
     */
    private void updateMainFrameState() {
        /*
        Log.info("The view manager state is:\n" + viewManager.getState());
        */
        state.setViewManagerState(viewManager.getState());
    }
    
    /**
     * Gets the main frame state.
     * 
     * @return The main frame state.
     */
    public MainFrameState getMainFrameState() {
        updateMainFrameState();
        return state;
    }
    
    /**
     * Sets the main frame state.
     * 
     * @param state The main frame state.
     */
    public void setMainFrameState(MainFrameState state) {
        this.state = state;
        viewManager.setState(state.getViewManagerState());
    }
    
    /**
     * Gets the main frame instance.
     * 
     * @return The main frame instance.
     */
    public static MainFrame get() {
        return instance;
    }
    
    /** */
    static void create() {
        Log.info("Creating the main frame");
        instance = new MainFrame();
        instance.doCreate();
    }
}
