package com.andcreations.ae.studio.plugins.text.editor.autocomplete;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.fife.ui.autocomplete.AbstractCompletion;
import org.fife.ui.autocomplete.CompletionProvider;

/**
 * @author Mikolaj Gucki
 */
public abstract class AbstractAutocompl implements Autocompl,TECompletion {
    /** */
    private String name;
    
    /** */
    private String lowerCaseName;
    
    /** */
    private String lowerCaseFullName;
    
    /** */
    private String prefix;
    
    /** */
    private String lowerCasePrefix;
    
    /** */
    private String displayText;
    
    /** */
    private String displayHtml;
    
    /** */
    private String replacementText;
    
    /** */
    private String description;
    
    /** */
    private String definedIn;    
    
    /** */
    private String definitionString;
    
    /** */
    private ImageIcon icon;
    
    /** */
    private AbstractCompletion completion;
    
    /** */
    protected AbstractAutocompl(String name) {
        this(null,name);
    }
    
    /** */
    protected AbstractAutocompl(String name,String prefix) {
        setName(name);
        setPrefix(prefix);
    }
    
    /** */
    private void setName(String name) {
        this.name = name;
        this.lowerCaseName = name.toLowerCase();
        updateLowerCaseFullName();
    }
    
    /** */
    public String getName() {
        return name;
    }
    
    /** */
    @Override
    public String getLowerCaseName() {
        if (lowerCaseName == null) {
            lowerCaseName = name.toLowerCase();            
        }
        return lowerCaseName;
    }

    /** */
    private void setPrefix(String prefix) {
        this.prefix = prefix;
        this.lowerCasePrefix = prefix.toLowerCase();
        updateLowerCaseFullName();
    }

    /** */
    public String getPrefix() {
        return prefix;
    }
    
    /** */
    @Override
    public String getLowerCasePrefix() {
        if (lowerCasePrefix == null) {
            lowerCasePrefix = prefix.toLowerCase();
        }
        return lowerCasePrefix;
    }
    
    /** */
    private void updateLowerCaseFullName() {
        if (name == null || prefix == null) {
            return;
        }
        lowerCaseFullName = getLowerCasePrefix() + getLowerCaseName();
    }
    
    /** */
    @Override
    public String getLowerCaseFullName() {
        if (lowerCaseFullName == null) {
            updateLowerCaseFullName();
        }
        return lowerCaseFullName;
    }
    
    /** */
    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }
    
    /** */
    @Override
    public String getDisplayText() {
        return displayText;
    }
    
    /** */
    public void setDisplayHTML(String displayHtml) {
        this.displayHtml = displayHtml;
    }
    
    /** */
    @Override
    public String getDisplayHTML() {
        return displayHtml;
    }    
    
    /** */
    public void setReplacementText(String replacementText) {
        this.replacementText = replacementText;
    }
    
    /** */
    public String getReplacementText() {
        return replacementText;
    }
    
    /** */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /** */
    public String getDescription() {
        return description;
    }    
    
    /** */
    public String getDefinedIn() {
        return definedIn;
    }
    
    /** */
    public void setDefinedIn(String definedIn) {
        this.definedIn = definedIn;
    }    
    
    /** */
    public void setDefinitionString(String definitionString) {
        this.definitionString = definitionString;
    }
    
    /** */
    public String getDefinitionString() {
        return definitionString;
    }
    
    /** */
    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }
    
    /** */
    @Override
    public Icon getIcon() {
        return icon;
    }
    
    /** */
    abstract AbstractCompletion createCompletion(CompletionProvider provider);
    
    /** */
    @Override
    public AbstractCompletion getCompletion(CompletionProvider provider) {
        if (completion == null) {
            completion = createCompletion(provider);
        }
        return completion;
    }
}