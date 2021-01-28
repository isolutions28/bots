package ua.isolutions.bot.commons.command;

import ua.isolutions.bot.commons.actions.Action;
import ua.isolutions.bot.commons.command.domain.CommandEntity;
import ua.isolutions.bot.commons.factory.action_initializers.ActionFactory;

import java.util.*;

import static java.util.Objects.nonNull;

public abstract class AbstractCommand implements Command {

	private ActionFactory actionFactory;
	private Set<Command> subCommands;
	private String command;
	private int position;
	private boolean enabled;
	private boolean topLayerCommand;

	public AbstractCommand(Set<Command> subCommands) {
		this.subCommands = subCommands;
		this.actionFactory = null;
	}

	public AbstractCommand(ActionFactory actionFactory) {
		this.subCommands = null;
		this.actionFactory = actionFactory;
	}

	public void initCommand(CommandEntity commandEntity) {
		this.command = commandEntity.getCommand();
		this.position = commandEntity.getPosition();
		this.enabled = commandEntity.isEnabled();
		this.topLayerCommand = commandEntity.isTopCommand();
	}

	public abstract String getLabel();

	public String getCommand() {
		return command;
	}

	public int getPosition() {
		return position;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean isTopLayerCommand() {
		return topLayerCommand;
	}

	@Override
	public boolean isAction() {
		return nonNull(actionFactory);
	}

	@Override
	public Action getAction() {
		return actionFactory.initActionChain();
	}

	@Override
	public Action getActionSince(String actionName) {
		Action action = getAction();
		while (nonNull(action)) {
			if (action.getClass().getName().equals(actionName)) {
				return action;
			}
			action = action.getNext();
		}
		throw new RuntimeException("Cannot find action: " + actionName + " for command: " + getCommand());
	}

	@Override
	public Collection<Command> getSubCommands() {
		return subCommands;
	}

	@Override
	public boolean hasSubCommands() {
		return nonNull(subCommands) && subCommands.size() > 0;
	}
}
