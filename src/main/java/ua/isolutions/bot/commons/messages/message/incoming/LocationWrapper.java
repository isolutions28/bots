package ua.isolutions.bot.commons.messages.message.incoming;

import lombok.Data;

@Data
public class LocationWrapper {
	private final double longitude;
	private final double latitude;
}
