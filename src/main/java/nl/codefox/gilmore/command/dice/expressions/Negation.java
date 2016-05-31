
package nl.codefox.gilmore.command.dice.expressions;

import java.util.ArrayList;
import nl.codefox.gilmore.command.dice.Lexer;

public class Negation extends Expression {

    public Negation(ArrayList<Lexer.Token> tokens) {
        super(tokens);
        
        boolean twoNegations = false;
        
        for (int i = 0; i < tokens.size(); i ++) {
            if (tokens.get(i).type == Lexer.TokenType.NEGATION) {
                twoNegations = true;
                Negation a = new Negation (new ArrayList<> (tokens.subList(i + 1, tokens.size())));
                value = 0 - a.value;
                description = "-" + a.description;
            }
        }
        
        if (!twoNegations) {
            Atomic atomic = new Atomic (tokens);
            value = atomic.value;
            description = atomic.description;
        }
        
    }

}
