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
package edu.trafficsim.plugin;

import java.util.Random;

import edu.trafficsim.model.Link;
import edu.trafficsim.model.OdMatrix;
import edu.trafficsim.model.VehicleType.VehicleClass;

/**
 * 
 * 
 * @author Xuan Shi
 */
public interface IRouting extends IPlugin {

	Link getSucceedingLink(OdMatrix odMatrix, Link precedingLink,
			VehicleClass vehicleClass, double forwardedTime, Random rand);

	Link getSucceedingLink(Link precedingLink, VehicleClass vehicleClass,
			double forwardedTime, Random rand);

}