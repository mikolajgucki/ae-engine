package com.andcreations.ae.studio.plugins.ui.common.settings;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.andcreations.ae.studio.plugins.ui.common.OptionDialog;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTree;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeNode;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeNodeAdapter;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class SettingsDialog extends OptionDialog {
    /** */
    private String title;
    
    /** */
    private LabelTree pageTree;
    
    /** */
    private JLabel pageTitle;
    
    /** */    
    private JPanel pagePanel;
    
    /** */
    private List<SettingsPage> pages = new ArrayList<>();
    
    /** */
    private Map<SettingsPage,LabelTreeNode> pageNodeMap =
        new HashMap<SettingsPage,LabelTreeNode>();
    
    /** */
    private SettingsPage currentPage;
    
    /** */
    private SettingsContext context;
        
    /** */
    public SettingsDialog(Frame owner,String title) {
        super(owner,true);
        this.title = title;
        createContext();        
    }
    
    /** */
    private void createContext() {
        context = new SettingsContext(this);
    }
    
    /** */
    public void create() {
        setTitle(title);
        setRelativeMinimumSize(48,48);
        setRelativePreferredSize(48,48);
        setResizable(false);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
    // page tree
        pageTree = new LabelTree(buildRootNode());
        pageTree.setRootVisible(false);
        JScrollPane treeScroll = new JScrollPane(pageTree);        
        panel.add(treeScroll,BorderLayout.WEST);

    // page panel
        pagePanel = new JPanel();
        pagePanel.setLayout(new BorderLayout());
        panel.add(pagePanel,BorderLayout.CENTER);
        
    // page title
        pageTitle = new JLabel();
        pagePanel.add(pageTitle,BorderLayout.NORTH);
        
    // borders
        final int size = pageTitle.getFont().getSize() / 2;
        pageTitle.setBorder(BorderFactory.createEmptyBorder(0,0,size,0));
        pagePanel.setBorder(BorderFactory.createEmptyBorder(0,size,size,size));
        treeScroll.setBorder(BorderFactory.createCompoundBorder(
            treeScroll.getBorder(),
            BorderFactory.createEmptyBorder(size,size,size,size)));
        
    // title font
        pageTitle.setFont(pageTitle.getFont().deriveFont(Font.BOLD,
            (float)(pageTitle.getFont().getSize() * 1.5)));
        
    // create dialog
        create(panel,new Option[]{Option.OK,Option.CANCEL});
    }
    
    /** */
    private void pageSelected(SettingsPage page) {
        if (currentPage != null) {
            pagePanel.remove(currentPage.getComponent());
        }
        currentPage = page;        
        page.update();
        
        pageTitle.setText(page.getTitle());
        pagePanel.add(page.getComponent(),BorderLayout.CENTER);
        page.getComponent().repaint();
    }
    
    /** */
    private LabelTreeNode createTreeNode(final SettingsPage page) {
        ImageIcon icon = Icons.getIcon(page.getIconName());
        LabelTreeNode node = new LabelTreeNode(icon,page.getTitle());
        node.addLabelTreeNodeListener(new LabelTreeNodeAdapter() {
            /** */
            @Override
            public void labelTreeNodeSelected(LabelTreeNode node) {
                pageSelected(page);
            }
        });
        
        return node;
    }
    
    /** */
    private LabelTreeNode buildRootNode() {
        LabelTreeNode root = new LabelTreeNode(null,title);
        
    // sort pages
    	Collections.sort(pages,new Comparator<SettingsPage>() {
    		/** */
			@Override
			public int compare(SettingsPage a, SettingsPage b) {
				return a.getTitle().compareTo(b.getTitle());
			}
    	});
        
        for (SettingsPage page:pages) {
            LabelTreeNode node = createTreeNode(page);
            pageNodeMap.put(page,node);
            root.add(node);
        }            
        
        return root;
    }
    
    /** */
    void setPageError(SettingsPage page) {
        LabelTreeNode node = pageNodeMap.get(page);
        node.setIcon(Icons.getIcon(page.getIconName(),DefaultIcons.DECO_ERROR));
        pageTree.repaint();
    }
    
    /** */
    void clearPageIssues(SettingsPage page) {
        LabelTreeNode node = pageNodeMap.get(page);
        node.setIcon(Icons.getIcon(page.getIconName()));
        pageTree.repaint();
    }
    
    /** */
    private void apply() {
        for (SettingsPage page:pages) {
            page.apply();
        }
    }
    
    /** */
    public void addPage(SettingsPage page) {
        pages.add(page);
        page.setSettingsContext(context);
    }
    
    /** */
    public void showSettingsDialog() {
        for (SettingsPage page:pages) {
            page.update();            
        }
        
        if (showOptionDialog() == Option.OK) {
            apply();
        }
    }
}