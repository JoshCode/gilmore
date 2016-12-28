package nl.codefox.gilmore.command.dice.expressions;

import nl.codefox.gilmore.command.dice.Lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Die extends Expression {

    private int numberOfDice = 0;
    private int numberOfFaces = 1;
    private int keepNumber = 0;
    private boolean keepHighest = false;
    private boolean keepLowest = false;
    private boolean exploding = false;
    private int rerollNumber = 0;
    private boolean rerollOnce = false;
    private boolean reroll = false;
    private int critSuccessNumber = numberOfFaces;
    private int critFailNumber = 1;
    private int rerolledDice = 0;

    /**
     * Sets up a die with the selected options. Then roll the dice and add the explanation to the
     * description.
     */
    public Die(ArrayList<Lexer.Token> tokens) {
        super(tokens);

        for (int i = 0; i < tokens.size(); i++) {
            Lexer.Token token = tokens.get(i);
            description += token.data;
            processTriggers(token);
        }

        // The reroll cooldown. Only used for rerollOnce
        boolean justRerolled = false;

        Random random = new Random();
        List<Integer> rolls = new ArrayList<>();
        List<String> rollDescriptions = new ArrayList<>();
        // Roll the dice, add extra where necessary
        for (int i = 0; i < Math.min(numberOfDice, 1000); i++) {
            int roll = random.nextInt(numberOfFaces) + 1;
            String rollDescription = String.valueOf(roll);

            // Roll explodes, add one more die
            if (exploding && roll >= critSuccessNumber) {
                numberOfDice++;
                rollDescription += "!";
            }

            // Roll is crit success
            if (roll >= critSuccessNumber) {
                rollDescription = "**" + rollDescription + "**";
            }

            // Roll is crit fail
            if (roll <= critFailNumber) {
                rollDescription = "*" + rollDescription + "*";
            }

            // Roll under rerollOnceNumber
            if (rerollOnce && roll <= rerollNumber && !justRerolled) {
                numberOfDice++;
                rerolledDice++;
                justRerolled = true;
                roll = 0;
                rollDescription = "~~" + rollDescription + "~~";
            }
            // Reset the reroll cooldown
            else if (rerollOnce && justRerolled) {
                justRerolled = false;
            }

            // Roll under rerollNumber
            if (reroll && roll <= rerollNumber) {
                numberOfDice++;
                rerolledDice++;
                roll = 0;
                rollDescription = "~~" + rollDescription + "~~";
            }

            rolls.add(roll);
            rollDescriptions.add(rollDescription);
        }

        // Drop the higher dice
        if (keepLowest) {
            for (int j = 0; j < rolls.size() - keepNumber - rerolledDice; j++) {
                int highest = 1, highestIndex = 0;
                // Find the highest die
                for (int i = 0; i < rolls.size(); i++) {
                    if (highest < rolls.get(i) && rolls.get(i) > 0) {
                        highest = rolls.get(i);
                        highestIndex = i;
                    }
                }
                // Destroy it
                rolls.set(highestIndex, 0);
                String desc = rollDescriptions.get(highestIndex);
                if (desc.length() < 2 || !"~~".equals(desc.substring(0, 2))) {
                    rollDescriptions.set(highestIndex, "~~" + desc + "~~");
                }
            }
        }

        // Drop the lower dice
        if (keepHighest) {
            for (int j = 0; j < rolls.size() - keepNumber - rerolledDice; j++) {
                int lowest = numberOfFaces, lowestIndex = 0;
                // Find the lowest die
                for (int i = 0; i < rolls.size(); i++) {
                    if (lowest > rolls.get(i) && rolls.get(i) > 0) {
                        lowest = rolls.get(i);
                        lowestIndex = i;
                    }
                }
                // Destroy it
                rolls.set(lowestIndex, 0);
                String desc = rollDescriptions.get(lowestIndex);
                if (desc.length() < 2 || !"~~".equals(desc.substring(0, 2))) {
                    rollDescriptions.set(lowestIndex, "~~" + desc + "~~");
                }
            }
        }

        // Construct the final variables (value and description)
        value = 0;
        for (int roll : rolls) {
            value += roll;
        }

        description += " (";
        for (String desc : rollDescriptions) {
            description += desc + ", ";
        }
        if (!rollDescriptions.isEmpty()) {
            description = description.substring(0, description.length() - 2);
        }
        description = description + ")";

    }

    /**
     * Convert all the options to variables.
     */
    private void processTriggers(Lexer.Token token) {

        switch (token.type) {
            // This is the main part, define the type of dice being rolled
            case DIE: {
                String data = token.data.toLowerCase();
                int firstD = data.indexOf('d');

                // Parse number of dice
                if (firstD > 0) {
                    numberOfDice = Integer.parseInt(data.substring(0, firstD));
                } else {
                    numberOfDice = 1;
                }

                // Parse type of die
                numberOfFaces = Integer.parseInt(data.substring(firstD + 1));
                // Prevent crash
                if (numberOfFaces == 0) {
                    numberOfFaces = 1;
                }
                critSuccessNumber = numberOfFaces;
                break;
            }

            case EXPLODING: {
                exploding = true;
                break;
            }

            case KEEPHIGH: {
                keepHighest = true;
                keepLowest = false;
                String data = token.data.toLowerCase();
                if (data.charAt(1) == 'h') {
                    keepNumber = Integer.parseInt(data.substring(2));
                } else {
                    keepNumber = Integer.parseInt(data.substring(1));
                }
                break;
            }

            case KEEPLOW: {
                keepHighest = false;
                keepLowest = true;
                String data = token.data.toLowerCase();
                keepNumber = Integer.parseInt(data.substring(2));
                break;
            }

            case CRITSUCCESS: {
                String data = token.data.toLowerCase();
                critSuccessNumber = Integer.parseInt(data.substring(3));
                break;
            }

            case CRITFAIL: {
                String data = token.data.toLowerCase();
                critFailNumber = Integer.parseInt(data.substring(3));
                break;
            }

            case REROLL: {
                reroll = true;
                rerollOnce = false;
                String data = token.data.toLowerCase();
                rerollNumber = Integer.parseInt(data.substring(2));
                break;
            }

            case REROLLONCE: {
                reroll = false;
                rerollOnce = true;
                String data = token.data.toLowerCase();
                rerollNumber = Integer.parseInt(data.substring(3));
                break;
            }
        }
    }


}
