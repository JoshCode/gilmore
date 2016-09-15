package nl.codefox.gilmore.command.game;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.GameCommand;
import nl.codefox.gilmore.command.GilmoreCommand;

import java.util.Arrays;
import java.util.List;

public class GameListCommand extends GilmoreCommand {


    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("!game list");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
        StringBuilder builder = new StringBuilder(String.format("[%s] `Here is a list of all available games`\n", author.getAsMention()));

        builder.append("```");

        if (!GameCommand.getGames().isEmpty()) {
            for (Game game : GameCommand.getGames()) {
                builder.append(String.format("> %s\n", game));
            }
        } else {
            builder.append("There are currently no registered games!");
        }

        builder.append("```");

        Message message = channel.sendMessage(builder.toString());
    }

}
