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
package edu.trafficsim.plugin.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.random.RandomGenerator;
import org.opengis.referencing.operation.TransformException;

import edu.trafficsim.engine.SimulationScenario;
import edu.trafficsim.engine.VehicleFactory;
import edu.trafficsim.model.DriverType;
import edu.trafficsim.model.Lane;
import edu.trafficsim.model.Link;
import edu.trafficsim.model.Od;
import edu.trafficsim.model.Vehicle;
import edu.trafficsim.model.VehicleType;
import edu.trafficsim.model.util.Randoms;
import edu.trafficsim.plugin.AbstractPlugin;
import edu.trafficsim.plugin.IRouting;
import edu.trafficsim.plugin.IVehicleGenerating;
import edu.trafficsim.plugin.PluginManager;

/**
 * 
 * 
 * @author Xuan Shi
 */
public class PoissonVehicleGenerating extends AbstractPlugin implements
		IVehicleGenerating {

	private static final long serialVersionUID = 1L;

	// Based on arrival rate (possion dist)
	// An alternative should be based on headway (negative exponential dist)
	@Override
	public final List<Vehicle> newVehicles(Od od, SimulationScenario scenario,
			VehicleFactory vehicleFactory) throws TransformException {
		double time = scenario.getTimer().getForwardedTime();
		double stepSize = scenario.getTimer().getStepSize();
		RandomGenerator rng = scenario.getTimer().getRand()
				.getRandomGenerator();
		Random rand = scenario.getTimer().getRand().getRandom();

		// calculate arrival rate
		int vph = od.vph(time);
		if (vph <= 0)
			return Collections.emptyList();
		double arrivalRate = ((double) vph) / (3600 / stepSize);

		// random num
		int num = Randoms.poission(arrivalRate, rng);

		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		for (int i = 0; i < num; i++) {

			// create vehicle with random vehicle type and driver type
			VehicleType vtypeToBuild = Randoms.randomElement(
					od.getVehicleComposition(), rand);
			DriverType dtypeToBuild = Randoms.randomElement(
					od.getDriverComposition(), rand);
			if (vtypeToBuild == null || dtypeToBuild == null)
				continue;

			Vehicle vehicle = vehicleFactory.createVehicle(vtypeToBuild,
					dtypeToBuild, scenario);

			// random initial link and lane
			List<Link> links = new ArrayList<Link>(od.getOrigin()
					.getDownstreams());
			if (links.isEmpty())
				continue;
			Link link = links.get(rand.nextInt(links.size()));
			List<Lane> lanes = Arrays.asList(link.getLanes());
			if (lanes.isEmpty())
				continue;
			Lane lane = lanes.get(rand.nextInt(lanes.size()));

			// random initial speed and acceleration
			double speed = Randoms.uniform(5, 30, rng);
			double accel = Randoms.uniform(0, 1, rng);
			vehicle.speed(speed);
			vehicle.acceleration(accel);
			// set vehicle initial position, keep a min headway (gap) from the
			// last
			// vehicle in lane
			double position = 0;
			Vehicle tailVehicle = lane.getTailVehicle();
			if (tailVehicle != null) {
				position = tailVehicle.position() - (3600 / vph / lanes.size())
						* speed;
				position = position > 0 ? 0 : position;
			}
			vehicle.position(position);

			// add vehicle to the current lane
			vehicle.currentLane(lane);
			vehicle.refresh();

			// update routing info
			IRouting routing = PluginManager.getRoutingImpl(scenario
					.getRoutingType(vtypeToBuild));
			Link targetLink = routing.getSucceedingLink(link, vehicle
					.getVehicleType().getVehicleClass(), scenario.getTimer()
					.getForwardedTime(), rand);
			vehicle.targetLink(targetLink);

			// add vehicle to the simulation
			vehicles.add(vehicle);

			// Test
			StringBuffer sb = new StringBuffer();
			sb.append("Time: " + time + "s -- " + "New Vehicle: ");
			sb.append(vehicle.getName());
			sb.append(" || ");
			sb.append("VehicleType -> ");
			sb.append(vtypeToBuild.getName());
			sb.append(" || ");
			sb.append("DriverType -> ");
			sb.append(dtypeToBuild.getName());
			System.out.println(sb.toString());
		}

		return vehicles;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void upgrade() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void activate() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivate() throws Exception {
		// TODO Auto-generated method stub

	}
}
