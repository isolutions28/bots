package ua.isolutions.bot.commons.messages.keyboard.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KeyboardButtonWrapper {
	private final String text;
	private final String action;

	@JsonCreator
	public KeyboardButtonWrapper(@JsonProperty("text") String text, @JsonProperty("action") String action) {
		this.text = text;
		this.action = action;
	}
}
