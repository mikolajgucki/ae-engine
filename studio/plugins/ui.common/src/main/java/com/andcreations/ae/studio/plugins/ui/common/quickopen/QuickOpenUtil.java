package com.andcreations.ae.studio.plugins.ui.common.quickopen;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Mikolaj Gucki 
 */
public class QuickOpenUtil {
    /** */
    public static void sortBySearchValue(List<QuickOpenItem> items) {
        Collections.sort(items,new Comparator<QuickOpenItem>() {
            /** */
            @Override
            public boolean equals(Object obj) {
                return obj == this;
            }
            
            /** */
            @Override
            public int compare(QuickOpenItem a,QuickOpenItem b) {
                return a.getSearchValue().compareToIgnoreCase(
                    b.getSearchValue());
            }
        });
    }
}