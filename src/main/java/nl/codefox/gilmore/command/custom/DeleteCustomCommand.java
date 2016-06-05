package nl.codefox.gilmore.command.custom;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.command.CustomCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class DeleteCustomCommand extends GilmoreCommand {

    public DeleteCustomCommand() {
        super("Delete a custom command", "Usage: !custom delete [command]", 2, new ArrayList<String>(Arrays.asList("Administrator", "Server Owner")), "!custom delete");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
        String label = (args[1].contains("!") ? args[1] : "!" + args[1]);

        if (CustomCommand.commandExists(label)) {
            GilmoreDatabase.deleteCommand(label);
            CustomCommand.deleteCommand(label);
            channel.sendMessage(String.format("[%s] `The command '%s' has been deleted`", author.getAsMention(), label));
        } else {
            channel.sendMessage(String.format("[%s] `This command doesn't exist, please create the command '%s', first`", author.getAsMention(), label));
        }
    }
}
