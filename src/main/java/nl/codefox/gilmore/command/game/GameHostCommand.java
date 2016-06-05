package nl.codefox.gilmore.command.game;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.command.GameCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.util.StringUtil;

public class GameHostCommand extends GilmoreCommand {

    public GameHostCommand() {
        super("", "Usage: !game host [name]", 2, 100, (Permission) null, "!game host");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {

        String name = StringUtil.arrayToString(args, 1, " ");

        if (!GameCommand.gameExists(name)) {
            channel.sendMessage(String.format("[%s] `This game doesn't exist, but it could. Try !game create " + name + "`", author.getAsMention(), name));
        } else {
            GameCommand.getGame(name).notifyUsers(author.getId(), event);
        }

    }

}
