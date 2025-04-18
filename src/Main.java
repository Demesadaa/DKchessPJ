import java.io.IOException;
import java.util.List;

import model.Board;
import model.Move;
import model.MoveInterpreter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String filePath = null;

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Please enter the absolute path to the PGN file: ");
            filePath = scanner.nextLine();
        }

        if (filePath == null || filePath.trim().isEmpty()) {
            System.err.println("Error: No file path entered. Exiting.");
            return;
        }



        System.out.println("Attempting to read PGN file: " + filePath);

        List<String> gameStrings;
        try {
            gameStrings = PgnReader.readGamesFromFile(filePath);
            System.out.println("Found " + gameStrings.size() + " game(s) in the file.");
        } catch (IOException e) {
            System.err.println("Error reading PGN file '" + filePath + "': " + e.getMessage());
            System.err.println("Please check if the path is correct and the file exists and is readable.");
            return;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during file reading: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        PgnParser parser = new PgnParser();
        MoveInterpreter interpreter = new MoveInterpreter();
        GameSimulator simulator = new GameSimulator();

        int gameCounter = 0;
        int validGames = 0;
        int syntaxErrorGames = 0;
        int logicErrorGames = 0;


        for (String gameString : gameStrings) {
            gameCounter++;
            System.out.println("\n--- Processing Game " + gameCounter + " ---");
            ChessGame game = parser.parseGame(gameString);

            if (game.hasSyntaxErrors()) {
                System.out.println("Result: Syntax Errors Found during Parsing.");
                game.setStatus(ChessGame.ValidationStatus.SYNTAX_ERROR);
                syntaxErrorGames++;
            } else {
                System.out.println("Parsing: Syntax OK.");
                game.setStatus(ChessGame.ValidationStatus.SYNTAX_OK);

                List<Move> interpretedMoves = new ArrayList<>();
                Board interpretationBoard = new Board();
                boolean interpretationFailed = false;

                for (int i = 0; i < game.getPgnMoveStrings().size(); i++) {
                    String pgnMove = game.getPgnMoveStrings().get(i);
                    MoveInterpreter.InterpretationResult result = interpreter.interpretMove(interpretationBoard, pgnMove);

                    if (result.isSuccess()) {
                        interpretedMoves.add(result.move);
                        try {
                            interpretationBoard.makeMove(result.move);
                        } catch (Exception e) {
                            System.err.println("!!! CRITICAL ERROR: Applying interpreted move failed during interpretation step for move " + (i+1) + " ('" + pgnMove + "'): " + e.getMessage());
                            e.printStackTrace();
                            System.err.println("Board state before failed application:\n" + interpretationBoard);
                            game.setLogicalError("Internal error applying interpreted move '" + pgnMove + "': " + e.getMessage(), i);
                            interpretationFailed = true;
                            break;
                        }
                    } else {
                        System.out.println("Interpretation failed at move " + (i+1) + " ('" + pgnMove + "'): " + result.errorMessage);
                        game.setLogicalError("Move Interpretation Failed: " + result.errorMessage, i);
                        interpretationFailed = true;
                        break;
                    }
                }

                if (!interpretationFailed) {
                    System.out.println("Interpretation: All moves interpreted successfully.");
                    GameSimulator.SimulationResult simResult = simulator.simulateGame(interpretedMoves);

                    if (simResult.isValid) {
                        System.out.println("Simulation: Game logic is VALID.");
                        game.setStatus(ChessGame.ValidationStatus.VALID);
                        validGames++;
                    } else {
                        System.out.println("Simulation: Game logic INVALID.");

                        game.setLogicalError(simResult.message, simResult.errorMoveIndex);
                        logicErrorGames++;
                    }
                } else {
                    System.out.println("Simulation: Skipped due to interpretation errors.");
                    logicErrorGames++;
                }
            }

            System.out.println("\n--- Game " + gameCounter + " Final Report ---");
            System.out.println(game.toString());
            System.out.println("---------------------------\n");

        }

        System.out.println("================ Summary ================");
        System.out.println("Total Games Processed: " + gameCounter);
        System.out.println("Valid Games:           " + validGames);
        System.out.println("Syntax Error Games:    " + syntaxErrorGames);
        System.out.println("Logical Error Games:   " + logicErrorGames);
        System.out.println("=======================================");
        System.out.println("Finished processing all games.");
    }
}