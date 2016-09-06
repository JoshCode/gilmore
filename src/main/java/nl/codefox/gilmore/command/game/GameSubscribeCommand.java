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

public class GameSubscribeCommand extends GilmoreCommand {

    public GameSubscribeCommand() {
        super("", "Usage: !game subscribe [name]", 2, 100, (Permission) null, "!game subscribe");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {

        String name = StringUtil.arrayToString(args, 1, " ");

        if (!GameCommand.gameExists(name)) {
            Message message = channel.sendMessage(String.format("[%s] `This game doesn't exist, but it could. Try !game create '%s'`", author.getAsMention(), name));
        } else {
            Game game = GameCommand.getGame(name);

            if (game.getInterestedUsers().contains(author.getId())) {
                Message message = channel.sendMessage(String.format("[%s] `You are already subscribed to the game '%s'`", author.getAsMention(), name));
                return;
            }

            game.addUser(author.getId());
            GilmoreDatabase.addSubscriber(name, author.getId());
            Message message = channel.sendMessage(String.format("[%s] `You are now subscribed to the game '%s'`", author.getAsMention(), name));
        }

    }

}
