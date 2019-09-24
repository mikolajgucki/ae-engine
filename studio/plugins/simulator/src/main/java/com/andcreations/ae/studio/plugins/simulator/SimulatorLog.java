package com.andcreations.ae.studio.plugins.simulator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;

import com.andcreations.ae.desktop.LogLevel;
import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.console.ConsoleComponent;
import com.andcreations.ae.studio.plugins.console.ConsoleIcons;
import com.andcreations.ae.studio.plugins.console.LinkEvent;
import com.andcreations.ae.studio.plugins.console.LinkListener;
import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.lua.lib.NativeLua;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.ae.studio.plugins.ui.main.view.DefaultViewProvider;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCategory;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class SimulatorLog {
    /** */
    private static SimulatorLog instance;
    
    /** */
    private static final String VIEW_ID = "simulator.log";    
    
    /** */
    private static final String TIMESTAMP_STYLE = "timestamp";
    
    /** */
    private static final String LINK_STYLE = "link";
    
    /** */
    private static final String ENGINE_LOG_STYLE = "engine";
    
    /** */
    private static BundleResources res =
        new BundleResources(SimulatorLog.class);       
    
    /** */
    private static DefaultViewProvider viewProvider;
    
    /** */
    private ConsoleComponent component;
    
    /** */
    private boolean logTimestamp = true;
    
    /** */
    private SimpleDateFormat dateFormat;
    
    /** */
    private SimulatorLog() {
        create();
    }
    
    /** */
    private void create() {
        component = new ConsoleComponent();
        dateFormat = new SimpleDateFormat("HH:mm:ss.SSS ");
        addStyles();
    }
    
    /** */
    private void addStyles() {
        component.addStyle(LogLevel.TRACE.name(),UIColors.fromHex("888888"));
        component.addStyle(LogLevel.DEBUG.name(),UIColors.fromHex("b8b8b8"));
        component.addStyle(LogLevel.INFO.name(),UIColors.fromHex("d8d8d8"));
        component.addStyle(LogLevel.WARNING.name(),UIColors.fromHex("dc9656"));
        component.addStyle(LogLevel.ERROR.name(),UIColors.fromHex("df5f5a"));
        component.addStyle(TIMESTAMP_STYLE,UIColors.fromHex("6484a1"));
        component.addStyle(LINK_STYLE,UIColors.fromHex("86c1b9"),true);
        component.addStyle(ENGINE_LOG_STYLE,UIColors.fromHex("b9925f"));
    }
    
    /** */
    private String trim(String str,int length) {
        if (str.length() > length) {
            return str.substring(0,length);
        }
        
        return String.format("%" + length + "s",str);
    }    
    
    /** */
    private int getErrorLineNumber(Matcher matcher,String msg) {
        int lineNumber = -1;
        try {
            lineNumber = Integer.parseInt(matcher.group(2));
            if (lineNumber < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException exception) {
            Log.error(String.format(
                "Failed to parse line number from Lua error %s",msg));
                
        }
        return lineNumber;
    }
    
    /** */
    private void checkLinks(int offset,String msg) {
        Pattern pattern = Pattern.compile("[^\\[]*" + NativeLua.ERROR_PATTERN);
        Matcher matcher = pattern.matcher(msg);
        if (matcher.matches() == true) {
        // line number
            final int lineNumber = getErrorLineNumber(matcher,msg);
            if (lineNumber == -1) {
                return;
            }
            
        // name, start, end
            final String name = matcher.group(1);
            int start = matcher.start(1);
            int end = matcher.end(2);
            
        // listener
            LinkListener listener = new LinkListener() {
                /** */
                @Override
                public void linkClicked(LinkEvent event) {
                    File file = ProjectLuaFiles.get().getFileByPath("/" + name);
                    if (file != null) {
                        LuaFile.edit(file,lineNumber);
                    }
                    else {
                        Log.error(
                            String.format("No file for path %s",name));
                    }                        
                }
            };

        // add link                                     
            component.addLink(offset + start,end - start,LINK_STYLE,listener);
        }
    }
    
    /** */
    void log(LogLevel level,String tag,String msg) {
        if (logTimestamp == true) {
            component.append(dateFormat.format(new Date()),TIMESTAMP_STYLE);
        }
        component.append(trim(level.toString(),4),level.name());
        component.append(String.format(" [%s] ",trim(tag,12)));
        
        String[] tokens = msg.split("\n");
        for (String token:tokens) {
            int offset = component.append(token + "\n");
            checkLinks(offset,token);
        }
    }
    
    /** */
    void info(String msg) {
        component.append(msg + "\n",LogLevel.INFO.name());
    }
    
    /** */
    void error(String msg) {
        component.append(msg + "\n",LogLevel.ERROR.name());
    }
    
    /** */
    void engineLog(String msg) {
        component.append(msg + "\n",ENGINE_LOG_STYLE);
    }
    
    /** */
    void clear() {
        component.clear();
    }
    
    /** */
    void show() {
        viewProvider.showView();
    }
    
    /** */
    ConsoleComponent getComponent() {
        return component;
    }

    /** */
    static void initUI() {
        ConsoleComponent component = SimulatorLog.get().getComponent();
        
        String title = res.getStr("view.title");
        ImageIcon icon = Icons.getIcon(ConsoleIcons.CONSOLE,
            SimulatorIcons.DECO_SIMULATOR);
        
    // view factory
        SimulatorLogViewFactory factory = new SimulatorLogViewFactory(
            VIEW_ID,component,title,icon);
        factory.setViewCategory(ViewCategory.LOG);
        MainFrame.get().getViewManager().registerViewFactory(factory);
        
    // view provider
        viewProvider = new DefaultViewProvider(
            title,icon,factory,VIEW_ID);
        MainFrame.get().getViewManager().addViewProvider(viewProvider);     
    }    
    
    /** */
    static SimulatorLog get() {
        if (instance == null) {
            instance = new SimulatorLog();
        }
        
        return instance;
    }
}