package edu.trafficsim.data.persistence.impl;

import java.util.Date;
import java.util.List;

import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Repository;

import edu.trafficsim.data.dom.OdMatrixDo;
import edu.trafficsim.data.persistence.OdMatrixDao;

@Repository("odMatrix-dao")
class OdMatrixDoImpl extends AbstractDaoImpl<OdMatrixDo> implements OdMatrixDao {

	@Override
	public OdMatrixDo findByName(String name) {
		return createQuery(name).get();
	}

	@Override
	public long countByName(String name) {
		return createQuery(name).countAll();
	}

	Query<OdMatrixDo> createQuery(String name) {
		return datastore.createQuery(OdMatrixDo.class).field("name")
				.equal(name);
	}

	@Override
	public List<OdMatrixDo> findByNetworkName(String networkName) {
		return datastore.createQuery(OdMatrixDo.class).field("networkName")
				.equal(networkName).asList();
	}

	@Override
	public void save(OdMatrixDo entity) {
		entity.setDate(new Date());
		super.save(entity);
	}

	@Override
	public void save(Iterable<OdMatrixDo> entities) {
		Date date = new Date();
		for (OdMatrixDo entity : entities) {
			entity.setDate(date);
		}
		super.save(entities);
	}
}