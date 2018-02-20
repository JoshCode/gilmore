package nl.codefox.gilmore.command.game;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.GameCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.util.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Joshua on 31/03/2017.
 */
public class GameAddSubSelectCommand extends GilmoreCommand {

	private GameAddSubCommand mainCommand;

	public GameAddSubSelectCommand(GameAddSubCommand mainCommand) {
		this.mainCommand = mainCommand;
	}

	@Override
	public String getUsage() {
		return "Usage: !game addsub select [Game]";
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public List<String> getAliases() {
		return Collections.singletonList("!game addsub select");
	}

	@Override
	public int getMinimumArguments() {
		return 1;
	}

	@Override
	public int getMaximumArguments() {
		return 100;
	}

	@Override
	public List<String> getRolePermission() {
		return Arrays.asList("Mod", "Administrator", "Server Owner");
	}

	@Override
	public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {

		String name = StringUtil.arrayToString(args, 0, " ");

		if (!GameCommand.gameExists(name)) {
			channel.sendMessage(String.format("[%s] `This game doesn't exist, but it could. Try !game create '%s'`", author.getAsMention(), name)).queue();
		} else {
			mainCommand.setSelectedGame(name);
			channel.sendMessage(String.format("[%s] `The game '%s' was selected`", author.getAsMention(), name)).queue();
		}

	}

}
