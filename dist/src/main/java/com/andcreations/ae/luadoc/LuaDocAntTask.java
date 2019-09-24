package com.andcreations.ae.luadoc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;

import com.andcreations.ant.AETask;
import com.andcreations.ant.AntPath;
import com.andcreations.ant.FileSetHelper;
import com.andcreations.resources.BundleResources;
import com.andcreations.resources.StrResources;

/**
 * Generates Lua API documentation from a set of source files.
 *
 * @author Mikolaj Gucki
 */
public class LuaDocAntTask extends AETask {
    /** The string resources. */
    private StrResources res = new BundleResources(LuaDocAntTask.class); 
    
    /** The source file set. */
    private FileSet srcFileSet;
    
    /** The destination directory. */
    private AntPath dstDir;
    
    /** The path to the configuration file. */
    private AntPath cfgFile;
    
    /** */
    public FileSet createSrcFiles() {
        if (srcFileSet != null) {
            duplicatedElement("srcfiles");
        }
        
        srcFileSet = new FileSet();
        return srcFileSet;
    }
    
    /** */
    public AntPath createDstDir() {
        if (dstDir != null) {
            duplicatedElement("dstdir");
        }
        
        dstDir = new AntPath();
        return dstDir;
    }
    
    /** */
    public AntPath createCfgFile() {
        if (cfgFile != null) {
            duplicatedElement("cfgfile");           
        }
        
        cfgFile = new AntPath();
        return cfgFile;
    }
    
    /** */
    private String getCfgProperty(Properties properties,String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new BuildException(res.getStr("missing.cfg.property",key));
        }
        
        return value;
    }
    
    /**
     * Loads the configuration.
     */
    private LuaDocGeneratorCfg loadCfg() {
        File file = new File(cfgFile.getPath());
        Properties properties = new Properties();
        
        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
            properties.load(input);
        } catch (IOException exception) {
            throw new BuildException(res.getStr("failed.to.load.cfg",
                file.getAbsolutePath(),exception.getMessage()));                
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException exception) {
                    throw new BuildException(res.getStr("failed.to.load.cfg",
                        file.getAbsolutePath(),exception.getMessage()));                
                }
            }
        }
        
        LuaDocGeneratorCfg cfg = new LuaDocGeneratorCfg();
        cfg.setTitle(getCfgProperty(properties,"title"));
        
        return cfg;
    }
    
    /**
     * Parses source files.
     *
     * @param files The files to parse.
     * @return The parsed modules.
     */
    private List<LuaDocModule> parse(File[] files) {
        boolean failure = false;
        List<LuaDocModule> modules = new ArrayList<LuaDocModule>();
        
        for (File file:files) {
            verbose(res.getStr("parsing.file",file.getAbsolutePath()));
            List<String> src;
        // read
            try {
                src = FileUtils.readLines(file,"UTF-8");
            } catch (IOException exception) {
                error(res.getStr("failed.to.read.file",
                    file.getAbsolutePath(),exception.getMessage()));
                failure = true;
                continue;
            }
            
        // parse
            LuaDocParser parser = new LuaDocParser();
            List<LuaDocModule> fileModules;
            try {
                fileModules = parser.parse(
                    file.getName(),src.toArray(new String[]{}));
            } catch (LuaDocException exception) {
                error(res.getStr("failed.to.parse.file",
                    file.getAbsolutePath(),exception.getMessage()));
                failure = true;
                continue;
            }
            modules.addAll(fileModules);
        }
        
        if (failure == true) {
            throw new BuildException(res.getStr("failed.to.parse.files"));
        }
        
        return modules;
    }
    
    /**
     * Validates the data.
     *
     * @param data The data.
     */
    private void validate(LuaDocData data) {
        try {
            data.validateSuperModules();
        } catch (LuaDocException exception) {
            throw new BuildException(exception.getMessage());
        }
    }
    
    /**
     * Merges modules.
     *
     * @param modules The modules to merge.
     */
    private void mergeModules(List<LuaDocModule> modules) {
        try {
            LuaDocModuleMerge.merge(modules);
        } catch (LuaDocException exception) {
            throw new BuildException(exception.getMessage());
        }
    }
    
    /**
     * Copies the necessary files to the destination directory.
     */
    private void copyFiles() {
        try {
            LuaDocGenerator.copyFiles(new File(dstDir.getPath()));
        } catch (IOException exception) {
            throw new BuildException(exception.getMessage());
        }
    }
    
    /**
     * Writes a document to a file.
     *
     * @param doc The document.
     * @param fileName The destination file name.
     */
    private void writeDoc(String doc,String fileName) {
        File dstFile = new File(dstDir.getPath(),fileName);
        
    // write
        try {
            FileUtils.write(dstFile,doc,"UTF-8");
        } catch (IOException exception) {
            throw new BuildException(res.getStr("failed.to.write.doc.file",
                dstFile.getAbsolutePath(),exception.getMessage()));
        }        
    }
    
    /**
     * Generates the documentation from the modules.
     *
     * @param data The parsed data.
     * @param cfg The generator configuration.     
     */
    private void generateDoc(LuaDocData data,LuaDocGeneratorCfg cfg) {
        LuaDocGenerator docGenerator = new LuaDocGenerator(cfg);
        
        for (LuaDocModule module:data.getModules()) {
            String moduleDoc;
            try {
                moduleDoc = docGenerator.generateModuleDoc(module,data);
            } catch (LuaDocException exception) {
                throw new BuildException(exception.getMessage());
            }
            writeDoc(moduleDoc,module.getName() + ".html");
        }
        
    // index
        String indexDoc;
        try {
            indexDoc = docGenerator.generateIndexDoc(data);
        } catch (LuaDocException exception) {
            throw new BuildException(exception.getMessage());
        }
        writeDoc(indexDoc,"index.html");
    }
    
    /** */
    @Override
    public void execute() {
        verifyElementSet(srcFileSet,"srcfiles");
        verifyElementSet(dstDir,"dstdir");
        verifyElementSet(cfgFile,"cfgfile");
        
        LuaDocGeneratorCfg cfg = loadCfg();
        File[] srcFiles = FileSetHelper.getFiles(srcFileSet);
        List<LuaDocModule> modules = parse(srcFiles);
        mergeModules(modules);
        LuaDocData data = new LuaDocData(modules);
        validate(data);
        generateDoc(data,cfg);
        copyFiles();
        
    // log
        File dir = new File(dstDir.getPath());
        log(res.getStr("processed.files",Integer.toString(srcFiles.length),
            dir.getAbsolutePath()));
    }
}