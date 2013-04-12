package edu.trafficsim.model.core;

import edu.trafficsim.model.DataContainer;

public interface Event extends DataContainer {
	
	public double getStartTime();

	public double getEndTime();
	
	public boolean isActive();

}
