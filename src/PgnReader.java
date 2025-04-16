import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PgnReader {

    private PgnReader() {}

    public static List<String> readGamesFromFile(String filePath) throws IOException {
        List<String> gameStrings = new ArrayList<>();
        StringBuilder currentGame = new StringBuilder();
        boolean inMoveSection = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    if (currentGame.length() > 0 && inMoveSection) {
                        gameStrings.add(currentGame.toString());
                        currentGame.setLength(0);
                        inMoveSection = false;
                    }
                    continue;
                }

                if (line.startsWith("[")) {
                    if (inMoveSection && currentGame.length() > 0) {
                        gameStrings.add(currentGame.toString());
                        currentGame.setLength(0);
                        inMoveSection = false;
                    }
                    currentGame.append(line).append("\n");
                }
                else if (Character.isDigit(line.charAt(0)) || line.startsWith("1.")) {
                    inMoveSection = true;
                    currentGame.append(line).append("\n");
                }
                else {
                    if (currentGame.length() > 0) {
                        currentGame.append(line).append("\n");
                        if (!inMoveSection && !line.startsWith("[")) {
                            inMoveSection = true;
                        }
                    }
                }
            }
            if (currentGame.length() > 0) {
                gameStrings.add(currentGame.toString());
            }
        }

        return gameStrings;
    }
}