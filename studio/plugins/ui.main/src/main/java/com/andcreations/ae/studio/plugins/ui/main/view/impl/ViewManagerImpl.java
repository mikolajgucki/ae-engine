package com.andcreations.ae.studio.plugins.ui.main.view.impl;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.KeyStroke;

import org.apache.commons.lang3.StringUtils;

import bibliothek.gui.DockController;
import bibliothek.gui.DockFrontend;
import bibliothek.gui.DockStation;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.StackDockStation;
import bibliothek.gui.dock.action.DefaultDockActionSource;
import bibliothek.gui.dock.action.LocationHint;
import bibliothek.gui.dock.action.actions.SeparatorAction;
import bibliothek.gui.dock.control.DockableSelector;
import bibliothek.gui.dock.control.focus.DefaultFocusRequest;
import bibliothek.gui.dock.event.DockableFocusEvent;
import bibliothek.gui.dock.event.DockableFocusListener;
import bibliothek.gui.dock.layout.DockableProperty;
import bibliothek.gui.dock.station.split.SplitDockGrid;
import bibliothek.gui.dock.util.DockUtilities;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugins.ui.common.UIKeys;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.StatusBar;
import com.andcreations.ae.studio.plugins.ui.main.view.View;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewFactory;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewManager;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewManagerListener;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewProvider;
import com.andcreations.base64.Base64;

/**
 * The view manager implementation based on the Docking Frames library. 
 * 
 * @author Mikolaj Gucki
 */
public class ViewManagerImpl implements ViewManager {
    /** */
    private static final String DOCK_FRONTEND_ROOT_ID = "ae_studio_root";
    
    /** */
    private static final String DOCK_FRONTEND_LAYOUT_ID = "ae_studio_layout";
    
    /** The manager listeners. */
    private List<ViewManagerListener> listeners = new ArrayList<>();
    
    /** The registered view factories. */
    private List<ViewFactory> viewFactories = new ArrayList<>();
    
    /** The dock controller. */
    private DockController dockController;
    
    /** */
    private SplitDockStation dockStation;
    
    /** The dock frontend. */
    private DockFrontend dockFrontend;
    
    /** The views. */
    private Set<ViewImpl> views = new HashSet<>();
    
    /** The view manager state. */
    private ViewManagerImplState state;
    
    /** The view providers. */
    private List<ViewProvider> viewProviders = new ArrayList<>();
    
    /** */
    public ViewManagerImpl() {
        create();
    }
    
    /** */
    private void create() {        
    // controller
        dockController = new DockController();

    // disable the default dockable selection (ctrl+shift+e)
        dockController.getProperties().unset(DockableSelector.INIT_SELECTION);
        
    // maximize accelerator
        dockController.getProperties().set(
            SplitDockStation.MAXIMIZE_ACCELERATOR,
            KeyStroke.getKeyStroke(KeyEvent.VK_M,UIKeys.menuKeyMask()));
        
    // focus listener
        dockController.addDockableFocusListener(new DockableFocusListener() {
            /** */
            @Override
            public void dockableFocused(DockableFocusEvent event) {
                StatusBar.get().setNoInfo();
                
                Dockable oldFocusOwner = event.getOldFocusOwner();
                ViewImpl oldViewImpl = findViewImpl(oldFocusOwner);
                if (oldViewImpl != null) {
                    oldViewImpl.notifyFocusLost();
                }
                
                Dockable newFocusOwner = event.getNewFocusOwner();
                ViewImpl newViewImpl = findViewImpl(newFocusOwner);
                if (newViewImpl != null) {
                    newViewImpl.notifyFocusGained();
                }
            }
        });
        
    // station
        dockStation = new SplitDockStation();
        dockController.add(dockStation);
        
    // frontend
        dockFrontend = new DockFrontend(dockController);
        dockFrontend.addRoot(DOCK_FRONTEND_ROOT_ID,dockStation);
        
    // theme
        dockController.setTheme(new AEStudioEclipeTheme());
        dockController.getIcons().setIconTheme("split.maximize",
            Icons.getIcon(DefaultIcons.MAXIMIZE_VIEW));
        dockController.getIcons().setIconTheme("split.normalize",
            Icons.getIcon(DefaultIcons.MAXIMIZE_VIEW));
    }
    
    /** */
    @Override
    public Component getComponent() {
        return dockStation.getComponent();
    }
    
    /** */
    @Override
    public void addViewManagerListener(ViewManagerListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    @Override
    public void registerViewFactory(ViewFactory viewFactory) {
    // check if there is already a factory of the same identifier
        for (ViewFactory itr:viewFactories) {
            if (itr.getId().equals(viewFactory.getId())) {
                throw new IllegalArgumentException(
                    String.format("View factory  %s already exists",
                    viewFactory.getId()));                
            }
        }
        
        viewFactories.add(viewFactory);
    }
    
    /** */
    private ViewFactory getFactory(String factoryId) {
        for (ViewFactory factory:viewFactories) {
            if (factory.getId().equals(factoryId)) {
                return factory;
            }
        }
        
        return null;
    }
    
    /** */
    private ViewImpl findViewImpl(String title) {
        for (ViewImpl viewImpl:views) {
            if (viewImpl.getTitle().equals(title)) {
                return viewImpl;
            }
        }
        
        return null;
    }
    
    /** */
    private ViewImpl findViewImpl(Dockable dockable) {
        for (ViewImpl viewImpl:views) {
            if (viewImpl.getDockable() == dockable) {
                return viewImpl;
            }
        }
        
        return null;
    }
    
    /** */
    private ViewImpl findViewImpl(ViewFactory viewFactory,String viewId) {
        for (ViewImpl viewImpl:views) {
            if (viewImpl.getFactory() == viewFactory &&
                StringUtils.equals(viewImpl.getViewId(),viewId)) {
            //
                return viewImpl;
            }
        }
        
        return null;
    }
    
    /** */
    private ViewImpl getFocusedViewImpl() {
        Dockable focusedDockable = dockController.getFocusedDockable();
        if (focusedDockable == null) {
            return null;
        }
        return findViewImpl(focusedDockable);
    }
    
    /** */
    @Override
    public View createView(ViewFactory viewFactory,String viewId) {    
        if (viewFactories.contains(viewFactory) == false) {
            throw new IllegalArgumentException("Unknown view factory");
        }
        if (findViewImpl(viewFactory,viewId) != null) {
            throw new IllegalArgumentException("View created by this factory" +
                " with this identifier already exists");
        }
        
        ViewImpl view = new ViewImpl(this,viewFactory,viewId);
        views.add(view);
        
        return view;
    }
    
    /** */
    private void createActions(ViewImpl viewImpl,DefaultDockable dockable) {
        DefaultDockActionSource source = new DefaultDockActionSource(
            new LocationHint( LocationHint.DOCKABLE, LocationHint.LEFT ));
            
    // view specific actions
        for (ViewAction action:viewImpl.getActions()) {
            source.add(action.getDockAction());
        }
        
    // default actions
        source.add(SeparatorAction.SEPARATOR); 
        source.add(new CloseAction(this,viewImpl));
        
        dockable.setActionOffers(source);
    }
    
    /** */
    private void createViewDockable(ViewImpl viewImpl) {
        DefaultDockable dockable = new DefaultDockable();
        viewImpl.setDockable(dockable);
        
        dockable.setTitleText(viewImpl.getTitle());
        dockable.setTitleIcon(viewImpl.getIcon());
        
        Component component = viewImpl.getComponent();
        if (component != null) {
            dockable.add(viewImpl.getComponent());        
            createActions(viewImpl,dockable);
        }
        else {
            Log.error(String.format("View of title %s provided no component",
                viewImpl.getTitle()));
        }
    }

    /** */
    private void focus(Dockable dockable) {
        dockController.setFocusedDockable(
            new DefaultFocusRequest(dockable,null,false));
    }
    
    /** */
    private TargetDockable createTargetDockable(StackDockStation dockStation) {
        TargetDockable target = new TargetDockable(dockStation);
        
        for (int index = 0; index < dockStation.getDockableCount(); index++) {
            Dockable childDockable = dockStation.getDockable(index);
            
            if (childDockable instanceof DefaultDockable) {
                ViewImpl viewImpl = findViewImpl(childDockable);
                target.inc(viewImpl.getCategory());
            }
        }        
        
        return target;
    }  
    
    /** */
    private List<TargetDockable> buildTargetDockableList() {
        List<TargetDockable> list = new ArrayList<>();
        
        for (int index = 0; index < dockStation.getDockableCount(); index++) {
            Dockable childDockable = dockStation.getDockable(index);
            
        // stack dock station
            if (childDockable instanceof StackDockStation) {
                StackDockStation stackDockStation =
                    (StackDockStation)childDockable;
                list.add(createTargetDockable(stackDockStation));
                continue;
            }
            
        // default dockable
            if (childDockable instanceof DefaultDockable) {
                ViewImpl viewImpl = findViewImpl(childDockable);
                if (viewImpl != null) {
                    TargetDockable target = new TargetDockable(childDockable);
                    target.inc(viewImpl.getCategory());
                    list.add(target);
                }
                continue;
            }
        }
        
        return list;
    }
    
    /** */     
    private Dockable findTargetDockable(View view) {
        if (view.getCategory() == null) {
            return dockController.getFocusedDockable();
        }
        
        List<TargetDockable> targets = buildTargetDockableList();
        TargetDockable bestTarget = null;
        
        for (TargetDockable target:targets) {
            if (bestTarget == null || target.getCount(view.getCategory()) >
                bestTarget.getCount(view.getCategory())) {
            //
                bestTarget = target;
            }
        }
        
        return bestTarget.getDockable();
    }
    
    /** */
    @Override
    public void showView(View view) {
        if (view.getManager() != this) {
            throw new IllegalArgumentException(
                "View not created by this manager");
        }
        if (view.getComponent() == null) {
            throw new IllegalArgumentException("View without component");
        }
        
        ViewImpl viewImpl = findViewImpl(view.getTitle());
        if (viewImpl == null) {
            throw new IllegalArgumentException("Unknown view");
        }        
        Dockable viewDockable = viewImpl.getDockable();
        
    // keep it in the state
        state.addViewImplState(view.getFactory().getId(),view.getViewId(),
            view.getTitle());
        
    // not dockable yet
        if (viewDockable == null) {
            createViewDockable(viewImpl);
            viewDockable = viewImpl.getDockable();
        }
        
    // no dockables visible - drop it from a grid
        if (dockStation.getDockableCount() == 0) {       
            dockFrontend.addDockable(viewImpl.getTitle(),viewDockable);
            
        // let the view occupy the entire area
            SplitDockGrid grid = new SplitDockGrid();
            grid.addDockable(0,0,1,1,viewDockable);
            dockStation.dropTree(grid.toTree());
                        
            focus(viewDockable);
            viewImpl.notifyShown();
            return;
        }
        
    // no focused dockable - don't know where to add
        if (dockController.getFocusedDockable() == null) {
            Dockable targetDockable = findTargetDockable(viewImpl); 
            focus(targetDockable);
        }

        if (dockFrontend.getDockable(viewImpl.getTitle()) == null) {
            dockFrontend.addDockable(viewImpl.getTitle(),viewDockable);
            
            Dockable targetDockable = findTargetDockable(viewImpl); 
            DockStation root = DockUtilities.getRoot(targetDockable);
            DockableProperty dockableProperty =
                DockUtilities.getPropertyChain(root,targetDockable);
            dockStation.drop(viewDockable,dockableProperty);
            
            viewImpl.notifyShown();
        } 
        focus(viewDockable);
    }

    /** */
    private void removeView(ViewImpl viewImpl) {
        state.removeViewImplState(
            viewImpl.getFactory().getId(),viewImpl.getViewId());
        views.remove(viewImpl);        
    }
    
    /** */
    @Override
    public void closeView(View view) {
        ViewImpl viewImpl = findViewImpl(view.getTitle());
        if (viewImpl == null) {
            throw new IllegalArgumentException("Unknown view");
        }
        
    // dockable
        Dockable dockable = viewImpl.getDockable();
        if (dockable == null) {
            return;
        }
        
    // really close?
        if (viewImpl.closing() == false) {
            return;
        }
        
        DockStation parent = dockable.getDockParent();
    // remove the dockable
        parent.drag(dockable);
        dockFrontend.remove(dockable);
        
    // remove the view
        removeView(viewImpl);
        
    // focus on the top dockable of the parent
        if (parent.getFrontDockable() != null) {
            focus(parent.getFrontDockable());
        }
        else if (parent.getDockableCount() == 1) {
            focus(parent.getDockable(0));
        }
        
    // notify
        viewImpl.notifyClosed();        
    }
    
    /** */
    private void storeDockFrontendLayout() {
    // save
        dockFrontend.save(DOCK_FRONTEND_LAYOUT_ID);
        
    // streams
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteArrayStream);
        
    // to base64
        String layout = null;
        try {
            dockFrontend.write(dataStream);
            layout = Base64.encode(byteArrayStream.toByteArray());
        } catch (IOException exception) {        
            Log.error("Failed to encode view layout",exception);
        }                
        state.setDockFrontendLayout(layout);
        
    // focused view
        Dockable focusedDockable = dockController.getFocusedDockable();
        state.setFocusedView(
            focusedDockable != null ? focusedDockable.getTitleText() : null);
    }
    
    /** */
    private void restoreDockFrontendLayout() {
    // from base64
        byte[] data = null;
        try {
            data = Base64.decode(state.getDockFrontendLayout());
        } catch (IOException exception) {
            Log.error("Failed to decode view layout",exception);
            return;
        }
        
    // streams
        ByteArrayInputStream byteArrayStream = new ByteArrayInputStream(data);
        DataInputStream dataStream = new DataInputStream(byteArrayStream);
        
    // read
        try {
            dockFrontend.read(dataStream);
        } catch (IOException exception) {
            Log.error("Failed to decode view layout",exception);
            return;
        }
        
    // focused view
        if (state.getFocusedView() != null) {
            ViewImpl focusedViewImpl = findViewImpl(state.getFocusedView());
            if (focusedViewImpl != null) {
                focus(focusedViewImpl.getDockable());
            }
            else {
                Log.warning(String.format("Focused view not found. " +
                    "View identifier is %s",state.getFocusedView()));
            }
        }
    }
    
    /** */
    @Override
    public String getState() {
        storeDockFrontendLayout();
        return PluginState.serialize(state);
    }

    /** */
    @Override
    public void setState(String stateStr) {
        if (stateStr == null) {
            state = new ViewManagerImplState();
            state.loadDefault();
            return;
        }
        
        state = PluginState.deserialize(stateStr,ViewManagerImplState.class);
    }

    /** */
    private ViewImpl createView(ViewImplState viewImplState) {
        ViewFactory factory = getFactory(viewImplState.getFactoryId());
        if (factory == null) {
            Log.warning(String.format("Failed to recreate view. " +
                "View factory %s not registered. View identifier is %s",
                viewImplState.getFactoryId(),viewImplState.getViewId()));
            return null;
        }
        if (viewImplState.getTitle() == null) {
            Log.warning(String.format("Failed to recreate view. " +
                "No title in state. View identifier is %s",
                viewImplState.getViewId()));
            return null;
        }
        
        View view = factory.createView(viewImplState.getViewId());
        if (view == null) {
            return null;
        }
        view.setTitle(viewImplState.getTitle()); // overwrite title
        ViewImpl viewImpl = findViewImpl(view.getTitle());
        createViewDockable(viewImpl);
        
        return viewImpl;
    }
    
    /** */
    @Override
    public void restoreViews() {
        if (state.getDockFrontendLayout() == null) {
            Log.trace("Not restoring views (no layout)");
            return;
        }
        Log.trace("Restoring views");
        
    // copy the state of the views
        List<ViewImplState> viewImplStateList = new ArrayList<>();
        viewImplStateList.addAll(state.getViewImplStateList());
        state.getViewImplStateList().clear();
        
    // views        
        for (ViewImplState viewImplState:viewImplStateList) {
            ViewImpl view = createView(viewImplState);
            if (view == null) {
                continue;
            }
            state.addViewImplState(
                view.getFactory().getId(),view.getViewId(),view.getTitle());
        }
    }
    
    /** */
    @Override
    public void restoreViewLayout() {
        if (state.getDockFrontendLayout() == null) {
            Log.trace("Not restoring view layout (no layout)");
            return;
        }
        Log.trace("Restoring view layout");
        
    // add to the dock station
        SplitDockGrid grid = new SplitDockGrid();
        for (ViewImpl viewImpl:views) {
            Dockable viewDockable = viewImpl.getDockable();
            dockFrontend.addDockable(viewImpl.getTitle(),viewDockable);
            grid.addDockable(0,0,1,1,viewDockable);
            viewImpl.notifyShown();
        }
        dockStation.dropTree(grid.toTree());            
        
    // layout
        restoreDockFrontendLayout();
    }
    
    /** */
    @Override
    public void addViewProvider(ViewProvider viewProvider) {
        viewProviders.add(viewProvider);
    }
    
    /** */
    @Override
    public List<ViewProvider> getViewProviders() {
        return Collections.unmodifiableList(viewProviders);
    }

    /** */
    @Override
    public View getViewByTitle(String title) {
        for (ViewImpl viewImpl:views) {
            if (StringUtils.equals(viewImpl.getTitle(),title)) {
                return viewImpl;
            }
        }
        
        return null;
    }
    
    /** */
    @Override
    public View getView(ViewFactory viewFactory,String viewId) {
        return findViewImpl(viewFactory,viewId);
    }
    
    /** */
    @Override
    public Set<View> getViews() {
        Set<View> set = new HashSet<>();
        set.addAll(views);
        
        return Collections.unmodifiableSet(set);
    }

    /** */
    @Override
    public void focus(View view) {
        ViewImpl viewImpl = findViewImpl(view.getTitle());
        if (viewImpl == null) {
            throw new IllegalArgumentException(String.format(
                "Focus on an unknown view %s",view.getTitle()));
        }
        focus(viewImpl.getDockable());
    }
    
    /** */
    private int getDockableIndex(DockStation station,Dockable dockable) {
        for (int index = 0; index < station.getDockableCount(); index++) {
            if (station.getDockable(index) == dockable) {
                return index;
            }
        }
        
        return -1;
    }
    
    /** */
    @Override
    public void goToNextView() {
        ViewImpl focusedView = getFocusedViewImpl();
        if (focusedView == null) {
            return;
        }        
        DockStation parent = focusedView.getDockable().getDockParent();
        if (parent == dockStation) {
            return;
        }
        
        int index = getDockableIndex(parent,focusedView.getDockable());
        if (index != -1) {
            int nextIndex = (index + 1) % parent.getDockableCount();
            Dockable nextDockable = parent.getDockable(nextIndex);
            ViewImpl nextViewImpl = findViewImpl(nextDockable);
            focus(nextViewImpl);
        }
    }
    
    /** */
    @Override
    public void goToPreviousView() {
        ViewImpl focusedView = getFocusedViewImpl();
        if (focusedView == null) {
            return;
        }
        DockStation parent = focusedView.getDockable().getDockParent();
        if (parent == dockStation) {
            return;
        }
       
        int index = getDockableIndex(parent,focusedView.getDockable());
        if (index != -1) {
            int previousIndex = index - 1;
            if (previousIndex < 0) {
                previousIndex = parent.getDockableCount() - 1;
            }
            Dockable previousDockable = parent.getDockable(previousIndex);
            ViewImpl previousViewImpl = findViewImpl(previousDockable);
            focus(previousViewImpl);
        }       
    }
    
    /** */
    @Override
    public List<View> getSiblingViews(View view) {        
        ViewImpl viewImpl = findViewImpl(view.getTitle());
        if (viewImpl == null) {
            throw new IllegalArgumentException("Unknown view");
        }
        
        DockStation parent = viewImpl.getDockable().getDockParent();
        if (parent == dockStation) {
            return null;
        }        
        
        List<View> siblings = new ArrayList<>();
        for (int index = 0; index < parent.getDockableCount(); index++) {
            Dockable childDockable = parent.getDockable(index);
            ViewImpl childView = findViewImpl(childDockable);
            if (childView == null) {
                throw new IllegalStateException("Unknown view");
            }
            siblings.add(childView);
        }
        
        return siblings;
    }
}
