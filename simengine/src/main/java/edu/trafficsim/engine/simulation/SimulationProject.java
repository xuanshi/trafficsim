package edu.trafficsim.engine.simulation;

import java.io.Serializable;

import edu.trafficsim.model.Network;
import edu.trafficsim.model.OdMatrix;

public class SimulationProject implements Serializable {

	private static final long serialVersionUID = 1L;

	String name;
	Network network;
	OdMatrix odMatrix;
	SimulationSettings settings;

	public SimulationProject(Network network, OdMatrix odMatrix,
			SimulationSettings settings) {
		this.network = network;
		this.odMatrix = odMatrix;
		this.settings = settings;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public OdMatrix getOdMatrix() {
		return odMatrix;
	}

	public void setOdMatrix(OdMatrix odMatrix) {
		this.odMatrix = odMatrix;
	}

	public SimulationSettings getSettings() {
		return settings;
	}

	public void setSettings(SimulationSettings settings) {
		this.settings = settings;
	}

}