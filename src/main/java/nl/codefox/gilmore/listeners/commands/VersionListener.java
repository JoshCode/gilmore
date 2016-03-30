package nl.codefox.gilmore.listeners.commands;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

/**
 * @author JoshCode
 */
public class VersionListener extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getContent().startsWith("!version")) {
            System.out.println("!version command called");
            new Thread(new HandleCommand(event)).start();
        }
    }

    class HandleCommand implements Runnable {
        MessageReceivedEvent event;

        HandleCommand(MessageReceivedEvent event) {
            this.event = event;
        }

        public void run() {
            StringBuilder sb = new StringBuilder();
            sb.append("**Gilmore's Glorious Bot**").append("\n");
            sb.append("Version   : ").append("v1.0.0-SNAPSHOT").append("\n");
            sb.append("Created by: ").append("Dexcuracy").append("\n");
            Message msg = event.getChannel().sendMessage(sb.toString());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            event.getMessage().deleteMessage();
            msg.deleteMessage();
        }
    }

}
