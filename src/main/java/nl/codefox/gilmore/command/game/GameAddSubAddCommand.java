package nl.codefox.gilmore.command.game;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.GameCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameAddSubAddCommand extends GilmoreCommand {

    private GameAddSubCommand mainCommand;

    public GameAddSubAddCommand(GameAddSubCommand mainCommand) {
        this.mainCommand = mainCommand;
    }

    @Override
    public String getUsage() {
        return "Usage: !game addsub add [List of UserID]";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("!game addsub add");
    }

    @Override
    public int getMinimumArguments() {
        return 0;
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

        String name = mainCommand.getSelectedGame();
        ArrayList<String> userIDs = new ArrayList<>();
        userIDs.addAll(Arrays.asList(args));

        if (!GameCommand.gameExists(name)) {
            channel.sendMessage(String.format("[%s] `This game doesn't exist, but it could. Try !game create '%s'`", author.getAsMention(), name)).queue();
        } else {
            Game game = GameCommand.getGame(name);

            for(String userid : userIDs) {
                if (game.getInterestedUsers().contains(userid)) {
                    channel.sendMessage(String.format("[%s] `This user is already subscribed to '%s'`", author.getAsMention(), name)).queue();
                } else {
                    game.addUser(userid);
                    GilmoreDatabase.addSubscriber(name, userid);
                    channel.sendMessage(String.format("[%s] `This user is now subscribed to '%s'`", author.getAsMention(), name)).queue();
                }
            }
        }

    }

}
