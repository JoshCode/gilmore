package nl.codefox.gilmore;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import nl.codefox.gilmore.command.AboutCommand;
import nl.codefox.gilmore.command.DiceCommand;
import nl.codefox.gilmore.command.GameCommand;
import nl.codefox.gilmore.command.HelpCommand;
import nl.codefox.gilmore.command.CustomCommand;
import nl.codefox.gilmore.config.GilmoreConfiguration;
import nl.codefox.gilmore.listener.CommandListener;
import nl.codefox.gilmore.listener.ConnectionListener;
import nl.codefox.gilmore.util.Logging;

import javax.security.auth.login.LoginException;

public class Gilmore 
{

    private static JDA JDA;
    private static CommandListener commandListener;
    private static ConnectionListener connectionListener;

    public static void main(String[] args) 
    {   

        try 
        {
            Logging.info("Gilmore starting up!");
            GilmoreConfiguration config = GilmoreConfiguration.getInstance();
        	
            JDABuilder builder = new JDABuilder();
    
            builder.setBotToken(config.getBotToken());
    
            commandListener = new CommandListener()
                                        .registerCommand(new AboutCommand())
                                        .registerCommand(new GameCommand())
                                        .registerCommand(new DiceCommand())
                                        .registerCommand(new HelpCommand())
                                        .registerCommand(new CustomCommand());

            connectionListener = new ConnectionListener();

            builder.addListener(commandListener);
            builder.addListener(connectionListener);

            JDA = builder.buildBlocking();

            JDA.getAccountManager().setGame("with your hearts");
            
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    
    public static JDA getJDA()
    {
        return JDA;
    }
    
    public static CommandListener getCommandListener()
    {
        return commandListener;
    }
}
