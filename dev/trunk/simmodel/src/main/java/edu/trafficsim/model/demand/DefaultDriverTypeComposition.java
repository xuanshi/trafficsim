package edu.trafficsim.model.demand;

import java.util.Set;

import edu.trafficsim.model.DriverTypeComposition;
import edu.trafficsim.model.core.AbstractComposition;
import edu.trafficsim.model.core.ModelInputException;
import edu.trafficsim.model.roadusers.DriverType;

public class DefaultDriverTypeComposition extends
		AbstractComposition<DriverType> implements DriverTypeComposition {

	private static final long serialVersionUID = 1L;

	public DefaultDriverTypeComposition(long id, String name,
			DriverType[] driverTypes, double[] probabilities)
			throws ModelInputException {
		super(id, name, driverTypes, probabilities);
	}

	@Override
	public final Set<DriverType> getDriverTypes() {
		return keys();
	}

}
