package com.andcreations.ae.studio.plugins.text.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Manages a list of gutter icons related to a single editor.
 *
 * @author Mikolaj Gucki
 */
class GutterIconList {
    /** */
    private List<GutterIcon> icons = new ArrayList<>();
    
    /** */
    private GutterIconListListener listener;
    
    /** */
    GutterIconList(GutterIconListListener listener) {
        this.listener = listener;
    }
    
    /** */
    void add(GutterIcon icon) {
        icons.add(icon);
    }
    
    /** */
    void remove(GutterIcon icon) {
        icons.remove(icon);
    }
    
    /** */
    private void sortByPriority(List<GutterIcon> iconsToSort) {
        Collections.sort(iconsToSort,new Comparator<GutterIcon>() {
            /** */
            @Override
            public boolean equals(Object obj) {
                return this == obj;
            }
            
            /** */
            public int compare(GutterIcon a,GutterIcon b) {
                return a.getPriority().getValue() - b.getPriority().getValue();
            }
        });
    }
    
    /** */
    List<GutterIcon> getGutterIcons() {
        return icons;
    }
    
    /** */
    List<GutterIcon> getGutterIcons(int line) {
        List<GutterIcon> lineIcons = new ArrayList<>();
        for (GutterIcon icon:icons) {
            if (icon.getLine() == line) {
                lineIcons.add(icon);
            }
        }
        
        sortByPriority(lineIcons);
        return lineIcons;
    }
    
    /** */
    Set<Integer> getLines() {
        Set<Integer> lines = new HashSet<>();
        for (GutterIcon icon:icons) {
            lines.add(Integer.valueOf(icon.getLine()));
        }
            
        return lines;
    }
    
    /** */
    void deduplicate() {
        while (true) {
            GutterIcon toRemove = null;
        // find duplicates
            OuterLoop:
            for (int ia = 0; ia < icons.size(); ia++) {
                GutterIcon a = icons.get(ia);
                for(int ib = ia + 1; ib < icons.size(); ib++) {
                    GutterIcon b = icons.get(ib);
                    if (a.getLine() == b.getLine() &&
                        a.getIconName().equals(b.getIconName()) == true) {
                    //
                        toRemove = a;
                        break OuterLoop;
                    }
                }
            }
            
            if (toRemove == null) {
                break;
            }
            icons.remove(toRemove);
            listener.removedGutterIcon(toRemove);
        }
    }
} 