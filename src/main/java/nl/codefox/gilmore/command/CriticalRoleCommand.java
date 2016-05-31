package nl.codefox.gilmore.command;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.command.criticalrole.CriticalRoleCharacter;
import nl.codefox.gilmore.command.criticalrole.CriticalRoleConstants;
import nl.codefox.gilmore.command.criticalrole.CriticalRoleTask;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;

public class CriticalRoleCommand extends GilmoreCommand {

    public CriticalRoleCommand() {
        super("Shows information about the main characters from Critical Role", "Usage: !criticalrole [character]", 2, null, "!criticalrole", "!critrole", "!cr");
        new CriticalRoleTask();
    }

    @Override
    public void run(String[] args, MessageReceivedEvent event) {

        if (CriticalRoleCharacter.getCharacter(args[1].toLowerCase()) != null) {
            Message message = event.getChannel().sendMessage(String.format("[%s] `Preparing that for you now - one moment...`", event.getAuthor().getAsMention()));
            draw(event, CriticalRoleCharacter.getCharacter(args[1].toLowerCase()));
            message.deleteMessage();
        } else {
            event.getChannel().sendMessage(String.format("[%s] `'%s' isn't a valid character name`", event.getAuthor().getAsMention(), args[1]));
        }

    }

    private void draw(MessageReceivedEvent event, CriticalRoleCharacter crc) {
        try {

            BufferedImage image = new BufferedImage(CriticalRoleConstants.IMAGE_WIDTH, CriticalRoleConstants.IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = image.createGraphics();

            graphics.drawImage(ImageIO.read(new URL(CriticalRoleConstants.IMAGE_URL)), 0, 0, null);

            try {
                graphics.drawImage(ImageIO.read(new URL(crc.getResource())), 0, 0, null);
            } catch (Exception ex) {
                graphics.drawImage(ImageIO.read(new URL(CriticalRoleConstants.AVATAR_URL)), 0, 0, null);
            }

            graphics.setColor(Color.BLACK);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            graphics.setFont(new Font("COCOGOOSE", Font.PLAIN, CriticalRoleConstants.NAME_FONT_SIZE));
            graphics.drawString(crc.getName(), CriticalRoleConstants.TEXT_X, CriticalRoleConstants.NAME_Y);

            graphics.setFont(new Font("COCOGOOSE", Font.PLAIN, CriticalRoleConstants.TITLE_FONT_SIZE));
            graphics.drawString(crc.getTitle(), CriticalRoleConstants.TEXT_X, CriticalRoleConstants.TITLE_Y);

            graphics.setFont(new Font("COCOGOOSE", Font.PLAIN, CriticalRoleConstants.DATA_FONT_SIZE));
            graphics.drawString(crc.getStrength(), CriticalRoleConstants.FIRST_COLUMN, CriticalRoleConstants.FIRST_ROW);
            graphics.drawString(crc.getDexerity(), CriticalRoleConstants.FIRST_COLUMN, CriticalRoleConstants.SECOND_ROW);
            graphics.drawString(crc.getConstitution(), CriticalRoleConstants.FIRST_COLUMN, CriticalRoleConstants.THIRD_ROW);
            graphics.drawString(crc.getIntelligence(), CriticalRoleConstants.SECOND_COLUMN, CriticalRoleConstants.FIRST_ROW);
            graphics.drawString(crc.getWisdom(), CriticalRoleConstants.SECOND_COLUMN, CriticalRoleConstants.SECOND_ROW);
            graphics.drawString(crc.getCharisma(), CriticalRoleConstants.SECOND_COLUMN, CriticalRoleConstants.THIRD_ROW);
            graphics.drawString(crc.getArmourClass(), CriticalRoleConstants.THIRD_COLUMN, CriticalRoleConstants.FIRST_ROW);
            graphics.drawString(crc.getCurrentHP(), CriticalRoleConstants.THIRD_COLUMN, CriticalRoleConstants.SECOND_ROW);
            graphics.drawString(crc.getTempHP(), CriticalRoleConstants.THIRD_COLUMN, CriticalRoleConstants.THIRD_ROW);

            float current = (crc.getCurrentHP().equals("?") ? 0 : Integer.parseInt(crc.getCurrentHP()));
            float max = (crc.getMaxHP().equals("?") ? 0 : Integer.parseInt(crc.getMaxHP()));

            float n = ((float) CriticalRoleConstants.HP_BAR_WIDTH / 100);
            float m = (((float) current / (float) max) * 100);

            int bar = (int) Math.round(n * m);

            graphics.setColor(CriticalRoleConstants.HP_BAR_COLOUR);
            graphics.fillRect(CriticalRoleConstants.HP_BAR_X, CriticalRoleConstants.HP_BAR_Y, bar, CriticalRoleConstants.HP_BAR_HEIGHT);

            File temp = new File(System.getProperty("user.home"), "temp.png");
            ImageIO.write(image, "png", temp);

            event.getChannel().sendFile(temp, null);
            temp.delete();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
