package nl.codefox.gilmore.listeners;

import net.dv8tion.jda.entities.User;
import nl.codefox.gilmore.Launcher;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JoshCode
 */
public class GameNotifier {

    private ArrayList<User> interestedUsers;

    private String name;

    public GameNotifier(String name) {
        this.name = name;
        this.interestedUsers = new ArrayList<User>();
    }

    public void addUser(User interestee) {
        interestedUsers.add(interestee);
    }

    public void notifyUsers(User host) {
        if (interestedUsers.isEmpty())
            return;

        List<User> allUsers = Launcher.JDA.getUsers();

        for(User u : allUsers) {
            if (interestedUsers.contains(u))
                u.getPrivateChannel().sendMessage(host.getUsername() + " is about to start a game of '" + name + "'");
        }
    }

    public String getName() {
        return name;
    }
}
