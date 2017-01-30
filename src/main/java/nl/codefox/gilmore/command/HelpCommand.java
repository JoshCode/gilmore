package nl.codefox.gilmore.command;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.Gilmore;
import nl.codefox.gilmore.util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
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
                builder.append("\tDescription : " + c.getDescription() + "\n");
            }

            if (!CustomCommand.getCommands().isEmpty())
                builder.append("\n*** Custom commands ***\n");
            for (String c : CustomCommand.getCommands()) {
                builder.append("> " + c + "\n");
            }
        } else if (isCommand(args)) {
            String label = getLabel(args);

            if (!label.startsWith("!")) {
                label = "!" + label;
            }

            builder.append(String.format("[%s] ```Here's more information about the '%s' command;\n", author.getAsMention(), label));
            getUsage(label, Gilmore.getCommandListener().getCommands(), builder);

        } else {
            String label = getLabel(args);

            if (!label.startsWith("!")) {
                label = "!" + label;
            }

            // TODO: Finalize this language
            builder.append(String.format("[%s] ```The '%s' command does not exist (yet);\n", author.getAsMention(), label));
        }
        builder.append("```");
        channel.sendMessage(builder.toString()).queue();
    }

    // Todo: Create unit tests
    private Predicate<GilmoreCommand> commandMatchesPredicate(String[] args) {
        return (GilmoreCommand s) ->
                Objects.equals(s.getAliases().get(0), getLabel(args)) ||
                        Objects.equals(s.getAliases().get(0), "!" + getLabel(args));
    }

    // Todo: Create unit tests
    private boolean isCommand(String[] args) {
        return Gilmore.getCommandListener().getCommands().stream().filter(
                commandMatchesPredicate(args))
                .collect(Collectors.toList()).size() == 0;
    }


    // Todo: Create unit tests
    private String getLabel(String[] args) {
        return StringUtil.arrayToString(args, 0, " ");
    }

    public boolean getUsage(String label, List<GilmoreCommand> command, StringBuilder builder) {
        for (GilmoreCommand c : command) {
            if (c.getAliases().contains(label)) {
                builder.append("> " + label + "\n");
                builder.append("\tAliases     : " + StringUtil.listToString(c.getAliases(), ", ") + "\n");
                builder.append("\tDescription : " + c.getDescription() + "\n");
                builder.append("\tUsage       : " + c.getUsage() + "\n");
                builder.append("\tPermission  : " + (c.getRolePermission() == null ? "None" : c.getRolePermission().toString()).replace("[", "").replace("]", "") + "\n\n");
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
