package nl.codefox.gilmore.listeners.commands;

import com.google.gson.Gson;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import nl.codefox.gilmore.Launcher;
import nl.codefox.gilmore.listeners.GameNotifier;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * @author JoshCode
 */
public class GameListener extends ListenerAdapter {

    private ArrayList<GameNotifier> games = new ArrayList<>();

    public GameListener() {
    }

    public GameListener(ArrayList<GameNotifier> games) {
        System.out.println("Incoming games\n" + games);
        this.games = games;
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        String command = event.getMessage().getContent();
        String commandcheck = command.toLowerCase();
        if (commandcheck.startsWith("!game list")) {
            if (!games.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (GameNotifier g : games) {
                    sb.append(g.toString()).append(System.getProperty("line.separator"));
                }
                event.getChannel().sendMessage(sb.toString());
            } else {
                event.getChannel().sendMessage("There are no games registered");
            }
            save();
        }
        if (commandcheck.startsWith("!game create ")) {
            String params = command.substring("!game create ".length());
            if (gameExists(params)) {
                event.getChannel().sendMessage("This game already exists, try `!game interest " + params + "`");
            } else {
                GameNotifier game = new GameNotifier(params);
                game.addUser(event.getAuthor().getId());
                games.add(game);
                event.getChannel().sendMessage("The game '" + params + "' has been created and '" + event.getAuthor().getUsername() + "' has been added to its interest list");
            }
            save();
        }
        if (commandcheck.startsWith("!game remove ")) {
            String params = command.substring("!game remove ".length());
            if (gameExists(params)) {
                for (GameNotifier game : games) {
                    if (game.getName().equals(params)) {
                        games.remove(game);
                        break;
                    }
                }
                event.getChannel().sendMessage("The game '" + params + "' has been removed");
            } else {
                event.getChannel().sendMessage("This game does not exist. Try `!game list`");
            }
            save();
        }
        if (commandcheck.startsWith("!game host ")) {
            String params = command.substring("!game host ".length());
            if (gameExists(params)) {
                GameNotifier game = null;
                for (GameNotifier g : games) {
                    if (g.getName().equals(params)) {
                        game = g;
                        break;
                    }
                }
                game.notifyUsers(event.getAuthor().getId());
            } else {
                event.getChannel().sendMessage("This game does not exist, try `!game create " + params + "`");
            }
            save();
        }
        if (commandcheck.startsWith("!game subscribe ")) {
            String params = command.substring("!game subscribe ".length());

            if (gameExists(params)) {
                for (GameNotifier game : games) {
                    if (game.getName().equals(params)) {
                        if (game.getInterestedUsers().contains(event.getAuthor().getId())) {
                            event.getChannel().sendMessage("You are already on the interest list of '" + params + "'");
                        } else {
                            game.addUser(event.getAuthor().getId());
                            event.getChannel().sendMessage("You have been added to the interest list of '" + params + "'");
                        }
                    }
                }
            } else {
                event.getChannel().sendMessage("This game does not exist, try `!game create " + params + "`");
            }
            save();
        }
        if (commandcheck.startsWith("!game unsubscribe ")) {
            String params = command.substring("!game unsubscribe ".length());

            if (gameExists(params)) {
                for (GameNotifier game : games) {
                    if (game.getName().equals(params))
                        game.removeUser(event.getAuthor().getId());
                }
                event.getChannel().sendMessage("You have been removed to the interest list of '" + params + "'");
            } else {
                event.getChannel().sendMessage("This game does not exist, try `!game create " + params + "`");
            }
            save();
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

    public synchronized void save() {
        PrintWriter pw = null;
        File dir = new File("db");
        dir.mkdirs();
        File output = new File(dir, "games.json");
        try {
            output.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pw = new PrintWriter(output);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        String json = gson.toJson(games);

//        System.out.println(json);

        pw.print(json);
        pw.flush();
        pw.close();
    }

    public ArrayList<GameNotifier> getGames() {
        return games;
    }

}
