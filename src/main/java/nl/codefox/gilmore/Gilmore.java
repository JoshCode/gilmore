package nl.codefox.gilmore;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import nl.codefox.gilmore.command.AboutCommand;
import nl.codefox.gilmore.command.DiceCommand;
import nl.codefox.gilmore.command.GameCommand;
import nl.codefox.gilmore.config.GilmoreConfiguration;
import nl.codefox.gilmore.listener.CommandListener;
import nl.codefox.gilmore.util.Logging;

import javax.security.auth.login.LoginException;

public class Gilmore 
{

    private static JDA JDA;
    private static CommandListener commandListener;
        
    public static void main(String[] args) 
    {   

        try 
        {
            Logging.info("Gilmore starting up!");
            GilmoreConfiguration config = GilmoreConfiguration.getInstance();
        	
            JDABuilder jdaBuilder = new JDABuilder();
    
            jdaBuilder.setBotToken(config.getBotToken());
    
            commandListener = new CommandListener()
                                        .registerCommand(new AboutCommand())
                                        .registerCommand(new GameCommand())
                                        .registerCommand(new DiceCommand());
            
            jdaBuilder.addListener(commandListener);

            JDA = jdaBuilder.buildBlocking();
            
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
