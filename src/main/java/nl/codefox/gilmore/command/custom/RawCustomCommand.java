package nl.codefox.gilmore.command.custom;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.CustomCommand;
import nl.codefox.gilmore.command.GilmoreCommand;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Joshua Slik
 */
public class RawCustomCommand extends GilmoreCommand {

    public RawCustomCommand() {
        super("Get the raw data for a custom command", "Usage: !custom raw [command]", 2, new ArrayList<>(
                Arrays.asList("Administrator", "Server Owner")), "!custom raw");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
        String label = (args[1].contains("!") ? args[1] : "!" + args[1]);

        if (CustomCommand.commandExists(label)) {
            StringBuilder sb = new StringBuilder();
            sb.append("```");
            sb.append(CustomCommand.getCommand(label));
            sb.append("```");

            Message message = channel.sendMessage(String.format("[%s] `The command '%s' has the following message:`\n%s", author.getAsMention(), label, sb.toString()));
        } else {
            Message message = channel.sendMessage(String.format("[%s] `This command doesn't exist, please create the command %s, first`", author.getAsMention(), label));
        }
    }
}
