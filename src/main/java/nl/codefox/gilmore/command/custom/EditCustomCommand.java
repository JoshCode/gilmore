package nl.codefox.gilmore.command.custom;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.CustomCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;
import nl.codefox.gilmore.util.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EditCustomCommand extends GilmoreCommand {

	@Override
	public String getDescription() {
		return "Edit a custom command";
	}

	@Override
	public String getUsage() {
		return "Usage: !custom edit [command] [description]";
	}

	@Override
	public List<String> getAliases() {
		return Collections.singletonList("!custom edit");
	}

	@Override
	public int getMinimumArguments() {
		return 2;
	}

	@Override
	public int getMaximumArguments() {
		return 2000;
	}

	@Override
	public List<String> getRolePermission() {
		return Arrays.asList("Administrator", "Server Owner");
	}

	@Override
	public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
		String desc = StringUtil.arrayToString(args, 1, " ");
		String label = (args[0].contains("!") ? args[0] : "!" + args[0]);

		if (CustomCommand.commandExists(label)) {
			GilmoreDatabase.editCommand(label, desc);
			CustomCommand.editCommand(label, desc);
			channel.sendMessage(String.format("[%s] `The command '%s' has been edited`", author.getAsMention(), label)).queue();
		} else {
			channel.sendMessage(String.format("[%s] `This command doesn't exist, please create the command %s, first`", author.getAsMention(), label)).queue();
		}
	}
}
