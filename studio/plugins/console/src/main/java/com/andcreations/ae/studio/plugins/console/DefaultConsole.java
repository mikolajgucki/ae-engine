package com.andcreations.ae.studio.plugins.console;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.common.UIKeys;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.ae.studio.plugins.ui.main.view.DefaultViewProvider;
import com.andcreations.ae.studio.plugins.ui.main.view.SingleViewFactory;
import com.andcreations.ae.studio.plugins.ui.main.view.View;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCategory;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewDecorator;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class DefaultConsole {
	/** */
	static final String VIEW_ID = DefaultConsole.class.getName();
	
    /** */
    private static final String STYLE_TRACE = "trace";
    
    /** */
    private static final String STYLE_WARNING = "warning";
    
    /** */
    private static final String STYLE_ERROR = "error";
    
    /** */
    private static DefaultConsole instance;
    
    /** */
    private static final BundleResources res =
        new BundleResources(DefaultConsole.class);    
    
    /** */
    private ConsoleComponent component;
    
    /** */
    private DefaultViewProvider viewProvider;
    
    /** */
    private DefaultConsole() {
    }
    
    /** */
    private void createComponent() {
        component = new ConsoleComponent();
        
    // styles
        component.addStyle(STYLE_TRACE,UIColors.blend(
            UIManager.getColor("Panel.background"),
            UIColors.fromHex("d0d0d0"),0.6));
        component.addStyle(STYLE_WARNING,UIColors.fromHex("dc9656"));
        component.addStyle(STYLE_ERROR,UIColors.fromHex("df5f5a"));
    }
    
    /** */
    public void clear() {
    	component.clear();
    }
    
    /** */
    public void show() {
    	viewProvider.showView();
    }
    
    /** */
    public void print(String str) {
        component.append(str);
    }
    
    /** */
    public void println(String str) {
        component.append(str + "\n");
    }
    
    /** */
    public void trace(String str) {
        component.append(str,STYLE_TRACE);
    }
    
    /** */
    public void traceln(String str) {
        component.append(str + "\n",STYLE_TRACE);
    }
    
    /** */
    public void warning(String str) {
        component.append(str,STYLE_WARNING);
    }
    
    /** */
    public void warningln(String str) {
        component.append(str + "\n",STYLE_WARNING);
    }
    
    /** */
    public void error(String str) {
        component.append(str,STYLE_ERROR);
    }
    
    /** */
    public void errorln(String str) {
        component.append(str + "\n",STYLE_ERROR);
    }
    
    /** */
    private void addClearConsoleMenu() {
        JMenuItem item = new JMenuItem(res.getStr("clear.console"));
        item.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_X,UIKeys.menuKeyMask() | KeyEvent.SHIFT_DOWN_MASK));        
        item.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                component.clear();
            }
        });
        MainFrame.get().getMainFrameMenuBar().getViewsMenu().addSeparator();
        MainFrame.get().getMainFrameMenuBar().getViewsMenu().add(item);        
    }    
    
    /** */
    private void initUI() {
    	createComponent();
    	
    // clear console menu item
        addClearConsoleMenu();
        
        String title = res.getStr("title");
        
    // view factory
        SingleViewFactory factory = new SingleViewFactory(VIEW_ID,component,
    		title,Icons.getIcon(ConsoleIcons.CONSOLE));
        factory.setViewCategory(ViewCategory.LOG);
        MainFrame.get().getViewManager().registerViewFactory(factory);
        factory.addViewDecorator(new ViewDecorator() {
        	/** */
			@Override
			public void decorateView(View view) {
				ConsoleViewActions.addActions(view);
			}
		});
        
    // view provider
        viewProvider = new DefaultViewProvider(res.getStr("title"),
            Icons.getIcon(ConsoleIcons.CONSOLE),factory,VIEW_ID);
        MainFrame.get().getViewManager().addViewProvider(viewProvider);    	
    }
    
    /** */
    void init() {
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
            	initUI();
            }
        });    	
    }
    
    /** */
    public static DefaultConsole get() {        
        if (instance == null) {
            UICommon.invokeAndWait(new Runnable() {
                /** */
                @Override
                public void run() {
                    instance = new DefaultConsole();
                }
            });
        }
        
        return instance;
    }
}