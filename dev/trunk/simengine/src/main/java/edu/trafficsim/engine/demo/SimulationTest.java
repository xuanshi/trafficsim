package edu.trafficsim.engine.demo;

import java.util.List;

import edu.trafficsim.engine.Simulation;
import edu.trafficsim.engine.VehicleFactory;
import edu.trafficsim.engine.VehicleGenerator;
import edu.trafficsim.engine.factory.DefaultSimulatorFactory;
import edu.trafficsim.model.Network;
import edu.trafficsim.model.Simulator;
import edu.trafficsim.model.Vehicle;
import edu.trafficsim.model.core.ModelInputException;

public class SimulationTest {

	public static void main(String[] args) throws ModelInputException {
		getInstance().run();
	}

	private static SimulationTest test = null;

	private Builder builder;

	private SimulationTest() {
		try {
			builder = new Builder();
		} catch (ModelInputException e) {
			builder = null;
			e.printStackTrace();
		}
	}

	public static SimulationTest getInstance() {
		if (test == null)
			test = new SimulationTest();
		return test;
	}

	public List<Vehicle> run() throws ModelInputException {
		Simulator simulator = DefaultSimulatorFactory.getInstance()
				.createSimulator(500, 1);

		Network network = builder.getNetwork();
		VehicleGenerator vehicleGenerator = builder.getVehicleGenerator();
		VehicleFactory vehicleFactory = builder.getVehicleFactory();

		Simulation simulation = new Simulation(simulator, network,
				vehicleGenerator, vehicleFactory);

		return simulation.run();
	}

	public Network getNetwork() {
		return builder.getNetwork();
	}
}
