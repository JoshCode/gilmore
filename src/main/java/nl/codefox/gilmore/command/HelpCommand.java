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

		if (args.length == 0) {
			EmbedBuilder eb = new GilEmbedBuilder();

			StringBuilder descBuilder = eb.getDescriptionBuilder();
			descBuilder.append("Here's a list of all available commands");

			for (GilmoreCommand c : Gilmore.getCommandListener().getCommands()) {
				StringBuilder titleBuilder = new StringBuilder();
				if (!c.getSubCommands().isEmpty()) {
					List<String> subCommands = c.getSubCommands().stream().map(sub -> sub.getAliases().get(0).replace(c.getAliases().get(0) + " ", "")).collect(Collectors.toList());

					titleBuilder.append(c.getAliases().get(0) + " [");
					titleBuilder.append(StringUtil.listToString(subCommands, ", "));
					titleBuilder.append("]");
				} else {
					titleBuilder.append(c.getAliases().get(0));
				}
				eb.addField(titleBuilder.toString(), c.getDescription(), false);
			}

			if (!CustomCommand.getCommands().isEmpty()) {
//				if (CustomCommand.getCommands().size() == 1) {
//					eb.addField("Custom Commands", "There is **1** custom command, type `!custom list` to see them!", false);
//				} else {
//					eb.addField("Custom Commands", "There are **" + CustomCommand.getCommands().size() + "** custom commands, type `!custom list` to see them!", false);
//				}
				StringBuilder sb = new StringBuilder();
				for(String c : CustomCommand.getCommands()) {
					sb.append("**").append(c).append("**").append("\n");
				}
				eb.addField("Custom commands",sb.toString(),false);
			}

			MessageBuilder mb = new MessageBuilder();
			mb.setEmbed(eb.build());
			mb.append(String.format("[%s]", author.getAsMention()));
			channel.sendMessage(mb.build()).queue();
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
			} else {
				StringBuilder descBuilder = eb.getDescriptionBuilder();
				descBuilder.append("Here's more information about **")
						.append(label)
						.append("**!");

				getUsage(label, Gilmore.getCommandListener().getCommands(), eb);
			}

			MessageBuilder mb = new MessageBuilder();
			mb.setEmbed(eb.build());
			mb.append(String.format("[%s]", author.getAsMention()));
			channel.sendMessage(mb.build()).queue();
		}
	}

	private boolean getUsage(String label, List<GilmoreCommand> commands, EmbedBuilder embedBuilder) {
		for (GilmoreCommand c : commands) {
			if (c.getAliases().contains(label)) {
				embedBuilder.addField("Aliases", StringUtil.listToString(c.getAliases(), ", "), false);
				embedBuilder.addField("Description", c.getDescription(), false);
				embedBuilder.addField("Usage", c.getUsage(), false);

				List<String> subCommands = c.getSubCommands().stream().map(sub -> sub.getAliases().get(0).replace(c.getAliases().get(0) + " ", "")).collect(Collectors.toList());
				embedBuilder.addField("Subcommands", StringUtil.listToString(subCommands, ", "), false);

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
