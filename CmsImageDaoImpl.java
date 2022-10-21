package natcash.news.ext.dao.impl;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import natcash.news.ext.dao.CmsImageDao;
import natcash.news.ext.entity.CmsImage;
import natcash.news.sql.QueryString;

@Repository
public class CmsImageDaoImpl implements CmsImageDao {

	private JdbcTemplate jdbcTemplate;

	public CmsImageDaoImpl(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void insertCmsAnh(CmsImage cmsImage) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement preparedStatement = con.prepareStatement(
						QueryString.INSERT_IMAGE,new String[] { "id" });
				return preparedStatement;
			}
		};
		PreparedStatementCallback<Integer> preparedStatementCallback = new PreparedStatementCallback<Integer>() {
			@Override
			public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				int paramIndex = 1;
				if (cmsImage.getId() != null) {
					ps.setLong(paramIndex++, cmsImage.getId());
				} else {
					ps.setNull(paramIndex++, Types.BIGINT);
				}
				if (cmsImage.getType() != null) {
					ps.setLong(paramIndex++, cmsImage.getType());
				} else {
					ps.setNull(paramIndex++, Types.BIGINT);
				}
				ps.setString(paramIndex++, cmsImage.getName());
				ps.setString(paramIndex++, cmsImage.getDecription());
				ps.setString(paramIndex++, cmsImage.getFolder());
				ps.setString(paramIndex++, cmsImage.getFileName());
				if (cmsImage.getWidth() != null) {
					ps.setLong(paramIndex++, cmsImage.getWidth());
				} else {
					ps.setNull(paramIndex++, Types.BIGINT);
				}
				if (cmsImage.getHeight() != null) {
					ps.setLong(paramIndex++, cmsImage.getHeight());
				} else {
					ps.setNull(paramIndex++, Types.BIGINT);
				}
				if (cmsImage.getAspectRatio() != null) {
					ps.setDouble(paramIndex++, cmsImage.getAspectRatio());
				} else {
					ps.setNull(paramIndex++, Types.DOUBLE);
				}
				ps.setString(paramIndex++, cmsImage.getCreatedBy());
				if (cmsImage.getDateCreated() != null) {
					ps.setDate(paramIndex++, new java.sql.Date(cmsImage.getDateCreated().getTime()));
				} else {
					ps.setDate(paramIndex++, null);
				}
				ps.setString(paramIndex++, cmsImage.getModifyBy());
				if (cmsImage.getDateModify() != null) {
					ps.setDate(paramIndex++, new java.sql.Date(cmsImage.getDateModify().getTime()));
				} else {
					ps.setDate(paramIndex++, null);
				}
				if (cmsImage.getStatus() != null) {
					ps.setLong(paramIndex++, cmsImage.getStatus());
				} else {
					ps.setLong(paramIndex++, Long.valueOf(0));
				}
				if (cmsImage.getCmsNewsId() != null) {
					ps.setLong(paramIndex++, cmsImage.getCmsNewsId());
				} else {
					ps.setNull(paramIndex++, Types.BIGINT);
				}
				if (cmsImage.getCmsEventId() != null) {
					ps.setLong(paramIndex++, cmsImage.getCmsEventId());
				} else {
					ps.setNull(paramIndex++, Types.BIGINT);
				}
				if (cmsImage.getCmsPromotionId() != null) {
					ps.setLong(paramIndex++, cmsImage.getCmsPromotionId());
				} else {
					ps.setNull(paramIndex++, Types.BIGINT);
				}
				ps.executeUpdate();
				try (ResultSet keys = ps.getGeneratedKeys()) {
					if (keys != null) {
						if (keys.next()) {
							Long id = keys.getLong(1);
							if (keys.wasNull()) {
								id = null;
							}
							cmsImage.setId(id);
						}
					}
				}
				return null;
			}
		};
		jdbcTemplate.execute(preparedStatementCreator, preparedStatementCallback);
	}

	@Override
	public void insertCmsAnh(List<CmsImage> cmsAnhList) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement preparedStatement = con.prepareStatement(
						QueryString.INSERT_IMAGE,
						new String[] { "id" });
				return preparedStatement;
			}
		};
		PreparedStatementCallback<Integer> preparedStatementCallback = new PreparedStatementCallback<Integer>() {
			@Override
			public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				for (CmsImage cmsImage : cmsAnhList) {
					int paramIndex = 1;
					if (cmsImage.getId() != null) {
						ps.setLong(paramIndex++, cmsImage.getId());
					} else {
						ps.setNull(paramIndex++, Types.BIGINT);
					}
					if (cmsImage.getType() != null) {
						ps.setLong(paramIndex++, cmsImage.getType());
					} else {
						ps.setNull(paramIndex++, Types.BIGINT);
					}
					ps.setString(paramIndex++, cmsImage.getName());
					ps.setString(paramIndex++, cmsImage.getDecription());
					ps.setString(paramIndex++, cmsImage.getFolder());
					ps.setString(paramIndex++, cmsImage.getFileName());
					if (cmsImage.getWidth() != null) {
						ps.setLong(paramIndex++, cmsImage.getWidth());
					} else {
						ps.setNull(paramIndex++, Types.BIGINT);
					}
					if (cmsImage.getHeight() != null) {
						ps.setLong(paramIndex++, cmsImage.getHeight());
					} else {
						ps.setNull(paramIndex++, Types.BIGINT);
					}
					if (cmsImage.getAspectRatio() != null) {
						ps.setDouble(paramIndex++, cmsImage.getAspectRatio());
					} else {
						ps.setNull(paramIndex++, Types.DOUBLE);
					}
					ps.setString(paramIndex++, cmsImage.getCreatedBy());
					if (cmsImage.getDateCreated() != null) {
						ps.setDate(paramIndex++, new java.sql.Date(cmsImage.getDateCreated().getTime()));
					} else {
						ps.setDate(paramIndex++, null);
					}
					ps.setString(paramIndex++, cmsImage.getModifyBy());
					if (cmsImage.getDateModify() != null) {
						ps.setDate(paramIndex++, new java.sql.Date(cmsImage.getDateModify().getTime()));
					} else {
						ps.setDate(paramIndex++, null);
					}
					if (cmsImage.getStatus() != null) {
						ps.setLong(paramIndex++, cmsImage.getStatus());
					} else {
						ps.setLong(paramIndex++, Long.valueOf(0));
					}
					if (cmsImage.getCmsNewsId() != null) {
						ps.setLong(paramIndex++, cmsImage.getCmsNewsId());
					} else {
						ps.setNull(paramIndex++, Types.BIGINT);
					}
					if (cmsImage.getCmsEventId() != null) {
						ps.setLong(paramIndex++, cmsImage.getCmsEventId());
					} else {
						ps.setNull(paramIndex++, Types.BIGINT);
					}
					if (cmsImage.getCmsPromotionId() != null) {
						ps.setLong(paramIndex++, cmsImage.getCmsPromotionId());
					} else {
						ps.setNull(paramIndex++, Types.BIGINT);
					}
					ps.executeUpdate();
					try (ResultSet keys = ps.getGeneratedKeys()) {
						if (keys != null) {
							if (keys.next()) {
								Long id = keys.getLong(1);
								if (keys.wasNull()) {
									id = null;
								}
								cmsImage.setId(id);
							}
						}
					}
				}
				return null;
			}
		};
		jdbcTemplate.execute(preparedStatementCreator, preparedStatementCallback);
	}

	
	@Override
	public void updateCmsAnh(CmsImage cmsAnh) {
		PreparedStatementCallback<Integer> preparedStatementCallback = new PreparedStatementCallback<Integer>() {
			@Override
			public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				if (cmsAnh.getType() != null) {
					ps.setLong(1, cmsAnh.getType());
				} else {
					ps.setNull(1, Types.BIGINT);
				}
				ps.setString(2, cmsAnh.getName());
				ps.setString(3, cmsAnh.getDecription());
				ps.setString(4, cmsAnh.getFolder());
				ps.setString(5, cmsAnh.getFileName());
				if (cmsAnh.getWidth() != null) {
					ps.setLong(6, cmsAnh.getWidth());
				} else {
					ps.setNull(6, Types.BIGINT);
				}
				if (cmsAnh.getHeight() != null) {
					ps.setLong(7, cmsAnh.getHeight());
				} else {
					ps.setNull(7, Types.BIGINT);
				}
				if (cmsAnh.getAspectRatio() != null) {
					ps.setDouble(8, cmsAnh.getAspectRatio());
				} else {
					ps.setNull(8, Types.DOUBLE);
				}
				ps.setString(9, cmsAnh.getCreatedBy());
				if (cmsAnh.getDateCreated() != null) {
					ps.setDate(10, new java.sql.Date(cmsAnh.getDateCreated().getTime()));
				} else {
					ps.setDate(10, null);
				}
				ps.setString(11, cmsAnh.getModifyBy());
				if (cmsAnh.getDateModify() != null) {
					ps.setDate(12, new java.sql.Date(cmsAnh.getDateModify().getTime()));
				} else {
					ps.setDate(12, null);
				}
				if (cmsAnh.getStatus() != null) {
					ps.setLong(13, cmsAnh.getStatus());
				} else {
					ps.setLong(13, Long.valueOf(0));
				}
				if (cmsAnh.getCmsNewsId() != null) {
					ps.setLong(14, cmsAnh.getCmsNewsId());
				} else {
					ps.setNull(14, Types.BIGINT);
				}
				if (cmsAnh.getCmsEventId() != null) {
					ps.setLong(15, cmsAnh.getCmsEventId());
				} else {
					ps.setNull(15, Types.BIGINT);
				}
				if (cmsAnh.getCmsPromotionId() != null) {
					ps.setLong(16, cmsAnh.getCmsPromotionId());
				} else {
					ps.setNull(16, Types.BIGINT);
				}
				if (cmsAnh.getId() != null) {
					ps.setLong(17, cmsAnh.getId());
				} else {
					ps.setNull(17, Types.BIGINT);
				}
				ps.executeUpdate();
				return null;
			}
		};
		jdbcTemplate.execute(QueryString.UPDATE_IMAGE,preparedStatementCallback);
	}

	@Override
	public void updateCmsAnh(List<CmsImage> cmsAnhList) {
		PreparedStatementCallback<Integer> preparedStatementCallback = new PreparedStatementCallback<Integer>() {
			@Override
			public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				for (CmsImage cmsAnh : cmsAnhList) {
					if (cmsAnh.getType() != null) {
						ps.setLong(1, cmsAnh.getType());
					} else {
						ps.setNull(1, Types.BIGINT);
					}
					ps.setString(2, cmsAnh.getName());
					ps.setString(3, cmsAnh.getDecription());
					ps.setString(4, cmsAnh.getFolder());
					ps.setString(5, cmsAnh.getFileName());
					if (cmsAnh.getWidth() != null) {
						ps.setLong(6, cmsAnh.getWidth());
					} else {
						ps.setNull(6, Types.BIGINT);
					}
					if (cmsAnh.getHeight() != null) {
						ps.setLong(7, cmsAnh.getHeight());
					} else {
						ps.setNull(7, Types.BIGINT);
					}
					if (cmsAnh.getAspectRatio() != null) {
						ps.setDouble(8, cmsAnh.getAspectRatio());
					} else {
						ps.setNull(8, Types.DOUBLE);
					}
					ps.setString(9, cmsAnh.getCreatedBy());
					if (cmsAnh.getDateCreated() != null) {
						ps.setDate(10, new java.sql.Date(cmsAnh.getDateCreated().getTime()));
					} else {
						ps.setDate(10, null);
					}
					ps.setString(11, cmsAnh.getModifyBy());
					if (cmsAnh.getDateModify() != null) {
						ps.setDate(12, new java.sql.Date(cmsAnh.getDateModify().getTime()));
					} else {
						ps.setDate(12, null);
					}
					if (cmsAnh.getStatus() != null) {
						ps.setLong(13, cmsAnh.getStatus());
					} else {
						ps.setLong(13, Long.valueOf(0));
					}
					if (cmsAnh.getCmsNewsId() != null) {
						ps.setLong(14, cmsAnh.getCmsNewsId());
					} else {
						ps.setNull(14, Types.BIGINT);
					}
					if (cmsAnh.getCmsEventId() != null) {
						ps.setLong(15, cmsAnh.getCmsEventId());
					} else {
						ps.setNull(15, Types.BIGINT);
					}
					if (cmsAnh.getCmsPromotionId() != null) {
						ps.setLong(16, cmsAnh.getCmsPromotionId());
					} else {
						ps.setNull(16, Types.BIGINT);
					}
					if (cmsAnh.getId() != null) {
						ps.setLong(17, cmsAnh.getId());
					} else {
						ps.setNull(17, Types.BIGINT);
					}
					ps.executeUpdate();
				}
				return null;
			}
		};
		jdbcTemplate.execute(
				"UPDATE cms_anh SET loai_anh = ?, ten = ?, mo_ta = ?, thu_muc = ?, ten_file = ?, width = ?, height = ?, aspect_ratio = ?, nguoi_tao = ?, ngay_tao = ?, nguoi_thay_doi = ?, ngay_thay_doi = ?, trang_thai = ?,cms_tinbai_id=? WHERE id = ?",
				preparedStatementCallback);
	}

	@Override
	public void deleteCmsAnh(Long id) {
		PreparedStatementCallback<Integer> preparedStatementCallback = new PreparedStatementCallback<Integer>() {
			@Override
			public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setLong(1, id);
				ps.executeUpdate();
				return null;
			}
		};
		jdbcTemplate.execute("DELETE FROM sys_image WHERE id = ?", preparedStatementCallback);
	}

	@Override
	public void deleteCmsAnh(CmsImage cmsAnh) {
		PreparedStatementCallback<Integer> preparedStatementCallback = new PreparedStatementCallback<Integer>() {
			@Override
			public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setLong(1, cmsAnh.getId());
				ps.executeUpdate();
				return null;
			}
		};
		jdbcTemplate.execute("DELETE FROM sys_image WHERE id = ?", preparedStatementCallback);
	}

	@Override
	public void deleteCmsAnh(List<CmsImage> cmsAnhList) {
		PreparedStatementCallback<Integer> preparedStatementCallback = new PreparedStatementCallback<Integer>() {
			@Override
			public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				for (CmsImage cmsAnh : cmsAnhList) {
					ps.setLong(1, cmsAnh.getId());
					ps.executeUpdate();
				}
				return null;
			}
		};
		jdbcTemplate.execute("DELETE FROM sys_image WHERE id = ?", preparedStatementCallback);
	}

	public CmsImage getCmsAnh(Long id) {
		if (id != null) {
			PreparedStatementSetter preparedStatementSetter = new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setLong(1, id);
				}
			};
			ResultSetExtractor<CmsImage> resultSetExtractor = new ResultSetExtractor<CmsImage>() {
				@Override
				public CmsImage extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						CmsImage cmsAnh = new CmsImage();
						Long o1Id = rs.getLong("o1_id");
						if (rs.wasNull()) {
							cmsAnh.setId(null);
						} else {
							cmsAnh.setId(o1Id);
						}
						Long o1LoaiAnh = rs.getLong("o1_type");
						if (rs.wasNull()) {
							cmsAnh.setType(null);
						} else {
							cmsAnh.setType(o1LoaiAnh);
						}
						String o1Ten = rs.getString("o1_name");
						if (rs.wasNull()) {
							cmsAnh.setName(null);
						} else {
							cmsAnh.setName(o1Ten);
						}
						String o1MoTa = rs.getString("o1_description");
						if (rs.wasNull()) {
							cmsAnh.setDecription(null);
						} else {
							cmsAnh.setDecription(o1MoTa);
						}
						String o1ThuMuc = rs.getString("o1_folder");
						if (rs.wasNull()) {
							cmsAnh.setFolder(null);
						} else {
							cmsAnh.setFolder(o1ThuMuc);
						}
						String o1TenFile = rs.getString("o1_file_name");
						if (rs.wasNull()) {
							cmsAnh.setFileName(null);
						} else {
							cmsAnh.setFileName(o1TenFile);
						}
						Long o1Width = rs.getLong("o1_width");
						if (rs.wasNull()) {
							cmsAnh.setWidth(null);
						} else {
							cmsAnh.setWidth(o1Width);
						}
						Long o1Height = rs.getLong("o1_height");
						if (rs.wasNull()) {
							cmsAnh.setHeight(null);
						} else {
							cmsAnh.setHeight(o1Height);
						}
						Double o1AspectRatio = rs.getDouble("o1_aspect_ratio");
						if (rs.wasNull()) {
							cmsAnh.setAspectRatio(null);
						} else {
							cmsAnh.setAspectRatio(o1AspectRatio);
						}
						String o1NguoiTao = rs.getString("o1_created_by");
						if (rs.wasNull()) {
							cmsAnh.setCreatedBy(null);
						} else {
							cmsAnh.setCreatedBy(o1NguoiTao);
						}
						java.sql.Date o1NgayTao = rs.getDate("o1_date_created");
						if (rs.wasNull()) {
							cmsAnh.setDateCreated(null);
						} else {
							cmsAnh.setDateCreated(new java.util.Date(o1NgayTao.getTime()));
						}
						String o1NguoiThayDoi = rs.getString("o1_modify_by");
						if (rs.wasNull()) {
							cmsAnh.setModifyBy(null);
						} else {
							cmsAnh.setModifyBy(o1NguoiThayDoi);
						}
						java.sql.Date o1NgayThayDoi = rs.getDate("o1_date_modify");
						if (rs.wasNull()) {
							cmsAnh.setDateModify(null);
						} else {
							cmsAnh.setDateModify(new java.util.Date(o1NgayThayDoi.getTime()));
						}
						Long o1TrangThai = rs.getLong("o1_status");
						if (rs.wasNull()) {
							cmsAnh.setStatus(null);
						} else {
							cmsAnh.setStatus(o1TrangThai);
						}
						Long o1CmsTinbaiId = rs.getLong("o1_news_id");
						if (rs.wasNull()) {
							cmsAnh.setCmsNewsId(null);
						} else {
							cmsAnh.setCmsNewsId(o1CmsTinbaiId);
						}
						Long o1RunVanodongvienId = rs.getLong("o1_event_id");
						if (rs.wasNull()) {
							cmsAnh.setCmsEventId(null);
						} else {
							cmsAnh.setCmsEventId(o1RunVanodongvienId);
						}
						Long o1SoLuotXem = rs.getLong("o1_promotion_id");
						if (rs.wasNull()) {
							cmsAnh.setCmsPromotionId(null);
						} else {
							cmsAnh.setCmsPromotionId(o1SoLuotXem);
						}
						return cmsAnh;
					}
					return null;
				}
			};
			return jdbcTemplate.query(
					"SELECT o1.id o1_id , o1.type o1_type , o1.name o1_name , o1.description o1_description , o1.folder o1_folder , o1.file_name o1_file_name , o1.width o1_width , o1.height o1_height , o1.aspect_ratio o1_aspect_ratio , o1.created_by o1_created_by , o1.date_created o1_date_created , o1.modify_by o1_modify_by , o1.date_modify o1_date_modify , o1.status o1_status, o1.news_id o1_news_id, o1.event_id o1_event_id, o1.promotion_id o1_promotion_id FROM sys_image o1  WHERE o1.id = ?",
					preparedStatementSetter, resultSetExtractor);
		}
		return null;
	}
}
