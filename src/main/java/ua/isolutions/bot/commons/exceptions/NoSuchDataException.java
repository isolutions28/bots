package ua.isolutions.bot.commons.exceptions;

public class NoSuchDataException extends RuntimeException {

	public NoSuchDataException() {
	}

	public NoSuchDataException(String message) {
		super(message);
	}

	public NoSuchDataException(String message, Throwable cause) {
		super(message, cause);
	}
}
