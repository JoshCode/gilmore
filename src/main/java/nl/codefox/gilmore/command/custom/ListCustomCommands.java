package nl.codefox.gilmore.command.custom;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.command.CustomCommand;
import nl.codefox.gilmore.command.GilmoreCommand;

import java.util.Arrays;
import java.util.List;

public class ListCustomCommands extends GilmoreCommand {

    @Override
    public String getDescription() {
        return "Lists custom command";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("!custom list");
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
    }
}
