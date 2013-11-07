package edu.trafficsim.model;

import java.util.Collection;

/**
 * @author Xuan
 * 
 */
public interface Lane extends Subsegment {

	/**
	 * Implement our own efficient collection to hold the vehicles within a lane
	 * or link.
	 */

	public Vehicle getHeadVehicle();

	public Vehicle getTailVehicle();

	public Vehicle getLeadingVehicle(Vehicle v);

	public Vehicle getPrecedingVehicle(Vehicle v);

	public void add(Vehicle vehicle);

	public void remove(Vehicle vehicle);

	public void update(Vehicle vehicle);

	public Collection<Vehicle> getVehicles();

	public double getStart();

	public double getEnd();

	public double getWidth();

	public double getLength();

	public double getShift();

	public int getLaneId();
}
