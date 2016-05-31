
package nl.codefox.gilmore.command.dice.expressions;

import java.util.ArrayList;
import nl.codefox.gilmore.command.dice.Lexer;

public class Die extends Expression {

    public Die(ArrayList<Lexer.Token> tokens) {
        super(tokens);
        
        int numberOfDice = 0;
        int numberOfFaces = 0;
        int keepNumber = 0;
        boolean keepHighest = false;
        boolean keepLowest = false;
        boolean exploding = false;
        int rerollNumber = 0;
        boolean rerollOnce = false;
        boolean reroll = false;
        
        for (int i = 0; i < tokens.size(); i ++) {
            description += tokens.get(i).data;
            
            if (tokens.get(i).type == Lexer.TokenType.DIE) {
                String data = tokens.get(i).data.toLowerCase();
                int firstD = data.indexOf('d');
                if (firstD > 0) {
                    numberOfDice = Integer.parseInt(data.substring(0, firstD));
                }
                else {
                    numberOfDice = 1;
                }
                numberOfFaces = Integer.parseInt(data.substring(firstD + 1));
            }
            
            if (tokens.get(i).type == Lexer.TokenType.EXPLODING) {
                exploding = true;
            }
            
            if (tokens.get(i).type == Lexer.TokenType.KEEPHIGH) {
                keepHighest = true;
                keepLowest = false;
                String data = tokens.get(i).data.toLowerCase();
                if (data.charAt(1) == 'h') {
                    keepNumber = Integer.parseInt(data.substring(2));
                }
                else {
                    keepNumber = Integer.parseInt(data.substring(1));
                }
            }
            
            if (tokens.get(i).type == Lexer.TokenType.KEEPLOW) {
                keepHighest = false;
                keepLowest = true;
                String data = tokens.get(i).data.toLowerCase();
                keepNumber = Integer.parseInt(data.substring(2));
            }
            
            if (tokens.get(i).type == Lexer.TokenType.REROLL) {
                reroll = true;
                rerollOnce = false;
                String data = tokens.get(i).data.toLowerCase();
                rerollNumber = Integer.parseInt(data.substring(2));
            }
            
            if (tokens.get(i).type == Lexer.TokenType.REROLLONCE) {
                reroll = false;
                rerollOnce = true;
                String data = tokens.get(i).data.toLowerCase();
                rerollNumber = Integer.parseInt(data.substring(3));
            }
        }
    }


}
