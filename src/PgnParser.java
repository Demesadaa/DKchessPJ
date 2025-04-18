import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PgnParser {

    private static final Pattern TAG_PATTERN = Pattern.compile("\\[\\s*(\\w+)\\s*\"(.*?)\"\\s*\\]");
    private static final Pattern RESULT_PATTERN = Pattern.compile("(1-0|0-1|1/2-1/2|\\*)$");
    private static final Pattern MOVE_NUMBER_PREFIX_PATTERN = Pattern.compile("^\\d+\\.");

    public ChessGame parseGame(String pgnText) {
        ChessGame game = new ChessGame();
        String[] lines = pgnText.split("\\r?\\n");

        StringBuilder moveTextCombined = new StringBuilder();
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
                    System.out.println("Warning (Line " + lineNumber + "): Tag pair found after moves started: " + trimmedLine);
                }
                String tagName = tagMatcher.group(1);
                String tagValue = tagMatcher.group(2);
                game.addTag(tagName, tagValue);

            } else if (trimmedLine.startsWith("[")) {
                game.addSyntaxError("Line " + lineNumber + ": Malformed tag pair: " + trimmedLine);
            } else if (trimmedLine.startsWith(";")) {
                continue;
            }
            else {
                inMoveSection = true;
                moveTextCombined.append(trimmedLine).append(" ");
            }
        }

        if (inMoveSection && moveTextCombined.length() > 0) {
            parseMoveText(game, moveTextCombined.toString().trim());
        } else if (!inMoveSection && !game.getTags().isEmpty()) {
            if (!game.getTags().containsKey("Result") && game.getPgnMoveStrings().isEmpty()) {
                game.addSyntaxError("Game has tags but no move text found.");
            }
        } else if (game.getTags().isEmpty() && !inMoveSection) {
            game.addSyntaxError("No valid tags or move text found in game block.");
        }

        if (!game.hasSyntaxErrors() && game.getPgnMoveStrings().isEmpty() && !game.getTags().containsKey("Result")) {
            if (!game.getTags().isEmpty()) {
                game.setStatus(ChessGame.ValidationStatus.SYNTAX_OK);
            } else {
                if (game.getStatus() != ChessGame.ValidationStatus.SYNTAX_ERROR){
                    game.addSyntaxError("Game block appears empty or invalid.");
                }
            }
        } else if (!game.hasSyntaxErrors()) {
            game.setStatus(ChessGame.ValidationStatus.SYNTAX_OK);
        }

        return game;
    }


    private void parseMoveText(ChessGame game, String moveData) {
        String cleanedMoveData = moveData.replaceAll("\\{.*?\\}", " ").replaceAll(";.*?(?=\\d+\\.|$|\\n)", " ");
        cleanedMoveData = cleanedMoveData.replaceAll("[!?]+", " ");
        cleanedMoveData = cleanedMoveData.replaceAll("\\$\\d+", " ");

        String[] tokens = cleanedMoveData.trim().split("\\s+");

        boolean gameEnded = false;

        for (String originalToken : tokens) {
            if (originalToken.isEmpty() || gameEnded) {
                continue;
            }

            Matcher resultMatcher = RESULT_PATTERN.matcher(originalToken);
            if (resultMatcher.matches()) {
                gameEnded = true;
                continue;
            }

            String movePart = originalToken;
            Matcher moveNumMatcher = MOVE_NUMBER_PREFIX_PATTERN.matcher(originalToken);

            if (moveNumMatcher.find()) {
                if (originalToken.matches("^\\d+\\.+$")) {
                    continue;
                } else {
                    movePart = originalToken.substring(moveNumMatcher.end());
                    if (movePart.isEmpty()) {
                        continue;
                    }
                }
            }

            if (!movePart.matches("^[a-h1-8KQRNBx=+#\\-O]+$")) {
                if (!movePart.equals("O-O") && !movePart.equals("O-O-O")) {
                    game.addSyntaxError("Invalid characters or format in move: '" + movePart + "' (from token: '" + originalToken + "')");
                    continue;
                }
            }

            game.addMoveString(movePart);
        }
    }
}