package nl.codefox.gilmore.listeners.commands;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class VoxMachinaListener extends ListenerAdapter 
{

    public void onMessageRecieved(MessageReceivedEvent event)
    {
        
        String command = event.getMessage().getContent();
               command = command.toLowerCase();
               
        switch(command)
        {
            case "!vex":
                handleVex(event);
                break;
            case "!vax":
                handleVax(event);
                break;
            case "!pike":
                handlePike(event);
                break;
            case "!grog":
                handleGrog(event);
                break;
            case "!percy":
                handlePercy(event);
                break;
            case "!scanlan":
                handleScanlan(event);
                break;
            case "!keyleth":
                handleKeyleth(event);
                break;
        }
        
    }

    private void handleVex(MessageReceivedEvent event) 
    {
        event.getChannel().sendMessage("\"\" ~ Vex'ahlia");
    }

    private void handleVax(MessageReceivedEvent event) 
    {
        event.getChannel().sendMessage("\"\" ~ Vax'ildan");
    }

    private void handlePike(MessageReceivedEvent event) 
    {
        event.getChannel().sendMessage("\"\" ~ Pike Trickfoot");
    }

    private void handleGrog(MessageReceivedEvent event) 
    {
        event.getChannel().sendMessage("\"I would like to RAGE!\" ~ Grog Strongjaw");
    }

    private void handlePercy(MessageReceivedEvent event) 
    {
        event.getChannel().sendMessage("\"YOUR SOUL IS FORFEIT!\" ~ Percival Fredrickstein Von Musel Klossowski de Rolo III");
    }

    private void handleScanlan(MessageReceivedEvent event) 
    {
        event.getChannel().sendMessage("\"Burt Reynolds: Attoney at Large.\" ~ Scanlan Shorthalt");
    }

    private void handleKeyleth(MessageReceivedEvent event) 
    {
        event.getChannel().sendMessage("\"Going Minxie!\" ~ Keyleth");
    }
    
}
