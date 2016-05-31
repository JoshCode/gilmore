package nl.codefox.gilmore.command;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.dice.Dice;

public class DiceCommand extends GilmoreCommand 
{

    public DiceCommand() 
    {
        super("Rolls dice based on input", "Usage: ![roll|dice] [value]", 2, null, "!roll", "!dice");
    }

    @Override
    public void run(String[] args, MessageReceivedEvent event) 
    {
        
        String expression = args[1];
        Dice dice = new Dice(expression);
        int result = dice.roll();
                
        event.getChannel().sendMessage(String.format("[%s] `You rolled '%d' %s with the dice notation '%s'`", event.getAuthor().getAsMention(), result, dice.getBreakdown(), expression));
        
    }

}
