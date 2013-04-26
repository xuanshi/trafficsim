package edu.trafficsim.engine.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.PoissonDistribution;

import edu.trafficsim.engine.VehicleGenerator;
import edu.trafficsim.model.Lane;
import edu.trafficsim.model.Link;
import edu.trafficsim.model.Od;
import edu.trafficsim.model.Simulator;
import edu.trafficsim.model.core.BaseEntity;
import edu.trafficsim.model.core.Randoms;
import edu.trafficsim.model.roadusers.DriverType;
import edu.trafficsim.model.roadusers.VehicleType;

public class DefaultVehicleGenerator extends
		BaseEntity<DefaultVehicleGenerator> implements VehicleGenerator {

	private static final long serialVersionUID = 1L;

	private static DefaultVehicleGenerator generator = null;

	private DefaultVehicleGenerator() {
	}

	public static DefaultVehicleGenerator getInstance() {
		if (generator == null)
			generator = new DefaultVehicleGenerator();
		return generator;
	}

	// HACK for the demo
	// TODO make it a plugin
	// Based on arrival rate
	// The other should be based on headway
	@Override
	public final List<VehicleToAdd> getVehicleToAdd(Od od, Simulator simulator) {
		double time = simulator.getForwarded();
		double stepSize = simulator.getStepSize();
		Random rand = simulator.getRand();

		int vph = od.getVph(time);
		double arrivalRate = ((double) vph) / (3600 / stepSize);

		PoissonDistribution dist = new PoissonDistribution(arrivalRate);
		double prob = rand.nextDouble();
		int num = dist.inverseCumulativeProbability(prob);

		List<VehicleToAdd> vehicles = new ArrayList<VehicleToAdd>();

		for (int i = 0; i < num; i++) {
			VehicleType vtypeToBuild = Randoms.randomElement(
					od.getVehicleTypeComposition(time), rand);
			DriverType dtypeToBuild = Randoms.randomElement(
					od.getDriverTypeComposition(time), rand);

			// TODO random speed and accel
			double speed = Randoms.uniform(5, 30, rand);
			double accel = 0.2;

			// TODO setup routing
			List<Link> links = new ArrayList<Link>(od.getOrigin()
					.getDownstreams());
			Link link = links.get(rand.nextInt(links.size()));
			List<Lane> lanes = new ArrayList<Lane>(link.getLanes());
			Lane lane = lanes.get(rand.nextInt(lanes.size()));

			VehicleToAdd vehicle = new VehicleToAdd(vtypeToBuild, dtypeToBuild,
					lane, speed, accel);
			vehicles.add(vehicle);

			// Test
			StringBuffer sb = new StringBuffer();
			sb.append("Time: " + time + "s -- " + "New Vehicle: ");
			sb.append("VehicleType -> ");
			sb.append(vtypeToBuild.getName());
			sb.append(" || ");
			sb.append("DriverType -> ");
			sb.append(dtypeToBuild.getName());
			System.out.println(sb.toString());
		}

		return vehicles;
	}

}
