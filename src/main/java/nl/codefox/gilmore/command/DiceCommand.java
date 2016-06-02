package nl.codefox.gilmore.command;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

import nl.codefox.gilmore.command.dice.Dice;

public class DiceCommand extends GilmoreCommand {

    public DiceCommand() 
    {
        super("Rolls dice based on input", "Usage: ![roll|dice] [expression]\n"
                + "for example: !roll 1d20 + 5 [to hit]\n"
                + "[comment]: this is ignored\n"
                + "2d20khX: keep the X highest dice\n"
                + "2d20klX: keep the X lowest dice\n"
                + "4d6r<X: reroll every die lower than X\n"
                + "4d6ro<X: reroll every die lower than X, but only once\n"
                + "1d10!: exploding die - every time you roll a crit, add an extra die", 2, 1000, null, "!roll", "!dice");
    }

    @Override
    public void run(String[] args, MessageReceivedEvent event) 
    {
        
        String expression = "";
        for (int i = 1; i < args.length; i ++) {
            expression += " " + args[i];
        }
        expression = expression.substring(1);
        
        Dice dice = new Dice(expression);
        int result = dice.roll();

        String message = String.format("[%s] %s = %d", event.getAuthor().getAsMention(), dice.getBreakdown(), result);
        if (message.length() > 1999) {
            message = message.substring(0,1940);
            message += "[This message got cut off because it is too long.]";
        }

        event.getChannel().sendMessage(message);
        
    }

}
