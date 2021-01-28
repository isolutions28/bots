package ua.isolutions.bot.commons.sting_generator.impl;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.sting_generator.StringGeneratorService;

import java.util.Random;
import java.util.UUID;

@Component
public class StringGeneratorServiceImpl implements StringGeneratorService {
	private static char[] NUMBERS = "0123456789".toCharArray();
	private static char[] ALPABET = "qwertyuiopasdfghjklzxcvbnmr".toCharArray();
	private static char[] TOKEN_CHARS = "qwertyuiopasdfghjklzxcvbnmrQWERTYUIOPASDFGHJKLZXCVBNM1234657980".toCharArray();
	private HexStringConverter stringConverter = new HexStringConverter();

	@Override
	public String token(int length) {
		return generateString(TOKEN_CHARS, length);
	}

	@Override
	public String numbersString(int length) {
		return generateString(NUMBERS, length);
	}

	@Override
	public String charsString(int length) {
		return generateString(ALPABET, length);
	}

	private String generateString(char[] symbols, int length) {
		Random random = new Random();
		char[] buffer = new char[length];
		for (int i = 0; i < buffer.length; i++) {
			int index = random.nextInt(symbols.length);
			buffer[i] = symbols[index];
		}

		return new String(buffer);
	}

	@SneakyThrows
	@Override
	public String hexString(int length) {
		String s = charsString(length * 2);
		String result = stringConverter.stringToHex(s);
		return result.substring(0, length);
	}

	@Override
	public String uuidString(int length) {
		if (length < 1) {
			length = 1;
		}
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < length; i++) {
			result.append(UUID.randomUUID().toString().toUpperCase().replace("-", ""));
		}
		return result.toString();
	}

	private static class HexStringConverter {
		private static char[] HEX_CHARS = "0123456789abcdef".toCharArray();

		public String stringToHex(String input) {
			if (input == null) throw new NullPointerException();
			return asHex(input.getBytes());
		}

		private String asHex(byte[] buf) {
			char[] chars = new char[2 * buf.length];
			for (int i = 0; i < buf.length; ++i) {
				chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
				chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
			}
			return new String(chars);
		}

	}
}
