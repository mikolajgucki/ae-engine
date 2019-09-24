package com.andcreations.xml;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class XMLUtil {
    /** */
    private static BundleResources res = new BundleResources(XMLUtil.class);      
    
    /**
     * Finds all child elements of a specified name.
     *
     * @param element The element whose child nodes to search.
     * @param name The name of the child element.
     * @return The list of the elements of the specified name.
     */
    public static Element[] findElements(Element element,String name) {
        List<Element> list = new ArrayList<>();
        NodeList nodes = element.getChildNodes();
    // for each child
        for (int index = 0; index < nodes.getLength(); index++) {
            Node node = nodes.item(index);
            
            if (node.getNodeType() == Node.ELEMENT_NODE &&
                node.getNodeName().equals(name)) {
            //
                list.add((Element)node);
            }
        }
        
        return (Element[])list.toArray(new Element[]{});
    }    
    
    /**
     * Finds the first child element of a specified name.
     *
     * @param element The element whose child nodes to search.
     * @param name The name of the child element.
     * @return The element of the specified name or null if there is no
     *   such child element.
     * @throws XMLException if there more than 1 such child elements.
     */    
    public static Element findElement(Element element,String name)
        throws XMLException {
    //
        Element[] childElements = findElements(element,name);
        if (childElements.length > 1) {
            throw new XMLException(res.getStr("more.than.one",name));
        }
        if (childElements.length == 0) {
            return null;
        }
        
        return childElements[0];
    }
}