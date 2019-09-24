package com.andcreations.ae.studio.plugins.ui.common.quickopen;

import java.awt.Frame;
import java.util.List;

import javax.swing.JButton;

import com.andcreations.ae.studio.plugins.ui.common.OptionDialog;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class QuickOpenDialog extends OptionDialog
    implements QuickOpenPanelListener {
    /** */
    private List<QuickOpenItem> items;
    
    /** */
    private QuickOpenPanel panel;
    
    /** */
    private QuickOpenItem selectedItem;
    
    /** */
    public QuickOpenDialog(Frame owner,String title,List<QuickOpenItem> items,
        boolean modal) {
    //
        super(owner,title,modal);
        this.items = items;
        create();
    }
    
    /** */
    private void create() {
        panel = new QuickOpenPanel(items,this);
        create(panel,new Option[]{Option.OK,Option.CANCEL});
        setRelativeMinimumSize(32,48);
        makeEscapable();
        
    // disabled as nothing is selected by default
        getButton(Option.OK).setEnabled(false);
    }
    
    /**
     * Gets the matcher.
     *
     * @return The matcher.
     */
    public QuickOpenMatcher getMatcher() {
        return panel.getMatcher();
    }
    
    /**
     * Gets the matcher label.
     *
     * @return The matcher label.
     */
    public String getMatcherLabel() {
        return panel.getMatcherLabel();
    }
    
    /**
     * Sets the matcher.
     *
     * @param label The label displayed above the pattern field.
     * @param matcher The matcher.
     */
    public void setMatcher(String label,QuickOpenMatcher matcher) {
        panel.setMatcher(label,matcher);
    }
    
    /** */
    @Override
    public void quickOpenItemSelected(QuickOpenItem item) {
        JButton ok = getButton(Option.OK);
        ok.setEnabled(item != null);
        
        selectedItem = item;
    }
    
    /** */
    @Override
    public void quickOpenItemPicked(QuickOpenItem item) {
        close(getButton(Option.OK));
    }
    
    /** */
    @Override
    public Option showOptionDialog() {
        Option option = super.showOptionDialog();
        if (option != Option.OK) {
            selectedItem = null;
        }
        
        return option;
    }
    
    /** */
    public QuickOpenItem getSelectedItem() {
        return selectedItem;
    }
}