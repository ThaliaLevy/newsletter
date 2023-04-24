package gft.exceptions;

public class AlreadyReportedException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String message;

	public AlreadyReportedException(String message) {
		super(message);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
