/*
 * Copyright (C) 2014 Xuan Shi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package edu.trafficsim.model.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.LineString;

import edu.trafficsim.model.BaseEntity;
import edu.trafficsim.model.Location;
import edu.trafficsim.model.Segment;
import edu.trafficsim.model.Subsegment;
import edu.trafficsim.model.util.Coordinates;

/**
 * 
 * 
 * @author Xuan Shi
 * @param <T>
 *            the generic type
 */
public abstract class AbstractSegment<T> extends BaseEntity<T> implements
		Segment {

	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_SUBSEGMENTS_SIZE = 3;

	private CoordinateReferenceSystem crs = null;
	protected List<Subsegment> subsegments;
	protected Location startLocation;
	protected Location endLocation;

	private LineString linearGeom;
	private double length;

	public AbstractSegment(long id, String name, Location startLocation,
			Location endLocation, LineString linearGeom)
			throws TransformException, ModelInputException {
		super(id, name);
		checkStartEnd(startLocation, endLocation, linearGeom);
		this.linearGeom = linearGeom;
		this.startLocation = startLocation;
		this.endLocation = endLocation;
		this.subsegments = new ArrayList<Subsegment>(DEFAULT_SUBSEGMENTS_SIZE);
	}

	private void checkStartEnd(Location startLocation, Location endLocation,
			LineString linearGeom) throws ModelInputException {
		if (!linearGeom.getStartPoint().getCoordinate()
				.equals(startLocation.getPoint().getCoordinate())
				|| !linearGeom.getEndPoint().getCoordinate()
						.equals(endLocation.getPoint().getCoordinate()))
			throw new ModelInputException(
					"Nodes and linear geometry doesn't match");
	}

	@Override
	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	@Override
	public final List<Subsegment> getSubsegments() {
		return Collections.unmodifiableList(subsegments);
	}

	@Override
	public final int sizeOfSubsegments() {
		return subsegments.size();
	}

	@Override
	public final LineString getLinearGeom() {
		return linearGeom;
	}

	@Override
	public void setLinearGeom(Location startLocation,
			Location endLocation, LineString linearGeom)
			throws TransformException, ModelInputException {
		checkStartEnd(startLocation, endLocation, linearGeom);
		this.linearGeom = linearGeom;
		this.startLocation = startLocation;
		this.endLocation = endLocation;
		onGeomUpdated();
	}

	@Override
	public final Location getStartLocation() {
		return startLocation;
	}

	@Override
	public final Location getEndLocation() {
		return endLocation;
	}

	@Override
	public double getWidth() {
		double width = 0;
		for (Subsegment subsegment : subsegments)
			width += subsegment.getWidth();
		return width;
	}

	@Override
	public final double getLength() {
		return length;
	}

	@Override
	public final double getGeomLength() {
		return getLinearGeom().getLength();
	}

	@Override
	public void onGeomUpdated() throws TransformException {
		length = Coordinates.orthodromicDistance(getCrs(), getLinearGeom());
		for (Subsegment subsegment : subsegments)
			subsegment.onGeomUpdated();
	}
}