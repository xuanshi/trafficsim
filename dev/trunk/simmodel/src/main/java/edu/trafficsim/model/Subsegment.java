package edu.trafficsim.model;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author Xuan
 * 
 */
public interface Subsegment extends DataContainer {

	public Long getId();

	public String getName();

	public Coordinate getStartCoord();

	public Coordinate getEndCoord();

	public double getStart();

	public double getEnd();

	public double getShift();

	public double getWidth();

	public double getLength();

	public Segment getSegment();
}
