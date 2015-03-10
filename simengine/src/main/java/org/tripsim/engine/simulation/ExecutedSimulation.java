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
package org.tripsim.engine.simulation;

import java.util.Date;

public class ExecutedSimulation {

	Date timestamp;
	String name;
	String networkName;
	String odMatrixName;

	SimulationSettings settings;

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNetworkName() {
		return networkName;
	}

	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}

	public String getOdMatrixName() {
		return odMatrixName;
	}

	public void setOdMatrixName(String odMatrixName) {
		this.odMatrixName = odMatrixName;
	}

	public SimulationSettings getSettings() {
		return settings;
	}

	public void setSettings(SimulationSettings settings) {
		this.settings = settings;
	}

	public long getTotalFrames() {
		return Math.round(settings.getDuration() / settings.getStepSize());
	}

	public long getDuration() {
		return settings.getDuration();
	}

	public double getStepSize() {
		return settings.getStepSize();
	}
}
