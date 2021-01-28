package ua.isolutions.bot.telegram.clients.dto;

import lombok.Data;

@Data
public class TelegramFile {
	private final String fileId;
	private final String name;
}
