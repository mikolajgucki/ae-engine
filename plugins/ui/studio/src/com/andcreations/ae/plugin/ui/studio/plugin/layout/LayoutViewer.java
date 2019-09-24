package com.andcreations.ae.plugin.ui.studio.plugin.layout;

import java.util.List;
import java.util.ArrayList;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import java.io.File;
import java.io.StringWriter;
import java.io.IOException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.FileUtils;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.VelocityContext;

import com.andcreations.lang.Strings;
import com.andcreations.resources.BundleResources;
import com.andcreations.resources.ResourceLoader;
import com.andcreations.io.json.JSON;
import com.andcreations.lua.PCallError;
import com.andcreations.lua.runner.LuaRunner;
import com.andcreations.lua.runner.LuaErrorException;
import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.ae.studio.plugins.ui.main.view.View;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewDecorator;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCategory;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButton;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCheckButton;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButtonListener;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCheckButtonListener;
import com.andcreations.ae.studio.plugins.ui.main.view.SingleViewFactory;
import com.andcreations.ae.studio.plugins.ui.main.view.DefaultViewProvider;
import com.andcreations.ae.studio.plugins.ae.dist.AEDist;
import com.andcreations.ae.studio.plugins.project.ProjectProperties;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.file.cache.TextFileCache;
import com.andcreations.ae.studio.plugins.file.EditedFileListener;
import com.andcreations.ae.studio.plugins.file.EditedFile;
import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.file.FileAdapter;
import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.lua.lib.NativeLua;
import com.andcreations.ae.studio.plugins.lua.lib.runner.LuaRunnerFactory;
import com.andcreations.ae.studio.plugins.simulator.Simulator;
import com.andcreations.ae.studio.plugins.simulator.SimulatorAdapter;
import com.andcreations.ae.plugin.ui.studio.plugin.UIIcons;
import com.andcreations.ae.plugin.ui.studio.plugin.layout.resources.R;

/**
 * @author Mikolaj Gucki
 */
public class LayoutViewer implements LayoutViewerComponentListener {
    /** The view identifier. */
    private static final String VIEW_ID =
        "com.andcreations.ae.plugin.ui.studio.plugin.layout";  
        
    /** */
    private static final String LAYOUT_FILE_SUFFIX = "_layout.lua";
    
    /** */
    private static final String TEMPLATE_NAME = "layout_to_json.vm";
    
    /** */
    private static final String HEADER_ERROR = "eror";
    
    /** */
    private static LayoutViewer instance;
    
    /** */
    private LayoutViewerState state;
    
    /** */
    private BundleResources res = new BundleResources(LayoutViewer.class);     

    /** */
    private ViewCheckButton pinButton;
    
    /** */
    private ViewButton refreshButton;
    
    /** */
    private ViewButton settingsButton;
    
    /** */
    private LuaRunner luaRunner;
    
    /** */
    private List<String> loadLuaSnippets = new ArrayList<>();
    
    /** */
    private LayoutViewerComponent component;
    
    /** */
    private LayoutViewerSettingsDialog settingsDialog;
    
    /** */
    private File currentFile;
    
    /** */
    private String getLuaModule(File file) {
        return ProjectLuaFiles.get().getLuaModule(file);
    }        
    
    /** */
    private String getSrc(File file,Dimension size) throws IOException {
    // Lua package paths
        File[] luaSrcDirs = ProjectProperties.get().getLuaSrcDirs();
        List<String> pkgPaths = new ArrayList<>();
        for (int index = 0; index < luaSrcDirs.length; index++) {
            pkgPaths.add(LuaFile.getPackagePath(luaSrcDirs[index]));
        }
        
    // the directory with UI plugin Lua sources
        File uiLuaSrcDir = AEDist.get().getPluginLuaSrcDir("ui");
        
    // the directory with the layout Lua file
        File layoutLuaSrcDir = file.getParentFile();
            
    // the Lua layout file base name
        String layoutBaseName = FilenameUtils.getBaseName(
            file.getAbsolutePath());
        
    // template
        String template = ResourceLoader.loadAsString(R.class,TEMPLATE_NAME);
        
    // textures, fonts
        boolean hasTexturesModules = ProjectLuaFiles.get().getNodeByPath(
            "/assets/textures/modules.lua",false) != null;
        boolean hasFontsModules = ProjectLuaFiles.get().getNodeByPath(
            "/assets/fonts/modules.lua",false) != null;
        
    // context
        VelocityContext context = new VelocityContext();
        context.put("width",size.width);
        context.put("height",size.height);
        context.put("pkgPaths",pkgPaths);
        context.put("loadLuaSnippets",loadLuaSnippets);
        context.put("userModulesToLoad",state.getUserModulesToLoad());
        context.put("layoutModule",getLuaModule(file));
        context.put("hasTexturesModules",hasTexturesModules);
        context.put("hasFontsModules",hasFontsModules);

    // write
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context,writer,"",template);
        
        return writer.toString();             
    }
    
    /** */
    private void setViewButtonsEnabled(boolean enabled) {
        /*
        pinButton.setEnabled(enabled);
        refreshButton.setEnabled(enabled);
        settingsButton.setEnabled(enabled);
        */
    }
    
    /** */
    private Dimension getLayoutSize(LayoutSize layoutSize) {
        int width;
        int height;
    // size
        if (layoutSize.getUseSimulatorSize() == false) {
            width = layoutSize.getWidth();
            height = layoutSize.getHeight();            
        }
        else {
            width = Simulator.get().getWidth();
            height = Simulator.get().getHeight();
        }

        return new Dimension(width,height);        
    }
    
    /** */
    private void setRoot(ComponentLuaResult root,LayoutSize layoutSize) {
        Dimension size = getLayoutSize(layoutSize);
        
    // show
        component.setRoot(root,size.width,size.height);
        setViewButtonsEnabled(true);
    }
    
    /** */
    private void clear() {
        component.clear();
        setViewButtonsEnabled(false);
        currentFile = null;
    }
    
    /** */
    private void tryAddToLayouts(File file) {
        component.tryAddToLayouts(file);
        setViewButtonsEnabled(false);
        currentFile = null;
    }
    
    /** */
    private void setError(String msg,String tooltip) {
        component.setError(msg,tooltip);
        setViewButtonsEnabled(false);
    }
    
    /** */
    private void setError(String msg) {
        setError(msg,msg);
    }
    
    /** */
    private LayoutLuaResult layout(File file,Dimension size)
        throws IOException {
    // source
        String src = getSrc(file,size);
        //System.out.println(">>>\n" + src + "\n<<<");
        
    // write to a temporary file and run
        File tmpFile = Files.get().getTmpFile(file.getName());
        String result = null;
        try {
            FileUtils.writeStringToFile(tmpFile,src,"UTF-8");
            result = luaRunner.run(tmpFile);
        } finally {
            tmpFile.delete();
        }
        
    // result header
        String header = result.substring(0,HEADER_ERROR.length());
        
    // error in result
        if (header.equals(HEADER_ERROR) == true) {
            LuaErrorException exception = new LuaErrorException();
            exception.setError(result.substring(HEADER_ERROR.length()));
            throw exception;
        }
        
    // parse the result
        return JSON.read(result,LayoutLuaResult.class);
    }
    
    /** */
    private String getLuaError(String error) {
    // parse the error
        PCallError pcallError = PCallError.parse(error);
        if (pcallError.getLine() >= 0) {
            return res.getStr("pcall.error.with.line",
                Integer.toString(pcallError.getLine()),
                pcallError.getMessage());
        }
        
        return res.getStr("pcall.error",pcallError.getMessage());
    }
    
    /** */
    private void showLayout(File file) throws IOException {
        long startTime = System.currentTimeMillis();

    // not a project Lua file
        if (ProjectLuaFiles.get().isLuaFile(file) == false) {
            clear();
            return;
        }
        
    // size
        String module = getLuaModule(file);
        LayoutSize layoutSize = state.findSize(module);
        
    // if not a layout
        if (layoutSize == null) {
            tryAddToLayouts(file);
            return;
        }
        currentFile = file;
        
        String src = TextFileCache.get().read(file);
    // compile
        String compilationError = NativeLua.compile(src);
        if (compilationError != null) {
            setError(res.getStr("compilation.errors",compilationError));
            return;
        }

    // get the layout
        LayoutLuaResult result = layout(file,getLayoutSize(layoutSize));
        if (result.getError() != null) {
            setError(getLuaError(result.getError()),result.getError());
            return;
        }

        
    // draw
        if (result.getRoot() != null) {
            setRoot(result.getRoot(),layoutSize);
        }
        else {
            clear();
        }
        
        long showTime = System.currentTimeMillis() - startTime;
        Log.trace(String.format("Layout show took %d ms",showTime));
    }
    
    /** */
    private boolean isPinned() {
        if (pinButton == null) {
            return false;
        }
        return pinButton.isChecked();
    }
    
    /** */
    private void tryShowLayout(File file) {
        if (isPinned() == true && file.equals(currentFile) == false) {
            return;
        }
        
        try {
            showLayout(file);
        } catch (LuaErrorException exception) {
            setError(getLuaError(exception.getError()),exception.getError());
            return;
        } catch (IOException exception) {
            setError(exception.getMessage());
            return;
        }
    }
    
    /** */
    private void refresh() {
        if (isPinned() == true) {
            return;
        }
        
        File file = EditedFile.get().getLastEditedFile();
        if (file != null) {
            tryShowLayout(file);
        }
        else {
            clear();
        }
    }

    /** */
    @Override
    public void addToLayouts(File file) {
    // initial layout size
        LayoutSize layoutSize = new LayoutSize(getLuaModule(file));
        layoutSize.setUseSimulatorSize(true);
        layoutSize.setWidth(Simulator.get().getWidth());
        layoutSize.setHeight(Simulator.get().getHeight());
        state.addSize(layoutSize);
        
        refresh();
    }
    
    /** */
    @Override
    public void layoutViewerComponentShown() {
        refresh();
    }

    /** */
    @Override
    public void layoutViewerComponentResized() {
        refresh();
    }
    
    /** */
    private void removeFromLayouts() {
        state.removeSize(getLuaModule(currentFile));
        refresh();
    }
    
    /** */
    private void showSettings() {
        if (currentFile == null) {
            return;
        }
        
        LayoutSize size = state.findSize(getLuaModule(currentFile));
        if (settingsDialog.showLayoutViewerSettingsDialog(size) == true) {
            refresh();
        }
    }
    
    /** */
    public void init(LayoutViewerState state) {
        this.state = state;
        
    // Lua runner
        luaRunner = LuaRunnerFactory.get().createLuaRunner();
        
    // file listener
        Files.get().addFileListener(new FileAdapter() {
            /** */
            @Override    
            public void fileChanged(File file) {
                if (file.equals(EditedFile.get().getLastEditedFile())) {
                    tryShowLayout(file);
                }
            }
        });
        
    // edited file
        EditedFile.get().addEditedFileListener(new EditedFileListener() {
            /** */
            @Override
            public synchronized void editedFileChanged(File file) {
                tryShowLayout(file);
            }                
        });
        
    // simulator listener
        Simulator.get().addSimulatorListener(new SimulatorAdapter() {
            /** */
            @Override
            public void simulatorResolutionChanged(int width,int height) {
                refresh();
            }
        });
        
    // UI
        initUI();
    }
    
    /** */
    private void initUI() {
    // component, dialog
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                component = new LayoutViewerComponent(LayoutViewer.this);
                settingsDialog = new LayoutViewerSettingsDialog();
            }
        });

        String title = res.getStr("view.title");
        ImageIcon icon = Icons.getIcon(UIIcons.LAYOUT);   
        
    // view factory
        SingleViewFactory factory = new SingleViewFactory(
            VIEW_ID,component,title,icon);
        factory.setViewCategory(ViewCategory.DETAILS);
        MainFrame.get().getViewManager().registerViewFactory(factory);
        
    // view provider
        DefaultViewProvider provider = new DefaultViewProvider(
            title,icon,factory);
        MainFrame.get().getViewManager().addViewProvider(provider);     
        
    // pin
        factory.addViewDecorator(new ViewDecorator() {
            /** */
            @Override
            public void decorateView(View view) {
                pinButton = view.addCheckButton();
                pinButton.setIcon(Icons.getIcon(DefaultIcons.PIN));
                pinButton.setTooltip(res.getStr("pin.tooltip"));
                pinButton.addViewCheckButtonListener(
                    new ViewCheckButtonListener() {
                        /** */
                        @Override
                        public void actionPerformed(ViewCheckButton button) {
                            if (button.isChecked() == false) {
                                refresh();
                            }
                        }
                });
            }
        });        
        
    // refresh
        factory.addViewDecorator(new ViewDecorator() {
            /** */
            @Override
            public void decorateView(View view) {
                refreshButton = view.addButton();
                refreshButton.setIcon(Icons.getIcon(DefaultIcons.REFRESH));
                refreshButton.setDisabledIcon(
                    Icons.getDisabledIcon(DefaultIcons.REFRESH));
                refreshButton.setTooltip(res.getStr("refresh.tooltip"));
                refreshButton.addViewButtonListener(new ViewButtonListener() {
                    /** */
                    @Override
                    public void actionPerformed(ViewButton button) {
                        refresh();
                    }
                });                  
            }
        });
        
    // settings
        factory.addViewDecorator(new ViewDecorator() {
            /** */
            @Override
            public void decorateView(View view) {
                settingsButton = view.addButton();
                settingsButton.setIcon(Icons.getIcon(DefaultIcons.SETTINGS));
                settingsButton.setDisabledIcon(
                    Icons.getDisabledIcon(DefaultIcons.SETTINGS));
                settingsButton.setTooltip(res.getStr("settings.tooltip"));
                settingsButton.addViewButtonListener(new ViewButtonListener() {
                    /** */
                    @Override
                    public void actionPerformed(ViewButton button) {
                        showSettings();
                    }
                });                  
            }
        });
        
    // remove
        factory.addViewDecorator(new ViewDecorator() {
            /** */
            @Override
            public void decorateView(View view) {
                settingsButton = view.addButton();
                settingsButton.setIcon(Icons.getIcon(DefaultIcons.DELETE));
                settingsButton.setDisabledIcon(
                    Icons.getDisabledIcon(DefaultIcons.DELETE));
                settingsButton.setTooltip(res.getStr("remove.tooltip"));
                settingsButton.addViewButtonListener(new ViewButtonListener() {
                    /** */
                    @Override
                    public void actionPerformed(ViewButton button) {
                        if (currentFile != null) {
                            removeFromLayouts();
                        }
                    }
                });                  
            }
        });
    }
    
    /** */
    public void addLoadLuaSnippet(String snippet) {
        loadLuaSnippets.add(snippet);
    }
    
    /** */
    public static LayoutViewer get() {
        if (instance == null) {
            instance = new LayoutViewer();
        }
        
        return instance;
    }
}