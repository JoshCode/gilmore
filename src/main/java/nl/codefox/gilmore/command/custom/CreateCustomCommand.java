package nl.codefox.gilmore.command.custom;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.command.CustomCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;
import nl.codefox.gilmore.util.StringUtil;

public class CreateCustomCommand extends GilmoreCommand {

    public CreateCustomCommand() {
        super("Create a custom command", "Usage: !custom create [command] [description]", 3, 100, Permission.MANAGE_SERVER, "!custom create");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
        String desc = StringUtil.arrayToString(args, 2, " ");
        String label = (args[1].contains("!") ? args[1] : "!" + args[1]);

        if (CustomCommand.commandExists(label)) {
            channel.sendMessage(String.format("[%s] `This command already exists, to edit use !custom edit %s [description]`", author.getAsMention(), label));
        } else {
            GilmoreDatabase.addCommand(label, desc);
            CustomCommand.editCommand(label, desc);
            channel.sendMessage(String.format("[%s] `The command '%s' has been created`", author.getAsMention(), label));
        }
    }
}
