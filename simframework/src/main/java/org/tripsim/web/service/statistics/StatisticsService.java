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
package org.tripsim.web.service.statistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tripsim.engine.network.NetworkFactory;
import org.tripsim.engine.simulation.ExecutedSimulation;
import org.tripsim.engine.simulation.SimulationManager;
import org.tripsim.engine.statistics.LinkState;
import org.tripsim.engine.statistics.StatisticsFrames;
import org.tripsim.engine.statistics.StatisticsManager;
import org.tripsim.engine.statistics.VehicleProperty;
import org.tripsim.engine.statistics.VehicleState;
import org.tripsim.model.util.Colors;
import org.tripsim.util.MultiKeyedMap;
import org.tripsim.util.WktUtils;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * 
 * 
 * @author Xuan Shi
 */
@Service
public class StatisticsService {

	@Autowired
	SimulationManager simulationManager;
	@Autowired
	StatisticsManager manager;
	@Autowired
	NetworkFactory factory;

	// --------------------------------------------------
	// Frames for animation
	// --------------------------------------------------
	public FramesDto getFrames(String simulationName, long startFrame,
			long steps) {
		ExecutedSimulation simulation = simulationManager
				.findSimulation(simulationName);
		if (simulation == null || simulation.getTotalFrames() < startFrame) {
			return new FramesDto(startFrame, startFrame);
		}
		long endFrame = simulation.getTotalFrames() < startFrame + steps ? simulation
				.getTotalFrames() : startFrame + steps;
		StatisticsFrames<VehicleState> frames = manager.getVehicleStatistics(
				simulationName, startFrame, steps);
		FramesDto result = new FramesDto(startFrame, endFrame);
		result.setElements(toFrameElements(frames));
		result.setVehicles(toFrameVehicles(simulationName, frames.getIds()));
		return result;
	}

	private List<String> toFrameVehicles(String simulationName,
			Collection<Long> vids) {
		List<VehicleProperty> vehicles = manager.getVehicleProperties(
				simulationName, vids);
		List<String> result = new ArrayList<String>();
		for (VehicleProperty vehicle : vehicles) {
			result.add(toFrameVehicle(vehicle));
		}
		return result;
	}

	private String toFrameVehicle(VehicleProperty property) {
		StringBuilder sb = new StringBuilder();
		sb.append(property.getVid());
		sb.append(",");
		sb.append(property.getWidth());
		sb.append(",");
		sb.append(property.getLength());
		return sb.toString();
	}

	private List<String> toFrameElements(StatisticsFrames<VehicleState> frames) {
		List<String> result = new ArrayList<String>();
		for (Long vid : frames.getIds()) {
			for (VehicleState vs : frames.getStatesById(vid)) {
				result.add(toFrameElement(vs));
			}
		}
		return result;
	}

	private String toFrameElement(VehicleState vs) {
		StringBuilder sb = new StringBuilder();
		sb.append(vs.getSequence());
		sb.append(",");
		sb.append(vs.getVid());
		sb.append(",");
		sb.append(vs.getLon());
		sb.append(",");
		sb.append(vs.getLat());
		sb.append(",");
		sb.append(vs.getAngle());
		sb.append(",");
		String color = Colors.getVehicleColor(vs.getSpeed());
		sb.append(color);
		return sb.toString();
	}

	// --------------------------------------------------
	// Trajectories for vehicle trajectories at each node
	// --------------------------------------------------
	public TrajectoriesDto getTrajectories(String simulationName, long nodeId,
			long startFrame, long steps) {
		StatisticsFrames<VehicleState> states = manager
				.getTrajectoriesFromNode(simulationName, nodeId, startFrame,
						steps);
		TrajectoriesDto result = new TrajectoriesDto(nodeId, startFrame);
		List<String> trajectories = new ArrayList<String>();
		result.setTrajectories(trajectories);
		for (long vid : states.getIds()) {
			Collection<VehicleState> vs = states.getStatesById(vid);
			if (vs.size() > 1) {
				trajectories.add(toTrajectory(vs));
			}
		}
		return result;
	}

	private String toTrajectory(Collection<VehicleState> states) {
		Coordinate[] coords = new Coordinate[states.size()];
		int i = 0;
		for (VehicleState vs : states) {
			Coordinate coord = new Coordinate(vs.getLon(), vs.getLat());
			coords[i] = coord;
			i++;
		}
		return WktUtils.toWKT(factory.createLineString(coords));
	}

	// --------------------------------------------------
	// Time-space diagram for each link
	// --------------------------------------------------
	public MeasureDto getTsd(String simulationName, long linkId, long startFrame,
			long steps) {
		StatisticsFrames<LinkState> frames = manager.getLinkStatistics(
				simulationName, linkId, startFrame, steps);
		MultiKeyedMap<Long, Long, Double> data = toSeriesesData(frames
				.getStatesById(linkId));
		List<List<List<Number>>> serieses = toSerieses(data);
		MeasureDto result = new MeasureDto(linkId, startFrame);
		result.setSerieses(serieses);
		return result;
	}

	/**
	 * key1 -> vid, key2 -> sequence , value -> position
	 * 
	 * @param states
	 * @return
	 */
	private MultiKeyedMap<Long, Long, Double> toSeriesesData(
			Collection<LinkState> states) {
		MultiKeyedMap<Long, Long, Double> result = new MultiKeyedMap<Long, Long, Double>();
		for (LinkState ls : states) {
			long sequence = ls.getSequence();
			for (Map.Entry<Long, Double> entry : ls.getPositions().entrySet()) {
				long vid = entry.getKey();
				double position = entry.getValue();
				result.put(vid, sequence, position);
			}
		}
		return result;
	}

	private List<List<List<Number>>> toSerieses(
			MultiKeyedMap<Long, Long, Double> data) {
		List<List<List<Number>>> result = new ArrayList<List<List<Number>>>();
		for (long vid : data.getPrimayKeys()) {
			result.add(toSeries(data.getByPrimary(vid)));
		}
		return result;
	}

	private List<List<Number>> toSeries(Map<Long, Double> data) {
		List<List<Number>> result = new ArrayList<List<Number>>();
		for (Map.Entry<Long, Double> entry : data.entrySet()) {
			long sequence = entry.getKey();
			double position = entry.getValue();
			result.add(Arrays.asList((Number) sequence, (Number) position));
		}
		return result;
	}

	// --------------------------------------------------
	// Link Time-Volume diagram
	// --------------------------------------------------
	public MeasureDto getVolumes(String simulationName, long linkId,
			long startFrame, long steps) {
		StatisticsFrames<LinkState> frames = manager.getLinkStatistics(
				simulationName, linkId, startFrame, steps);
		Map<Long, Double> volumes = toVolume(frames.getStatesById(linkId));
		MeasureDto result = new MeasureDto(linkId, startFrame);
		result.setSerieses(toSerieses(volumes));
		return result;
	}

	private Map<Long, Double> toVolume(Collection<LinkState> states) {
		Map<Long, Double> result = new HashMap<Long, Double>();
		for (LinkState ls : states) {
			result.put(ls.getSequence(), ls.getVolume());
		}
		return result;
	}

	private List<List<List<Number>>> toSerieses(Map<Long, Double> data) {
		List<List<List<Number>>> serieses = new ArrayList<List<List<Number>>>(1);
		serieses.add(toSeries(data));
		return serieses;
	}

	// --------------------------------------------------
	// Link Time-Speed diagram
	// --------------------------------------------------
	public MeasureDto getAvgSpeeds(String simulationName, long linkId,
			long startFrame, long steps) {
		StatisticsFrames<LinkState> frames = manager.getLinkStatistics(
				simulationName, linkId, startFrame, steps);
		Map<Long, Double> speeds = toAvgSpeeds(frames.getStatesById(linkId));
		MeasureDto result = new MeasureDto(linkId, startFrame);
		result.setSerieses(toSerieses(speeds));
		return result;
	}

	private Map<Long, Double> toAvgSpeeds(Collection<LinkState> states) {
		Map<Long, Double> result = new HashMap<Long, Double>();
		for (LinkState ls : states) {
			result.put(ls.getSequence(), ls.getAvgSpeed());
		}
		return result;
	}

	// --------------------------------------------------
	// Fundamental diagram for network
	// --------------------------------------------------
	public FdDto getFd(String simulationName, long startFrame, long steps) {
		FdDto fd = new FdDto();
		return fd;
	}
}
