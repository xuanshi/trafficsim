package edu.trafficsim.web.controller;

import java.util.Map;

import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.trafficsim.model.Simulator;
import edu.trafficsim.web.service.SimulationService;

@Controller
@RequestMapping(value = "/simulator")
public class SimulatorController extends AbstractController {

	@Autowired
	SimulationService simulationService;

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String simulatorViw(Model model) {
		Simulator simulator = project.getSimulator();
		if (simulator == null)
			return "components/empty";

		model.addAttribute("simulator", simulator);
		return "components/simulator";
	}

	@RequestMapping(value = "/run", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> runSimulation() {
		try {
			simulationService.runSimulation();
		} catch (TransformException e) {
			return failureResponse("Transform issues!");
		}
		return messageOnlySuccessResponse("Simulation finished.");
	}
}