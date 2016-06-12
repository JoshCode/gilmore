package nl.codefox.gilmore.command;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.Gilmore;
import nl.codefox.gilmore.util.MessageDeleter;
import nl.codefox.gilmore.util.StringUtil;

public class HelpCommand extends GilmoreCommand {

    public HelpCommand() {
        super("Shows a description of all available commands", "Usage: !help", 0, 1, null, "!help");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {

        StringBuilder builder = new StringBuilder();

        if (args.length == 0) {
            builder.append(String.format("[%s] ```Here is a list of all avaliable commands;\n", author.getAsMention()));

            for (GilmoreCommand c : Gilmore.getCommandListener().getCommands()) {
                builder.append("> " + c.getAliases().get(0) + "\n");
                builder.append("\tDescription : " + c.getDescription() + "\n\n");
            }

            builder.append("```");
        } else {
            String label = (args[0].contains("!") ? args[0] : "!" + args[0]);

            builder.append(String.format("[%s] ```Here's more information about the '%s' command;\n", author.getAsMention(), label));

            for (GilmoreCommand c : Gilmore.getCommandListener().getCommands()) {
                if (c.getAliases().contains(label)) {
                    builder.append("> " + label + "\n");
                    builder.append("\tAliases     : " + StringUtil.listToString(c.getAliases(), ", ") + "\n");
                    builder.append("\tDescription : " + c.getDescription() + "\n");
                    builder.append("\tUsage       : " + c.getUsage() + "\n");
                    builder.append("\tPermission  : " + (c.getPermission() == null ? "None" : c.getPermission().name()) + "\n\n");
                    break;
                }
            }

            builder.append("```");
        }
        Message message = channel.sendMessage(builder.toString());
        new MessageDeleter(message, 10_000);
    }

}
