
package nl.codefox.gilmore.command.dice.expressions;

import java.util.ArrayList;
import nl.codefox.gilmore.command.dice.Lexer;

public abstract class Expression {

    public ArrayList<Lexer.Token> tokens;
    public int value;
    public String description = "";
    
    public Expression (ArrayList <Lexer.Token> tokens) {
        this.tokens = tokens;
    }
    
}
