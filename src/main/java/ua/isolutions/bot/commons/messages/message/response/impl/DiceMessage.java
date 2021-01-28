package ua.isolutions.bot.commons.messages.message.response.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Random;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DiceMessage extends AbstractMessage {
	private static final String PATTERN_TEXT = "Out battle will be legendary! \nMy score: %s, your score: %s. \nSeems like: %s\nAnother game? \uD83C\uDFB2";
	private static final String TIE = "it's a tie!";
	private static final String YOU_WON = "you won!";
	private static final String I_WON = "I won!";
	private final String actualMessage;
	private final int userResult;
	private final int botResult;

	public DiceMessage(int value) {
		botResult = getRandom();
		userResult = value < 1 ? getRandom() : value;
		String result = botResult == userResult ? TIE : botResult > userResult ? I_WON : YOU_WON;
		actualMessage = String.format(PATTERN_TEXT, botResult, userResult, result);
	}

	private int getRandom() {
		return 1 + new Random().nextInt(6);
	}
}
