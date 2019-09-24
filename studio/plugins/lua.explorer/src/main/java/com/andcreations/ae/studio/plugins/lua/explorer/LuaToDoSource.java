package com.andcreations.ae.studio.plugins.lua.explorer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.andcreations.ae.studio.plugins.file.LineLocation;
import com.andcreations.ae.studio.plugins.file.cache.TextFileCache;
import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.todo.AbstractToDoFileSource;
import com.andcreations.ae.studio.plugins.todo.ToDoItem;
import com.andcreations.ae.studio.plugins.todo.ToDoItemListener;
import com.andcreations.io.FileNode;
import com.andcreations.io.FileTree;

/**
 * @author Mikolaj Gucki
 */
class LuaToDoSource extends AbstractToDoFileSource {
    /** */
    private LuaToDoSource(File[] files) {
        super(files,true);
    }
    
    /** */
    private ToDoItem match(final File file,String tag,String line,
        final int lineNo) {
    //
        Pattern pattern = Pattern.compile(String.format("\\s*-- +%s (.*)",tag));
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches() == false) {
            return null;
        }
        
        String resource = ProjectLuaFiles.get().getPath(file);
        LineLocation location = new LineLocation(lineNo);
        ToDoItem item = new ToDoItem(
            tag,matcher.group(1),resource,location,file);
        item.addToDoItemListener(new ToDoItemListener() {
            /** */
            @Override
            public void toDoItemDoubleClicked(ToDoItem item) {
                LuaFile.edit(file,lineNo);
            }
        });
        
        return item;
    }
    
    /** */
    @Override
    protected boolean shouldParse(File file) {
        return ProjectLuaFiles.get().isLuaFile(file);
    }
    
    /** */
    @Override
    protected List<ToDoItem> parse(File file,String[] tags)
        throws IOException {
    //
        List<ToDoItem> items = new ArrayList<>();
        String src = TextFileCache.get().read(file);
        BufferedReader reader = new BufferedReader(new StringReader(src));
        int lineNo = 0;
        
    // line by line
        String line;
        while ((line = reader.readLine()) != null) {
            lineNo++;
            line = line.trim();
            
            for (String tag:tags) {
                ToDoItem item = match(file,tag,line,lineNo);
                if (item != null) {
                    items.add(item);
                }
            }
        }
        
        return items;
    }
    
    /** */
    static void create() {
        FileTree tree = ProjectLuaFiles.get().getLuaSourceTree();
        List<FileNode> nodes = tree.flatten();
        List<File> files = new ArrayList<>();
        
    // for each node
        for (FileNode node:nodes) {
            if (node.isFile() == false) {
                continue;
            }
            if (ProjectLuaFiles.getError(node) != null) {
                continue;
            }
            
            files.add(node.getFile());
        }
        
        new LuaToDoSource(files.toArray(new File[]{}));
    }
}