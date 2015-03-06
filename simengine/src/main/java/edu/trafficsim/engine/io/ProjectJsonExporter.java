package edu.trafficsim.engine.io;

import static edu.trafficsim.engine.io.ProjectImportExportConstant.CONNECTORS;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.DESCRIPTION;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.DESTINATION;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.DRIVERCOMPOSITION;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.DRIVERCOMPOSITIONS;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.DRIVERTYPES;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.DURATION;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.END;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.ENDNODE;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.FROMLANE;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.GEOM;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.HIGHWAY;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.ID;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.LANES;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.LINKS;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.LINKTYPE;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.NAME;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.NETWORK;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.NETWORKNAME;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.NODES;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.NODESTINATION;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.NODETYPE;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.ODMATRIX;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.ODS;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.ORIGIN;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.PROBABILITIES;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.REVERSELINKID;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.ROADID;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.ROADINFO;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.ROADINFOS;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.ROADNAME;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.SD;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.SEED;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.SETTINGS;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.SHIFT;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.START;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.STARTNODE;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.STEPSIZE;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.TIMES;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.TOLANE;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.VEHICLECOMPOSITION;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.VEHICLECOMPOSITIONS;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.VEHICLETYPES;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.VPHS;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.WARMUP;
import static edu.trafficsim.engine.io.ProjectImportExportConstant.WIDTH;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerator;

import edu.trafficsim.api.model.Connector;
import edu.trafficsim.api.model.Lane;
import edu.trafficsim.api.model.Link;
import edu.trafficsim.api.model.Network;
import edu.trafficsim.api.model.Node;
import edu.trafficsim.api.model.Od;
import edu.trafficsim.api.model.OdMatrix;
import edu.trafficsim.api.model.RoadInfo;
import edu.trafficsim.api.model.TypesComposition;
import edu.trafficsim.engine.simulation.SimulationProject;
import edu.trafficsim.engine.simulation.SimulationSettings;
import edu.trafficsim.util.JsonSerializer;
import edu.trafficsim.util.WktUtils;

final class ProjectJsonExporter {

	public static void exportProject(SimulationProject project, OutputStream out)
			throws IOException {

		JsonGenerator generator = JsonSerializer.createJsonGenerator(out);

		Set<RoadInfo> roadInfos = new HashSet<RoadInfo>();
		Set<TypesComposition> vehicleCompositions = new HashSet<TypesComposition>();
		Set<TypesComposition> driverCompositions = new HashSet<TypesComposition>();

		// start project (root)
		generator.writeStartObject();

		// start network
		Network network = project.getNetwork();
		generator.writeObjectFieldStart(NETWORK);
		if (network != null) {
			generator.writeFieldName(NAME);
			generator.writeString(network.getName());

			// start nodes
			generator.writeArrayFieldStart(NODES);
			for (Node node : network.getNodes()) {
				// start node
				generator.writeStartObject();
				generator.writeFieldName(ID);
				generator.writeNumber(node.getId());
				generator.writeFieldName(DESCRIPTION);
				generator.writeString(node.getDescription());
				generator.writeFieldName(GEOM);
				generator.writeString(WktUtils.toWKT(node.getPoint()));
				generator.writeFieldName(NODETYPE);
				generator.writeString(node.getNodeType());

				// start connectors
				generator.writeArrayFieldStart(CONNECTORS);
				for (Connector connector : node.getConnectors()) {
					// start connector
					generator.writeStartObject();
					generator.writeFieldName(ID);
					generator.writeNumber(connector.getId());
					generator.writeFieldName(FROMLANE);
					generator.writeNumber(connector.getFromLane().getId());
					generator.writeFieldName(TOLANE);
					generator.writeNumber(connector.getToLane().getId());
					generator.writeEndObject();
					// end node
				}
				generator.writeEndArray();
				// end connectors
				generator.writeEndObject();
				// end node
			}
			generator.writeEndArray();
			// end nodes

			// start links
			generator.writeArrayFieldStart(LINKS);
			for (Link link : network.getLinks()) {
				// start link
				generator.writeStartObject();
				generator.writeFieldName(ID);
				generator.writeNumber(link.getId());
				generator.writeFieldName(DESCRIPTION);
				generator.writeString(link.getDescription());
				generator.writeFieldName(LINKTYPE);
				generator.writeString(link.getLinkType());
				generator.writeFieldName(GEOM);
				generator.writeString(WktUtils.toWKT(link.getLinearGeom()));
				if (link.getReverseLink() != null) {
					generator.writeFieldName(REVERSELINKID);
					generator.writeNumber(link.getReverseLink().getId());
				}
				generator.writeNumber(link.getId());
				generator.writeFieldName(STARTNODE);
				generator.writeNumber(link.getStartNode().getId());
				generator.writeFieldName(ENDNODE);
				generator.writeNumber(link.getEndNode().getId());
				generator.writeFieldName(ROADINFO);
				generator.writeNumber(link.getRoadInfo().getId());

				// start lanes
				generator.writeArrayFieldStart(LANES);
				for (Lane lane : link.getLanes()) {
					// start lane
					generator.writeStartObject();
					generator.writeFieldName(ID);
					generator.writeNumber(lane.getId());
					generator.writeFieldName(START);
					generator.writeNumber(lane.getStart());
					generator.writeFieldName(END);
					generator.writeNumber(lane.getEnd());
					generator.writeFieldName(WIDTH);
					generator.writeNumber(lane.getWidth());
					generator.writeFieldName(SHIFT);
					generator.writeNumber(lane.getShift());
					generator.writeEndObject();
					// end lane
				}
				generator.writeEndArray();
				// end lanes
				generator.writeEndObject();
				// end link

				roadInfos.add(link.getRoadInfo());
			}
			generator.writeEndArray();
			// end links
			generator.writeEndObject();
			// end network

			// start road infos
			generator.writeArrayFieldStart(ROADINFOS);
			for (RoadInfo roadInfo : roadInfos) {
				// start road info
				generator.writeStartObject();
				generator.writeFieldName(ID);
				generator.writeNumber(roadInfo.getId());
				generator.writeFieldName(ROADID);
				generator.writeNumber(roadInfo.getRoadId());
				generator.writeFieldName(ROADNAME);
				generator.writeString(roadInfo.getRoadName());
				generator.writeFieldName(HIGHWAY);
				generator.writeString(roadInfo.getHighway());
				generator.writeEndObject();
				// end road info
			}
			generator.writeEndArray();
			// end road infos
		}

		// start odMatrix
		OdMatrix odMatrix = project.getOdMatrix();
		if (odMatrix != null) {
			generator.writeObjectFieldStart(ODMATRIX);
			generator.writeFieldName(NAME);
			generator.writeString(odMatrix.getName());
			generator.writeFieldName(NETWORKNAME);
			generator.writeString(odMatrix.getNetworkName());

			// start ods
			generator.writeArrayFieldStart(ODS);
			for (Od od : odMatrix.getOds()) {
				// start od
				generator.writeStartObject();
				generator.writeFieldName(ID);
				generator.writeNumber(od.getId());
				generator.writeFieldName(ORIGIN);
				generator.writeNumber(od.getOriginNodeId());
				generator.writeFieldName(DESTINATION);
				generator
						.writeNumber(od.getDestinationNodeId() == null ? NODESTINATION
								: od.getDestinationNodeId());
				generator.writeFieldName(VEHICLECOMPOSITION);
				generator.writeString(od.getVehicleComposition().getName());
				generator.writeFieldName(DRIVERCOMPOSITION);
				generator.writeString(od.getDriverComposition().getName());

				// start time intervals
				generator.writeArrayFieldStart(TIMES);
				for (Double d : od.getJumpTimes())
					generator.writeNumber(d);
				generator.writeEndArray();
				// end time intervals

				// start vph
				generator.writeArrayFieldStart(VPHS);
				for (Integer i : od.getVphs())
					generator.writeNumber(i);
				generator.writeEndArray();
				// end vph

				generator.writeEndObject();
				// end od

				vehicleCompositions.add(od.getVehicleComposition());
				driverCompositions.add(od.getDriverComposition());
			}
			generator.writeEndArray();
			// end ods

			// start turn percentages
			// TODO
			// end turn percentages

			generator.writeEndObject();
			// end odMatrix
		}

		// start vehicle compositions
		generator.writeArrayFieldStart(VEHICLECOMPOSITIONS);
		for (TypesComposition composition : vehicleCompositions) {
			// start vehicle composition
			generator.writeStartObject();
			generator.writeFieldName(NAME);
			generator.writeString(composition.getName());

			// start vehicle type
			generator.writeArrayFieldStart(VEHICLETYPES);
			for (String v : composition.getTypes()) {
				generator.writeString(v);
			}
			generator.writeEndArray();
			// end vehicle type

			// start vehicle probability
			generator.writeArrayFieldStart(PROBABILITIES);
			for (Double d : composition.values())
				generator.writeNumber(d);
			generator.writeEndArray();
			// end vehicle probability

			generator.writeEndObject();
			// end vehicle composition
		}
		generator.writeEndArray();
		// end vehicle compositions

		// start driver compositions
		generator.writeArrayFieldStart(DRIVERCOMPOSITIONS);
		for (TypesComposition composition : driverCompositions) {
			// start driver composition
			generator.writeStartObject();
			generator.writeFieldName(NAME);
			generator.writeString(composition.getName());

			// start driver type
			generator.writeArrayFieldStart(DRIVERTYPES);
			for (String d : composition.getTypes()) {
				generator.writeString(d);
			}
			generator.writeEndArray();
			// end driver type

			// start driver probability
			generator.writeArrayFieldStart(PROBABILITIES);
			for (Double d : composition.values())
				generator.writeNumber(d);
			generator.writeEndArray();
			// end driver probability

			generator.writeEndObject();
			// end driver composition
		}
		generator.writeEndArray();
		// end driver compositions

		// start simulation settings
		SimulationSettings settings = project.getSettings();
		generator.writeObjectFieldStart(SETTINGS);
		generator.writeFieldName(DURATION);
		generator.writeNumber(settings.getDuration());
		generator.writeFieldName(WARMUP);
		generator.writeNumber(settings.getWarmup());
		generator.writeFieldName(STEPSIZE);
		generator.writeNumber(settings.getStepSize());
		generator.writeFieldName(SEED);
		generator.writeNumber(settings.getSeed());
		generator.writeFieldName(SD);
		generator.writeNumber(settings.getSd());
		generator.writeEndObject();
		// end settings

		generator.writeEndObject();
		// end simulation project

		generator.close();
	}
}
