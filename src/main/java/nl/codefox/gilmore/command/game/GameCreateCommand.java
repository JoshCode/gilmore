package nl.codefox.gilmore.command.game;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.command.GameCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;
import nl.codefox.gilmore.util.MessageDeleter;
import nl.codefox.gilmore.util.StringUtil;

public class GameCreateCommand extends GilmoreCommand {

    public GameCreateCommand() {
        super("", "Usage: !game create [name]", 2, 100, Permission.Kick_Members, "!game create");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
        String name = StringUtil.arrayToString(args, 1, " ");

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
