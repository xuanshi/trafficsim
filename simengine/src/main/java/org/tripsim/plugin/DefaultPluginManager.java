/*
 * Copyright (c) 2015 Xuan Shi
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.a
 * 
 * @author Xuan Shi
 */
package org.tripsim.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tripsim.plugin.api.ICarFollowing;
import org.tripsim.plugin.api.IDriver;
import org.tripsim.plugin.api.ILaneChanging;
import org.tripsim.plugin.api.IMoving;
import org.tripsim.plugin.api.IRouting;
import org.tripsim.plugin.api.ISimulating;
import org.tripsim.plugin.api.IVehicle;
import org.tripsim.plugin.api.IVehicleGenerating;
import org.tripsim.plugin.api.PluginManager;

/**
 * 
 * 
 * @author Xuan Shi
 */
@Service("default-plugin-manager")
class DefaultPluginManager implements PluginManager {

	private static final Logger logger = LoggerFactory
			.getLogger(DefaultPluginManager.class);

	@Autowired
	private Map<String, ISimulating> simulatings = new HashMap<String, ISimulating>();
	@Autowired
	private Map<String, IVehicleGenerating> vehicleGeneratings = new HashMap<String, IVehicleGenerating>();
	@Autowired
	private Map<String, IMoving> movings = new HashMap<String, IMoving>();
	@Autowired
	private Map<String, IRouting> routings = new HashMap<String, IRouting>();
	@Autowired
	private Map<String, ICarFollowing> carFollowings = new HashMap<String, ICarFollowing>();
	@Autowired
	private Map<String, ILaneChanging> laneChangings = new HashMap<String, ILaneChanging>();
	@Autowired
	private Map<String, IVehicle> vehicleImpls = new HashMap<String, IVehicle>();
	@Autowired
	private Map<String, IDriver> driverImpls = new HashMap<String, IDriver>();

	@Override
	public Set<String> getSimulatings() {
		return simulatings.keySet();
	}

	@Override
	public ISimulating getSimulatingImpl(String name) {
		ISimulating impl = simulatings.get(name);
		if (impl == null) {
			logger.debug("ISimulating '{}' not found, using default.", name);
			impl = simulatings.get(DEFAULT_SIMULATING);
		}
		return impl;
	}

	@Override
	public Set<String> getMovings() {
		return movings.keySet();
	}

	@Override
	public IMoving getMovingImpl(String name) {
		IMoving impl = movings.get(name);
		if (impl == null) {
			logger.debug("IMoving '{}' not found, Using default.", name);
			impl = movings.get(DEFAULT_MOVING);
		}
		return impl;
	}

	@Override
	public Set<String> getCarFollowings() {
		return carFollowings.keySet();
	}

	@Override
	public ICarFollowing getCarFollowingImpl(String name) {
		ICarFollowing impl = carFollowings.get(name);
		if (impl == null) {
			logger.debug("ICarFollowing '{}' not found, Using default.", name);
			impl = carFollowings.get(DEFAULT_CAR_FOLLOWING);
		}
		return impl;
	}

	@Override
	public Set<String> getLaneChangings() {
		return laneChangings.keySet();
	}

	@Override
	public ILaneChanging getLaneChangingImpl(String name) {
		ILaneChanging impl = laneChangings.get(name);
		if (impl == null) {
			logger.debug("ILaneChanging '{}' not found, Using default.", name);
			impl = laneChangings.get(DEFAULT_LANE_CHANGING);
		}
		return impl;
	}

	@Override
	public Set<String> getRoutings() {
		return routings.keySet();
	}

	@Override
	public IRouting getRoutingImpl(String name) {
		IRouting impl = routings.get(name);
		if (impl == null) {
			logger.debug("IRouting '{}' not found, Using default. ", name);
			impl = routings.get(DEFAULT_ROUTING);
		}
		return impl;
	}

	@Override
	public Set<String> getVehicleGeneratings() {
		return vehicleGeneratings.keySet();
	}

	@Override
	public IVehicleGenerating getVehicleGeneratingImpl(String name) {
		IVehicleGenerating impl = vehicleGeneratings.get(name);
		if (impl == null) {
			logger.debug("IVehicleGenerating '{}' not found, Using default.",
					name);
			impl = vehicleGeneratings.get(DEFAULT_GENERATING);
		}
		return impl;
	}

	@Override
	public Set<String> getVehicleImpls() {
		return vehicleImpls.keySet();
	}

	@Override
	public IVehicle getVehicleImpl(String name) {
		IVehicle impl = vehicleImpls.get(name);
		if (impl == null) {
			logger.debug("IVehicle '{}' not found, Using default type.", name);
			impl = vehicleImpls.get(DEFAULT_VEHICLE_IMPL);
		}
		return impl;
	}

	@Override
	public Set<String> getDriverImpls() {
		return driverImpls.keySet();
	}

	@Override
	public IDriver getDriverImpl(String name) {
		IDriver impl = driverImpls.get(name);
		if (impl == null) {
			logger.debug("IDriver '{}' not found, Using default type.", name);
			impl = driverImpls.get(DEFAULT_DRIVER_IMPL);
		}
		return impl;
	}

}
