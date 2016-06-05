package nl.codefox.gilmore.command.game;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.command.GameCommand;
import nl.codefox.gilmore.command.GilmoreCommand;

public class GameListCommand extends GilmoreCommand {

    public GameListCommand() {
        super("", "!game list", 1, (Permission) null, "!game list");
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

        channel.sendMessage(builder.toString());
    }

}
