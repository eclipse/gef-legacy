/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.swt.graphics.Image;

/**
 * A Checkbox is a toggle figure which toggles between the checked and unchecked figures
 * to simulate a check box. A check box contains a text label to represent it.
 */
public final class CheckBox 
	extends Toggle 
{

private Label label = null;

static final Image
	UNCHECKED = new Image(null,
		CheckBox.class.getResourceAsStream("images/checkboxenabledoff.gif")), //$NON-NLS-1$
	CHECKED = new Image(null,
		CheckBox.class.getResourceAsStream("images/checkboxenabledon.gif")); //$NON-NLS-1$

/**
 * Constructs a CheckBox with no text.
 * 
 * @since 2.0
 */
public CheckBox() {
	this(""); //$NON-NLS-1$
}

/**
 * Constructs a CheckBox with the passed text in its label.
 * @param text The label text
 * @since 2.0
 */
public CheckBox(String text) {
	setContents(label = new Label(text, UNCHECKED));
}

/**
 * Adjusts CheckBox's icon depending on selection status.
 * 
 * @since 2.0
 */
protected void handleSelectionChanged() {
	if (isSelected())
		label.setIcon(CHECKED);
	else
		label.setIcon(UNCHECKED);
}

/**
 * Initializes this Clickable by setting a default model and adding a clickable event
 * handler for that model. Also adds a ChangeListener to update icon when  selection
 * status changes.
 * 
 * @since 2.0
 */
protected void init() {
	super.init();
	addChangeListener(new ChangeListener () {
		public void handleStateChanged(ChangeEvent changeEvent) { 
			if (changeEvent.getPropertyName().equals(ButtonModel.SELECTED_PROPERTY))
				handleSelectionChanged();
		}
	});
}

}