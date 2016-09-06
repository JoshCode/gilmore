package nl.codefox.gilmore.command.custom;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.command.CustomCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;
import nl.codefox.gilmore.util.MessageDeleter;
import nl.codefox.gilmore.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateCustomCommand extends GilmoreCommand {

    public CreateCustomCommand() {
        super("Create a custom command", "Usage: !custom create [command] [description]", 3, 100, new ArrayList<>(
                Arrays.asList("Administrator", "Server Owner")), "!custom create");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
        String desc = StringUtil.arrayToString(args, 2, " ");
        String label = (args[1].contains("!") ? args[1] : "!" + args[1]);

        if (CustomCommand.commandExists(label)) {
            Message message = channel.sendMessage(String.format("[%s] `This command already exists, to edit use !custom edit %s [description]`", author.getAsMention(), label));
        } else {
            GilmoreDatabase.addCommand(label, desc);
            CustomCommand.editCommand(label, desc);
            Message message = channel.sendMessage(String.format("[%s] `The command '%s' has been created`", author.getAsMention(), label));
        }
    }
}
