package natcash.news.ext.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import natcash.news.ext.dao.ConfigDao;
import natcash.news.ext.dao.params.ConfigFindParams;
import natcash.news.ext.entity.ConfigObject;
import natcash.news.sql.QueryString;

@Repository
public class ConfigDaoImpl implements ConfigDao {
	private JdbcTemplate jdbcTemplate;

	public ConfigDaoImpl(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	public List<ConfigObject> findConfigObject(ConfigFindParams configFindParams) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// TODO Auto-generated method stub
				StringBuilder wherePartBuilder = new StringBuilder();
				if (configFindParams.getConfigKey() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.config_key = ? ");
				}
				if (configFindParams.getLanguage() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.language = ? ");
				}
				if (configFindParams.getStatus() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.status = ? ");
				}
				if (configFindParams.getGroupId() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.group_config_id = ? ");
				}
				String wherePart = wherePartBuilder.toString();
				if (wherePart.isEmpty() == false) {
					wherePart = "WHERE " + wherePart;
				}
				String orderByPart = "";
				String sql = QueryString.GET_CONFIG_SQL+ wherePart + orderByPart;
				PreparedStatement ps = con.prepareStatement(sql);
				int paramIndex = 1;
				if (configFindParams.getConfigKey() != null) {
					ps.setString(paramIndex++, configFindParams.getConfigKey());
				}
				if (configFindParams.getLanguage() != null) {
					ps.setString(paramIndex++, configFindParams.getLanguage());
				}
				if (configFindParams.getStatus() != null) {
					ps.setString(paramIndex++, configFindParams.getStatus());
				}
				if (configFindParams.getGroupId() != null) {
					ps.setString(paramIndex++, configFindParams.getGroupId());
				}
				return ps;
			}
			
		};
		RowMapper<ConfigObject> rowMapper = new RowMapper<ConfigObject>() {

			@Override
			public ConfigObject mapRow(ResultSet rs, int rowNum) throws SQLException {
				ConfigObject config = new ConfigObject();
				String o1ConfigKey = rs.getString("o1_config_key");
				if (rs.wasNull()) {
					config.setConfigKey(null);
				} else {
					config.setConfigKey(o1ConfigKey);
				}
				String o1ConfigValue = rs.getString("o1_config_value");
				if (rs.wasNull()) {
					config.setConfigValue(null);
				} else {
					config.setConfigValue(o1ConfigValue);
				}
				String o1Language = rs.getString("o1_language");
				if (rs.wasNull()) {
					config.setLang(null);
				} else {
					config.setLang(o1Language);
				}
				String o1Status = rs.getString("o1_status");
				if (rs.wasNull()) {
					config.setStatus(null);
				} else {
					config.setStatus(o1Status);
				}
				String o1Group = rs.getString("o1_group_config_id");
				if (rs.wasNull()) {
					config.setGroupId(null);
				} else {
					config.setGroupId(o1Group);
				}
				
				return config;

			}

		};
		return jdbcTemplate.query(preparedStatementCreator, rowMapper);
	}

}
