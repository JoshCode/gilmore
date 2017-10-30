package nl.codefox.gilmore.command;

import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class GilmoreCommand {

	private final String ALL_CHANNELS = "*";
	
	private final String MISSING_PERMISSION_CHANNEL = "[%s] `Sorry, I cannot run that command for you, this command is channel restricted.`";
	private final String MISSING_PERMISSION = "[%s] `Sorry, I cannot run that command for you, you don't have permission.`";
	private final String INVALID_USAGE = "[%s] `Sorry, I cannot run that command without the correct arguments. %s`";
	private List<GilmoreCommand> subCommands = new ArrayList<>();

	public String getUsage() {
		if (getAliases().size() > 1)
			return String.format("Usage ![%s]", StringUtil.listToString(getAliases(), ", ")).replace("!!", "!");
		else
			return String.format("Usage !%s", getAliases().get(0)).replace("!!", "!");
	}

	public abstract String getDescription();

	/**
	 * Used if minimum arguments and maximum arguments are the same. Default -1 to use minimum and
	 * maximum checks.
	 *
	 * @return The amount of required args for the command
	 */
	public int getRequiredArguments() {
		return -1;
	}

	public int getMinimumArguments() {
		return 0;
	}

	public int getMaximumArguments() {
		return 0;
	}

	public abstract List<String> getAliases();

	public List<String> getRolePermission() {
		return null;
	}
	
	public List<String> getSupportedChannels() {
		List<String> channels = new ArrayList<String>();
		channels.add(ALL_CHANNELS);	// By default, commands being run in all channels.
		return channels;
	}

	public void addSubCommand(GilmoreCommand command) {
		subCommands.add(command);
	}

	public List<GilmoreCommand> getSubCommands() {
		return subCommands;
	}

	public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
	}

	public void invalidChannel(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
		channel.sendMessage(String.format(MISSING_PERMISSION_CHANNEL, author.getAsMention())).queue();
	}

	public void invalidPermissions(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
		channel.sendMessage(String.format(MISSING_PERMISSION, author.getAsMention())).queue();
	}

	public void invalidUsage(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
		channel.sendMessage(String.format(INVALID_USAGE, author.getAsMention(), getUsage())).queue();
	}

	public boolean isValidUsage(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
		if (getRequiredArguments() != -1)
			return getRequiredArguments() == args.length;

		return args.length >= getMinimumArguments() && args.length <= getMaximumArguments();
	}

	public boolean hasRole(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
		if (getRolePermission() == null) {
			return true;
		} else
			for (Role role : event.getGuild().getMember(author).getRoles())
				if (getRolePermission().contains(role.getName()))
					return true;

		return false;
	}

	public void runCommand(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
		
		if (!hasRole(command, args, channel, author, event)) {
			invalidPermissions(command, args, channel, author, event);
			return;
		}
		
		if(!getSupportedChannels().contains(ALL_CHANNELS) && !getSupportedChannels().contains(channel.getName())) {
			invalidChannel(command, args, channel, author, event);
			return;
		}

		if (!isValidUsage(command, args, channel, author, event)) {
			invalidUsage(command, args, channel, author, event);
			return;
		}

		if (subCommands.size() > 0 && args.length > 0) {
			String subCommand = command + " " + args[0];
			String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
			List<GilmoreCommand> subCommandList = getSubCommands().stream().filter(gilmoreCommand -> gilmoreCommand.getAliases().contains(subCommand)).collect(Collectors.toList());
			if (subCommandList.size() == 0) {
				invalidUsage(command, args, channel, author, event);
				return;
			} else {
				for (GilmoreCommand gilmoreCommand : subCommandList) {
					gilmoreCommand.runCommand(subCommand, newArgs, channel, author, event);
				}
				return;
			}
		}

		process(command, args, channel, author, event);
	}
}
