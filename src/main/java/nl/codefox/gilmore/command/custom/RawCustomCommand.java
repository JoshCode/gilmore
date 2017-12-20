package nl.codefox.gilmore.command.custom;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.CustomCommand;
import nl.codefox.gilmore.command.GilmoreCommand;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Joshua Slik
 */
public class RawCustomCommand extends GilmoreCommand {

	@Override
	public String getDescription() {
		return "Get the raw data for a custom command";
	}

	@Override
	public String getUsage() {
		return "Usage: !custom raw [command]";
	}

	@Override
	public List<String> getAliases() {
		return Collections.singletonList("!custom raw");
	}

	@Override
	public int getMinimumArguments() {
		return 1;
	}

	@Override
	public int getMaximumArguments() {
		return 1;
	}

	@Override
	public List<String> getRolePermission() {
		return Arrays.asList("Administrator", "Server Owner");
	}

	@Override
	public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
		String label = (args[0].contains("!") ? args[0] : "!" + args[0]);

		if (CustomCommand.commandExists(label)) {
			StringBuilder sb = new StringBuilder();
			sb.append("```");
			sb.append(CustomCommand.getCommand(label));
			sb.append("```");

			channel.sendMessage(String.format("[%s] `The command '%s' has the following message:`\n%s", author.getAsMention(), label, sb.toString())).queue();
		} else {
			channel.sendMessage(String.format("[%s] `This command doesn't exist, please create the command %s, first`", author.getAsMention(), label)).queue();
		}
	}
}
