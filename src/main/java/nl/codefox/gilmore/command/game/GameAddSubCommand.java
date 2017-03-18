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

public class GameAddSubCommand extends GilmoreCommand {

    @Override
    public String getUsage() {
        return "Usage: !game addsub [UserID] [game]";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("!game addsub");
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
        return Arrays.asList("Mod", "Administrator", "Server Owner");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {

        String name = StringUtil.arrayToString(args, 1, " ");
        String userid = args[0];

        if (!GameCommand.gameExists(name)) {
            channel.sendMessage(String.format("[%s] `This game doesn't exist, but it could. Try !game create '%s'`", author.getAsMention(), name)).queue();
        } else {
            Game game = GameCommand.getGame(name);

            if (game.getInterestedUsers().contains(userid)) {
                channel.sendMessage(String.format("[%s] `This user is already subscribed to '%s'`", author.getAsMention(), name)).queue();
                return;
            }

            game.addUser(userid);
            GilmoreDatabase.addSubscriber(name, userid);
            channel.sendMessage(String.format("[%s] `This user is now subscribed to '%s'`", author.getAsMention(), name)).queue();
        }

    }

}
