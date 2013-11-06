package edu.trafficsim.engine.factory;

import java.util.List;

import org.geotools.geometry.jts.JTSFactoryFinder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

import edu.trafficsim.engine.NetworkFactory;
import edu.trafficsim.model.Lane;
import edu.trafficsim.model.Link;
import edu.trafficsim.model.LinkType;
import edu.trafficsim.model.Network;
import edu.trafficsim.model.Node;
import edu.trafficsim.model.NodeType;
import edu.trafficsim.model.Segment;
import edu.trafficsim.model.core.ModelInputException;
import edu.trafficsim.model.network.DefaultConnector;
import edu.trafficsim.model.network.DefaultLane;
import edu.trafficsim.model.network.DefaultLink;
import edu.trafficsim.model.network.DefaultNetwork;
import edu.trafficsim.model.network.DefaultNode;

public class DefaultNetworkFactory extends AbstractFactory implements
		NetworkFactory {

	private static DefaultNetworkFactory factory;
	private GeometryFactory geometryFactory;

	private final static double DEFAULT_RADIUS = 20.0d;
	private final static double DEFAULT_WIDTH = 4.0d;

	private DefaultNetworkFactory() {
		geometryFactory = JTSFactoryFinder.getGeometryFactory();
	}

	public static DefaultNetworkFactory getInstance() {
		if (factory == null)
			factory = new DefaultNetworkFactory();
		return factory;
	}

	@Override
	public Network createEmptyNetwork(String name) {
		return new DefaultNetwork(nextId(), name);
	}

	// TODO set default types
	private static NodeType nodeType = new NodeType(0, "temp");
	private static LinkType linkType = new LinkType(0, "temp");

	public Point createPoint(double x, double y) {
		return geometryFactory.createPoint(new Coordinate(x, y));
	}

	public Point createPoint(Coordinate coord) {
		return geometryFactory.createPoint(coord);
	}

	public LineString createLineString(Coordinate[] coords) {
		return geometryFactory.createLineString(coords);
	}

	public DefaultNode createNode(String name, double x, double y) {
		return createNode(name, createPoint(x, y));
	}

	@Override
	public DefaultNode createNode(String name, Coordinate coord) {
		return createNode(name, createPoint(coord));
	}

	public DefaultNode createNode(String name, Point point) {
		return new DefaultNode(nextId(), name, nodeType, point, DEFAULT_RADIUS);
	}

	@Override
	public DefaultLink createLink(String name, Node startNode, Node endNode,
			Coordinate[] coords) throws ModelInputException {
		LineString lineString = createLineString(coords);
		return createLink(name, startNode, endNode, lineString);
	}

	public DefaultLink createLink(String name, Node startNode, Node endNode,
			LineString lineString) throws ModelInputException {
		DefaultLink link = new DefaultLink(nextId(), name, linkType, startNode,
				endNode, lineString);
		startNode.add(link);
		return link;
	}

	@Override
	public DefaultLink createReverseLink(String name, Link link)
			throws ModelInputException {
		DefaultLink newLink = createLink(name, link.getEndNode(),
				link.getStartNode(), (LineString) link.getLinearGeom()
						.reverse());
		link.setReverseLink(newLink);
		return newLink;
	}

	@Override
	public DefaultLane createLane(Segment segment, double start, double end,
			double width, double shift, int laneId) throws ModelInputException {
		return new DefaultLane(nextId(), segment, start, end, width, shift,
				laneId);
	}

	@Override
	public List<Lane> createLanes(Link link, int num)
			throws ModelInputException {
		for (int i = 0; i < num; i++) {
			double shift = (num / 2.0 - i) * DEFAULT_WIDTH;
			DefaultLane lane = createLane(link, 0, 1, DEFAULT_WIDTH, shift, i);
			link.add(lane);
		}
		return link.getLanes();
	}

	@Override
	public DefaultConnector createConnector(Lane laneFrom, Lane laneTo)
			throws ModelInputException {
		DefaultConnector connector = new DefaultConnector(nextId(), laneFrom,
				laneTo, DEFAULT_WIDTH);
		Lane lane = createLane(connector, 0, 1, DEFAULT_WIDTH, 0, -1);
		connector.setLane(lane);

		Node node = ((Link) laneFrom.getSegment()).getEndNode();
		node.add(connector);
		return connector;
	}
}
