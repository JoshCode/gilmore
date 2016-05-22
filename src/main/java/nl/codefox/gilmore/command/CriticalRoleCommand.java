package nl.codefox.gilmore.command;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.criticalrole.CriticalRoleCharacter;
import nl.codefox.gilmore.command.criticalrole.CriticalRoleConstants;
import nl.codefox.gilmore.command.criticalrole.CriticalRoleTask;
import nl.codefox.gilmore.util.GoogleSheetsUtil;

public class CriticalRoleCommand extends GilmoreCommand 
{

    public CriticalRoleCommand() 
    {
        super("Shows information about the main characters from Critical Role", "Usage: !criticalrole [character]", 2, null, "!criticalrole", "!critrole", "!cr");
        new CriticalRoleTask();
    }

    @Override
    public void run(String[] args, MessageReceivedEvent event) 
    {
        if(CriticalRoleCharacter.getCharacter(args[1].toLowerCase()) != null)
        {
            Message message = event.getChannel().sendMessage(String.format("[%s] `Preparing that for you now - one moment...`", event.getAuthor().getAsMention()));
            draw(event, CriticalRoleCharacter.getCharacter(args[1].toLowerCase()));
            message.deleteMessage();
        }
        else
        {
            event.getChannel().sendMessage(String.format("[%s] `'%s' isn't a valid character name`", event.getAuthor().getAsMention(), args[1]));
        }

    }
    
    private String[] getData(int row)
    {
        
        try 
        {
            
            Sheets service = GoogleSheetsUtil.getSheetsService();
            
            String sid = CriticalRoleConstants.SPREADSHEET_ID;
            String range = String.format(CriticalRoleConstants.SPREADSHEET_RANGE, row, row);
            
            ValueRange response = service.spreadsheets().values().get(sid, range).execute();
            
            List<List<Object>> values = response.getValues();
            
            if(values != null && values.size() != 0)
            {
                for (List<Object> r : values) 
                {
                    String[] data = new String[10];
                    data[CriticalRoleConstants.HP] =String.valueOf(r.get(0));
                    data[CriticalRoleConstants.MAXHP] =String.valueOf(r.get(1));
                    data[CriticalRoleConstants.TMPHP] =String.valueOf(r.get(2));
                    data[CriticalRoleConstants.AC] =String.valueOf(r.get(3));
                    data[CriticalRoleConstants.STR] =String.valueOf(r.get(5));
                    data[CriticalRoleConstants.DEX] =String.valueOf(r.get(6));
                    data[CriticalRoleConstants.CON] =String.valueOf(r.get(7));
                    data[CriticalRoleConstants.INT] =String.valueOf(r.get(8));
                    data[CriticalRoleConstants.WIS] =String.valueOf(r.get(9));
                    data[CriticalRoleConstants.CHA] =String.valueOf(r.get(10));
                    return data;
                }
            }
            
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        return new String[10];
    }
    
    private void draw(MessageReceivedEvent event, CriticalRoleCharacter crc)
    {
        try
        {
            
            String[] data = getData(crc.getRow());
            
            BufferedImage image = new BufferedImage(CriticalRoleConstants.IMAGE_WIDTH, CriticalRoleConstants.IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = image.createGraphics();

            graphics.drawImage(ImageIO.read(new URL(CriticalRoleConstants.IMAGE_URL)), 0, 0, null);
            
            try
            {
                graphics.drawImage(ImageIO.read(new URL(crc.getResource())), 0, 0, null);
            }
            catch (Exception ex)
            {
                graphics.drawImage(ImageIO.read(new URL(CriticalRoleConstants.AVATAR_URL)), 0, 0, null);
            }

            graphics.setColor(Color.BLACK);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            
            graphics.setFont(new Font("COCOGOOSE", Font.PLAIN, CriticalRoleConstants.NAME_FONT_SIZE));
            graphics.drawString(crc.getName(), CriticalRoleConstants.TEXT_X, CriticalRoleConstants.NAME_Y);
            
            graphics.setFont(new Font("COCOGOOSE", Font.PLAIN, CriticalRoleConstants.TITLE_FONT_SIZE));
            graphics.drawString(crc.getTitle(), CriticalRoleConstants.TEXT_X, CriticalRoleConstants.TITLE_Y);
            
            graphics.setFont(new Font("COCOGOOSE", Font.PLAIN, CriticalRoleConstants.DATA_FONT_SIZE)); 
            graphics.drawString(data[CriticalRoleConstants.STR],    CriticalRoleConstants.FIRST_COLUMN, CriticalRoleConstants.FIRST_ROW);
            graphics.drawString(data[CriticalRoleConstants.DEX],    CriticalRoleConstants.FIRST_COLUMN, CriticalRoleConstants.SECOND_ROW);
            graphics.drawString(data[CriticalRoleConstants.CON],    CriticalRoleConstants.FIRST_COLUMN, CriticalRoleConstants.THIRD_ROW);
            graphics.drawString(data[CriticalRoleConstants.INT],    CriticalRoleConstants.SECOND_COLUMN, CriticalRoleConstants.FIRST_ROW);
            graphics.drawString(data[CriticalRoleConstants.WIS],    CriticalRoleConstants.SECOND_COLUMN, CriticalRoleConstants.SECOND_ROW);
            graphics.drawString(data[CriticalRoleConstants.CHA],    CriticalRoleConstants.SECOND_COLUMN, CriticalRoleConstants.THIRD_ROW);
            graphics.drawString(data[CriticalRoleConstants.AC],     CriticalRoleConstants.THIRD_COLUMN, CriticalRoleConstants.FIRST_ROW);
            graphics.drawString(data[CriticalRoleConstants.HP],     CriticalRoleConstants.THIRD_COLUMN, CriticalRoleConstants.SECOND_ROW);
            graphics.drawString(data[CriticalRoleConstants.TMPHP],  CriticalRoleConstants.THIRD_COLUMN, CriticalRoleConstants.THIRD_ROW);
            
            float current = (data[CriticalRoleConstants.HP].equals("?")   ? 0 : Integer.parseInt(data[CriticalRoleConstants.HP]));
            float max    = (data[CriticalRoleConstants.MAXHP].equals("?") ? 0 : Integer.parseInt(data[CriticalRoleConstants.MAXHP]));
            
            float n = ((float) CriticalRoleConstants.HP_BAR_WIDTH / 100);
            float m = (((float) current / (float) max) * 100);

            int bar = (int) Math.round(n * m);
            
            graphics.setColor(Color.decode("0xE14040"));
            graphics.fillRect(CriticalRoleConstants.HP_BAR_X, CriticalRoleConstants.HP_BAR_Y, bar, CriticalRoleConstants.HP_BAR_HEIGHT);
            
            File temp = new File(System.getProperty("user.home"), "temp.png");
            ImageIO.write(image, "png", temp);
            
            event.getChannel().sendFile(temp, null);
            temp.delete();
            
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
