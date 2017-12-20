package nl.codefox.gilmore.command;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.dice.Dice;

import java.util.Arrays;
import java.util.List;

public class DiceCommand extends GilmoreCommand {

	@Override
	public String getDescription() {
		return "Rolls dice based on input";
	}

	@Override
	public String getUsage() {
		return "Usage: ![roll|dice] [expression]\n"
				+ "for example: !roll 1d20 + 5 [to hit]\n"
				+ "[comment]: this is ignored\n"
				+ "2d20khX: keep the X highest dice\n"
				+ "2d20klX: keep the X lowest dice\n"
				+ "4d6r<X: reroll every die lower than X\n"
				+ "4d6ro<X: reroll every die lower than X, but only once\n"
				+ "1d10!: exploding die - every time you roll a crit, add an extra die\n"
				+ "stats: 6x 4d6 drop the lowest";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("!roll", "!dice");
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
		if (args.length > 0 && args[0].equalsIgnoreCase("stats")) {
			StringBuilder builder = new StringBuilder();
			Dice dice = new Dice("4d6kh3");
			for (int i = 0; i < 6; i++) {
				builder.append(String.format("%s = %d\n", dice.getBreakdown(), dice.roll()));
			}

			channel.sendMessage(builder.toString()).queue();
		} else {
			String expression = "";

			for (int i = 0; i < args.length; i++) {
				expression += " " + args[i];
			}
			expression = expression.substring(1);

			Dice dice = new Dice(expression);
			int result = dice.roll();

			String msg = String.format("[%s] %s = %d", author.getAsMention(), dice.getBreakdown(), result);
			if (msg.length() > 500) {
				msg = msg.substring(0, 500);
				msg += "[This message got cut off because it is too long.]";
			}

			channel.sendMessage(msg).queue();
		}
	}
}