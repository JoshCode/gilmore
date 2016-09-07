package nl.codefox.gilmore.command.game;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.GameCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;
import nl.codefox.gilmore.util.StringUtil;

public class GameUnsubscribeCommand extends GilmoreCommand {

    public GameUnsubscribeCommand() {
        super("", "Usage: !game unsubscribe [name]", 2, 100, (Permission) null, "!game unsubscribe");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {

        String name = StringUtil.arrayToString(args, 1, " ");

        if (!GameCommand.gameExists(name)) {
            Message message = channel.sendMessage(String.format("[%s] `This game doesn't exist. Try !game list to get a list of available games", author.getAsMention(), name));
        } else {
            Game game = GameCommand.getGame(name);

            if (!game.getInterestedUsers().contains(author.getId())) {
                Message message = channel.sendMessage(String.format("[%s] `You are not subscribed to the game '%s'`", author.getAsMention(), name));
                return;
            }

            game.removeUser(author.getId());
            GilmoreDatabase.removeSubscriber(name, author.getId());
            Message message = channel.sendMessage(String.format("[%s] `You are now unsubscribed from the game '%s'`", author.getAsMention(), name));
        }

    }

}
