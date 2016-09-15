package nl.codefox.gilmore.command.game;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.GameCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;
import nl.codefox.gilmore.util.StringUtil;

import java.util.Arrays;
import java.util.List;

public class GameRemoveCommand extends GilmoreCommand {

    @Override
    public String getUsage() {
        return "Usage: !game remove [game]";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("!game remove");
    }

    @Override
    public int getMinimumArguments() {
        return 1;
    }

    @Override
    public int getMaximumArguments() {
        return 100;
    }

    @Override
    public List<String> getRolePermission() {
        return Arrays.asList("Administrator", "Server Owner");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {

        String name = StringUtil.arrayToString(args, 0, " ");

        if (!GameCommand.gameExists(name)) {
            Message message = channel.sendMessage(String.format("[%s] `The game '%s' doesn't exist. Use !game list to see all available games", author.getAsMention(), name));
        } else {
            GilmoreDatabase.removeGame(name);
            GameCommand.removeGame(GameCommand.getGame(name));
            Message message = channel.sendMessage(String.format("[%s] `The game '%s' has been removed.`", author.getAsMention(), name));
        }
    }
}
