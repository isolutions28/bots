package ua.isolutions.bot.commons.command.factory.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.command.Command;
import ua.isolutions.bot.commons.command.ShowCommandListCommand;
import ua.isolutions.bot.commons.command.dao.CommandRepository;
import ua.isolutions.bot.commons.command.domain.CommandEntity;
import ua.isolutions.bot.commons.command.factory.CommandFactory;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Component
public class CommandFactoryImpl implements CommandFactory {

	private final Map<String, Command> commands;

	@Autowired
	CommandFactoryImpl(Collection<Command> commands, CommandRepository repository) {
		this.commands = commands.stream()
				.peek(el -> el.initCommand(getDbCommand(el.getLabel(), repository)))
				.collect(Collectors.toMap(Command::getCommand, command -> command));
		checkIfShowCommandListImplemented();
	}

	private CommandEntity getDbCommand(String label, CommandRepository repository) {
		return repository.findOneByLabel(label)
				.orElseThrow(() -> new RuntimeException("Cannot find command entity " + label));
	}

	private void checkIfShowCommandListImplemented() {
		commands.values().stream()
				.filter(el -> el instanceof ShowCommandListCommand)
				.findFirst()
				.orElseThrow(() -> new RuntimeException(ShowCommandListCommand.class.getName() + " is not implemented."));
	}

	@Override
	public Command getCommand(String text) {
		return ofNullable(commands.get(text))
				.orElseThrow(() -> new RuntimeException("Command not found: " + text));
	}
}
