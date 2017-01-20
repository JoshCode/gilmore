package nl.codefox.gilmore.command.game;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.GameCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;
import nl.codefox.gilmore.util.StringUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameSubscribedCommand extends GilmoreCommand {

    @Override
    public String getUsage() {
        return "Usage: !game subscribed";
    }

    @Override
    public String getDescription() {
        return "Returns a list of games you are subscribed to";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("!game subscribed");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {

        List<Game> subscribedList = new ArrayList<>();

        for (Game g : GameCommand.getGames()) {
            if (g.getInterestedUsers().contains(author.getId())) {
                subscribedList.add(g);
            }
        }

        if(subscribedList.isEmpty()) {
            channel.sendMessage(String.format("[%s] `You aren't subscribed to any games. Try !game list and !game subscribe!`", author.getAsMention())).queue();
            return;
        }

        StringBuilder reply = new StringBuilder();
        reply.append(String.format("[%s] `You are subscribed to the following games:`", author.getAsMention())).append("\n");
        reply.append("`");
        boolean first = true;

        for (Game g : GameCommand.getGames()) {
            if (g.getInterestedUsers().contains(author.getId())) {
                if (!first) {
                    reply.append(", ");
                } else {
                    first = false;
                }
                reply.append(g.getName());
            }
        }

        reply.append("`");
        channel.sendMessage(reply.toString()).queue();
    }

}
