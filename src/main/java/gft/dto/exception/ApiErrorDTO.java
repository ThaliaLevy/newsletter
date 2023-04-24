package gft.dto.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;

public class ApiErrorDTO {

	private Date timestamp;
	private String message;
	private HttpStatus status;

	public ApiErrorDTO() {}

	public ApiErrorDTO(Date timestamp, String message, HttpStatus status) {
		this.timestamp = timestamp;
		this.message = message;
		this.status = status;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}
