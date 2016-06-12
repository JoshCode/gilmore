package nl.codefox.gilmore.command;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.command.dice.Dice;
import nl.codefox.gilmore.util.MessageDeleter;

public class DiceCommand extends GilmoreCommand {

    public DiceCommand() {
        super("Rolls dice based on input", "Usage: ![roll|dice] [expression]\n"
                + "for example: !roll 1d20 + 5 [to hit]\n"
                + "[comment]: this is ignored\n"
                + "2d20khX: keep the X highest dice\n"
                + "2d20klX: keep the X lowest dice\n"
                + "4d6r<X: reroll every die lower than X\n"
                + "4d6ro<X: reroll every die lower than X, but only once\n"
                + "1d10!: exploding die - every time you roll a crit, add an extra die", 1, 1000, null, "!roll", "!dice");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
        String expression = "";

        for (String arg : args) {
            expression += " " + arg;
        }
        expression = expression.substring(1);

        Dice dice = new Dice(expression);
        int result = dice.roll();

        String msg = String.format("[%s] %s = %d", author.getAsMention(), dice.getBreakdown(), result);
        if (msg.length() > 500) {
            msg = msg.substring(0, 500);
            msg += "[This message got cut off because it is too long.]";
        }

        Message message = channel.sendMessage(msg);
        new MessageDeleter(message, 30_000);
    }

}
