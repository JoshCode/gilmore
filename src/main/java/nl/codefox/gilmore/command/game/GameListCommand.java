package nl.codefox.gilmore.command.game;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.command.GameCommand;
import nl.codefox.gilmore.command.GilmoreCommand;

public class GameListCommand extends GilmoreCommand {

    public GameListCommand() {
        super("", "!game list", 2, null, "!game list");
    }

    @Override
    public void run(String[] args, MessageReceivedEvent event) {
        StringBuilder builder = new StringBuilder(String.format("[%s] `Here is a list of all available games`\n", event.getAuthor().getAsMention()));

        builder.append("```");

        if (!GameCommand.getGames().isEmpty()) {
            for (Game game : GameCommand.getGames()) {
                builder.append(String.format("> %s\n", game));
            }
        } else {
            builder.append("There are currently no registered games!");
        }

        builder.append("```");

        event.getChannel().sendMessage(builder.toString());
    }

}
