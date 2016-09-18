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

public class GameCreateCommand extends GilmoreCommand {

    @Override
    public String getUsage() {
        return "Usage: !game create [game]";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("!game create");
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

        if (GameCommand.gameExists(name)) {
            Message message = channel.sendMessage(String.format("[%s] `This game already exists, try !game subscribe " + name + "`", author.getAsMention(), name));
        } else {
            GilmoreDatabase.addGame(name);
            GilmoreDatabase.addSubscriber(name, author.getId());
            GameCommand.addGame(new Game(name, author.getId()));
            Message message = channel.sendMessage(String.format("[%s] `The game '%s' has been created and you are subscribed to it.`", author.getAsMention(), name));
        }

    }

}
