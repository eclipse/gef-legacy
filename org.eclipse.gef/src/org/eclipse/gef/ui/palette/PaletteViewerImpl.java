package org.eclipse.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.ButtonGroup;
import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.*;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.palette.*;
import org.eclipse.gef.ui.palette.customize.PaletteCustomizerDialog;
import org.eclipse.gef.ui.palette.editparts.*;
import org.eclipse.gef.ui.parts.PaletteViewerKeyHandler;

public class PaletteViewerImpl
	extends org.eclipse.gef.ui.parts.GraphicalViewerImpl
	implements PaletteViewer 
{

private PaletteCustomizer customizer = null;
private PaletteCustomizerDialog customizerDialog = null;
private List paletteListeners = new ArrayList();
private ButtonGroup buttonGroup = null;
private PaletteEntry selectedEntry = null;
private PaletteRoot paletteRoot = null;
private PaletteViewerPreferences prefs = null;

private boolean controlHooked = false;

private PropertyChangeListener prefListener = new PropertyChangeListener() {
	public void propertyChange(PropertyChangeEvent evt) {
		EditPart root = getRootEditPart().getContents();
		if (evt.getPropertyName().equals(PaletteViewerPreferences.PREFERENCE_FONT)) {
			Font font = new Font(Display.getCurrent(), 
					getPaletteViewerPreferencesSource().getFontData());
			IFigure fig = ((AbstractGraphicalEditPart)root).getFigure();
			fig.setFont(font);
			fig.invalidateTree();
		}
		refreshAllEditParts(root);
	}
	private void refreshAllEditParts(EditPart part) {
		part.refresh();
		List children = part.getChildren();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			EditPart child = (EditPart) iter.next();
			refreshAllEditParts(child);
		}
	}
};

public PaletteViewerImpl() {
	setEditDomain(new DefaultEditDomain(null));
	setKeyHandler(new PaletteViewerKeyHandler(this));
}

public void addPaletteListener(PaletteListener paletteListener) {
	if (paletteListeners != null)
		paletteListeners.add(paletteListener);
}

protected void createDefaultRoot() {
	setRootEditPart(new PaletteRootEditPart());
}

/**
 * @see org.eclipse.gef.EditPartViewer#dispose()
 */
public void dispose() {
	super.dispose();
	prefs.dispose();
}

protected void firePaletteSelectionChanged() {
	if (paletteListeners == null)
		return;
	PaletteEvent event = new PaletteEvent(this, selectedEntry);
	for (int listener = 0; listener < paletteListeners.size(); listener++)
		((PaletteListener) paletteListeners.get(listener)).entrySelected(
			event);
}

public ButtonGroup getButtonGroup() {
	if (buttonGroup == null)
		buttonGroup = new ButtonGroup();
	return buttonGroup;
}

/**
 * Returns the customizer.
 * @return PaletteCustomizer
 */
public PaletteCustomizer getCustomizer() {
	return customizer;
}

public PaletteCustomizerDialog getCustomizerDialog() {
	if (customizerDialog == null) {
		customizerDialog = new PaletteCustomizerDialog(getControl().getShell(),
	                                                   getCustomizer(),
	                                                   paletteRoot);
	}
	return customizerDialog;
}

public EditPartFactory getEditPartFactory() {
	if (super.getEditPartFactory() == null)
		setEditPartFactory(new PaletteEditPartFactory());
	return super.getEditPartFactory();
}

public PaletteViewerPreferences getPaletteViewerPreferencesSource() {
	if (prefs == null) {
		prefs = new DefaultPaletteViewerPreferences();
		if (controlHooked) {
			prefs.addPropertyChangeListener(prefListener);
		}
	}
	return prefs;
}

/**
 * @see org.eclipse.gef.ui.parts.GraphicalViewerImpl#hookControl()
 */
protected void hookControl() {
	super.hookControl();
	if (prefs != null) {
		prefs.addPropertyChangeListener(prefListener);
	}
	controlHooked = true;
}

public PaletteToolEntry getSelectedEntry() {
	return (PaletteToolEntry) selectedEntry;
}

public void removePaletteListener(PaletteListener paletteListener) {
	paletteListeners.remove(paletteListener);
}

/**
 * Sets the customizer.
 * @param customizer The customizer to set
 */
public void setCustomizer(PaletteCustomizer customizer) {
	this.customizer = customizer;
}

public void setPaletteRoot(PaletteRoot root) {
	paletteRoot = root;
	if (paletteRoot != null) {
		EditPart palette =
			getEditPartFactory().createEditPart(getRootEditPart(), root);
		getRootEditPart().setContents(palette);
	}
}

/*
 * @TODO:Pratik
 * Looks like we don't really need this method.  It can only be called once anyways
 * (before getPaletteViewerPreferencesSource() is called the first time), and probably
 * right after construction.  Discuss this with Randy.
 */
public void setPaletteViewerPreferencesSource(PaletteViewerPreferences prefs) {
	if (prefs == null) {
		return;
	}

	if (this.prefs != null) {
		this.prefs.removePropertyChangeListener(prefListener);
	}

	this.prefs = prefs;
	if (controlHooked) {
		this.prefs.addPropertyChangeListener(prefListener);
	}
}

/*
public void setSelection(ISelection newSelection){
	IStructuredSelection selected = (IStructuredSelection)newSelection;
	if(selected==null){
		if(getButtonGroup().getSelected()!=null)
			getButtonGroup().getSelected().setSelected(false);
	}else{
		Object entry = selected.getFirstElement();
		if(entry instanceof PaletteToolEntry)
			setSelection((PaletteEntry)entry);
	}
}*/

public void setSelection(PaletteEntry entry) {
	if (selectedEntry == entry)
		return;
	if (entry == null) {
		getButtonGroup().setSelected(null);
		selectedEntry = null;
		getButtonGroup().setSelected(getButtonGroup().getDefault());
		if (getButtonGroup().getSelected() == null) {
			selectedEntry = null;
			firePaletteSelectionChanged();
		}
	} else {
		selectedEntry = entry;
		ToolEntryEditPart ep = (ToolEntryEditPart)getEditPartRegistry().get(entry);
		ep.select();
		firePaletteSelectionChanged();
	}
}

/**
 * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#unhookControl()
 */
protected void unhookControl() {
	super.unhookControl();
	if (prefs != null) {
		prefs.removePropertyChangeListener(prefListener);
	}
	controlHooked = false;
}

}
