package org.tripsim.engine.od;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tripsim.api.model.OdMatrix;
import org.tripsim.data.dom.OdMatrixDo;
import org.tripsim.data.persistence.OdMatrixDao;

@Service("default-od-manager")
class DefaultOdManager implements OdManager {

	private static final Logger logger = LoggerFactory
			.getLogger(DefaultOdManager.class);

	@Autowired
	OdMatrixDao odMatrixDao;
	@Autowired
	OdConverter converter;

	@Override
	public void insertOdMatrix(OdMatrix odMatrix) {
		if (odMatrix == null) {
			throw new RuntimeException(
					"OdMatrix is null, cannot be saved to db!");
		}
		if (odMatrixDao.countByName(odMatrix.getName()) != 0) {
			logger.info("OdMatrix '{}' already exists in db!",
					odMatrix.getName());

		}
		OdMatrixDo entity = converter.toOdMatrixDo(odMatrix);
		odMatrixDao.save(entity);
	}

	@Override
	public void saveOdMatrix(OdMatrix odMatrix) {
		if (odMatrix == null) {
			throw new RuntimeException(
					"OdMatrix is null, cannot be saved to db!");
		}
		OdMatrixDo entity = odMatrixDao.findByName(odMatrix.getName());
		if (entity == null) {
			entity = converter.toOdMatrixDo(odMatrix);
		} else {
			converter.applyOdMatrixDo(entity, odMatrix);
		}
		odMatrixDao.save(entity);
	}

	@Override
	public OdMatrix loadOdMatrix(String name) {
		OdMatrixDo odMatrix = odMatrixDao.findByName(name);
		if (odMatrix == null) {
			return null;
		}
		return converter.toOdMatrix(odMatrix);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getOdMatrixNames(String networkName) {
		return (List<String>) odMatrixDao.getTypeField("name");
	}

}