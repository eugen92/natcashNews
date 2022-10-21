package natcash.news.ext.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import natcash.news.sql.*;

import natcash.news.ext.dao.SysParamDao;
import natcash.news.ext.entity.SysParam;
@Repository
public class SysParamDaoImpl implements SysParamDao {
	private JdbcTemplate jdbcTemplate;

	public SysParamDaoImpl(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	@Override
	public SysParam getParam(String key) {
		if (key != null) {
			PreparedStatementSetter preparedStatementSetter = new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, key);
				}
			};
			ResultSetExtractor<SysParam> resultSetExtractor = new ResultSetExtractor<SysParam>() {
				@Override
				public SysParam extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						SysParam sysParam = new SysParam();
						String o1Key = rs.getString("o1_config_key");
						if (rs.wasNull()) {
							sysParam.setKey(null);
						} else {
							sysParam.setKey(o1Key);
						}
						String o1Value = rs.getString("o1_config_value");
						if (rs.wasNull()) {
							sysParam.setValue(null);
						} else {
							sysParam.setValue(o1Value);
						}
						return sysParam;
					}
					return null;
				}
			};
			return jdbcTemplate.query(QueryString.GET_PARAM,preparedStatementSetter, resultSetExtractor);
		}
		return null;
	}

}
