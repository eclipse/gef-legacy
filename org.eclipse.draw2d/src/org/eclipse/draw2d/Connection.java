package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.PointList;

/**
 * A Connection is a figure that connects two objects.
 */
public interface Connection extends IFigure {

/** 
 * The connection router property.  Used to signify that the ConnectionRouter has changed.
 */
String PROPERTY_CONNECTION_ROUTER = "connectionRouter"; //$NON-NLS-1$
/**
 * The points property.  Used to signify the points in the Connection have changed. */
String PROPERTY_POINTS = "points"; //$NON-NLS-1$

/**
 * Returns the ConnectionRouter used to route this Connection.  Does not return null.
 * 
 * @return The ConnectionRouter for this Connection
 */
ConnectionRouter getConnectionRouter();

/**
 * Sets the ConnectionRouter for this Connection.
 * 
 * @param router The ConnectionRouter to set for this Connection
 */
void setConnectionRouter(ConnectionRouter router);

/**
 * Returns the ConnectionAnchor at the start of this Connection.
 * @return The ConnectionAnchor at the start of this Connection
 * @deprecated Use {@link #getSourceAnchor()} instead. 
 */
ConnectionAnchor getStartAnchor();

/**
 * Returns the ConnectionAnchor at the end of this Connection.
 * @return The ConnectionAnchor at the end of this Connection
 * @deprecated Use {@link #getTargetAnchor()} instead.
 */
ConnectionAnchor getEndAnchor();


/**
 * Returns the ConnectionAnchor at the <b>source</b> end of this Connection.
 * @return The ConnectionAnchor at the <b>source</b> end of this Connection
 */
ConnectionAnchor getSourceAnchor();

/**
 * Returns the ConnectionAnchor at the <b>target</b> end of this Connection.
 * @return The ConnectionAnchor at the <b>target</b> end of this Connection
 */
ConnectionAnchor getTargetAnchor();

/**
 * Returns the routing constraint.  May be <code>null</code>.
 * @return The routing constraint
 */
Object getRoutingConstraint();

/**
 * Sets the routing constraint used by the router.
 * @param cons The routing constraint
 */
void setRoutingConstraint(Object cons);

/**
 * Sets the ConnectionAnchor to be used at the <i>source</i> end of this Connection.
 * @param anchor The source anchor
 */
void setSourceAnchor(ConnectionAnchor anchor);

/**
 * Sets the ConnectionAnchor to be used at the <i>target</i> end of this Connection.
 * @param anchor The target anchor
 */
void setTargetAnchor(ConnectionAnchor anchor);

/**
 * Sets the ConnectionAnchor to be used at the <i>source</i> end of this Connection.
 * @param anchor The source anchor
 * @deprecated Use {@link #setSourceAnchor(ConnectionAnchor)} instead.
 */
void setStartAnchor(ConnectionAnchor anchor);

/**
 * Sets the ConnectionAnchor to be used at the <i>target</i> end of this Connection.
 * @param anchor The target anchor
 * @deprecated Use {@link #setTargetAnchor(ConnectionAnchor)} instead.
 */
void setEndAnchor(ConnectionAnchor anchor);

/**
 * Returns the PointList containing the Points that make up this Connection.  This may be
 * returned by reference.
 * @return The points for this Connection
 */
PointList getPoints();

/**
 * Sets the PointList containing the Points that make up this Connection.
 * @param list The points for this Connection
 */
void setPoints(PointList list);

}