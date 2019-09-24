package com.andcreations.ae.sdk.update;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.andcreations.ae.dist.AEDistFiles;
import com.andcreations.ae.project.AEProject;
import com.andcreations.ae.project.AEProject.Assets;
import com.andcreations.ae.project.AEProjectProperties;
import com.andcreations.ae.project.NoSuchVariableException;
import com.andcreations.io.DirScanner;
import com.andcreations.lang.ArrayUtil;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public abstract class SDKUpdater {
    /** */
    protected static final String[] COMMON_SRC_PATHS = {
        "src/lua","src/SDL2","src/ae"
    };
    
    /** */
    private BundleResources res =
        new BundleResources(SDKUpdater.class);
        
    /** */
    private SDKUpdateCfg cfg;
    
    /** */
    private SDKUpdaterListener listener;
    
    /** */
    private Properties properties;    
    
    /** */
    private String pluginsProperty;
    
    /** */
    private Map<String,String> extraVars = new HashMap<String,String>();
    
    /** */
    protected SDKUpdater(SDKUpdateCfg cfg) {
        this.cfg = cfg;
    }
    
    /** */
    protected void setPluginsProperty(String pluginsProperty) {
        this.pluginsProperty = pluginsProperty;
    }
    
    /** */
    public void setSDKUpdaterListener(SDKUpdaterListener listener) {
        this.listener = listener;
    }
    
    /** */
    public SDKUpdaterListener getSDKUpdaterListener() {
        return listener;
    }
    
    /** */
    protected void log(String message) {
        if (listener != null) {
            listener.log(message);
        }
    }
    
    /** */
    protected void notifyCopying(File srcFile,File dstFile) {
        if (listener == null) {
            return;
        }
        listener.copying(srcFile,dstFile);
    }
        
    /** */
    protected void notifyDeleting(File file) {
        if (listener == null) {
            return;
        }
        listener.deleting(file);
    }    
    
    /** */
    protected void notifySkipping(File srcFile,File dstFile) {
        if (listener == null) {
            return;
        }
        listener.skipping(srcFile,dstFile);
    }
        
    /** */
    protected void notifyUpdatingIcon(File file) {
        if (listener == null) {
            return;
        }
        listener.updatingIcon(file);
    }    
    
    /** */
    protected File getAEDistFile(String path) {
        File file =  new File(cfg.getAEDistDir(),path);
        try {
            file = file.getCanonicalFile();
        } catch (IOException exception) {
        }
        
        return file;
    }
    
    /** */
    protected File getProjectFile(String path) {
        File file =  new File(cfg.getProjectDir(),path);
        try {
            file = file.getCanonicalFile();
        } catch (IOException exception) {
        }
        
        return file;
    }
    
    /** */
    private void readProperties() throws IOException {
        properties = new Properties();
        
    // load
        FileInputStream input = new FileInputStream(
            getProjectFile(AEProject.PROPERTIES_FILE));
        properties.load(input);
        input.close();
    } 
    
    /**
     * Gets the value of a project property as read from the configuration file.
     *
     * @param key The property key.
     * @return The property value.
     */
    protected String getProjectRawProperty(String key) {
        return properties.getProperty(key);
    }    
    
    /**
     * Gets the value of a project property with variables replaced.
     *
     * @param key The property key.
     * @return The property value.
     */
    protected String getProjectProperty(String key)
        throws SDKUpdateException {
    //
        String value = getProjectRawProperty(key);
        try {
            value = AEProjectProperties.replaceEnvVariables(value,extraVars);
        } catch (NoSuchVariableException exception) {
            throw new SDKUpdateException(res.getStr("missing.var",
                exception.getName(),key));
        }

        return value;         
    }
    
    /** */
    private void addExtraVars() {
        String pluginsDir = getPluginsDir().getAbsolutePath();
        AEProjectProperties.putLuaPluginsDirs(
            pluginsDir,getPlugins(),extraVars);
    }
    
    /** */
    protected void init() throws IOException,SDKUpdateException {
        readProperties();
        addExtraVars();
    }
    
    /** */
    protected List<FileSet> fileSetsFromProjectProperty(String property)
        throws SDKUpdateException {
    // property value
        String value = getProjectProperty(property);
        
        List<FileSet> fileSets = new ArrayList<>();
    // directories
        String[] dirs = AEProjectProperties.getDirs(value);
        for (String dirStr:dirs) {
            File dir = new File(dirStr);
            if (dir.isAbsolute() == false) {
                dir = getProjectFile(dirStr);
            }
            if (dir.exists() == false) {
                throw new SDKUpdateException(res.getStr(
                    "src.dir.not.exists",dir.getAbsolutePath()));                
            }
            fileSets.add(FileSet.build(dir));
        }       
        
        return fileSets;
    }

    /** */
    protected List<FileSet> fileSetsFromProjectProperties(String... properties)
        throws SDKUpdateException {
    //
        List<FileSet> fileSets = new ArrayList<>();
        for (String property:properties) {
            fileSets.addAll(fileSetsFromProjectProperty(property));
        }
        
        return fileSets;
    }
    
    /** */
    protected FileSetList getAEDistFileSetList(String... paths) {
        FileSetList fileSetList = new FileSetList();        
        for (String path:paths) {
            fileSetList.add(FileSet.build(getAEDistFile(path)));
        }        
        return fileSetList;
    }
    
    /** */
    protected FileSetList getLuaAssetsFileSets() throws SDKUpdateException {        
        FileSetList list = new FileSetList();
        
    // lua
        List<FileSet> luaFileSets = fileSetsFromProjectProperties(
            AEProjectProperties.LUA_SRC_DIRS);
        FileSet.setPrefix(luaFileSets,Assets.LUA_PATH + File.separator);
        list.add(luaFileSets);
        
        return list;
    }
    
    /** */
    protected FileSetList getNonLuaAssetsFileSets() throws SDKUpdateException {
        FileSetList list = new FileSetList();
        
    // assets
        List<FileSet> assetsFileSets = fileSetsFromProjectProperties(
            AEProjectProperties.ASSET_DIRS);
        list.add(assetsFileSets);
        
    // textures
        List<FileSet> texturesFileSets = fileSetsFromProjectProperties(
            AEProjectProperties.TEXTURE_DIRS);
        FileSet.setPrefix(texturesFileSets,
            Assets.TEXTURES_PATH + File.separator);
        list.add(texturesFileSets);

    // sounds
        List<FileSet> soundsFileSets = fileSetsFromProjectProperties(
            AEProjectProperties.SOUND_DIRS);
        FileSet.setPrefix(soundsFileSets,Assets.SOUNDS_PATH + File.separator);
        list.add(soundsFileSets);
        
    // music
        List<FileSet> musicFileSets = fileSetsFromProjectProperties(
            AEProjectProperties.MUSIC_DIRS);
        FileSet.setPrefix(musicFileSets,Assets.MUSIC_PATH + File.separator);
        list.add(musicFileSets);        
        
        return list;
    }
    
    /** */
    private void copyDir(File srcDir,File dstDir,
        FileUpdateStrategy updateStrategy) throws IOException {
    //
        DirScanner scanner = new DirScanner(srcDir,false);
        for (String path:scanner.build()) {
            File srcFile = new File(srcDir,path);
            File dstFile = new File(dstDir,path);
            notifyCopying(srcFile,dstFile);
            updateStrategy.updateFile(srcFile,dstFile);
        }
    }

    /** */
    protected void deleteUnknownFiles(FileSetList fileSetList,File dstDir)
        throws IOException {
    //
        DirScanner scanner = new DirScanner(dstDir);
        for (String path:scanner.build()) {
            if (fileSetList.containsPath(path) == false) {
                File file = new File(dstDir,path);
                if (file.exists() == true) {
                    notifyDeleting(file);
                    FileUtils.forceDelete(file);
                }
            }
        }       
    }
    
    /** */
    protected void updateDir(FileSetList fileSetList,File dstDir,
        FileUpdateStrategy updateStrategy) throws IOException {
    //
        for (FileSet fileSet:fileSetList) {
            for (String path:fileSet.getPaths()) {
                File srcFile = fileSet.getSrcFile(path);
                File dstFile = fileSet.getDstFile(dstDir,path);
                
                if (srcFile.exists() == true && (dstFile.exists() == false ||
                    FileUtils.isFileNewer(srcFile,dstFile) == true)) {
                //
                    if (srcFile.isDirectory() == true) {
                        copyDir(srcFile,dstFile,updateStrategy);
                    }
                    else {
                        notifyCopying(srcFile,dstFile);
                        updateStrategy.updateFile(srcFile,dstFile);
                    }
                    continue;
                }
                if (srcFile.exists() == false && dstFile.exists() == true) {
                    notifyDeleting(dstFile);
                    FileUtils.forceDelete(dstFile);
                    continue;
                }
                
                notifySkipping(srcFile,dstFile);
            }
        }
    }
    
    /** */
    protected String[] getPlugins() {
        if (pluginsProperty == null) {
            throw new IllegalStateException("Plugins property not set");
        }
        
        String propertyValue = getProjectRawProperty(pluginsProperty);
        if (propertyValue == null) {
            return null;
        }
        
        return AEProjectProperties.getPlugins(propertyValue);
    }
    
    /** */
    protected File getPluginsDir() {
        return AEDistFiles.getPluginsDir(cfg.getAEDistDir());
    }
    
    /** */
    protected void checkPluginExists(String plugin) throws SDKUpdateException {
        File aeDistPluginDir = getAEDistFile("plugins/" + plugin);
        if (aeDistPluginDir.exists() == false) {
            throw new SDKUpdateException(res.getStr("unknown.plugin",plugin));
        }
    }
    
    /** */
    protected void checkPluginsExist() throws SDKUpdateException {
        String[] plugins = getPlugins();
        if (plugins != null) {
            for (String plugin:plugins) {
                checkPluginExists(plugin);
            }        
        }        
    }
    
    /** */
    protected void logPlugins() {
        String[] plugins = getPlugins();
        if (plugins != null) {
            log(res.getStr("plugins",ArrayUtil.toListString(plugins,", ")));
        }
        else {
            log(res.getStr("no.plugins"));
        }
    }
    
    /** */
    protected FileSet getPluginFileSet(String plugin,String path,
        FileFilter fileFilter) {
    //
        File dir = getAEDistFile("plugins/" + plugin + "/" + path);
        return FileSet.build(dir,fileFilter);
    }
    
    /** */
    protected FileSetList getPluginsFileSets(String[] plugins,
        FileFilter fileFilter,String... paths) {
    //
        FileSetList fileSetList = new FileSetList();
        
        if (plugins != null) {
            for (String plugin:plugins) {
                for (String path:paths) {
                    fileSetList.add(getPluginFileSet(plugin,path,fileFilter));
                }
            }
        }
        
        return fileSetList;
    }
    
    /** */
    protected FileSetList getPluginsFileSets(String[] plugins,String... paths) {
        return getPluginsFileSets(plugins,null,paths);
    }
    
    /** */
    protected void updateAssets(File dstDir)
        throws IOException,SDKUpdateException {
    // Lua assets
        FileSetList luaAssets = getLuaAssetsFileSets();
        updateDir(luaAssets,dstDir,FileHashStrategy.INSTANCE);        
    
    // non-Lua assets
        FileSetList nonLuaAssets = getNonLuaAssetsFileSets();
        updateDir(nonLuaAssets,dstDir,FileCopyStrategy.INSTANCE);
        
    // all assets
        FileSetList assets = new FileSetList();
        assets.add(luaAssets);
        assets.add(nonLuaAssets);
        
    // remove unknown files
        deleteUnknownFiles(assets,dstDir);
    }
    
    /**
     * Updates a SDK project.
     *
     * @throws IOException on I/O error.
     * @throws SDKUpdateException if update fails.
     */
    public abstract void update() throws IOException,SDKUpdateException;
    
    /**
     * Cleans a SDK project.
     *
     * @throws IOException on I/O error.
     * @throws SDKUpdateException if clean fails.
     */
    public abstract void clean() throws IOException,SDKUpdateException;
}