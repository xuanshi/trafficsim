package edu.trafficsim.model.network;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.Point;

import edu.trafficsim.model.Connector;
import edu.trafficsim.model.Lane;
import edu.trafficsim.model.Link;
import edu.trafficsim.model.Node;
import edu.trafficsim.model.Router;
import edu.trafficsim.model.core.AbstractLocation;
import edu.trafficsim.model.core.ModelInputException;

public class DefaultNode extends AbstractLocation<DefaultNode> implements Node {

	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_INITIAL_CONNECTOR_SET_CAPACITY = 2;

	// public static final short MOVEMENT_RESTRICTED = -1;
	// public static final short MOVEMENT_RIGHT = 1;
	// public static final short MOVEMENT_THROUGH = 2;
	// public static final short MOVEMENT_LEFT = 3;
	// public static final short MOVEMENT_UTURN = 4;

	private NodeType nodeType;

	private Router router;

	private final Set<Link> downstreams = new HashSet<Link>();
	private final Set<Link> upstreams = new HashSet<Link>();

	private final Map<Lane, Set<Connector>> connectors = new HashMap<Lane, Set<Connector>>();

	public DefaultNode(String name, NodeType nodeType, Point point,
			double radius) {
		this(name, nodeType, point, radius, null);
	}

	public DefaultNode(String name, NodeType nodeType, Point point,
			double radius, Router router) {
		super(point, radius);
		setName(name);
		this.nodeType = nodeType;
		this.router = router;
	}

	@Override
	public final void add(Link link) throws ModelInputException {
		if (link.getEndNode() == this)
			upstreams.add(link);
		else if (link.getStartNode() == this)
			downstreams.add(link);
		else
			throw new ModelInputException(
					"The link doesnt start or end from the node.");
	}

	public final void remove(Link link) {
		upstreams.remove(link);
		downstreams.remove(link);
	}

	@Override
	public final boolean upstream(Link link) {
		return upstreams.contains(link);
	}

	@Override
	public final boolean downstream(Link link) {
		return downstreams.contains(link);
	}

	@Override
	public final Collection<Link> getUpstreams() {
		return Collections.unmodifiableCollection(upstreams);
	}

	@Override
	public final Collection<Link> getDownstreams() {
		return Collections.unmodifiableCollection(downstreams);
	}

	@Override
	public final NodeType getNodeType() {
		return nodeType;
	}

	@Override
	public Collection<Connector> getConnectors(Lane fromLane) {
		return Collections.unmodifiableCollection(connectors.get(fromLane));
	}

	public void add(Connector connector) {
		Set<Connector> fromLaneConnectors = connectors.get(connector
				.getFromLane());
		if (fromLaneConnectors == null) {
			fromLaneConnectors = new HashSet<Connector>(
					DEFAULT_INITIAL_CONNECTOR_SET_CAPACITY);
			connectors.put(connector.getFromLane(), fromLaneConnectors);
		}
		fromLaneConnectors.add(connector);
	}

	@Override
	public final Router getRouter() {
		return router;
	}

	@Override
	public final void setRouter(Router router) {
		this.router = router;
	}

	@Override
	public void transform(CoordinateFilter filter) {
		super.transform(filter);
		// TODO transform unit properly
	}
}
