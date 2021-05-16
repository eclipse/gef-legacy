/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl;

import java.util.List;

import org.eclipse.draw2dl.geometry.Dimension;
import org.eclipse.draw2dl.geometry.Rectangle;

/**
 * Figures using the StackLayout as their layout manager have their children
 * placed on top of one another. Order of placement is determined by the order
 * in which the children were added, first child added placed on the bottom.
 */
public class StackLayout extends org.eclipse.draw2dl.AbstractHintLayout {

	/**
	 * Returns the minimum size required by the input container. This is the
	 * size of the largest child of the container, as all other children fit
	 * into this size.
	 * 
	 * @see AbstractHintLayout#calculateMinimumSize(org.eclipse.draw2dl.IFigure, int, int)
	 */
	protected Dimension calculateMinimumSize(org.eclipse.draw2dl.IFigure figure, int wHint,
                                             int hHint) {
		if (wHint > -1)
			wHint = Math.max(0, wHint - figure.getInsets().getWidth());
		if (hHint > -1)
			hHint = Math.max(0, hHint - figure.getInsets().getHeight());
		Dimension d = new Dimension();
		List<IFigure> children = figure.getChildren();
		for (IFigure child : children) {
			if (!isObservingVisibility() || child.isVisible())
				d.union(child.getMinimumSize(wHint, hHint));
		}

		d.expand(figure.getInsets().getWidth(), figure.getInsets().getHeight());
		d.union(getBorderPreferredSize(figure));
		return d;

	}

	/**
	 * Calculates and returns the preferred size of the given figure. This is
	 * the union of the preferred sizes of the widest and the tallest of all its
	 * children.
	 * 
	 * @see AbstractLayout#calculatePreferredSize(org.eclipse.draw2dl.IFigure, int, int)
	 */
	protected Dimension calculatePreferredSize(org.eclipse.draw2dl.IFigure figure, int wHint,
                                               int hHint) {
		if (wHint > -1)
			wHint = Math.max(0, wHint - figure.getInsets().getWidth());
		if (hHint > -1)
			hHint = Math.max(0, hHint - figure.getInsets().getHeight());
		Dimension d = new Dimension();
		List<IFigure> children = figure.getChildren();
		for (IFigure child : children) {
			if (!isObservingVisibility() || child.isVisible())
				d.union(child.getPreferredSize(wHint, hHint));
		}

		d.expand(figure.getInsets().getWidth(), figure.getInsets().getHeight());
		d.union(getBorderPreferredSize(figure));
		return d;
	}

	/**
	 * @see LayoutManager#layout(org.eclipse.draw2dl.IFigure)
	 */
	public void layout(org.eclipse.draw2dl.IFigure figure) {
		Rectangle r = figure.getClientArea();
		List<IFigure> children = figure.getChildren();
		for (IFigure child : children) {
			child.setBounds(r);
		}
	}

}