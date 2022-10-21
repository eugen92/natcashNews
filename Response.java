package natcash.proxy.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {
	private String errorCode;
	private String errorMessage;
	private T result;
	private Long rowCount;
	private List<T> results;
	
	public Response() {
		super();
	}
	public Response(T result) {
		super();
		this.result = result;
	}
	public Response(String errorCode, String errorMessage, List<T> results) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.results = results;
	}
	public Response(String errorCode, String errorMessage, T result) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.result = result;
	}

	public Response(String errorCode, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	public Response(Long rowCount, List<T> results) {
		super();
		this.rowCount = rowCount;
		this.results = results;
	}

	public Response(String errorCode, String errorMessage, Long rowCount, List<T> results) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.rowCount = rowCount;
		this.results = results;
	}

	public String getErrorCode() {
		return errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public T getResult() {
		return result;
	}
	public Long getRowCount() {
		return rowCount;
	}
	public List<T> getResults() {
		return results;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public void setResult(T result) {
		this.result = result;
	}
	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}
	public void setResults(List<T> results) {
		this.results = results;
	}
}
