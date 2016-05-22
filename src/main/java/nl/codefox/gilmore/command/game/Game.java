package nl.codefox.gilmore.command.game;

import nl.codefox.gilmore.Gilmore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.entities.User;


public class Game 
{

    private List<String> users = new ArrayList<String>();

    private String name;

    public Game(String name, String... users) 
    {
        this.name = name;
        this.users.addAll(Arrays.asList(users));
    }

    public void addUser(String userID) 
    {
        users.add(userID);
    }

    public void removeUser(String userID) 
    {
        users.remove(userID);
    }

    public List<String> getInterestedUsers() 
    {
        return users;
    }

    public void notifyUsers(String hid) 
    {
        if (users.isEmpty())
        {
            return;
        }

        User host = Gilmore.getJDA().getUserById(hid);
        for (String uid : users) 
        {
            User user = Gilmore.getJDA().getUserById(uid);
            user.getPrivateChannel().sendMessage(
                    String.format(
                            "[%s] `%s is about to start a game of '%s', to stop recieving messages about this game type '!game unsubscribe %s'`",
                            user.getAsMention(), host.getUsername(), name, name
                     )
            );
        }
    }

    public String getName() 
    {
        return name;
    }

    public String toString() 
    {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName()).append(" ");
        sb.append("(").append(users.size()).append(" interested").append(")");
        return sb.toString();
    }
}