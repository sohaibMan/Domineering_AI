package com.example.domineering.Helpers;

import com.example.domineering.DomineeringGUI;
import com.example.domineering.Position.Position;
import javafx.scene.control.Alert;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static com.example.domineering.DomineeringGUIConfig.*;

public class DomineeringHelpers extends Helpers {
    @Override
    public void saveGame(Position position) {

        // saveGame function (using a file.txt)

        // Create a FileChooser object
        FileChooser fileChooser = new FileChooser();

        // Set the title of the FileChooser
        fileChooser.setTitle("Save Game");

        // Set the initial directory
        fileChooser.setInitialDirectory(new File("."));

        // Set the extension filter
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"), new FileChooser.ExtensionFilter("All Files", "*.*"));

        // Display the save file dialog
        File file = fileChooser.showSaveDialog(null);

        // Check if the user clicked the Save button
        if (file != null) {
            // Save the game to the file
            try (FileWriter fileWriter = new FileWriter(file)) {
                // Write the current player
                fileWriter.write(position.getCurrentPlayer() + "\n");

                // Write the number of moves for each player
                fileWriter.write(position.getMovesPlayer(1) + "\n");
                fileWriter.write(position.getMovesPlayer(2) + "\n");
                // write the number of hints
                fileWriter.write(DomineeringGUI.hintCount + "\n");

                // Write the state of each square
                for (int row = 0; row < position.getNumSquares(); row++) {
                    for (int col = 0; col < position.getNumSquares(); col++) {
                        Rectangle square = (Rectangle) position.getGridPane().getChildren().get(row * position.getNumSquares() + col);
                        fileWriter.write(square.isDisable() + "\n");

                        // Write the color of the square
                        boolean isFirstPlayerColor = square.getFill().equals(FIRST_PLAYER_COLOR);
                        fileWriter.write(isFirstPlayerColor + "\n");
                    }
                }

                // Display a confirmation dialog
                Alert confirmationDialog = new Alert(Alert.AlertType.INFORMATION);
                confirmationDialog.setTitle("Confirmation");
                confirmationDialog.setHeaderText("Game Saved");
                confirmationDialog.setContentText("The game has been saved successfully.");
                confirmationDialog.show();
            } catch (IOException e) {
                // Display an error dialog
                Alert errorDialog = new Alert(Alert.AlertType.ERROR);
                errorDialog.setTitle("Error");
                errorDialog.setHeaderText("Error Saving Game");
                errorDialog.setContentText("An error occurred while saving the game.");
                errorDialog.show();
            }
        }

    }


    //Add Load Game function (loading from a text file)
    public void loadGame(Position position) {
        // Create a FileChooser object
        FileChooser fileChooser = new FileChooser();

        // Set the title of the FileChooser
        fileChooser.setTitle("Load Game");

        // Set the initial directory
        fileChooser.setInitialDirectory(new File("."));

        // Set the extension filter
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"), new FileChooser.ExtensionFilter("All Files", "*.*"));

        // Display the open file dialog
        File file = fileChooser.showOpenDialog(null);

        // Check if the user clicked the Open button
        if (file != null) {
            // Load the game from the file
            try (Scanner scanner = new Scanner(file)) {
                // Read the current player
                position.setCurrentPlayer(scanner.nextInt());

                // Read the number of moves for each player
                position.setMovesPlayer(1, scanner.nextInt());
                position.setMovesPlayer(2, scanner.nextInt());

                // read the number of hints

                DomineeringGUI.hintCount = scanner.nextInt();
                System.out.println("-------------HINT-----------------"+DomineeringGUI.hintCount);
//                DomineeringGUI.setHintCount(DomineeringGUI.hintCount);


                // Read the state of each square
                for (int row = 0; row < position.getNumSquares(); row++) {
                    for (int col = 0; col < position.getNumSquares(); col++) {
                        Rectangle square = (Rectangle) position.getGridPane().getChildren().get(row * position.getNumSquares() + col);
                        boolean isSquareDisabled = scanner.nextBoolean();
                        square.setDisable(isSquareDisabled);

                        // Read and set the color of the square
                        boolean isFirstPlayerColor = scanner.nextBoolean();
                        square.setFill(isFirstPlayerColor ? FIRST_PLAYER_COLOR : SECOND_PLAYER_COLOR);

                        if (isSquareDisabled) {
                            square.setStroke(DEFAULT_STROKE_COLOR);
                        } else {
                            square.setStroke(DEFAULT_STROKE_COLOR);
                        }
                    }
                }


                // Display a confirmation dialog
                Alert confirmationDialog = new Alert(Alert.AlertType.INFORMATION);
                confirmationDialog.setTitle("Confirmation");
                confirmationDialog.setHeaderText("Game Loaded");
                confirmationDialog.setContentText("The game has been loaded successfully.");
                confirmationDialog.show();
            } catch (IOException e) {
                // Display an error dialog
                Alert errorDialog = new Alert(Alert.AlertType.ERROR);
                errorDialog.setTitle("Error");
                errorDialog.setHeaderText("Error Loading Game");
                errorDialog.setContentText("An error occurred while loading the game.");
                errorDialog.show();
            }
        }
    }


}
