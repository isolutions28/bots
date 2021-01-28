package ua.isolutions.bot.commons.command.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.isolutions.bot.commons.command.Command;
import ua.isolutions.bot.commons.command.factory.CommandFactory;
import ua.isolutions.bot.commons.command.service.CommandsService;

@Service
@RequiredArgsConstructor
public class CommandsServiceImpl implements CommandsService {

	private final CommandFactory commandFactory;

	@Override
	public Command getCommand(String command) {
		return commandFactory.getCommand(command);
	}
}
