package nl.codefox.gilmore.command;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class AboutCommand extends GilmoreCommand {

    public AboutCommand() {
        super("Shows information about this bot", "Usage: !about", 1, null, "!about");
    }

    @Override
    public void run(String[] args, MessageReceivedEvent event) {
        StringBuilder sb = new StringBuilder();

        sb.append("```");
        sb.append("Version : ").append("v1.0.0-SNAPSHOT").append("\n");
        sb.append("Github  : ").append("https://github.com/joshcode/gilmore");
        sb.append("```");

        event.getChannel().sendMessage(sb.toString());

    }

}
