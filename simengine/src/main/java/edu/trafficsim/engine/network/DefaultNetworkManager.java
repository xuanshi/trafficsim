package edu.trafficsim.engine.network;

import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vividsolutions.jts.io.ParseException;

import edu.trafficsim.data.dom.NetworkDo;
import edu.trafficsim.data.persistence.NetworkDao;
import edu.trafficsim.model.Network;
import edu.trafficsim.model.core.ModelInputException;

@Service("default-network-manager")
class DefaultNetworkManager implements NetworkManager {

	private final static Logger logger = LoggerFactory
			.getLogger(DefaultNetworkManager.class);

	@Autowired
	NetworkDao networkDao;
	@Autowired
	NetworkConverter converter;

	@Override
	public void insertNetwork(Network network) {
		if (network == null) {
			throw new RuntimeException(
					"Network is null, cannot be saved to db!");
		}
		if (networkDao.countByName(network.getName()) != 0) {
			logger.info("Network '{}' already exists in db!", network.getName());

		}
		NetworkDo entity = converter.toNetworkDo(network);
		networkDao.save(entity);
	}

	@Override
	public void saveNetwork(Network network) {
		if (network == null) {
			throw new RuntimeException(
					"Network is null, cannot be saved to db!");
		}
		NetworkDo entity = networkDao.findByName(network.getName());
		converter.applyNetworkDo(entity, network);
		networkDao.save(entity);
	}

	@Override
	public Network loadNetwork(String name) {
		NetworkDo network = networkDao.findByName(name);
		if (network == null) {
			return null;
		}
		try {
			return converter.toNetwork(network);
		} catch (ParseException | ModelInputException | TransformException e) {
			throw new RuntimeException(e);
		}
	}

}
