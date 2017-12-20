package nl.codefox.gilmore.command;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.config.GilmoreConfiguration;
import nl.codefox.gilmore.util.GilEmbedBuilder;

import java.util.Collections;
import java.util.List;

public class AboutCommand extends GilmoreCommand {

	@Override
	public String getDescription() {
		return "Shows information about this bot";
	}

	@Override
	public List<String> getAliases() {
		return Collections.singletonList("!about");
	}

	@Override
	public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
		EmbedBuilder eb = new GilEmbedBuilder();

		eb.addField("Version", GilmoreConfiguration.getInstance().getVersion(), false);
		eb.addField("GitHub", "https://github.com/joshcode/gilmore", false);

		channel.sendMessage(eb.build()).queue();
	}

}
