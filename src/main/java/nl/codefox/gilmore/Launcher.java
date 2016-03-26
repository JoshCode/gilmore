package nl.codefox.gilmore;

import com.mashape.unirest.http.Unirest;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.impl.JDAImpl;
import net.dv8tion.jda.utils.InviteUtil;
import nl.codefox.gilmore.listeners.ReadyListener;
import nl.codefox.gilmore.listeners.commands.GameListener;
import nl.codefox.gilmore.listeners.commands.VersionListener;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JoshCode
 */
public class Launcher {

    public static  JDA JDA;

    public static void main(String[] args) {
        JDABuilder jdaBuilder = new JDABuilder();

        jdaBuilder.setBotToken(System.getenv("GILMORE_BOT_TOKEN"));
        jdaBuilder.addListener(new ReadyListener());
        jdaBuilder.addListener(new VersionListener());
        jdaBuilder.addListener(new GameListener());

        try {
            JDA = jdaBuilder.buildBlocking();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
