package nl.codefox.gilmore.command;

import flexjson.JSONSerializer;
import net.dv8tion.jda.core.MessageHistory;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.requests.WebSocketClient;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryCommand extends GilmoreCommand {

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("!history");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
		JSONSerializer serializer = new JSONSerializer()
						.exclude("*.JDA")
				.exclude("*.guild")
				.exclude("*.privateChannel")
				.include("author.id")
				.include("author.name")
				.include("author.bot")
				.exclude("author.*")
				.include("channel.name")
				.include("channel.id")
				.exclude("channel.*")
				.exclude("mentionsEveryone")
				.exclude("textChannel")
				.exclude("time")
				.exclude("author.email");

		MessageHistory history = null;
		List<Message> fullMessageList = new ArrayList<>();
		boolean end = false;
		Message last = event.getMessage();

		while (!end) {
			try {
				history = channel.getHistoryAround(last, 100).block();
			} catch (RateLimitedException e) {
				e.printStackTrace();
			}
			List<Message> cachedHistory = history.getCachedHistory();
			cachedHistory = cachedHistory.subList(cachedHistory.indexOf(last), cachedHistory.size());

			boolean added = false;

			for (Message m : cachedHistory) {
				if (!fullMessageList.contains(m)) {
					fullMessageList.add(m);
//					System.out.println(m);
					if (!added)
						added = true;
				}
			}

			if(!added)
				end = true;

			last = cachedHistory.get(cachedHistory.size()-1);
			float diff = last.getCreationTime().toEpochSecond() - channel.getCreationTime().toEpochSecond();
			float totaltime = event.getMessage().getCreationTime().toEpochSecond() - channel.getCreationTime().toEpochSecond();
			System.out.println(Math.round(100+(-diff)/totaltime*100) + "%");

		}
		System.out.println(fullMessageList.size() + " messages");

		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new BufferedWriter(new FileWriter(channel.getName() + " history flex.txt")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		assert pw != null;
		pw.print(serializer.serialize(fullMessageList));
		pw.flush();
		pw.close();
		System.out.println("Done");
	}

}
