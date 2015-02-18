package edu.trafficsim.data.persistence.impl;

import java.util.List;

import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

import edu.trafficsim.data.dom.ElementTypeDo;
import edu.trafficsim.data.dom.TypeCategoryDo;
import edu.trafficsim.data.persistence.ElementTypeDao;

@Repository("element-type-dao")
class ElementTypeDaoImpl extends AbstractDaoImpl<ElementTypeDo> implements
		ElementTypeDao {

	@SuppressWarnings("unchecked")
	@Override
	public String getDefaultTypeName(TypeCategoryDo category) {
		DBObject query = new BasicDBObjectBuilder().add("category", category)
				.add("defaultType", true).get();
		List<String> result = datastore.getCollection(ElementTypeDo.class)
				.distinct("name", query);
		return result.size() > 0 ? result.get(0) : null;
	}

	@Override
	public List<?> getTypeField(TypeCategoryDo category, String field) {
		return datastore.getCollection(ElementTypeDo.class).distinct(field,
				new BasicDBObject("category", category));
	}

	@Override
	public ElementTypeDo getByName(TypeCategoryDo category, String name) {
		return createQuery(category).field("name").equal(name).get();
	}

	@Override
	public ElementTypeDo getDefaultByCategory(TypeCategoryDo category) {
		return createQuery(category).field("defaultType").equal(true).get();
	}

	@Override
	public List<ElementTypeDo> getByCategory(TypeCategoryDo category) {
		return createQuery(category).asList();
	}

	private Query<ElementTypeDo> createQuery(TypeCategoryDo category) {
		return datastore.createQuery(ElementTypeDo.class).field("category")
				.equal(category);
	}
}
