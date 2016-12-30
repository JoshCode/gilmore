package nl.codefox.gilmore.listener;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import nl.codefox.gilmore.Gilmore;
import nl.codefox.gilmore.command.CustomCommand;
import nl.codefox.gilmore.command.GilmoreCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ChannelListener extends ListenerAdapter {

    private List<GilmoreCommand> commands = new ArrayList<>();

    public ChannelListener registerCommand(GilmoreCommand command) {
        commands.add(command);
        return this;
    }

    public List<GilmoreCommand> getCommands() {
        return commands;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        User author = event.getAuthor();
        String message = event.getMessage().getRawContent();

        if (!message.startsWith("!")) {
            return;
        }

        if (author != Gilmore.getJDA().getSelfUser()) {
            String[] args = message.split(" ");
            String command = args[0];
            String[] finalArgs = Arrays.copyOfRange(args, 1, args.length);
            TextChannel channel = event.getTextChannel();
            Optional<GilmoreCommand> c = commands.stream().filter(cc -> cc.getAliases().contains(command)).findFirst();

            if (c.isPresent()) {
                GilmoreCommand cc = c.get();

                new Thread() {
                    @Override
                    public void run() {
                        cc.runCommand(command, finalArgs, channel, author, event);
                        interrupt();
                    }
                }.start();
            } else if (CustomCommand.commandExists(command)) {
                String commandDesc = CustomCommand.getCommand(command);
                channel.sendMessage(String.format("[%s] %s", author.getAsMention(), commandDesc)).queue();
            }
        }
    }
}
