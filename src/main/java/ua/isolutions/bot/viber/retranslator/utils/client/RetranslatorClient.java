package ua.isolutions.bot.viber.retranslator.utils.client;


import ua.isolutions.bot.viber.retranslator.dto.SendMessagesRequest;

public interface RetranslatorClient {
	void sendMessage(SendMessagesRequest sendMessagesRequest);
}
