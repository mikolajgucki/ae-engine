package com.andcreations.ae.lua.doc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class LuaDocFileDataList {
    /** */
    private List<LuaDocFileData> dataList = new ArrayList<>();
    
    /** */
    public void addFileData(LuaDocFileData data) {
        dataList.add(data);
    }
    
    /** */
    public List<LuaDocFileData> getDataList() {
        return Collections.unmodifiableList(dataList);
    }
}