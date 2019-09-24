package com.andcreations.ae.studio.plugins.console;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import com.andcreations.ae.studio.plugins.search.common.EqualsSearchMatcher;
import com.andcreations.ae.studio.plugins.search.common.SearchException;
import com.andcreations.ae.studio.plugins.search.common.SearchOccurence;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class FindPanel extends JPanel {
    /** */
    private final BundleResources res = new BundleResources(FindPanel.class);     
    
    /** */
    private JTextField findTextField;
    
    /** */
    private String previousFindText;
    
    /** */
    private JCheckBox matchCaseBox;
    
    /** */
    private JButton findButton;
    
    /** */
    private JButton previousButton;
    
    /** */
    private JButton nextButton;
    
    /** */
    private JLabel countLabel;
    
    /** */
    private EqualsSearchMatcher matcher;
    
    /** */
    private TextPaneSearchEngine engine;
    
    /** */
    private List<SearchOccurence> occurences;
    
    /** */
    private int currentIndex;
    
    /** */
    FindPanel(JTextPane textPane) {
        create(textPane);
    }
    
    /** */
    private void create(JTextPane textPane) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(null);
    
    // text field
        findTextField = new JTextField(20);
        findTextField.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                find(false);
            }
        });  
        add(findTextField);
        
    // match case
        matchCaseBox = new JCheckBox(res.getStr("match.case"));
        add(matchCaseBox);
        
    // find
        findButton = new JButton(res.getStr("find"));
        findButton.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                find(true);
            }
        });         
        add(findButton);            
        
    // previous
        previousButton = new JButton(res.getStr("previous"));
        previousButton.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                goToPreviousOccurence();
            }
        });         
        add(previousButton);        
        
    // next
        nextButton = new JButton(res.getStr("next"));
        nextButton.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                goToNextOccurence();
            }
            
        });         
        add(nextButton);        
        
    // count
        countLabel = new JLabel();
        add(countLabel);
        
    // engine
        matcher = new EqualsSearchMatcher();
        engine = new TextPaneSearchEngine(textPane);
    }
    
    /** */
    void clear() {
        countLabel.setText("");
        engine.clear();
        occurences = null;
    }
    
    /** */
    private void find(boolean force) {
        String findText = findTextField.getText();
        if (findText.length() == 0) {
            clear();
            return;
        }
        
        if (force == false) {
            if (previousFindText != null && previousFindText.equals(findText)) {
                goToNextOccurence();
                return;
            }
        }
        previousFindText = findText;
        
        try {
            matcher.init(matchCaseBox.isSelected(),findText);
        } catch (SearchException exception) {
            // TODO Show error dialog.
        }
        
    // search
        occurences = engine.search(matcher);
        engine.highlight(occurences);
        
    // go to the first occurence
        currentIndex = 0;
        goToCurrentOccurence();
        
    // count label
        updateCountLabel();
    }
    
    /** */
    private void updateCountLabel() {
        if (occurences.isEmpty() == false) {
            countLabel.setText(res.getStr("count.n",
                Integer.toString(currentIndex + 1),
                Integer.toString(occurences.size())));            
        }
        else {
            countLabel.setText(res.getStr("count.0"));
        }
    }
    
    /** */
    private void goToCurrentOccurence() {
        if (occurences.isEmpty() == true) {
            return;
        }
        if (currentIndex < 0 || currentIndex >= occurences.size()) {
            throw new IllegalStateException("Invalid index");
        }
        engine.goTo(occurences.get(currentIndex));
        updateCountLabel();
    }
    
    /** */
    private void goToPreviousOccurence() {
        if (occurences == null) {
            return;
        }
        
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = occurences.size() - 1;
        }
        goToCurrentOccurence();        
    }
    
    /** */
    private void goToNextOccurence() {
        if (occurences == null) {
            return;
        }
        
        currentIndex++;
        if (currentIndex >= occurences.size()) {
            currentIndex = 0;
        }
        goToCurrentOccurence();
    }
    
    /** */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible == true) {
            findTextField.requestFocus();
            findTextField.selectAll();
        }
        else {
            clear();
        }
    }
}