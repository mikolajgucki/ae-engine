package com.andcreations.ae.studio.plugins.ui.common.settings;

import javax.swing.JLabel;

import com.andcreations.ae.studio.plugins.ui.common.UIFonts;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;

/**
 * @author Mikolaj Gucki
 */
public class SettingsIssueLabelWrapper {
    /** */
    public static enum Issue {
        /** */
        WARNING(DefaultIcons.WARNING_SMALL),
        
        /** */
        ERROR(DefaultIcons.ERROR_SMALL);
        
        /** */
        private String iconName;
        
        /** */
        private Issue(String iconName) {
            this.iconName = iconName;
        }
        
        /** */
        public String getIconName() {
            return iconName;
        }
    }
    
    /** */
    private JLabel label;
    
    /** */
    private SettingsIssueLabelWrapperListener listener;
    
    /** */
    private Issue issue;
    
    /** */
    public SettingsIssueLabelWrapper(JLabel label,
        SettingsIssueLabelWrapperListener listener) {
    //
        this.label = label;
        this.listener = listener;
        create();
    }
    
    /** */
    private void create() {
        label.setFont(UIFonts.getSmallFont(label.getFont()));
    }
    
    private void setIssue(Issue newIssue) {
        if (issue == newIssue) {
            return;
        }
        
        issue = newIssue;
        if (listener != null) {
            listener.issueChanged(this);
        }
    }
    
    /** */
    public void setIssue(Issue issue,String msg) {
        label.setText(msg);
        label.setIcon(Icons.getIcon(issue.getIconName()));
        setIssue(issue);
    }
    
    /** */
    public void clear() {
        label.setText(" ");
        label.setIcon(null);
        setIssue(null);
    }
    
    /** */
    public boolean hasError() {
        return issue == Issue.ERROR;
    }
    
    /** */
    public boolean hasWarning() {
        return issue == Issue.WARNING;
    }
}