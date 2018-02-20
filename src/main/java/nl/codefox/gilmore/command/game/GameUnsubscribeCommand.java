package nl.codefox.gilmore.command.game;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.GameCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;
import nl.codefox.gilmore.util.StringUtil;

import java.util.Arrays;
import java.util.List;

public class GameUnsubscribeCommand extends GilmoreCommand {

	@Override
	public String getUsage() {
		return "Usage: !game unsubscribe [game]";
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("!game unsubscribe", "!game unsub");
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
	public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {

		String name = StringUtil.arrayToString(args, 0, " ");

		if (!GameCommand.gameExists(name)) {
			channel.sendMessage(String.format("[%s] `This game doesn't exist. Try !game list to get a list of available games", author.getAsMention())).queue();
		} else {
			Game game = GameCommand.getGame(name);

			if (!game.getInterestedUsers().contains(author.getId())) {
				channel.sendMessage(String.format("[%s] `You are not subscribed to the game '%s'`", author.getAsMention(), name)).queue();
				return;
			}

			game.removeUser(author.getId());
			GilmoreDatabase.removeSubscriber(name, author.getId());
			channel.sendMessage(String.format("[%s] `You are now unsubscribed from the game '%s'`", author.getAsMention(), name)).queue();
		}
	}
}
