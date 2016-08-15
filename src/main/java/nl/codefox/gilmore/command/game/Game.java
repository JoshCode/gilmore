package nl.codefox.gilmore.command.game;

import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.Gilmore;
import nl.codefox.gilmore.command.GameCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;
import nl.codefox.gilmore.util.Logging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Game {

    private List<String> users = new ArrayList<String>();

    private String name;

    public Game(String name, String... users) {
        this.name = name;
        this.users.addAll(Arrays.asList(users));
    }

    public void addUser(String userID) {
        users.add(userID);
    }

    public void removeUser(String userID) {
        users.remove(userID);
    }

    public List<String> getInterestedUsers() {
        return users;
    }

    public void notifyUsers(String hid, MessageReceivedEvent event) {
        // TODO: Make the implementation of messages longer than 2000 characters better

        if (users.isEmpty()) {
            return;
        }

        User host = Gilmore.getJDA().getUserById(hid);
        ArrayList<String> responses = new ArrayList<>();
        StringBuilder currentMessage = new StringBuilder();
        currentMessage.append(
                String.format(
                        "[%s] `You're hosting a game of '%s'. Notifying interested users.`\n",
                        host.getAsMention(), name
                ));
        StringBuilder mentions = new StringBuilder();
        for (String uid : users) {
            User user = Gilmore.getJDA().getUserById(uid);
            String mention;
            if (user == null) {
                Logging.error("UserID '" + uid + "' not found on this server, removing from subscribers");
                Game game = GameCommand.getGame(name);
                game.removeUser(uid);
                GilmoreDatabase.removeSubscriber(name, uid);
            } else {
                mention = user.getAsMention();

                if (currentMessage.length() + mentions.length() + mention.length() >= 2_000) {
                    currentMessage.append(mentions.toString());
                    responses.add(currentMessage.toString());

                    currentMessage = new StringBuilder();
                    mentions = new StringBuilder();
                    currentMessage.append(
                            String.format(
                                    "[%s] `You're hosting a game of '%s'. Notifying interested users.`\n",
                                    host.getAsMention(), name
                            ));
                }

                if (mentions.length() != 0)
                    mentions.append(", ");
                mentions.append(mention);

//            user.getPrivateChannel().sendMessage(
//                    String.format(
//                            "[%s] `%s is about to start a game of '%s', to stop recieving messages about this game type '!game unsubscribe %s'`",
//                            user.getAsMention(), host.getUsername(), name, name
//                    )
//            );
            }
        }
        currentMessage.append(mentions.toString());

        String end = String.format(
                "\n`To stop receiving messages about this game type '!game unsubscribe %s'`", name
        );

        if (currentMessage.length() + end.length() >= 2_000) {
            responses.add(currentMessage.toString());

            currentMessage = new StringBuilder();
            end = String.format(
                    "`To stop receiving messages about this game type '!game unsubscribe %s'`", name
            );
        }


        currentMessage.append(end);
        responses.add(currentMessage.toString());

        for (String response : responses) {
            event.getChannel().sendMessage(response);

//            String.format(
//                    "[%s] `You're hosting a game of '%s'. Notifying interested users.`\n%s\n`To stop receiving messages about this game type '!game unsubscribe %s'`",
//                    host.getAsMention(), name, mentions.toString(), name
//            )
        }
    }

    public String getName() {
        return name;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName()).append(" ");
        sb.append("(").append(users.size()).append(" interested").append(")");
        return sb.toString();
    }

}
