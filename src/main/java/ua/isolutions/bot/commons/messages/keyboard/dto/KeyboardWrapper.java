package ua.isolutions.bot.commons.messages.keyboard.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class KeyboardWrapper {

	private final List<KeyboardButtonWrapper> buttons;
	private final int buttonsPerRow;

	@JsonCreator
	public KeyboardWrapper(@JsonProperty("buttons") List<KeyboardButtonWrapper> buttons, @JsonProperty("buttonsPerRow") int buttonsPerRow) {
		this.buttons = buttons;
		this.buttonsPerRow = buttonsPerRow;
	}

	public KeyboardWrapper(List<KeyboardButtonWrapper> buttons) {
		this.buttons = buttons;
		buttonsPerRow = 1;
	}

	public Map<String, String> getButtonsAsMap() {
		return Optional.ofNullable(buttons)
				.orElseGet(Collections::emptyList)
				.stream()
				.collect(Collectors.toMap(KeyboardButtonWrapper::getText, KeyboardButtonWrapper::getAction, (a, b) -> b, LinkedHashMap::new));
	}
}
