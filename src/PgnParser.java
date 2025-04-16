import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PgnParser {

    private static final Pattern TAG_PATTERN = Pattern.compile("\\[\\s*(\\w+)\\s*\"(.*?)\"\\s*\\]");

    private static final Pattern RESULT_PATTERN = Pattern.compile("(1-0|0-1|1/2-1/2|\\*)$");
    public ChessGame parseGame(String pgnText) {
        ChessGame game = new ChessGame();
        String[] lines = pgnText.split("\\r?\\n");

        String moveTextCombined = "";
        boolean inMoveSection = false;
        int lineNumber = 0;

        for (String line : lines) {
            lineNumber++;
            String trimmedLine = line.trim();

            if (trimmedLine.isEmpty()) {
                continue;
            }

            Matcher tagMatcher = TAG_PATTERN.matcher(trimmedLine);
            if (tagMatcher.matches()) {
                if (inMoveSection) {
                    game.addSyntaxError("Line " + lineNumber + ": Tag pair found after moves started: " + trimmedLine);
                } else {
                    String tagName = tagMatcher.group(1);
                    String tagValue = tagMatcher.group(2);
                    game.addTag(tagName, tagValue);
                }
            } else if (trimmedLine.startsWith("[")) {
                game.addSyntaxError("Line " + lineNumber + ": Malformed tag pair: " + trimmedLine);
            }
            else {
                inMoveSection = true;
                moveTextCombined += trimmedLine + " ";
            }
        }

        if (inMoveSection && !moveTextCombined.isEmpty()) {
            parseMoveText(game, moveTextCombined.trim());
        } else if (!inMoveSection && !game.getTags().isEmpty()) {
            game.addSyntaxError("Game has tags but no move text found.");
        } else if (game.getTags().isEmpty() && !inMoveSection) {
            game.addSyntaxError("No valid tags or move text found in game block.");
        }

        if (!game.hasSyntaxErrors()) {
            if (!game.hasSyntaxErrors()) {
                game.setStatus(ChessGame.ValidationStatus.SYNTAX_OK);
            }
        }

        return game;
    }
    private void parseMoveText(ChessGame game, String moveData) {
        String cleanedMoveData = moveData.replaceAll("\\{.*?\\}", "").replaceAll(";.*", "");
        String[] tokens = cleanedMoveData.split("\\s+");

        boolean ended = false;
        for (String token : tokens) {
            if (token.isEmpty() || ended) continue;
            Matcher resultMatcher = RESULT_PATTERN.matcher(token);
            if (resultMatcher.matches()) {
                ended = true;
                continue;
            }

            if (token.matches("\\d+\\.+")) {
                continue;
            }
            if (!token.matches("^[a-h1-8KQRNBx=+#\\-O]+$")) {
                if (!token.equals("O-O") && !token.equals("O-O-O")) {
                    game.addSyntaxError("Invalid characters in move token: " + token);
                    continue;
                }
            }

            game.addMoveString(token);
        }
    }
}