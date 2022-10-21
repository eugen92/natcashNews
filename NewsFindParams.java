package natcash.news.ext.dao.params;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewsFindParams {
	private Long newsId;
	private Long categoryID;
	private Long authorID;
	private String title;
	private Date startTime;
	private Date endTime;
	private Long status;
	private String orderBy;
	private Long queryOffset;
	private Long queryLimit;
	
	private String language;
	
	public Long getAuthorID() {
		return authorID;
	}
	public void setAuthorID(Long authorID) {
		this.authorID = authorID;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public Long getNewsId() {
		return newsId;
	}
	public Long getCategoryID() {
		return categoryID;
	}

	public String getTitle() {
		return title;
	}
	public Date getStartTime() {
		return startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setNewsId(Long newsId) {
		this.newsId = newsId;
	}
	public void setCategoryID(Long categoryID) {
		this.categoryID = categoryID;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public Long getQueryOffset() {
		return queryOffset;
	}
	public Long getQueryLimit() {
		return queryLimit;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public void setQueryOffset(Long queryOffset) {
		this.queryOffset = queryOffset;
	}
	public void setQueryLimit(Long queryLimit) {
		this.queryLimit = queryLimit;
	}
	
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	@Override
	public String toString() {
		return "NewsFindParams \n[newsId=" + newsId + "\ncategoryID=" + categoryID + "\nauthorID=" + authorID + "\ntitle="
				+ title + "\nstartTime=" + startTime + "\nendTime=" + endTime + "\nstatus=" + status + "\norderBy="
				+ orderBy + "\nqueryOffset=" + queryOffset + "\nqueryLimit=" + queryLimit +"\nlanguage="+language+ "]";
	}
	
}
