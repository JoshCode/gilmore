package nl.codefox.gilmore.command.custom;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.command.CustomCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;
import nl.codefox.gilmore.util.MessageDeleter;

public class DeleteCustomCommand extends GilmoreCommand {

    public DeleteCustomCommand() {
        super("Delete a custom command", "Usage: !custom delete [command]", 1, Permission.MANAGE_SERVER, "!custom delete");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
        String label = (args[0].contains("!") ? args[0] : "!" + args[0]);

        if (CustomCommand.commandExists(label)) {
            GilmoreDatabase.deleteCommand(label);
            CustomCommand.deleteCommand(label);
            Message message = channel.sendMessage(String.format("[%s] `The command '%s' has been deleted`", author.getAsMention(), label));
            new MessageDeleter(message);
        } else {
            Message message = channel.sendMessage(String.format("[%s] `This command doesn't exist, please create the command '%s', first`", author.getAsMention(), label));
            new MessageDeleter(message);
        }
    }
}
