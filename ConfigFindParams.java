package natcash.news.ext.dao.params;

public class ConfigFindParams {
	private String configKey;
	private String languge;
	private String groupId;
	private String status;
	public String getConfigKey() {
		return configKey;
	}
	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}
	public String getLanguage() {
		return languge;
	}
	public void setLanguage(String languge) {
		this.languge = languge;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
