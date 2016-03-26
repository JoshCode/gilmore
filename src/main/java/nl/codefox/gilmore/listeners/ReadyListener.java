package nl.codefox.gilmore.listeners;

import net.dv8tion.jda.events.Event;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.hooks.EventListener;

/**
 * @author JoshCode
 */
public class ReadyListener implements EventListener {

    public void onEvent(Event event) {
        if (event instanceof ReadyEvent)
            System.out.println("API is ready!");


    }

}