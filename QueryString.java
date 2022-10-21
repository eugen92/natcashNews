package natcash.news.sql;

public class QueryString {
	public static final String FIND_NEWS_SQL = "SELECT o1.news_id o1_news_id, o1.category_id o1_category_id, o1.title o1_title, o1.author_id o1_author_id, o1.status o1_status, "
			+ "o2.ID o2_id, o2.language o2_language,o2.config_key o2_config_key, o2.config_value o2_config_value FROM NEWS o1 "
			+ "LEFT JOIN LANG_CONFIG o2 ON o1.title = o2.config_key ";
	public static final String GET_CONFIG_SQL = "select o1.config_key o1_config_key, o1.language o1_language, o1.config_value o1_config_value, o1.status o1_status, o1.group_config_id o1_group_config_id FROM LANG_CONFIG o1 ";
	public static final String GET_PARAM = "SELECT o1.config_key o1_config_key , o1.config_value o1_config_value FROM app_config o1  WHERE o1.config_key = ? ";
	public static final String INSERT_IMAGE="INSERT INTO sys_image(id, type, name, description, folder, file_name, width, height, aspect_ratio, created_by, date_created, modify_by, date_modify, status, news_id, event_id, promotion_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String UPDATE_IMAGE="UPDATE sys_image SET type = ?, name = ?, description = ?, folder = ?, file_name = ?, width = ?, height = ?, aspect_ratio = ?, created_by = ?, date_created = ?, modify_by = ?, date_modify = ?, status = ? , news_id = ?, event_id = ?, promotion_id = ? WHERE id = ? ";
	public static final String FIND_NEWS_BY_PARAMS_SQL = "SELECT news_id, category_id, date_created, start_time, status, author_id, end_time, title, content, language, position_group_id FROM NEWS WHERE language = ? and position_group_id = ? ";
	public static final String INSERT_NEWS_SQL ="INSERT INTO news (news_id, category_id, date_created,start_time, status, author_id, end_time, title, content, language, position_group_id) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	public static final String UPDATE_NEWS_SQL ="UPDATE News SET news_id = ?, category_id = ?, date_created = ?, start_time = ?, status = ?, author_id = ?, end_time =?, title = ?, content = ?, language = ?, position_group_id = ? WHERE news_id = ? ";
	public static final String DELETE_NEWS_SQL ="DELETE FROM NEWS WHERE news_id = ? ";
	
}

