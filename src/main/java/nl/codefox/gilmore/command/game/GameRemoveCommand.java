package nl.codefox.gilmore.command.game;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.command.GameCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;
import nl.codefox.gilmore.util.StringUtil;

public class GameRemoveCommand extends GilmoreCommand {

    public GameRemoveCommand() {
        super("", "Usage: !game remove [name]", 3, 100, null, "!game remove");
    }

    @Override
    public void run(String[] args, MessageReceivedEvent event) {

        String name = StringUtil.arrayToString(args, 2, " ");

        if (!GameCommand.gameExists(name)) {
            event.getChannel().sendMessage(String.format("[%s] `The game '%s' doesn't exist. Use !game list to see all available games", event.getAuthor().getAsMention(), name));
        } else {
            GilmoreDatabase.removeGame(name);
            GameCommand.removeGame(GameCommand.getGame(name));
            event.getChannel().sendMessage(String.format("[%s] `The game '%s' has been removed.`", event.getAuthor().getAsMention(), name));
        }

    }

}
