package nl.codefox.gilmore.command.custom;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.command.CustomCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;
import nl.codefox.gilmore.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class EditCustomCommand extends GilmoreCommand {

    public EditCustomCommand() {
        super("Edit a custom command", "Usage: !custom edit [command] [new description]", 3, 100, new ArrayList<>(
                Arrays.asList("Administrator", "Server Owner")), "!custom edit");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
        String desc = StringUtil.arrayToString(args, 2, " ");
        String label = (args[1].contains("!") ? args[1] : "!" + args[1]);

        if (CustomCommand.commandExists(label)) {
            GilmoreDatabase.editCommand(label, desc);
            CustomCommand.editCommand(label, desc);
            channel.sendMessage(String.format("[%s] `The command '%s' has been edited`", author.getAsMention(), label));
        } else {
            channel.sendMessage(String.format("[%s] `This command doesn't exist, please create the command %s, first`", author.getAsMention(), label));
        }
    }
}
