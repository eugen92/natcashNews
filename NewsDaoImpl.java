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

import natcash.news.ext.dao.NewsDao;
import natcash.news.ext.dao.params.NewsFindParams;
import natcash.news.ext.entity.LanguageContent;
import natcash.news.ext.entity.News;
import natcash.news.sql.*;
@Repository
public class NewsDaoImpl implements NewsDao {
	private JdbcTemplate jdbcTemplate;

	public NewsDaoImpl(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<News> findNews(NewsFindParams newsFindParams) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// TODO Auto-generated method stub
				StringBuilder wherePartBuilder = new StringBuilder();
				if (newsFindParams.getNewsId() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.news_id = ? ");
				}
				if (newsFindParams.getCategoryID() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.category_id = ? ");
				}
				if (newsFindParams.getTitle() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.title = ? ");
				}
				if (newsFindParams.getAuthorID() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.author_id = ? ");
				}
				if (newsFindParams.getStatus() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.status = ? ");
				}
				if (newsFindParams.getLanguage() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o2.language = ? ");
				}
				String wherePart = wherePartBuilder.toString();
				if (wherePart.isEmpty() == false) {
					wherePart = "WHERE " + wherePart;
				}
				String orderByPart = "";
				if (newsFindParams.getOrderBy() != null) {
					String orderBy = newsFindParams.getOrderBy();
					orderByPart = "";
					for (String orderPart : orderBy.split(",")) {
						String orderField = null;
						String orderType = "ASC";
						String[] orderPartSplited = orderPart.split(" ");
						if (orderPartSplited.length == 1) {
							orderField = orderPartSplited[0];
						} else if (orderPartSplited.length == 2) {
							orderField = orderPartSplited[0];
							if (orderPartSplited[1].equalsIgnoreCase("desc")) {
								orderType = "DESC";
							} else {
								orderType = "ASC";
							}
						} else {
							throw new SQLException("Invalid orderby");
						}
						if (orderField != null) {
							if (orderField.equalsIgnoreCase("startTime")) {
								if (orderByPart.isEmpty()) {
									orderByPart = "ORDER BY ";
								} else {
									orderByPart = orderByPart + ", ";
								}
								orderByPart = orderByPart + "o1.start_time " + orderType;
							} else if (orderField.equalsIgnoreCase("endTime")) {
								if (orderByPart.isEmpty()) {
									orderByPart = "ORDER BY ";
								} else {
									orderByPart = orderByPart + ", ";
								}
								orderByPart = orderByPart + "o1.end_time " + orderType;
							} else {
								throw new SQLException("invalid order by field");
							}
						}
					}
				}
				String sql = QueryString.FIND_NEWS_SQL+ wherePart + orderByPart;
				if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0
						&& newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
					sql = "SELECT * from (SELECT ROWNUM RN,DATA.* FROM (" + sql + ") DATA ) WHERE RN >= ? AND RN < ? ";
				} else {
					if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0) {
						sql = "SELECT * from (SELECT ROWNUM RN,DATA.* FROM (" + sql + ") DATA ) WHERE RN < ? ";
					}
					if (newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
						sql = "SELECT * from (SELECT ROWNUM RN,DATA.* FROM (" + sql + ") DATA ) WHERE RN >= ? ";
					}
				}
				PreparedStatement ps = con.prepareStatement(sql);
				int paramIndex = 1;
				if (newsFindParams.getNewsId() != null) {
					ps.setLong(paramIndex++, newsFindParams.getNewsId());
				}
				if (newsFindParams.getCategoryID() != null) {
					ps.setLong(paramIndex++, newsFindParams.getCategoryID());
				}
				if (newsFindParams.getTitle() != null) {
					ps.setString(paramIndex++, newsFindParams.getTitle());
				}
				if (newsFindParams.getAuthorID() != null) {
					ps.setLong(paramIndex++, newsFindParams.getAuthorID());
				}
				if (newsFindParams.getStatus() != null) {
					ps.setLong(paramIndex++, newsFindParams.getStatus());
				}
				if(newsFindParams.getLanguage() != null) {
					ps.setString(paramIndex++, newsFindParams.getLanguage());
				}
				if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0
						&& newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
					ps.setLong(paramIndex++, newsFindParams.getQueryOffset().longValue());
					ps.setLong(paramIndex++,
							newsFindParams.getQueryOffset().longValue() + newsFindParams.getQueryLimit().longValue());
				} else {
					if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0) {
						ps.setLong(paramIndex++, newsFindParams.getQueryLimit().longValue());
					}
					if (newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
						ps.setLong(paramIndex++, newsFindParams.getQueryOffset().longValue());
					}
				}
				return ps;
			}
		};
		RowMapper<News> rowMapper = new RowMapper<News>() {

			@Override
			public News mapRow(ResultSet rs, int rowNum) throws SQLException {
				News news = new News();
				Long o1NewsId = rs.getLong("o1_news_id");
				if (rs.wasNull()) {
					news.setId(null);
				} else {
					news.setId(o1NewsId);
				}
				Long o1CategoryId = rs.getLong("o1_category_id");
				if (rs.wasNull()) {
					news.setCategoryID(null);
				} else {
					news.setCategoryID(o1CategoryId);
				}
				Long o1AuthorId = rs.getLong("o1_author_id");
				if (rs.wasNull()) {
					news.setAuthorID(null);
				} else {
					news.setAuthorID(o1AuthorId);
				}
				String o1Title = rs.getString("o2_config_value");
				if (rs.wasNull()) {
					news.setTitle(null);
				} else {
					news.setTitle(o1Title);
				}
				Long o1Status = rs.getLong("o1_status");
				if (rs.wasNull()) {
					news.setStatus(null);
				} else {
					news.setStatus(o1Status);
				}
				LanguageContent languageContent = new LanguageContent();
				Long o2LanguageID = rs.getLong("o2_id");
				if (rs.wasNull()) {
					languageContent.setId(null);
				} else {
					languageContent.setId(o2LanguageID);
				}
				String o2Language = rs.getString("o2_language");
				if (rs.wasNull()) {
					languageContent.setLanguage(null);
				} else {
					languageContent.setLanguage(o2Language);
				}
				String o2ConfigKey = rs.getString("o2_config_key");
				if (rs.wasNull()) {
					languageContent.setConfigKey(null);
				} else {
					languageContent.setConfigKey(o2ConfigKey);
				}
				String o2Value=rs.getString("o2_config_value");
				if (rs.wasNull()) {
					languageContent.setConfigValue(null);
				} else {
					languageContent.setConfigValue(o2Value);
				}
				if(languageContent.getId() !=null) {
					news.setLaguageContent(languageContent);
				}
				return news;

			}

		};
		return jdbcTemplate.query(preparedStatementCreator, rowMapper);
	}

	@Override
	public List<News> findNews(NewsFindParams newsFindParams, String language, Long positionGroudId) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// TODO Auto-generated method stub
				StringBuilder wherePartBuilder = new StringBuilder();
				if (newsFindParams.getNewsId() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.news_id = ? ");
				}
				if (newsFindParams.getCategoryID() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.category_id = ? ");
				}
				if (newsFindParams.getTitle() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.title = ? ");
				}
				if (newsFindParams.getAuthorID() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.author_id = ? ");
				}
				if (newsFindParams.getStatus() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.status = ? ");
				}
				if (newsFindParams.getLanguage() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o2.language = ? ");
				}
				String wherePart = wherePartBuilder.toString();
				
				if (wherePart.isEmpty() == false) {
					wherePart = "WHERE " + wherePart;
				}
				String orderByPart = "";
				if (newsFindParams.getOrderBy() != null) {
					String orderBy = newsFindParams.getOrderBy();
					orderByPart = "";
					for (String orderPart : orderBy.split(",")) {
						String orderField = null;
						String orderType = "ASC";
						String[] orderPartSplited = orderPart.split(" ");
						if (orderPartSplited.length == 1) {
							orderField = orderPartSplited[0];
						} else if (orderPartSplited.length == 2) {
							orderField = orderPartSplited[0];
							if (orderPartSplited[1].equalsIgnoreCase("desc")) {
								orderType = "DESC";
							} else {
								orderType = "ASC";
							}
						} else {
							throw new SQLException("Invalid orderby");
						}
						if (orderField != null) {
							if (orderField.equalsIgnoreCase("startTime")) {
								if (orderByPart.isEmpty()) {
									orderByPart = "ORDER BY ";
								} else {
									orderByPart = orderByPart + ", ";
								}
								orderByPart = orderByPart + "o1.start_time " + orderType;
							} else if (orderField.equalsIgnoreCase("endTime")) {
								if (orderByPart.isEmpty()) {
									orderByPart = "ORDER BY ";
								} else {
									orderByPart = orderByPart + ", ";
								}
								orderByPart = orderByPart + "o1.end_time " + orderType;
							} else {
								throw new SQLException("invalid order by field");
							}
						}
					}
				}
				String sql = QueryString.FIND_NEWS_BY_PARAMS_SQL+ wherePart + orderByPart;
				if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0
						&& newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
					sql = "SELECT * from (SELECT ROWNUM RN,DATA.* FROM (" + sql + ") DATA ) WHERE RN >= ? AND RN < ? ";
				} else {
					if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0) {
						sql = "SELECT * from (SELECT ROWNUM RN,DATA.* FROM (" + sql + ") DATA ) WHERE RN < ? ";
					}
					if (newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
						sql = "SELECT * from (SELECT ROWNUM RN,DATA.* FROM (" + sql + ") DATA ) WHERE RN >= ? ";
					}
				}
				
				PreparedStatement ps = con.prepareStatement(sql);
				int paramIndex = 1;
				if (newsFindParams.getNewsId() != null) {
					ps.setLong(paramIndex++, newsFindParams.getNewsId());
				}
				if (newsFindParams.getCategoryID() != null) {
					ps.setLong(paramIndex++, newsFindParams.getCategoryID());
				}
				if (newsFindParams.getTitle() != null) {
					ps.setString(paramIndex++, newsFindParams.getTitle());
				}
				if (newsFindParams.getAuthorID() != null) {
					ps.setLong(paramIndex++, newsFindParams.getAuthorID());
				}
				if (newsFindParams.getStatus() != null) {
					ps.setLong(paramIndex++, newsFindParams.getStatus());
				}
				if(newsFindParams.getLanguage() != null) {
					ps.setString(paramIndex++, newsFindParams.getLanguage());
				}
				if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0
						&& newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
					ps.setLong(paramIndex++, newsFindParams.getQueryOffset().longValue());
					ps.setLong(paramIndex++,
							newsFindParams.getQueryOffset().longValue() + newsFindParams.getQueryLimit().longValue());
				} else {
					if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0) {
						ps.setLong(paramIndex++, newsFindParams.getQueryLimit().longValue());
					}
					if (newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
						ps.setLong(paramIndex++, newsFindParams.getQueryOffset().longValue());
					}
				}
				return ps;
			}
		};
		RowMapper<News> rowMapper = new RowMapper<News>() {

			@Override
			public News mapRow(ResultSet rs, int rowNum) throws SQLException {
				News news = new News();
				Long o1NewsId = rs.getLong("o1_news_id");
				if (rs.wasNull()) {
					news.setId(null);
				} else {
					news.setId(o1NewsId);
				}
				Long o1CategoryId = rs.getLong("o1_category_id");
				if (rs.wasNull()) {
					news.setCategoryID(null);
				} else {
					news.setCategoryID(o1CategoryId);
				}
				Long o1AuthorId = rs.getLong("o1_author_id");
				if (rs.wasNull()) {
					news.setAuthorID(null);
				} else {
					news.setAuthorID(o1AuthorId);
				}
				String o1Title = rs.getString("o2_config_value");
				if (rs.wasNull()) {
					news.setTitle(null);
				} else {
					news.setTitle(o1Title);
				}
				Long o1Status = rs.getLong("o1_status");
				if (rs.wasNull()) {
					news.setStatus(null);
				} else {
					news.setStatus(o1Status);
				}
				LanguageContent languageContent = new LanguageContent();
				Long o2LanguageID = rs.getLong("o2_id");
				if (rs.wasNull()) {
					languageContent.setId(null);
				} else {
					languageContent.setId(o2LanguageID);
				}
				String o2Language = rs.getString("o2_language");
				if (rs.wasNull()) {
					languageContent.setLanguage(null);
				} else {
					languageContent.setLanguage(o2Language);
				}
				String o2ConfigKey = rs.getString("o2_config_key");
				if (rs.wasNull()) {
					languageContent.setConfigKey(null);
				} else {
					languageContent.setConfigKey(o2ConfigKey);
				}
				String o2Value=rs.getString("o2_config_value");
				if (rs.wasNull()) {
					languageContent.setConfigValue(null);
				} else {
					languageContent.setConfigValue(o2Value);
				}
				if(languageContent.getId() !=null) {
					news.setLaguageContent(languageContent);
				}
				return news;

			}

		};
		return jdbcTemplate.query(preparedStatementCreator, rowMapper);	}

	@Override
	public News addNews(NewsFindParams newsFindParams, News news) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// TODO Auto-generated method stub
				StringBuilder wherePartBuilder = new StringBuilder();
				if (newsFindParams.getNewsId() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.news_id = ? ");
				}
				if (newsFindParams.getCategoryID() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.category_id = ? ");
				}
				if (newsFindParams.getTitle() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.title = ? ");
				}
				if (newsFindParams.getAuthorID() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.author_id = ? ");
				}
				if (newsFindParams.getStatus() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.status = ? ");
				}
				if (newsFindParams.getLanguage() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o2.language = ? ");
				}
				String wherePart = wherePartBuilder.toString();
				
				if (wherePart.isEmpty() == false) {
					wherePart = "WHERE " + wherePart;
				}
				String orderByPart = "";
				if (newsFindParams.getOrderBy() != null) {
					String orderBy = newsFindParams.getOrderBy();
					orderByPart = "";
					for (String orderPart : orderBy.split(",")) {
						String orderField = null;
						String orderType = "ASC";
						String[] orderPartSplited = orderPart.split(" ");
						if (orderPartSplited.length == 1) {
							orderField = orderPartSplited[0];
						} else if (orderPartSplited.length == 2) {
							orderField = orderPartSplited[0];
							if (orderPartSplited[1].equalsIgnoreCase("desc")) {
								orderType = "DESC";
							} else {
								orderType = "ASC";
							}
						} else {
							throw new SQLException("Invalid orderby");
						}
						if (orderField != null) {
							if (orderField.equalsIgnoreCase("startTime")) {
								if (orderByPart.isEmpty()) {
									orderByPart = "ORDER BY ";
								} else {
									orderByPart = orderByPart + ", ";
								}
								orderByPart = orderByPart + "o1.start_time " + orderType;
							} else if (orderField.equalsIgnoreCase("endTime")) {
								if (orderByPart.isEmpty()) {
									orderByPart = "ORDER BY ";
								} else {
									orderByPart = orderByPart + ", ";
								}
								orderByPart = orderByPart + "o1.end_time " + orderType;
							} else {
								throw new SQLException("invalid order by field");
							}
						}
					}
				}
				String sql = QueryString.INSERT_NEWS_SQL+ wherePart + orderByPart;
				if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0
						&& newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
					sql = "SELECT * from (SELECT ROWNUM RN,DATA.* FROM (" + sql + ") DATA ) WHERE RN >= ? AND RN < ? ";
				} else {
					if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0) {
						sql = "SELECT * from (SELECT ROWNUM RN,DATA.* FROM (" + sql + ") DATA ) WHERE RN < ? ";
					}
					if (newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
						sql = "SELECT * from (SELECT ROWNUM RN,DATA.* FROM (" + sql + ") DATA ) WHERE RN >= ? ";
					}
				}
				
				PreparedStatement ps = con.prepareStatement(sql);
				int paramIndex = 1;
				if (newsFindParams.getNewsId() != null) {
					ps.setLong(paramIndex++, newsFindParams.getNewsId());
				}
				if (newsFindParams.getCategoryID() != null) {
					ps.setLong(paramIndex++, newsFindParams.getCategoryID());
				}
				if (newsFindParams.getTitle() != null) {
					ps.setString(paramIndex++, newsFindParams.getTitle());
				}
				if (newsFindParams.getAuthorID() != null) {
					ps.setLong(paramIndex++, newsFindParams.getAuthorID());
				}
				if (newsFindParams.getStatus() != null) {
					ps.setLong(paramIndex++, newsFindParams.getStatus());
				}
				if(newsFindParams.getLanguage() != null) {
					ps.setString(paramIndex++, newsFindParams.getLanguage());
				}
				if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0
						&& newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
					ps.setLong(paramIndex++, newsFindParams.getQueryOffset().longValue());
					ps.setLong(paramIndex++,
							newsFindParams.getQueryOffset().longValue() + newsFindParams.getQueryLimit().longValue());
				} else {
					if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0) {
						ps.setLong(paramIndex++, newsFindParams.getQueryLimit().longValue());
					}
					if (newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
						ps.setLong(paramIndex++, newsFindParams.getQueryOffset().longValue());
					}
				}
				return ps;
			}
		};
		RowMapper<News> rowMapper = new RowMapper<News>() {

			@Override
			public News mapRow(ResultSet rs, int rowNum) throws SQLException {
				News news = new News();
				Long o1NewsId = rs.getLong("o1_news_id");
				if (rs.wasNull()) {
					news.setId(null);
				} else {
					news.setId(o1NewsId);
				}
				Long o1CategoryId = rs.getLong("o1_category_id");
				if (rs.wasNull()) {
					news.setCategoryID(null);
				} else {
					news.setCategoryID(o1CategoryId);
				}
				Long o1AuthorId = rs.getLong("o1_author_id");
				if (rs.wasNull()) {
					news.setAuthorID(null);
				} else {
					news.setAuthorID(o1AuthorId);
				}
				String o1Title = rs.getString("o2_config_value");
				if (rs.wasNull()) {
					news.setTitle(null);
				} else {
					news.setTitle(o1Title);
				}
				Long o1Status = rs.getLong("o1_status");
				if (rs.wasNull()) {
					news.setStatus(null);
				} else {
					news.setStatus(o1Status);
				}
				LanguageContent languageContent = new LanguageContent();
				Long o2LanguageID = rs.getLong("o2_id");
				if (rs.wasNull()) {
					languageContent.setId(null);
				} else {
					languageContent.setId(o2LanguageID);
				}
				String o2Language = rs.getString("o2_language");
				if (rs.wasNull()) {
					languageContent.setLanguage(null);
				} else {
					languageContent.setLanguage(o2Language);
				}
				String o2ConfigKey = rs.getString("o2_config_key");
				if (rs.wasNull()) {
					languageContent.setConfigKey(null);
				} else {
					languageContent.setConfigKey(o2ConfigKey);
				}
				String o2Value=rs.getString("o2_config_value");
				if (rs.wasNull()) {
					languageContent.setConfigValue(null);
				} else {
					languageContent.setConfigValue(o2Value);
				}
				if(languageContent.getId() !=null) {
					news.setLaguageContent(languageContent);
				}
				return news;
			}
		};
		return (News) jdbcTemplate.query(preparedStatementCreator, rowMapper);	}

	@Override
	public News updateNews(NewsFindParams newsFindParams, News news) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// TODO Auto-generated method stub
				StringBuilder wherePartBuilder = new StringBuilder();
				if (newsFindParams.getNewsId() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.news_id = ? ");
				}
				if (newsFindParams.getCategoryID() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.category_id = ? ");
				}
				if (newsFindParams.getTitle() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.title = ? ");
				}
				if (newsFindParams.getAuthorID() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.author_id = ? ");
				}
				if (newsFindParams.getStatus() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.status = ? ");
				}
				if (newsFindParams.getLanguage() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o2.language = ? ");
				}
				String wherePart = wherePartBuilder.toString();
				
				if (wherePart.isEmpty() == false) {
					wherePart = "WHERE " + wherePart;
				}
				String orderByPart = "";
				if (newsFindParams.getOrderBy() != null) {
					String orderBy = newsFindParams.getOrderBy();
					orderByPart = "";
					for (String orderPart : orderBy.split(",")) {
						String orderField = null;
						String orderType = "ASC";
						String[] orderPartSplited = orderPart.split(" ");
						if (orderPartSplited.length == 1) {
							orderField = orderPartSplited[0];
						} else if (orderPartSplited.length == 2) {
							orderField = orderPartSplited[0];
							if (orderPartSplited[1].equalsIgnoreCase("desc")) {
								orderType = "DESC";
							} else {
								orderType = "ASC";
							}
						} else {
							throw new SQLException("Invalid orderby");
						}
						if (orderField != null) {
							if (orderField.equalsIgnoreCase("startTime")) {
								if (orderByPart.isEmpty()) {
									orderByPart = "ORDER BY ";
								} else {
									orderByPart = orderByPart + ", ";
								}
								orderByPart = orderByPart + "o1.start_time " + orderType;
							} else if (orderField.equalsIgnoreCase("endTime")) {
								if (orderByPart.isEmpty()) {
									orderByPart = "ORDER BY ";
								} else {
									orderByPart = orderByPart + ", ";
								}
								orderByPart = orderByPart + "o1.end_time " + orderType;
							} else {
								throw new SQLException("invalid order by field");
							}
						}
					}
				}
				String sql = QueryString.DELETE_NEWS_SQL+ wherePart + orderByPart;
				if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0
						&& newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
					sql = "SELECT * from (SELECT ROWNUM RN,DATA.* FROM (" + sql + ") DATA ) WHERE RN >= ? AND RN < ? ";
				} else {
					if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0) {
						sql = "SELECT * from (SELECT ROWNUM RN,DATA.* FROM (" + sql + ") DATA ) WHERE RN < ? ";
					}
					if (newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
						sql = "SELECT * from (SELECT ROWNUM RN,DATA.* FROM (" + sql + ") DATA ) WHERE RN >= ? ";
					}
				}
				
				PreparedStatement ps = con.prepareStatement(sql);
				int paramIndex = 1;
				if (newsFindParams.getNewsId() != null) {
					ps.setLong(paramIndex++, newsFindParams.getNewsId());
				}
				if (newsFindParams.getCategoryID() != null) {
					ps.setLong(paramIndex++, newsFindParams.getCategoryID());
				}
				if (newsFindParams.getTitle() != null) {
					ps.setString(paramIndex++, newsFindParams.getTitle());
				}
				if (newsFindParams.getAuthorID() != null) {
					ps.setLong(paramIndex++, newsFindParams.getAuthorID());
				}
				if (newsFindParams.getStatus() != null) {
					ps.setLong(paramIndex++, newsFindParams.getStatus());
				}
				if(newsFindParams.getLanguage() != null) {
					ps.setString(paramIndex++, newsFindParams.getLanguage());
				}
				if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0
						&& newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
					ps.setLong(paramIndex++, newsFindParams.getQueryOffset().longValue());
					ps.setLong(paramIndex++,
							newsFindParams.getQueryOffset().longValue() + newsFindParams.getQueryLimit().longValue());
				} else {
					if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0) {
						ps.setLong(paramIndex++, newsFindParams.getQueryLimit().longValue());
					}
					if (newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
						ps.setLong(paramIndex++, newsFindParams.getQueryOffset().longValue());
					}
				}
				return ps;
			}
		};
		RowMapper<News> rowMapper = new RowMapper<News>() {

			@Override
			public News mapRow(ResultSet rs, int rowNum) throws SQLException {
				News news = new News();
				Long o1NewsId = rs.getLong("o1_news_id");
				if (rs.wasNull()) {
					news.setId(null);
				} else {
					news.setId(o1NewsId);
				}
				Long o1CategoryId = rs.getLong("o1_category_id");
				if (rs.wasNull()) {
					news.setCategoryID(null);
				} else {
					news.setCategoryID(o1CategoryId);
				}
				Long o1AuthorId = rs.getLong("o1_author_id");
				if (rs.wasNull()) {
					news.setAuthorID(null);
				} else {
					news.setAuthorID(o1AuthorId);
				}
				String o1Title = rs.getString("o2_config_value");
				if (rs.wasNull()) {
					news.setTitle(null);
				} else {
					news.setTitle(o1Title);
				}
				Long o1Status = rs.getLong("o1_status");
				if (rs.wasNull()) {
					news.setStatus(null);
				} else {
					news.setStatus(o1Status);
				}
				LanguageContent languageContent = new LanguageContent();
				Long o2LanguageID = rs.getLong("o2_id");
				if (rs.wasNull()) {
					languageContent.setId(null);
				} else {
					languageContent.setId(o2LanguageID);
				}
				String o2Language = rs.getString("o2_language");
				if (rs.wasNull()) {
					languageContent.setLanguage(null);
				} else {
					languageContent.setLanguage(o2Language);
				}
				String o2ConfigKey = rs.getString("o2_config_key");
				if (rs.wasNull()) {
					languageContent.setConfigKey(null);
				} else {
					languageContent.setConfigKey(o2ConfigKey);
				}
				String o2Value=rs.getString("o2_config_value");
				if (rs.wasNull()) {
					languageContent.setConfigValue(null);
				} else {
					languageContent.setConfigValue(o2Value);
				}
				if(languageContent.getId() !=null) {
					news.setLaguageContent(languageContent);
				}
				return news;

			}

		};
		return (News) jdbcTemplate.query(preparedStatementCreator, rowMapper);}

	@Override
	public News findById(NewsFindParams newsFindParams, long id) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				News news = new News();
				StringBuilder wherePartBuilder = new StringBuilder();
				if (newsFindParams.getNewsId() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.news_id = ? ");
				}
				if (newsFindParams.getCategoryID() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.category_id = ? ");
				}
				if (newsFindParams.getTitle() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.title = ? ");
				}
				if (newsFindParams.getAuthorID() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.author_id = ? ");
				}
				if (newsFindParams.getStatus() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.status = ? ");
				}
				if (newsFindParams.getLanguage() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o2.language = ? ");
				}
				String wherePart = wherePartBuilder.toString();
				
				if (wherePart.isEmpty() == false) {
					wherePart = "WHERE " + wherePart;
				}
				String orderByPart = "";
				if (newsFindParams.getOrderBy() != null) {
					String orderBy = newsFindParams.getOrderBy();
					orderByPart = "";
					for (String orderPart : orderBy.split(",")) {
						String orderField = null;
						String orderType = "ASC";
						String[] orderPartSplited = orderPart.split(" ");
						if (orderPartSplited.length == 1) {
							orderField = orderPartSplited[0];
						} else if (orderPartSplited.length == 2) {
							orderField = orderPartSplited[0];
							if (orderPartSplited[1].equalsIgnoreCase("desc")) {
								orderType = "DESC";
							} else {
								orderType = "ASC";
							}
						} else {
							throw new SQLException("Invalid orderby");
						}
						if (orderField != null) {
							if (orderField.equalsIgnoreCase("startTime")) {
								if (orderByPart.isEmpty()) {
									orderByPart = "ORDER BY ";
								} else {
									orderByPart = orderByPart + ", ";
								}
								orderByPart = orderByPart + "o1.start_time " + orderType;
							} else if (orderField.equalsIgnoreCase("endTime")) {
								if (orderByPart.isEmpty()) {
									orderByPart = "ORDER BY ";
								} else {
									orderByPart = orderByPart + ", ";
								}
								orderByPart = orderByPart + "o1.end_time " + orderType;
							} else {
								throw new SQLException("invalid order by field");
							}
						}
					}
				}
				String sql = QueryString.UPDATE_NEWS_SQL+ wherePart + orderByPart;
				if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0
						&& newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
					sql = "SELECT * from (SELECT ROWNUM RN,DATA.* FROM (" + sql + ") DATA ) WHERE RN >= ? AND RN < ? ";
				} else {
					if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0) {
						sql = "SELECT * from (SELECT ROWNUM RN,DATA.* FROM (" + sql + ") DATA ) WHERE RN < ? ";
					}
					if (newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
						sql = "SELECT * from (SELECT ROWNUM RN,DATA.* FROM (" + sql + ") DATA ) WHERE RN >= ? ";
					}
				}
				
				PreparedStatement ps = con.prepareStatement(sql);
				int paramIndex = 1;
				if (newsFindParams.getNewsId() != null) {
					ps.setLong(paramIndex++, newsFindParams.getNewsId());
				}
				if (newsFindParams.getCategoryID() != null) {
					ps.setLong(paramIndex++, newsFindParams.getCategoryID());
				}
				if (newsFindParams.getTitle() != null) {
					ps.setString(paramIndex++, newsFindParams.getTitle());
				}
				if (newsFindParams.getAuthorID() != null) {
					ps.setLong(paramIndex++, newsFindParams.getAuthorID());
				}
				if (newsFindParams.getStatus() != null) {
					ps.setLong(paramIndex++, newsFindParams.getStatus());
				}
				if(newsFindParams.getLanguage() != null) {
					ps.setString(paramIndex++, newsFindParams.getLanguage());
				}
				if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0
						&& newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
					ps.setLong(paramIndex++, newsFindParams.getQueryOffset().longValue());
					ps.setLong(paramIndex++,
							newsFindParams.getQueryOffset().longValue() + newsFindParams.getQueryLimit().longValue());
				} else {
					if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0) {
						ps.setLong(paramIndex++, newsFindParams.getQueryLimit().longValue());
					}
					if (newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
						ps.setLong(paramIndex++, newsFindParams.getQueryOffset().longValue());
					}
				}
				return ps;
			}
		};
		RowMapper<News> rowMapper = new RowMapper<News>() {

			@Override
			public News mapRow(ResultSet rs, int rowNum) throws SQLException {
				News news = new News();
				Long o1NewsId = rs.getLong("o1_news_id");
				if (rs.wasNull()) {
					news.setId(null);
				} else {
					news.setId(o1NewsId);
				}
				Long o1CategoryId = rs.getLong("o1_category_id");
				if (rs.wasNull()) {
					news.setCategoryID(null);
				} else {
					news.setCategoryID(o1CategoryId);
				}
				Long o1AuthorId = rs.getLong("o1_author_id");
				if (rs.wasNull()) {
					news.setAuthorID(null);
				} else {
					news.setAuthorID(o1AuthorId);
				}
				String o1Title = rs.getString("o2_config_value");
				if (rs.wasNull()) {
					news.setTitle(null);
				} else {
					news.setTitle(o1Title);
				}
				Long o1Status = rs.getLong("o1_status");
				if (rs.wasNull()) {
					news.setStatus(null);
				} else {
					news.setStatus(o1Status);
				}
				LanguageContent languageContent = new LanguageContent();
				Long o2LanguageID = rs.getLong("o2_id");
				if (rs.wasNull()) {
					languageContent.setId(null);
				} else {
					languageContent.setId(o2LanguageID);
				}
				String o2Language = rs.getString("o2_language");
				if (rs.wasNull()) {
					languageContent.setLanguage(null);
				} else {
					languageContent.setLanguage(o2Language);
				}
				String o2ConfigKey = rs.getString("o2_config_key");
				if (rs.wasNull()) {
					languageContent.setConfigKey(null);
				} else {
					languageContent.setConfigKey(o2ConfigKey);
				}
				String o2Value=rs.getString("o2_config_value");
				if (rs.wasNull()) {
					languageContent.setConfigValue(null);
				} else {
					languageContent.setConfigValue(o2Value);
				}
				if(languageContent.getId() !=null) {
					news.setLaguageContent(languageContent);
				}
				return news;

			}

		};
		return (News) jdbcTemplate.query(preparedStatementCreator, rowMapper);} 

	@Override
	public News deleteNewsById(NewsFindParams newsFindParams, long id) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// TODO Auto-generated method stub
				StringBuilder wherePartBuilder = new StringBuilder();
				if (newsFindParams.getNewsId() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.news_id = ? ");
				}
				if (newsFindParams.getCategoryID() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.category_id = ? ");
				}
				if (newsFindParams.getTitle() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.title = ? ");
				}
				if (newsFindParams.getAuthorID() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.author_id = ? ");
				}
				if (newsFindParams.getStatus() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o1.status = ? ");
				}
				if (newsFindParams.getLanguage() != null) {
					if (wherePartBuilder.length() > 0) {
						wherePartBuilder.append("AND ");
					}
					wherePartBuilder.append("o2.language = ? ");
				}
				String wherePart = wherePartBuilder.toString();
				
				if (wherePart.isEmpty() == false) {
					wherePart = "WHERE " + wherePart;
				}
				String orderByPart = "";
				if (newsFindParams.getOrderBy() != null) {
					String orderBy = newsFindParams.getOrderBy();
					orderByPart = "";
					for (String orderPart : orderBy.split(",")) {
						String orderField = null;
						String orderType = "ASC";
						String[] orderPartSplited = orderPart.split(" ");
						if (orderPartSplited.length == 1) {
							orderField = orderPartSplited[0];
						} else if (orderPartSplited.length == 2) {
							orderField = orderPartSplited[0];
							if (orderPartSplited[1].equalsIgnoreCase("desc")) {
								orderType = "DESC";
							} else {
								orderType = "ASC";
							}
						} else {
							throw new SQLException("Invalid orderby");
						}
						if (orderField != null) {
							if (orderField.equalsIgnoreCase("startTime")) {
								if (orderByPart.isEmpty()) {
									orderByPart = "ORDER BY ";
								} else {
									orderByPart = orderByPart + ", ";
								}
								orderByPart = orderByPart + "o1.start_time " + orderType;
							} else if (orderField.equalsIgnoreCase("endTime")) {
								if (orderByPart.isEmpty()) {
									orderByPart = "ORDER BY ";
								} else {
									orderByPart = orderByPart + ", ";
								}
								orderByPart = orderByPart + "o1.end_time " + orderType;
							} else {
								throw new SQLException("invalid order by field");
							}
						}
					}
				}
				String sql = QueryString.DELETE_NEWS_SQL+ wherePart + orderByPart;
				if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0
						&& newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
					sql = "SELECT * from (SELECT ROWNUM RN,DATA.* FROM (" + sql + ") DATA ) WHERE RN >= ? AND RN < ? ";
				} else {
					if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0) {
						sql = "SELECT * from (SELECT ROWNUM RN,DATA.* FROM (" + sql + ") DATA ) WHERE RN < ? ";
					}
					if (newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
						sql = "SELECT * from (SELECT ROWNUM RN,DATA.* FROM (" + sql + ") DATA ) WHERE RN >= ? ";
					}
				}
				
				PreparedStatement ps = con.prepareStatement(sql);
				int paramIndex = 1;
				if (newsFindParams.getNewsId() != null) {
					ps.setLong(paramIndex++, newsFindParams.getNewsId());
				}
				if (newsFindParams.getCategoryID() != null) {
					ps.setLong(paramIndex++, newsFindParams.getCategoryID());
				}
				if (newsFindParams.getTitle() != null) {
					ps.setString(paramIndex++, newsFindParams.getTitle());
				}
				if (newsFindParams.getAuthorID() != null) {
					ps.setLong(paramIndex++, newsFindParams.getAuthorID());
				}
				if (newsFindParams.getStatus() != null) {
					ps.setLong(paramIndex++, newsFindParams.getStatus());
				}
				if(newsFindParams.getLanguage() != null) {
					ps.setString(paramIndex++, newsFindParams.getLanguage());
				}
				if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0
						&& newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
					ps.setLong(paramIndex++, newsFindParams.getQueryOffset().longValue());
					ps.setLong(paramIndex++,
							newsFindParams.getQueryOffset().longValue() + newsFindParams.getQueryLimit().longValue());
				} else {
					if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 0) {
						ps.setLong(paramIndex++, newsFindParams.getQueryLimit().longValue());
					}
					if (newsFindParams.getQueryOffset() != null && newsFindParams.getQueryOffset().longValue() > 0) {
						ps.setLong(paramIndex++, newsFindParams.getQueryOffset().longValue());
					}
				}
				return ps;
			}
		};
		RowMapper<News> rowMapper = new RowMapper<News>() {

			@Override
			public News mapRow(ResultSet rs, int rowNum) throws SQLException {
				News news = new News();
				Long o1NewsId = rs.getLong("o1_news_id");
				if (rs.wasNull()) {
					news.setId(null);
				} else {
					news.setId(o1NewsId);
				}
				Long o1CategoryId = rs.getLong("o1_category_id");
				if (rs.wasNull()) {
					news.setCategoryID(null);
				} else {
					news.setCategoryID(o1CategoryId);
				}
				Long o1AuthorId = rs.getLong("o1_author_id");
				if (rs.wasNull()) {
					news.setAuthorID(null);
				} else {
					news.setAuthorID(o1AuthorId);
				}
				String o1Title = rs.getString("o2_config_value");
				if (rs.wasNull()) {
					news.setTitle(null);
				} else {
					news.setTitle(o1Title);
				}
				Long o1Status = rs.getLong("o1_status");
				if (rs.wasNull()) {
					news.setStatus(null);
				} else {
					news.setStatus(o1Status);
				}
				LanguageContent languageContent = new LanguageContent();
				Long o2LanguageID = rs.getLong("o2_id");
				if (rs.wasNull()) {
					languageContent.setId(null);
				} else {
					languageContent.setId(o2LanguageID);
				}
				String o2Language = rs.getString("o2_language");
				if (rs.wasNull()) {
					languageContent.setLanguage(null);
				} else {
					languageContent.setLanguage(o2Language);
				}
				String o2ConfigKey = rs.getString("o2_config_key");
				if (rs.wasNull()) {
					languageContent.setConfigKey(null);
				} else {
					languageContent.setConfigKey(o2ConfigKey);
				}
				String o2Value=rs.getString("o2_config_value");
				if (rs.wasNull()) {
					languageContent.setConfigValue(null);
				} else {
					languageContent.setConfigValue(o2Value);
				}
				if(languageContent.getId() !=null) {
					news.setLaguageContent(languageContent);
				}
				return news;

			}

		};
		
		return (News) jdbcTemplate.query(preparedStatementCreator, rowMapper);	}

}
