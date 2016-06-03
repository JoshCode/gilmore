package nl.codefox.gilmore.command.game;

import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.command.GameCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;
import nl.codefox.gilmore.util.StringUtil;

public class GameRemoveCommand extends GilmoreCommand {

    public GameRemoveCommand() {
        super("", "Usage: !game remove [name]", 2, 100, null, "!game remove");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {

        String name = StringUtil.arrayToString(args, 1, " ");

        if (!GameCommand.gameExists(name)) {
            channel.sendMessage(String.format("[%s] `The game '%s' doesn't exist. Use !game list to see all available games", author.getAsMention(), name));
        } else {
            GilmoreDatabase.removeGame(name);
            GameCommand.removeGame(GameCommand.getGame(name));
            channel.sendMessage(String.format("[%s] `The game '%s' has been removed.`", author.getAsMention(), name));
        }

    }

}
