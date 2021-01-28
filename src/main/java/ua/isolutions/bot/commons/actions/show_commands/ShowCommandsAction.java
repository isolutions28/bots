package ua.isolutions.bot.commons.actions.show_commands;


import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.actions.TextAction;
import ua.isolutions.bot.commons.cache.CacheService;
import ua.isolutions.bot.commons.command.Command;
import ua.isolutions.bot.commons.command.dao.CommandRepository;
import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import ua.isolutions.bot.commons.messages.keyboard.dto.KeyboardButtonWrapper;
import ua.isolutions.bot.commons.messages.keyboard.dto.KeyboardWrapper;
import ua.isolutions.bot.commons.messages.keyboard.factory.KeyboardFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ShowCommandsAction extends TextAction {

	private static final TypeReference<Collection<KeyboardButtonWrapper>> BUTTONS_REFERENCE = new TypeReference<Collection<KeyboardButtonWrapper>>() {};
	private static final String CHOOSE_COMMAND_TEXT = "choose.command.text";
	private static final String AVAILABLE_COMMANDS = "available.commands.cache";

	private CommandRepository commandRepository;
	private CacheService cacheService;
	private KeyboardFactory keyboardFactory;

	@Override
	protected String textIdentifier() {
		return CHOOSE_COMMAND_TEXT;
	}

	@Override
	protected KeyboardWrapper keyboard(ActionMessage message) {
		Collection<KeyboardButtonWrapper> buttons = (message.getType() == ActionMessage.MessageType.SUBCOMMANDS
				? getSubcommands(message)
				: getActiveCommands(message))
				.stream()
				.map(el -> new KeyboardButtonWrapper(addLocalization(el.getText(), message), el.getAction()))
				.collect(Collectors.toList());
		return keyboardFactory.createKeyboard(buttons, 2);
	}

	private String addLocalization(String label, ActionMessage message) {
		return text(label, message);
	}

	private List<KeyboardButtonWrapper> getActiveCommands(ActionMessage message) {
		return new ArrayList<>(cacheService.getSilent(AVAILABLE_COMMANDS, BUTTONS_REFERENCE)
				.orElseGet(this::createActiveCommandsKeyboard));
	}

	private Collection<KeyboardButtonWrapper> createActiveCommandsKeyboard() {
		Collection<KeyboardButtonWrapper> availableCommands = commandRepository.getActiveCommands()
				.stream()
				.map(el -> new KeyboardButtonWrapper(el.getLabel(), el.getCommand()))
				.collect(Collectors.toList());

		cacheService.addValueExpired(AVAILABLE_COMMANDS, availableCommands);

		return availableCommands;
	}

	private Collection<KeyboardButtonWrapper> getSubcommands(ActionMessage message) {
		return message.getParentCommand().getSubCommands()
				.stream()
				.filter(Command::isEnabled)
				.map(el -> new KeyboardButtonWrapper(el.getLabel(), el.getCommand()))
				.collect(Collectors.toList());
	}

	@Autowired
	public void setCommandRepository(CommandRepository commandRepository) {
		this.commandRepository = commandRepository;
	}

	@Autowired
	public void setCacheService(CacheService cacheService) {
		this.cacheService = cacheService;
	}

	@Autowired
	public void setKeyboardFactory(KeyboardFactory keyboardFactory) {
		this.keyboardFactory = keyboardFactory;
	}
}

