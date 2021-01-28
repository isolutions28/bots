package ua.isolutions.bot.commons.messages.message.response.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ua.isolutions.bot.commons.messages.keyboard.dto.KeyboardWrapper;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TextMessage extends AbstractMessage {

	private final String text;

	public TextMessage(String text) {
		this.text = text;
	}

	public TextMessage(String text, KeyboardWrapper keyboard) {
		this.text = text;
		setKeyboard(keyboard);
	}
}
