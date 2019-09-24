package com.andcreations.ae.tex.pack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.andcreations.ae.issue.Issue;
import com.andcreations.ae.tex.pack.data.TexImage;
import com.andcreations.ae.tex.pack.data.TexPack;
import com.andcreations.ae.tex.pack.data.TexPath;
import com.andcreations.ae.tex.pack.gen.TexPackGen;
import com.andcreations.ae.tex.pack.gen.TexPackGenCfg;
import com.andcreations.ae.tex.pack.gen.TexPackSrcGen;
import com.andcreations.ae.tex.pack.gen.TexPackSrcGenCfg;
import com.andcreations.io.UpToDate;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class TexPacker {
    /** */
    public static final String ISSUE_NO_DATA = "no.data";
    
    /** */
    public static final String ISSUE_NO_DIRS = "no.dirs";
    
    /** */
    public static final String ISSUE_NO_DIR = "no.dir";
    
    /** */
    public static final String ISSUE_NOT_A_DIR = "not.a.dir";
    
    /** */
    public static final String ISSUE_NO_IMAGE_FILE = "no.image.file";    
        
    /** */
    public static final String ISSUE_NO_IMAGES = "no.images";        
    
    /** */
    public static final String ISSUE_IMAGE_WITHOUT_ID = "image.without.id";    
    
    /** */
    public static final String ISSUE_UNKNOWN_IMAGE = "unknown.image";    
    
    /** */
    private BundleResources res = new BundleResources(TexPacker.class);    
    
    /** */
    private File file;    
    
    /** */
    private TexPackerCfg cfg;

    /** */
    private TexPackerListener listener;
    
    /** */
    private TexPackGen gen;
    
    /** */
    public TexPacker(File file,TexPackerCfg cfg) {
        this.file = file;
        this.cfg = cfg;
        createGen();
    }
        
    /** */
    private void createGen() {
        if (gen != null) {
            return;
        }
        if (cfg.getData() == null || cfg.getData().getDirs() == null) {
            return;
        }
        
        List<File> dirs = new ArrayList<>();
        for (TexPath path:cfg.getData().getDirs()) {
            dirs.add(getRelativeFile(path.getPath()));
        }
        
        TexPackGenCfg genCfg = new TexPackGenCfg(cfg,dirs);
        gen = new TexPackGen(genCfg);
    }    
    
    /** */
    public void setTexPackerListener(TexPackerListener listener) {
        this.listener = listener;
    }
    
    /** */
    private File getRelativeFile(String path) {
        if (path == null) {
            return null;
        }
        return new File(file.getParentFile(),path);
    }    
    
    /** */
    public List<Issue> getIssues() {
        List<Issue> issues = new ArrayList<>();
        
    // configuration
        if (cfg.getData() == null) {
            issues.add(Issue.error(ISSUE_NO_DATA,res.getStr("no.data"),file));            
        }
        else {
            TexPack data = cfg.getData();
            if (data.getDirs() == null || data.getDirs().size() == 0) {
                issues.add(Issue.error(ISSUE_NO_DIRS,
                    res.getStr("no.dirs"),file));              
            }
            else {
            // directories
                for (TexPath path:data.getDirs()) {
                    File dir = getRelativeFile(path.getPath());
                    if (dir.exists() == false) {
                        issues.add(Issue.error(ISSUE_NO_DIR,
                            res.getStr("no.dir",path.getPath()),file));
                        continue;
                    }
                    if (dir.isDirectory() == false) {
                        issues.add(Issue.error(ISSUE_NOT_A_DIR,
                            res.getStr("not.a.dir",path.getPath()),file));
                        continue;
                    }
                }            
            }
        }
        if (cfg.getImageFile() == null) {
            issues.add(Issue.error(ISSUE_NO_IMAGE_FILE,
                res.getStr("no.image.file"),file));
        }
        
        issues.addAll(getImageIssues());
        return issues;
    }
    
    /** */
    private List<Issue> getImageIssues() {
        List<Issue> issues = new ArrayList<>();
        TexPack data = cfg.getData();
        if (data == null || gen == null) {
            return issues;
        }        
        
        List<File> files = gen.listImageFiles();
    // any files to pack?
        if (files.isEmpty() == true) {
            issues.add(Issue.error(ISSUE_NO_IMAGES,res.getStr("no.images")));            
        }
        
    // no additional image info
        if (data.getImages() == null) {
            return issues;
        }
        
    // image identifiers
        List<String> ids = new ArrayList<>();
        for (File file:files) {
            ids.add(TexPackGen.getImageId(file));
        }
        
    // additional image info
        for (TexImage texImage:data.getImages()) {
            if (texImage.getId() == null) {
                issues.add(Issue.error(ISSUE_IMAGE_WITHOUT_ID,
                    res.getStr("image.without.id")));
            }            
            if (texImage.getId() != null &&
                ids.contains(texImage.getId()) == false) {
            //
                issues.add(Issue.error(ISSUE_UNKNOWN_IMAGE,
                    res.getStr("unknown.image",texImage.getId())));
            }
        }
        
        return issues;
    }
    
    /** */
    private void generate() throws IOException,TexPackException {
        gen.run();
    }
    
    /** */
    private void mkdirs(File newDir) {
        newDir.mkdirs();
        if (listener != null) {
            listener.texPackDirCreated(file,newDir);
        }
    }
    
    /** */
    private void fileCreated(File newFile) {
        if (listener != null) {
            listener.texPackFileCreated(file,newFile);
        }
    }
    
    /** */
    private void writeImageFile() throws IOException {
        File imageFile = cfg.getImageFile();
        mkdirs(imageFile.getParentFile());
        
    // write
        gen.getTextureImage().savePNG(imageFile);
        fileCreated(imageFile);
    }    
    
    /** */
    private void writeLuaFile() throws IOException {
        File luaFile = cfg.getLuaFile();
        if (luaFile == null) {
            return;
        }
        mkdirs(luaFile.getParentFile());
        
        File imageFile = cfg.getImageFile();
    // generate
        TexPackSrcGenCfg srcGenCfg = new TexPackSrcGenCfg(
            cfg.getId(),imageFile.getName(),luaFile.getName(),
            gen.getResultData());
        TexPackSrcGen srcGen = new TexPackSrcGen(srcGenCfg);
        srcGen.run();
        
    // write
        FileUtils.writeStringToFile(luaFile,srcGen.getSrc(),"UTF-8");
        fileCreated(luaFile);        
    }
    
    /** */
    private void writeFiles() throws IOException {
        writeImageFile();
        writeLuaFile();
    }
    
    /** */
    public void run() throws IOException,TexPackException {
        generate();
        writeFiles();
    }
    
    /** */
    public boolean isUpToDate() {
        List<File> srcFiles = new ArrayList<>();
        srcFiles.add(file);
        srcFiles.addAll(gen.listImageFiles());
        
        File[] dstFiles = new File[]{cfg.getImageFile(),cfg.getLuaFile()};        
        return UpToDate.is(srcFiles,dstFiles);
    }
}