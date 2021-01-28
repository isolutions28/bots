package ua.isolutions.bot.commons.text.dao;

import lombok.Data;
import ua.isolutions.bot.commons.messages.message.response.MessengerType;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
@Data
public class UserLocale {

	@Id
	private String chatId;
	@Enumerated
	private Locale locale;
	@Enumerated
	private MessengerType messengerType;
}
