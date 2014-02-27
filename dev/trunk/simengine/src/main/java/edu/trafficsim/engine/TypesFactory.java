package edu.trafficsim.engine;

import edu.trafficsim.model.DriverTypeComposition;
import edu.trafficsim.model.VehicleTypeComposition;
import edu.trafficsim.model.core.ModelInputException;
import edu.trafficsim.model.roadusers.DriverType;
import edu.trafficsim.model.roadusers.VehicleType;
import edu.trafficsim.model.roadusers.VehicleType.VehicleClass;

public interface TypesFactory {

	public VehicleType createVehicleType(Long id, String name,
			VehicleClass vehicleClass);

	public DriverType createDriverType(Long id, String name);

	public VehicleTypeComposition createVehicleTypeComposition(Long id,
			String name, VehicleType[] vehicleTypes, double[] probabilities)
			throws ModelInputException;

	public DriverTypeComposition createDriverTypeComposition(Long id,
			String name, DriverType[] driverTypes, double[] probabilities)
			throws ModelInputException;
}
