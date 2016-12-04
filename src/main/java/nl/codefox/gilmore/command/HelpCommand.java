package nl.codefox.gilmore.command;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.Gilmore;
import nl.codefox.gilmore.util.StringUtil;

import java.util.Arrays;
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
        return Arrays.asList("!help");
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
                builder.append("\tDescription : " + c.getDescription() + "\n\n");
            }

            if(!CustomCommand.getCommands().isEmpty())
                builder.append("\n*** Custom commands ***\n");
            for (String c : CustomCommand.getCommands()) {
                builder.append("> " + c + "\n");
            }

            builder.append("```");
        } else {
            String label = StringUtil.arrayToString(args, 0, " ");

            if (!label.startsWith("!")) {
                label = "!" + label;
            }

            builder.append(String.format("[%s] ```Here's more information about the '%s' command;\n", author.getAsMention(), label));
            getUsage(label, Gilmore.getCommandListener().getCommands(), builder);

            builder.append("```");
        }
        Message message = channel.sendMessage(builder.toString());
    }

    public boolean getUsage(String label, List<GilmoreCommand> command, StringBuilder builder) {
        for (GilmoreCommand c : command) {
            if (c.getAliases().contains(label)) {
                builder.append("> " + label + "\n");
                builder.append("\tAliases     : " + StringUtil.listToString(c.getAliases(), ", ") + "\n");
                builder.append("\tDescription : " + c.getDescription() + "\n");
                builder.append("\tUsage       : " + c.getUsage() + "\n");
                builder.append("\tPermission  : " + (c.getPermission() == null ? "None" : c.getPermission().name()) + "\n\n");
                return true;
            } else {
                if (getUsage(label, c.getSubCommands(), builder)) {
                    return true;
                }
            }
        }
        return false;
    }

}
