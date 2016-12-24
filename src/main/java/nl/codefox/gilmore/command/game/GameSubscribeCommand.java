package nl.codefox.gilmore.command.game;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.command.GameCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;
import nl.codefox.gilmore.util.StringUtil;

import java.util.Arrays;
import java.util.List;

public class GameSubscribeCommand extends GilmoreCommand {

    @Override
    public String getUsage() {
        return "Usage: !game subscribe [game]";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("!game subscribe", "!game sub");
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
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {

        String name = StringUtil.arrayToString(args, 0, " ");

        if (!GameCommand.gameExists(name)) {
            channel.sendMessage(String.format("[%s] `This game doesn't exist, but it could. Try !game create '%s'`", author.getAsMention(), name)).queue();
        } else {
            Game game = GameCommand.getGame(name);

            if (game.getInterestedUsers().contains(author.getId())) {
                channel.sendMessage(String.format("[%s] `You are already subscribed to the game '%s'`", author.getAsMention(), name)).queue();
                return;
            }

            game.addUser(author.getId());
            GilmoreDatabase.addSubscriber(name, author.getId());
            channel.sendMessage(String.format("[%s] `You are now subscribed to the game '%s'`", author.getAsMention(), name)).queue();
        }

    }

}
