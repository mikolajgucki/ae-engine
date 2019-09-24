package com.andcreations.ae.tex.font;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.andcreations.ae.image.Image;
import com.andcreations.ae.issue.Issue;
import com.andcreations.ae.tex.font.data.TexFontData;
import com.andcreations.ae.tex.font.gen.TexFontGen;
import com.andcreations.ae.tex.font.gen.TexFontGenCfg;
import com.andcreations.ae.tex.font.gen.TexFontSrcGen;
import com.andcreations.ae.tex.font.gen.TexFontSrcGenCfg;
import com.andcreations.io.UpToDate;
import com.andcreations.io.json.JSON;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class TexFont {
    /** */
    public static final String ISSUE_NO_ID = "no.id";
    
    /** */
    public static final String ISSUE_NO_FONT_FILE = "no.font.file";
    
    /** */
    public static final String ISSUE_NO_FONT_SIZE = "no.font.size";
    
    /** */
    public static final String ISSUE_NO_CHARACTERS = "no.characters";
    
    /** */
    public static final String ISSUE_FONT_FILE_NOT_FOUND =
        "font.file.not.found";
    
    /** */
    public static final String ISSUE_FONT_FILE_ERROR = "font.file.error";
    
    /** */
    private BundleResources res = new BundleResources(TexFont.class);
    
    /** */
    private File file;
    
    /** */
    private TexFontCfg cfg;
    
    /** */
    private TexFontListener listener;
    
    /** */
    private TexFontGen gen;
    
    /** */
    public TexFont(File file,TexFontCfg cfg) {
        this.file = file;
        this.cfg = cfg;
    }
    
    /** */
    public void setTexFontListener(TexFontListener listener) {
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
    public File getFontFile() {
        return getRelativeFile(cfg.getData().getFontFile());
    }
    
    /** */
    public List<Issue> getIssues() {
        List<Issue> issues = new ArrayList<>();
        TexFontData data = cfg.getData();
        
    // configuration
        if (StringUtils.isBlank(cfg.getId()) == true) {
            issues.add(Issue.error(ISSUE_NO_ID,res.getStr("no.id"),file));                
        }
        if (StringUtils.isBlank(data.getFontFile()) == true) {
            issues.add(Issue.error(ISSUE_NO_FONT_FILE,
                res.getStr("no.font.file"),file));
        }
        if (data.getSize() <= 0) {
            issues.add(Issue.error(ISSUE_NO_FONT_SIZE,
                res.getStr("no.font.size"),file));
        }
        if (StringUtils.isBlank(data.getCharacters()) == true) {
            issues.add(Issue.error(ISSUE_NO_CHARACTERS,
                res.getStr("no.characters"),file));
        }
        
    // font file
        File fontFile = getFontFile();     
        if (fontFile == null) {
            return issues;
        }
        if (fontFile.exists() == false || fontFile.isFile() == false) {
            issues.add(Issue.error(ISSUE_FONT_FILE_NOT_FOUND,
                res.getStr("font.file.not.found",fontFile.getAbsolutePath()),
                file));
        }
        try {
            loadFont();
        } catch (TexFontException exception) {
            issues.add(Issue.error(ISSUE_FONT_FILE_ERROR,
                exception.getMessage(),file));
        }
    
        return issues;
    }
    
    /** */
    private Font loadFont() throws TexFontException {
        File fontFile = getFontFile();
        Font font;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT,fontFile);
        } catch (Exception exception) {
            throw new TexFontException(res.getStr("failed.to.load.font",
                fontFile.getAbsolutePath(),exception.getMessage()),exception);
        }
        return font;
    }
    
    /** */
    private void createGen() throws TexFontException {
        if (gen != null) {
            return;
        }
        
        TexFontData data = cfg.getData();
        Font font = loadFont();
        
        TexFontGenCfg genCfg = new TexFontGenCfg(cfg.getId(),font,
            data.getSize(),data.getCharacters());
        genCfg.setSpacing(data.getSpacing());
        genCfg.setPadding(data.getPadding());
        gen = new TexFontGen(genCfg);
    }
    
    /** */
    private void generate() throws TexFontException {
        createGen();
        gen.run();
    }
        
    /** */
    private void mkdirs(File newDir) {
        newDir.mkdirs();
        if (listener != null) {
            listener.texFontDirCreated(file,newDir);
        }
    }
    
    /** */
    private void fileCreated(File newFile) {
        if (listener != null) {
            listener.texFontFileCreated(file,newFile);
        }
    }    
    
    /** */
    private void writeRawFile() throws IOException {
        File file = cfg.getRawFile();
        if (file == null) {
            return;
        }
        mkdirs(file.getParentFile());
        
    // write
        Image image = new Image(gen.getRawImage());
        image.savePNG(file);
        fileCreated(file);
    }
    
    /** */
    private void writeMaskFile() throws IOException {
        File file = cfg.getMaskFile();
        if (file == null) {
            return;
        }
        mkdirs(file.getParentFile());
        
    // write
        Image image = new Image(gen.getMaskImage());
        image.savePNG(file);
        fileCreated(file);
    }
    
    /** */
    private void writeDataFile() throws IOException {
        File file = cfg.getDataFile();
        if (file == null) {
            return;
        }
        mkdirs(file.getParentFile());
        
    // write
        JSON.write(file,gen.getResultData());
        fileCreated(file);
    }
    
    /** */
    private void writeLuaFile() throws IOException {
        File file = cfg.getLuaFile();
        if (file == null) {
            return;
        }
        mkdirs(file.getParentFile());
        
    // generate
        TexFontSrcGenCfg srcGenCfg = new TexFontSrcGenCfg(
            cfg.getId(),file.getName(),gen.getResultData());
        TexFontSrcGen srcGen = new TexFontSrcGen(srcGenCfg);
        srcGen.run();
        
    // write
        FileUtils.writeStringToFile(file,srcGen.getSrc(),"UTF-8");
        fileCreated(file);        
    }
    
    /** */  
    private void writeImageFile() throws IOException {
        File file = cfg.getImageFile();
        if (file == null || file.exists() == true) {
            return;
        }
        mkdirs(file.getParentFile());
        
    // write
        Image image = new Image(gen.getRawImage());
        image.savePNG(file);
        fileCreated(file);        
    }
    
    /** */
    private void writeFiles() throws IOException {
        writeRawFile();
        writeMaskFile();
        writeDataFile();
        writeLuaFile();
        writeImageFile();
    }
    
    /** */
    public void run() throws TexFontException,IOException {
        generate();
        writeFiles();
    }
    
    /** */
    public boolean isUpToDate() {
        File[] srcFiles = new File[]{file,getFontFile()};
        File[] dstFiles = new File[]{cfg.getLuaFile(),cfg.getRawFile(),
            cfg.getMaskFile(),cfg.getDataFile()};
        
        return UpToDate.is(srcFiles,dstFiles);
    }
}