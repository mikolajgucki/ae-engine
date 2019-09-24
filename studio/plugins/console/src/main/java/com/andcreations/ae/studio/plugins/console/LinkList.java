package com.andcreations.ae.studio.plugins.console;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
class LinkList {
    /** */
    private List<Link> links = new ArrayList<>();
    
    /** */
    synchronized void clear() {
        links.clear();
    }
    
    /** */
    synchronized void add(Link link) {
        links.add(link);
    }
    
    /** */
    synchronized Link find(int position) {
        for (Link link:links) {
            if (link.inside(position) == true) {
                return link;
            }
        }
        return null;
    }
}
