package nl.codefox.gilmore.command.custom;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.command.CustomCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.util.MessageDeleter;

public class ListCustomCommands extends GilmoreCommand {

    public ListCustomCommands() {
        super("Lists custom command", "Usage: !custom list", 1, null, "!custom list");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
        StringBuilder builder = new StringBuilder(String.format("[%s] `Here is a list of all available custom commands`\n", author.getAsMention()));

        builder.append("```");

        if (!CustomCommand.getCommands().isEmpty()) {
            for (String str : CustomCommand.getCommands()) {
                builder.append(String.format("> %s\n", str));
            }
        } else {
            builder.append("There are currently no custom commands!");
        }

        builder.append("```");

        Message message = channel.sendMessage(builder.toString());
        new MessageDeleter(message);
    }
}
