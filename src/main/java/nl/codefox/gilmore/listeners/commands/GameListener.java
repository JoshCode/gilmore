package nl.codefox.gilmore.listeners.commands;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import nl.codefox.gilmore.listeners.GameNotifier;

import java.util.ArrayList;

/**
 * @author JoshCode
 */
public class GameListener extends ListenerAdapter {

    private ArrayList<GameNotifier> games = new ArrayList<>();

    public void onMessageReceived(MessageReceivedEvent event) {
        String command = event.getMessage().getContent();
        if (command.startsWith("!game list")) {
            if (!games.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (GameNotifier g : games) {
                    sb.append(g.getName()).append(System.getProperty("line.separator"));
                }
                event.getChannel().sendMessage(sb.toString());
            } else {
                event.getChannel().sendMessage("There are no games registered");
            }
        }
        if (command.startsWith("!game create ")) {
            String params = command.substring("!game create ".length());
            if (gameExists(params)) {
                event.getChannel().sendMessage("This game already exists, try `!game interest + " + params + "`");
            } else {
                GameNotifier game = new GameNotifier(params);
                game.addUser(event.getAuthor());
                games.add(game);
                event.getChannel().sendMessage("The game '" + params + "' has been created and '" + event.getAuthor() + "' has been added to its interest list");
            }
        }
        if (command.startsWith("!game host ")) {
            String params = command.substring("!game host ".length());
            if (gameExists(params)) {
                GameNotifier game = null;
                for (GameNotifier g : games) {
                    if (g.getName().equals(params)) {
                        game = g;
                        break;
                    }
                }
                game.notifyUsers(event.getAuthor());
            } else {
                event.getChannel().sendMessage("This game does not exist, try `!game create + " + params + "`");
            }
        }
        if (command.startsWith("!game interest ")) {
            String params = command.substring("!game interest ".length());

            if (gameExists(params)) {
                for (GameNotifier game : games) {
                    if (game.getName().equals(params))
                        game.addUser(event.getAuthor());
                }
                event.getChannel().sendMessage("You have been added to the interest list of '" + params + "'");
            } else {
                event.getChannel().sendMessage("This game does not exist, try `!game create + " + params + "`");
            }
        }
    }

    private boolean gameExists(String name) {
        if (games.isEmpty())
            return false;

        for (GameNotifier g : games) {
            if (g.getName().equals(name))
                return true;
        }
        return false;
    }

    public ArrayList<GameNotifier> getGames() {
        return games;
    }

}
