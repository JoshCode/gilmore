package nl.codefox.gilmore.command;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.Gilmore;
import nl.codefox.gilmore.util.StringUtil;

public class HelpCommand extends GilmoreCommand {

    public HelpCommand() {
        super("Shows a description of all available commands", "Usage: !help", 1, 2, null, "!help");
    }

    @Override
    public void run(String[] args, MessageReceivedEvent event) {

        StringBuilder builder = new StringBuilder();

        if (args.length == 1) {
            builder.append(String.format("[%s] ```Here is a list of all avaliable commands;\n", event.getAuthor().getAsMention()));

            for (GilmoreCommand command : Gilmore.getCommandListener().getCommands()) {
                builder.append("> " + command.getAliases().get(0) + "\n");
                builder.append("\tDescription : " + command.getDescription() + "\n\n");
            }

            builder.append("```");
        } else {
            String label = (args[1].contains("!") ? args[1] : "!" + args[1]);

            builder.append(String.format("[%s] ```Here's more information about the '%s' command;\n", event.getAuthor().getAsMention(), label));

            for (GilmoreCommand command : Gilmore.getCommandListener().getCommands()) {
                if (command.getAliases().contains(label)) {
                    builder.append("> " + label + "\n");
                    builder.append("\tAliases     : " + StringUtil.listToString(command.getAliases(), ", ") + "\n");
                    builder.append("\tDescription : " + command.getDescription() + "\n");
                    builder.append("\tUsage       : " + command.getUsage() + "\n");
                    builder.append("\tPermission  : " + (command.getPermission() == null ? "None" : command.getPermission().name()) + "\n\n");
                    break;
                }
            }

            builder.append("```");
        }
        event.getChannel().sendMessage(builder.toString());
    }

}
