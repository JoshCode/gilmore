package nl.codefox.gilmore.command.dice;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexer {
    public static enum TokenType {
        COMMENT("\\[([^\\]]+)\\]"), DIE("(\\d+)?[dD](\\d+)"), REROLLONCE("ro<(\\d+)"), REROLL("r<(\\d+)"), EXPLODING("\\!"), KEEPHIGH("k(h?)(\\d+)"), KEEPLOW("kl(\\d+)"),
        CRITSUCCESS("cs>(\\d+)"), CRITFAIL("cf<(\\d+)"), MULTIPLICATION("\\*"), NEGATION("\\+?\\-"), ADDITION("\\+"), ATOMIC("(\\d+)"), GIBBERISH ("[a-zA-Z]+"), WHITESPACE("(\\s+)");

        public final String pattern;

        private TokenType(String pattern) {
          this.pattern = pattern;
        }
    }

    public static class Token {
        public TokenType type;
        public String data;

        public Token(TokenType type, String data) {
            this.type = type;
            this.data = data;
        }

        @Override
        public String toString() {
            return String.format("(%s %s)", type.name(), data);
        }
    }

    public static ArrayList<Token> lex(String input) {

        ArrayList<Token> tokens = new ArrayList<>();

        StringBuilder tokenPatternsBuffer = new StringBuilder();
        for (TokenType tokenType : TokenType.values()) {
            tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
        }
        Pattern tokenPatterns = Pattern.compile(tokenPatternsBuffer.substring(1));

        Matcher matcher = tokenPatterns.matcher(input);
        while (matcher.find()) {
            for (TokenType tk: TokenType.values()) {
                if (matcher.group(TokenType.WHITESPACE.name()) != null)
                    continue;
                else if (matcher.group(tk.name()) != null) {
                    if (tk == TokenType.NEGATION)
                        tokens.add(new Token(TokenType.ADDITION, "+"));
                    tokens.add(new Token(tk, matcher.group(tk.name())));
                    break;
                }
            }
        }

        return tokens;
    }

    public static void main(String[] args) {
        String input = "1d2 1D2";
        System.out.println(input);
        
        ArrayList<Token> tokens = lex(input);
        for (Token token : tokens)
            System.out.println(token);
    }
}