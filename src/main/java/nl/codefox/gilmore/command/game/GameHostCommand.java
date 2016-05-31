package nl.codefox.gilmore.command.game;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.command.GameCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.util.StringUtil;

public class GameHostCommand extends GilmoreCommand {

    public GameHostCommand() {
        super("", "Usage: !game host [name]", 3, 100, null, "!game host");
    }

    @Override
    public void run(String[] args, MessageReceivedEvent event) {

        String name = StringUtil.arrayToString(args, 2, " ");

        if (!GameCommand.gameExists(name)) {
            event.getChannel().sendMessage(String.format("[%s] `This game doesn't exist, but it could. Try !game create " + name + "`", event.getAuthor().getAsMention(), name));
        } else {
            GameCommand.getGame(name).notifyUsers(event.getAuthor().getId());
        }

    }

}
