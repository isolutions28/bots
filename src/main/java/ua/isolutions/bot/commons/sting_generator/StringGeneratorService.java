package ua.isolutions.bot.commons.sting_generator;

public interface StringGeneratorService {

	String token(int length);

	String numbersString(int length);

	String charsString(int length);

	String hexString(int length);

	String uuidString(int length);
}
