package nl.codefox.gilmore.command;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class AboutCommand extends GilmoreCommand {

    public AboutCommand() {
        super("Shows information about this bot", "Usage: !about", 0, (Permission) null, "!about");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
        StringBuilder sb = new StringBuilder();

        sb.append("```");
        sb.append("Version : ").append("v1.0.0-SNAPSHOT").append("\n");
        sb.append("GitHub  : ").append("https://github.com/joshcode/gilmore");
        sb.append("```");

        Message message = channel.sendMessage(sb.toString());
    }

}
