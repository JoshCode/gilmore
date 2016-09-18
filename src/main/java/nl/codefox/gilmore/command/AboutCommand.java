package nl.codefox.gilmore.command;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class AboutCommand extends GilmoreCommand {

    @Override
    public String getDescription() {
        return "Shows information about this bot";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("!about");
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
