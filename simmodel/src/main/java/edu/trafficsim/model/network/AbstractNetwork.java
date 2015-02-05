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
package edu.trafficsim.model.network;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.trafficsim.model.BaseEntity;
import edu.trafficsim.model.Link;
import edu.trafficsim.model.Network;
import edu.trafficsim.model.Node;

/**
 * 
 * 
 * @author Xuan Shi
 * @param <T>
 *            the generic type
 */
public abstract class AbstractNetwork<T> extends BaseEntity<T> implements
		Network {

	private static final long serialVersionUID = 1L;

	protected final Map<Long, Node> nodes = new HashMap<Long, Node>();
	protected final Map<Long, Link> links = new HashMap<Long, Link>();

	public AbstractNetwork(long id, String name) {
		super(id, name);
	}

	@Override
	public Collection<Node> getNodes() {
		return Collections.unmodifiableCollection(nodes.values());
	}

	@Override
	public Collection<Link> getLinks() {
		return Collections.unmodifiableCollection(links.values());
	}

	@Override
	public boolean contains(Node node) {
		return nodes.values().contains(node);
	}

	@Override
	public boolean contains(Link link) {
		return links.values().contains(link);
	}

	public boolean containsNode(long id) {
		return nodes.get(id) == null;
	}

	public boolean containsLink(long id) {
		return links.get(id) == null;
	}

	@Override
	public Node getNode(long id) {
		return nodes.get(id);
	}

	@Override
	public Link getLink(long id) {
		return links.get(id);
	}

	@Override
	public void add(Node node) {
		nodes.put(node.getId(), node);
	}

	@Override
	public void add(Link link) {
		links.put(link.getId(), link);
	}

	@Override
	public void add(Node... nodes) {
		for (Node node : nodes)
			add(node);
	}

	@Override
	public void add(Link... links) {
		for (Link link : links)
			add(link);
	}

	@Override
	public Node removeNode(long id) {
		return nodes.remove(id);
	}

	@Override
	public Link removeLink(long id) {
		Link link = links.remove(id);
		link.getStartNode().remove(link);
		link.getEndNode().remove(link);
		return link;
	}

	@Override
	public Node removeNode(Node node) {
		return removeNode(node.getId());
	}

	@Override
	public Link removeLink(Link link) {
		return removeLink(link.getId());
	}

}