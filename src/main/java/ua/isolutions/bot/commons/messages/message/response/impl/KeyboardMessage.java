package ua.isolutions.bot.commons.messages.message.response.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ua.isolutions.bot.commons.messages.keyboard.dto.KeyboardWrapper;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class KeyboardMessage extends AbstractMessage {
	public KeyboardMessage(KeyboardWrapper keyboard) {
		setKeyboard(keyboard);
	}
}
