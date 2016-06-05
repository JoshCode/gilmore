package nl.codefox.gilmore.util;

import net.dv8tion.jda.entities.Message;

/**
 * @author Joshua Slik
 */
public class MessageDeleter implements Runnable {

    private Message message;
    private long milis;
    private long targetTime;

    public MessageDeleter(Message message) {
        this.message = message;
        this.milis = 10_000;
        targetTime = System.nanoTime() / 1_000_000 + milis;

        new Thread(this).start();
    }

    public MessageDeleter(Message message, long milis) {
        this.message = message;
        this.milis = milis;
        targetTime = System.nanoTime() / 1_000_000 + milis;

        new Thread(this).start();
    }

    @Override
    public void run() {
        boolean deleted = false;
        while (!deleted)
            if (System.nanoTime() / 1_000_000 > targetTime) {
                message.deleteMessage();
                deleted = true;
            } else {
                try {
                    Thread.sleep(milis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        Thread.currentThread().interrupt();
    }
}
