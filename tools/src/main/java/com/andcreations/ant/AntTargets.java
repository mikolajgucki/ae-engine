package com.andcreations.ant;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.andcreations.resources.BundleResources;
import com.andcreations.xml.XMLParser;
import com.andcreations.xml.XMLUtil;

/**
 * @author Mikolaj Gucki
 */
public class AntTargets {
    /** */
    private static final String PROJECT = "project";

    /** */
    private static final String NAME = "name";

    /** */
    private static final String DESCRIPTION = "description";
    
    /** */
    private static BundleResources res = new BundleResources(AntTargets.class);  
    
    /** */
    public Map<String,String> listTargets(File baseDir) throws IOException {
    // parse
        File file = new File(baseDir,AntRunner.BUILD_FILE);
        Document document = XMLParser.parse(file);
        
    // project (root)
        Element projectElement = document.getDocumentElement();
        if (PROJECT.equals(projectElement.getNodeName()) == false) {
            throw new IOException(res.getStr("project.element.not.found"));
        }
        
        Map<String,String> targets = new HashMap<String,String>();
    // targets
        Element[] targetElements = XMLUtil.findElements(
            projectElement,"target");
        for (Element targetElement:targetElements) {
        // name
            String name = null;
            if (targetElement.hasAttribute(NAME) == true) {
                name = targetElement.getAttribute(NAME);
             
            // description
                String description = null;
                if (targetElement.hasAttribute(DESCRIPTION) == true) {
                    description = targetElement.getAttribute(DESCRIPTION);
                }
                
                targets.put(name,description);
            }
        }
        
        return targets;
    }
}