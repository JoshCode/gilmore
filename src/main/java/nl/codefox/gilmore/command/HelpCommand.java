package nl.codefox.gilmore.command;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.Gilmore;
import nl.codefox.gilmore.util.GilEmbedBuilder;
import nl.codefox.gilmore.util.StringUtil;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HelpCommand extends GilmoreCommand {

	@Override
	public String getDescription() {
		return "Shows a description of all available commands";
	}

	@Override
	public String getUsage() {
		return "Usage: !help";
	}

	@Override
	public List<String> getAliases() {
		return Collections.singletonList("!help");
	}

	@Override
	public int getMaximumArguments() {
		return 100;
	}

	@Override
	public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {

		StringBuilder builder = new StringBuilder();

		if (args.length == 0) {
			builder.append(String.format("[%s] ```Here is a list of all avaliable commands;\n", author.getAsMention()));

			for (GilmoreCommand c : Gilmore.getCommandListener().getCommands()) {
				if (c.getSubCommands().size() > 0) {
					List<String> subCommands = c.getSubCommands().stream().map(sub -> sub.getAliases().get(0).replace(c.getAliases().get(0) + " ", "")).collect(Collectors.toList());

					builder.append("> " + c.getAliases().get(0) + " [");
					builder.append(StringUtil.listToString(subCommands, ", "));
					builder.append("]\n");
				} else {
					builder.append("> " + c.getAliases().get(0) + "\n");
				}
				builder.append("\tDescription : " + c.getDescription() + "\n");
			}

			if (!CustomCommand.getCommands().isEmpty())
				builder.append("\n*** Custom commands ***\n");
			for (String c : CustomCommand.getCommands()) {
				builder.append("> " + c + "\n");
			}

			builder.append("```");
		} else {
			EmbedBuilder eb = new GilEmbedBuilder();
			String label = StringUtil.arrayToString(args, 0, " ");
			if (!label.startsWith("!")) {
				label = "!" + label;
			}

			if (!commandExists(label)) {
				StringBuilder descBuilder = eb.getDescriptionBuilder();
				descBuilder.append(String.format("The '%s' command does not exist (yet)", label));
				eb.setColor(new Color(255, 0, 0));
			}

			getUsage(label, Gilmore.getCommandListener().getCommands(), eb);

			MessageBuilder mb = new MessageBuilder();
			mb.setEmbed(eb.build());
			mb.append(String.format("[%s]", author.getAsMention()));
			channel.sendMessage(mb.build()).queue();
			return;
		}
		channel.sendMessage(builder.toString()).queue();
	}

	private boolean getUsage(String label, List<GilmoreCommand> commands, EmbedBuilder embedBuilder) {
		for (GilmoreCommand c : commands) {
			if (c.getAliases().contains(label)) {
				embedBuilder.setTitle(label);
				embedBuilder.addField("Aliases", StringUtil.listToString(c.getAliases(), ", "), false);
				embedBuilder.addField("Description", c.getDescription(), false);
				embedBuilder.addField("Usage", c.getUsage(), false);
				embedBuilder.addField("Permission", (c.getRolePermission() == null ? "None" : c.getRolePermission().toString()).replace("[", "").replace("]", ""), false);
				return true;
			} else if (commandExists(label, c.getSubCommands()))
				return true;
		}
		return false;
	}

	private boolean commandExists(String label) {
		for (GilmoreCommand c : Gilmore.getCommandListener().getCommands()) {
			if (c.getAliases().contains(label)) {
				return true;
			} else {
				if (commandExists(label, c.getSubCommands()))
					return true;
			}
		}
		return false;
	}

	private boolean commandExists(String label, List<GilmoreCommand> commands) {
		for (GilmoreCommand c : commands) {
			if (c.getAliases().contains(label)) {
				return true;
			} else if (commandExists(label, c.getSubCommands()))
				return true;
		}
		return false;
	}
}
