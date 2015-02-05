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
package edu.trafficsim.engine.demo;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;

import edu.trafficsim.engine.NetworkFactory;
import edu.trafficsim.engine.OdFactory;
import edu.trafficsim.engine.SimulationScenario;
import edu.trafficsim.engine.factory.DefaultNetworkFactory;
import edu.trafficsim.engine.factory.DefaultOdFactory;
import edu.trafficsim.engine.factory.Sequence;
import edu.trafficsim.engine.library.TypesLibrary;
import edu.trafficsim.model.Lane;
import edu.trafficsim.model.Link;
import edu.trafficsim.model.LinkType;
import edu.trafficsim.model.Network;
import edu.trafficsim.model.Node;
import edu.trafficsim.model.NodeType;
import edu.trafficsim.model.Od;
import edu.trafficsim.model.OdMatrix;
import edu.trafficsim.model.RoadInfo;
import edu.trafficsim.model.TurnPercentage;
import edu.trafficsim.model.VehicleType.VehicleClass;
import edu.trafficsim.model.core.ModelInputException;
import edu.trafficsim.model.util.GeoReferencing;
import edu.trafficsim.model.util.GeoReferencing.TransformCoordinateFilter;
import edu.trafficsim.utility.CoordinateTransformer;
import edu.trafficsim.utility.Timer;

// Hack to create nodes links...
/**
 * 
 * 
 * @author Xuan Shi
 */
public class DemoBuilder {

	private Sequence seq;
	private Network network;
	private OdMatrix odMatrix;
	private Timer timer;

	static TypesLibrary typesLibrary;

	public DemoBuilder() throws ModelInputException,
			NoSuchAuthorityCodeException, FactoryException, TransformException {
		manualBuild();
	}

	private void manualBuild() throws ModelInputException,
			NoSuchAuthorityCodeException, FactoryException, TransformException {

		seq = Sequence.create();
		typesLibrary = TypesLibrary.defaultLibrary();
		NetworkFactory networkFactory = DefaultNetworkFactory.getInstance();
		OdFactory odFactory = DefaultOdFactory.getInstance();

		// TODO using WTKReader, or other well known format reader if viable

		// Links and Nodes need to have there own coordinate object to avoid
		// transform errors
		// Coonectors, on the other hands, shared the end coordinate object with
		// connecting Links

		// Johnson@Randall
		Coordinate coord53596818 = new Coordinate(43.0728056, -89.409022);
		Coordinate coord53596819 = new Coordinate(43.072726, -89.408787);
		Coordinate coord1345424868 = new Coordinate(43.0726121, -89.4084588);
		Coordinate coord53596820 = new Coordinate(43.072565, -89.408323);
		Coordinate coord53596821 = new Coordinate(43.07241, -89.407859);
		Coordinate coord53596824 = new Coordinate(43.072325, -89.407584);
		Coordinate coord53596826 = new Coordinate(43.0722933, -89.4074786);
		// Johnson@Orchard
		Coordinate coord1345424866 = new Coordinate(43.0722666, -89.4073831);
		Coordinate coord1533633321 = new Coordinate(43.072231, -89.407203);
		Coordinate coord53596827 = new Coordinate(43.0722033, -89.4069557);
		Coordinate coord1345416864 = new Coordinate(43.0722028, -89.4069465);
		Coordinate coord1859358846 = new Coordinate(43.0721947, -89.406805);
		Coordinate coord53720210 = new Coordinate(43.0721884, -89.4066958);
		Coordinate coord1859358892 = new Coordinate(43.072183, -89.4065084);
		Coordinate coord53720208 = new Coordinate(43.072171, -89.40609);
		// Johnson@Charter
		Coordinate coord53607075 = new Coordinate(43.072159, -89.405751);

		// Johson from Randall to Orchard
		Coordinate[] coords1 = new Coordinate[] { coord53596818, coord53596819,
				coord1345424868, coord53596820, coord53596821, coord53596824,
				coord53596826, coord1345424866 };
		// Johson from Orchard to Charter
		Coordinate[] coords2 = new Coordinate[] {
				new Coordinate(coord1345424866), coord1533633321,
				coord53596827, coord1345416864, coord1859358846, coord53720210,
				coord1859358892, coord53720208, coord53607075 };

		// Nodes
		NodeType nodeType = typesLibrary.getDefaultNodeType();
		Node node1 = networkFactory.createNode(seq, "Johnson at Randall",
				nodeType, new Coordinate(coord53596818));
		Node node2 = networkFactory.createNode(seq, "Johnson at Orchardl",
				nodeType, new Coordinate(coord1345424866));
		Node node3 = networkFactory.createNode(seq, "Johnson at Charter",
				nodeType, new Coordinate(coord53607075));
		node1.setId(1l);
		node2.setId(2l);
		node3.setId(3l);
		// Node node4 = networkFactory.createNode("Johnson at Mill");
		// Node node5 = networkFactory.createNode("Johnson at Park");
		// Links
		LinkType linkType = typesLibrary.getDefaultLinkType();
		Link link1 = networkFactory.createLink(seq, "Johson1", linkType, node1,
				node2, coords1, null);
		Link link2 = networkFactory.createLink(seq, "Johson2", linkType, node2,
				node3, coords2, null);
		link1.setId(1l);
		link2.setId(2l);

		// RoadInfo
		RoadInfo info1 = networkFactory.createRoadInfo(seq, "Test name", 12345,
				"Test highway");
		RoadInfo info2 = networkFactory.createRoadInfo(seq, "Test name", 54321,
				"Test highway");
		link1.setRoadInfo(info1);
		link2.setRoadInfo(info2);

		// Network
		network = networkFactory.createNetwork(seq, "test");
		network.add(node1, node2, node3);
		network.add(link1, link2);
		network.discover();

		// Transform
		TransformCoordinateFilter filter = GeoReferencing.getTransformFilter(
				GeoReferencing.getCrs(GeoReferencing.CRS_CODE_4326),
				GeoReferencing.getCrs(GeoReferencing.CRS_CODE_900913));
		CoordinateTransformer.transform(network, filter);

		// Lanes
		Lane[] lanes1 = networkFactory.createLanes(seq, link1, 10, -10, 4, 3);
		Lane[] lanes2 = networkFactory.createLanes(seq, link2, 10, -10, 4, 3);
		// Connectors
		networkFactory.connect(seq, lanes1[0], lanes2[0], 4);
		networkFactory.connect(seq, lanes1[1], lanes2[1], 4);
		networkFactory.connect(seq, lanes1[2], lanes2[2], 4);

		// Origin Destination
		// no destination 0s ~ 100s 1000vph
		// no destination 100s~200s 800vph
		double[] times = new double[] { 300, 500 };
		Integer[] vphs = new Integer[] { 4000, 4800 };
		Od od = odFactory.createOd(seq, "od", node1, null,
				typesLibrary.getDefaultVehicleComposition(),
				typesLibrary.getDefaultDriverComposition(), times, vphs);

		odMatrix = odFactory.createOdMatrix(seq, "odm");
		odMatrix.add(od);

		// Turn Percentage
		double[] times1 = new double[] { 500 };
		TurnPercentage turnPercentage1 = odFactory.createTurnPercentage(seq,
				"tp1", link1, new Link[] { link2 }, new double[] { 1.0 });
		TurnPercentage[] turnPercentages = new TurnPercentage[] { turnPercentage1 };
		odMatrix.setTurnPercentage(link1, VehicleClass.Car, times1,
				turnPercentages);

		// Timer
		timer = Timer.create(100, 0.5, 0);
	}

	public SimulationScenario getScenario() {
		return SimulationScenario.create(network, odMatrix, timer, seq);
	}

	public TypesLibrary getTypesLibrary() {
		return typesLibrary;
	}

}