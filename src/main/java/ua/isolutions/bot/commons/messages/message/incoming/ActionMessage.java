package ua.isolutions.bot.commons.messages.message.incoming;

import lombok.Data;
import ua.isolutions.bot.commons.command.Command;
import ua.isolutions.bot.commons.dto.ChatIdMessenger;
import ua.isolutions.bot.commons.messages.message.response.MessengerType;

@Data
public class ActionMessage {

	private final ChatIdMessenger chatIdMessenger;
    private String message;
	private MessengerFileWrapper file;
	private int messageId;
	private LocationWrapper location;
	private MessageType type;
	private Command parentCommand;

	public ActionMessage(String userId, MessageType type, MessengerType messengerType) {
		this.type = type;
		this.chatIdMessenger = new ChatIdMessenger(userId, messengerType);
	}

	public ActionMessage(String userId, String message, MessageType messageEventType, MessengerType messengerType) {
		this.message = message;
		this.type = messageEventType;
		this.chatIdMessenger = new ChatIdMessenger(userId, messengerType);
	}

	public ActionMessage(String userId, MessengerFileWrapper file, MessageType type, MessengerType messengerType) {
		this.file = file;
		this.type = type;
		this.chatIdMessenger = new ChatIdMessenger(userId, messengerType);
	}

	public ActionMessage(String userId, LocationWrapper location, MessengerType messengerType) {
		this.location = location;
		this.type = MessageType.LOCATION;
		this.chatIdMessenger = new ChatIdMessenger(userId, messengerType);
	}

	public ActionMessage(ChatIdMessenger chatIdMessenger, Command command) {
		this.parentCommand = command;
		this.type = MessageType.SUBCOMMANDS;
		this.chatIdMessenger = chatIdMessenger;
	}

	public ActionMessage(ChatIdMessenger chatIdMessenger) {
		this.chatIdMessenger = chatIdMessenger;
	}

	public enum  MessageType {
		TEXT, CONTACT, FILE, LOCATION, PICTURE, STICKER, URL, VIDEO, KEYBOARD, SUBSCRIBE, UNSUBSCRIBE, SUBCOMMANDS, DICE, AUDIO
	}
}
