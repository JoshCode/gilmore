package nl.codefox.gilmore.command.game;

import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.Gilmore;

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
        if (users.isEmpty()) {
            return;
        }

        User host = Gilmore.getJDA().getUserById(hid);
        StringBuilder mentions = new StringBuilder();
        for (String uid : users) {
            User user = Gilmore.getJDA().getUserById(uid);
            if(mentions.length() != 0)
                mentions.append(", ");
            mentions.append(user.getAsMention());

//            user.getPrivateChannel().sendMessage(
//                    String.format(
//                            "[%s] `%s is about to start a game of '%s', to stop recieving messages about this game type '!game unsubscribe %s'`",
//                            user.getAsMention(), host.getUsername(), name, name
//                    )
//            );
        }

        event.getChannel().sendMessage(
                String.format(
                        "[%s] `You're hosting a game of '%s'. Notifying interested users.\n%s\nTo stop receiving messages about this game type '!game unsubscribe %s'`",
                        host.getUsername(), name, mentions.toString(), name
                ));
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
