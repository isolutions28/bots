package ua.isolutions.bot.viber.utils.update.exception;

public class WrongViberMessageTypeException extends RuntimeException {

	public WrongViberMessageTypeException(String expected, String actual, String additionalMessage) {
		super(String.format("Viber, wrong message type. Expected: %s, received: %s. %s", expected, actual, additionalMessage));
	}
}
