package nl.codefox.gilmore.listeners;

import nl.codefox.gilmore.Launcher;

import java.util.ArrayList;

/**
 * @author JoshCode
 */
public class GameNotifier {

    private ArrayList<String> interestedUsers;

    private String name;

    public GameNotifier(String name) {
        this.name = name;
        this.interestedUsers = new ArrayList<>();
    }

    public void addUser(String userID) {
        interestedUsers.add(userID);
    }

    public void removeUser(String userID) {
        interestedUsers.remove(userID);
    }

    public ArrayList<String> getInterestedUsers() {
        return interestedUsers;
    }

    public void notifyUsers(String hostUserID) {
        if (interestedUsers.isEmpty())
            return;

        System.out.println(interestedUsers);

        for (String userID : interestedUsers) {
            System.out.println("PMing " + Launcher.JDA.getUserById(userID));
            Launcher.JDA.getUserById(userID).getPrivateChannel().sendMessage(Launcher.JDA.getUserById(hostUserID).getUsername() + " is about to start a game of '" + name + "'");
        }
    }

    public String getName() {
        return name;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName()).append(" ");
        sb.append("(").append(interestedUsers.size()).append(" interested").append(")");
        return sb.toString();
    }
}
