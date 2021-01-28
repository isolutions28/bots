package ua.isolutions.bot.commons.command;

import ua.isolutions.bot.commons.actions.Action;
import ua.isolutions.bot.commons.command.domain.CommandEntity;

import java.util.Collection;

public interface Command {

	/**
	 * Command label eg register.user.command, can be used for command localization
	 *
	 * @return string name
	 */
	String getLabel();

	/**
	 * Checks if command can be processed as Action
	 *
	 * @return true if does not contain subcommands and has ActionFactory
	 */
	boolean isAction();

	/**
	 * Method inits action chain and returns first element in chain
	 *
	 * @return first action in chain
	 */
	Action getAction();

	/**
	 * Method inits action chain and returns element which class name equals name
	 * @param action class name of an action
	 * @return action which class name equals name
	 */
	Action getActionSince(String action);

	/**
	 * Method to obtain subcommands
	 *
	 * @return collection of subcommands
	 */
	Collection<Command> getSubCommands();

	/**
	 * Method could be user for sorting/ordering purposes
	 *
	 * @return number representation of desired command position
	 */
	int getPosition();

	/**
	 * Command to call command, eg. /register
	 *
	 * @return command string command
	 */
	String getCommand();

	/**
	 * Method can be used to hide disabled commands
	 *
	 * @return true is command is enabled
	 */
	boolean isEnabled();

	/**
	 * Method can be used to filter top layer commands
	 *
	 * @return true is top layer command
	 */
	boolean isTopLayerCommand();

	/**
	 * Method can initialize Command with DB data
	 *
	 * @param commandEntity data from a DB
	 */
	void initCommand(CommandEntity commandEntity);

	/**
	 * Check if command has subcommands
	 *
	 * @return true if subcommands exist
	 */
	boolean hasSubCommands();

}
