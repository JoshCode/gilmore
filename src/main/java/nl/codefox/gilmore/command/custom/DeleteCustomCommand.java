package nl.codefox.gilmore.command.custom;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.command.CustomCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;

import java.util.Arrays;
import java.util.List;

public class DeleteCustomCommand extends GilmoreCommand {

    @Override
    public String getDescription() {
        return "Delete a custom command";
    }

    @Override
    public String getUsage() {
        return "Usage: !custom delete [command]";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("!custom delete");
    }

    @Override
    public int getMinimumArguments() {
        return 1;
    }

    @Override
    public int getMaximumArguments() {
        return 1;
    }

    @Override
    public List<String> getRolePermission() {
        return Arrays.asList("Administrator", "Server Owner");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
        String label = (args[0].contains("!") ? args[0] : "!" + args[0]);

        if (CustomCommand.commandExists(label)) {
            GilmoreDatabase.deleteCommand(label);
            CustomCommand.deleteCommand(label);
            channel.sendMessage(String.format("[%s] `The command '%s' has been deleted`", author.getAsMention(), label)).queue();
        } else {
            channel.sendMessage(String.format("[%s] `This command doesn't exist, please create the command '%s', first`", author.getAsMention(), label)).queue();
        }
    }
}
