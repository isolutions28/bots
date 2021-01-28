package ua.isolutions.bot.commons.bot.service.impl;

import com.google.common.util.concurrent.Futures;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ua.isolutions.bot.commons.action_cache.dto.CacheIdentifier;
import ua.isolutions.bot.commons.action_cache.service.ActionCacheService;
import ua.isolutions.bot.commons.actions.Action;
import ua.isolutions.bot.commons.bot.service.BotIncomingService;
import ua.isolutions.bot.commons.command.Command;
import ua.isolutions.bot.commons.command.service.CommandsService;
import ua.isolutions.bot.commons.dto.ChatIdMessenger;
import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import ua.isolutions.bot.commons.messages.message.response.impl.DiceMessage;
import ua.isolutions.bot.commons.messages.message.response.Message;
import ua.isolutions.bot.commons.messages.message.response.MessageWrapper;
import ua.isolutions.bot.commons.messages.message.response.impl.TextMessage;
import ua.isolutions.bot.commons.settings.service.SettingsService;
import ua.isolutions.bot.commons.text.service.TextService;

import java.util.*;
import java.util.concurrent.Future;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
@Log4j2
public class BotIncomingServiceImpl implements BotIncomingService {

	private static final String SHOW_COMMANDS_LIST_COMMAND = "show.commands.list.command";

	private final CommandsService commandService;
	private final SettingsService settingsService;
	private final ActionCacheService actionCache;
	private final TextService textService;

	@Override
	@Async
	public Future<Collection<Message>> processMessage(ActionMessage message) {
		return Futures.immediateFuture(getMessages(message));
	}

	private Collection<Message> getMessages(ActionMessage message) {
		MessageWrapper responseMessage;
		if (isCommand(message)) {
			responseMessage = processByCommand(message, message.getMessage());
		} else if (hasAction(message.getChatIdMessenger())) {
			responseMessage = processByCommand(message, actionCache.get(message.getChatIdMessenger(), CacheIdentifier.COMMAND), actionCache.get(message.getChatIdMessenger(), CacheIdentifier.ACTION));
		} else if (foolingAround(message)) {
			responseMessage = stillFoolingAround(message);
		} else {
			responseMessage = processByCommand(message, showCommandsListCommand());
		}

		Collection<Message> messageList = new ArrayList<>(responseMessage.getMessage());

		if (responseMessage.isShowAllCommands()) {
			messageList.addAll(processByCommand(message, showCommandsListCommand()).getMessage());
		}
		return messageList;
	}

	private MessageWrapper processByCommand(ActionMessage message, String commandName) {
		return processByCommand(message, commandName, null);
	}

	private MessageWrapper processByCommand(ActionMessage message, String commandName, String actionName) {
		try {
			Command command = commandService.getCommand(commandName);
			clearCache(message, actionName);
			if (command.hasSubCommands()) { /* show command subcommands */
				Command toProcess = command;
				command = commandService.getCommand(showCommandsListCommand());
				message = new ActionMessage(message.getChatIdMessenger(), toProcess);
			}
			actionCache.add(message.getChatIdMessenger(), CacheIdentifier.COMMAND, command.getCommand());
			Action action = isNull(actionName) ? command.getAction() : command.getActionSince(actionName);
			MessageWrapper response = action.processMessage(message);
			log.info("Prepared response for " + message.getChatIdMessenger() + ". Response: " + response);
			processUnregister(message, response);
			return response;
		} catch (Exception e) {
			log.error("Exception during message processing: " + message, e);
			return new MessageWrapper(getErrorMessage(message), null, true);
		}
	}

	private List<Message> getErrorMessage(ActionMessage message) {
		return Collections.singletonList(new TextMessage(textService.getText("error.message", message.getChatIdMessenger())));
	}

	private String showCommandsListCommand() {
		return settingsService.getSetting(SHOW_COMMANDS_LIST_COMMAND);
	}

	private void clearCache(ActionMessage message, String actionName) {
		if (isNull(actionName)) {
			actionCache.clearCache(message.getChatIdMessenger());
		}
	}

	private void processUnregister(ActionMessage message, MessageWrapper response) {
		if (response.isUnregister()) {
			actionCache.clearCache(message.getChatIdMessenger());
		} else {
			actionCache.add(message.getChatIdMessenger(), CacheIdentifier.ACTION, response.getLastAction().getClass().getName());
		}
	}

	private boolean foolingAround(ActionMessage message) {
		return message.getType() == ActionMessage.MessageType.DICE;
	}

	private MessageWrapper stillFoolingAround(ActionMessage message) {
		int value = message.getMessage().matches("[1-6]") ? Integer.parseInt(message.getMessage()) : -1;
		return new MessageWrapper(new DiceMessage(value), null, true, false);
	}

	private boolean hasAction(ChatIdMessenger userId) {
		return nonNull(actionCache.get(userId, CacheIdentifier.ACTION));
	}

	private boolean isCommand(ActionMessage message) {
		return message.getType() == ActionMessage.MessageType.TEXT && message.getMessage().startsWith("/");
	}
}
