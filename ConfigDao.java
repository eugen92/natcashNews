package natcash.news.ext.dao;

import java.util.List;

import natcash.news.ext.dao.params.ConfigFindParams;
import natcash.news.ext.entity.ConfigObject;
public interface ConfigDao {
	List<ConfigObject> findConfigObject(ConfigFindParams configFindParams);
}
