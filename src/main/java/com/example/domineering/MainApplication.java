package com.example.domineering;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Arrays;


public class MainApplication extends Application {

    private static final int BOX_SIZE = 160; // Size of the box
    private static final int NUM_SQUARES = 5; // Number of squares in each dimension

    private int currentPlayer = 1; // 1 for horizontal player, 2 for vertical player

    //? first player is horizontal player
    private static final Color FIRST_PLAYER_COLOR = Color.RED;
    private static final Color FIRST_PLAYER_HOVER_COLOR = Color.web("#ffc9c9");
    private static final Color FIRST_PLAYER_HOVER_FILL_COLOR = Color.web("#e03131");

    //? second player is vertical player
    private static final Color SECOND_PLAYER_COLOR = Color.web("#1864ab");
    private static final Color SECOND_PLAYER_HOVER_COLOR = Color.web("#1864ab");
    private static final Color SECOND_PLAYER_HOVER_FILL_COLOR = Color.web("74c0fc");

    private static final Color DEFAULT_FILL_COLOR = Color.TRANSPARENT;
    private static final Color DEFAULT_FILL_COLOR_2 = Color.BLACK;
    private static final Color DEFAULT_STROKE_COLOR = Color.BLACK;


    private static final boolean DEBUG = false;

    private final GridPane gridPane = new GridPane();

    @Override
    public void start(Stage stage) {
        // Set the title of the window
        stage.setTitle("Welcome to Domineering Game!");

        int squareSize = BOX_SIZE / NUM_SQUARES;

        for (int row = 0; row < NUM_SQUARES; row++) {
            for (int col = 0; col < NUM_SQUARES; col++) {
                Rectangle square = new Rectangle(squareSize, squareSize);
                square.setFill((row + col) % 2 == 0 ? DEFAULT_FILL_COLOR : DEFAULT_FILL_COLOR_2);
                square.setStroke(DEFAULT_STROKE_COLOR);

                // Add the square to the grid
                gridPane.add(square, col, row);
                if (DEBUG) {
                    gridPane.add(new Text(col + "," + row), col, row);
                }

                square.getProperties().put("col", col);
                square.getProperties().put("row", row);

                square.setOnMouseClicked(this::onSquareClicked);

                square.setOnMouseEntered(event -> {
                    Rectangle hoveredSquare = (Rectangle) event.getSource();
                    Rectangle neighbourSquare = getNeighbourSquare((Rectangle) event.getSource());
                    if (neighbourSquare == null) return;

                    Paint currentPlayerHoverColor = this.currentPlayer == 1 ? FIRST_PLAYER_HOVER_COLOR : SECOND_PLAYER_HOVER_COLOR;
                    Paint currentPlayerFillColor = this.currentPlayer == 1 ? FIRST_PLAYER_HOVER_FILL_COLOR : SECOND_PLAYER_HOVER_FILL_COLOR;

                    hoveredSquare.setStroke(currentPlayerHoverColor);
                    hoveredSquare.setFill(currentPlayerFillColor);
                    neighbourSquare.setStroke(currentPlayerHoverColor);
                    neighbourSquare.setFill(currentPlayerFillColor);
                });

                square.setOnMouseExited(event -> {
                    Rectangle hoveredSquare = (Rectangle) event.getSource();
                    Rectangle neighbourSquare = getNeighbourSquare((Rectangle) event.getSource());
                    resetRectangleColors(hoveredSquare, neighbourSquare);
                });
            }
        }

        // Create a VBox to contain the grid and center it
        VBox vBox = new VBox(gridPane);
        vBox.setAlignment(Pos.CENTER);

        // Shift the entire VBox to the right to create a margin
        vBox.setTranslateX(20); // Adjust the value (20) as needed

        // Create a MenuBar
        MenuBar menuBar = new MenuBar();

        // Create a File menu
        Menu fileMenu = new Menu("File");

        // Create menu items
        MenuItem newGameMenuItem = new MenuItem("New Game");
        MenuItem restartMenuItem = new MenuItem("Restart");
        MenuItem exitMenuItem = new MenuItem("Exit");

        // Set event handlers for menu items
        newGameMenuItem.setOnAction(e -> startNewGame());
        restartMenuItem.setOnAction(e -> restartGame());
        exitMenuItem.setOnAction(e -> exitGame());

        // Add menu items to the File menu
        fileMenu.getItems().addAll(newGameMenuItem, restartMenuItem, exitMenuItem);

        // Add the File menu to the MenuBar
        menuBar.getMenus().add(fileMenu);

        // Create a new scene with the VBox and MenuBar as the root
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(vBox);

        Scene scene = new Scene(root, BOX_SIZE * NUM_SQUARES, BOX_SIZE * NUM_SQUARES);

        // Set the scene to the stage
        stage.setScene(scene);

        // Set the minimum size of the stage to create a square window
        stage.setMinWidth(BOX_SIZE * NUM_SQUARES + 20); // Consider the margin
        stage.setMinHeight(BOX_SIZE * NUM_SQUARES);

        // Show the stage
        stage.show();
    }

    private void startNewGame() {
        // Enable all squares and reset their appearance
        gridPane.getChildren().forEach(node -> {
            if (node instanceof Rectangle) {
                Rectangle square = (Rectangle) node;
                square.setDisable(false);
                resetRectangleColor(square);
            }
        });

        // Reset any other game state variables if needed

        // Start the game with the initial player (let's assume it's player 1)
        this.currentPlayer = 1;

        System.out.println("Starting a new game!");
    }


    // TODO: RESTART GAME
    private void restartGame() {
        // Display a confirmation dialog before restarting the game
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.initModality(Modality.APPLICATION_MODAL);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText("Restart Game");
        confirmationDialog.setContentText("Are you sure you want to restart the game?");

        // Customize the buttons in the confirmation dialog
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        confirmationDialog.getButtonTypes().setAll(yesButton, noButton);

        // Show the confirmation dialog and handle the result
        confirmationDialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == yesButton) {
                // User clicked "Yes," so restart the game
                startNewGame();
            } else {
                // User clicked "No," do nothing
            }
        });
    }


    // TODO: EXIT GAME
    private void exitGame() {
        // Display a confirmation dialog before exiting the game
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.initModality(Modality.APPLICATION_MODAL);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText("Exit Game");
        confirmationDialog.setContentText("Are you sure you want to exit the game?");

        // Customize the buttons in the confirmation dialog
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        confirmationDialog.getButtonTypes().setAll(yesButton, noButton);

        // Show the confirmation dialog and handle the result
        confirmationDialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == yesButton) {
                // User clicked "Yes," so exit the application
                System.exit(0);
            } else {
                // User clicked "No," do nothing
            }
        });
    }



    // Event handler for square click
    private void onSquareClicked(MouseEvent event) {
        Rectangle neighbourSquare = getNeighbourSquare((Rectangle) event.getSource());
        if (neighbourSquare != null) {
            Rectangle clickedSquare = (Rectangle) event.getSource();
            Paint currentPlayerColor = this.currentPlayer == 1 ? FIRST_PLAYER_COLOR : SECOND_PLAYER_COLOR;
            Paint currentPlayerStorkColor = DEFAULT_STROKE_COLOR;
            // filling the bottom square with the color of the current player
            // filling the square with the color of the current player
            clickedSquare.setFill(currentPlayerColor);
            clickedSquare.setStroke(currentPlayerStorkColor);
            // disable the square
            clickedSquare.setDisable(true);
            // filling the bottom square with the color of the current player
            neighbourSquare.setFill(currentPlayerColor);
            neighbourSquare.setStroke(currentPlayerStorkColor);
            // disable the square
            neighbourSquare.setDisable(true);

            //toggle player
            this.currentPlayer = this.currentPlayer == 1 ? 2 : 1;

            // check if the game is over
            checkIfGameIsOver();
        }


    }

    private void checkIfGameIsOver() {
        // enhance this function
        for (int row = 0; row < NUM_SQUARES; row++) {
            for (int col = 0; col < NUM_SQUARES; col++) {
                Rectangle square = (Rectangle) gridPane.getChildren().get(row * NUM_SQUARES + col);
                Rectangle neighbourSquare = getNeighbourSquare(square);
                if (!square.isDisable() && neighbourSquare != null && !neighbourSquare.isDisable()) return;
            }
        }
        // announce that the game is over on a  box
        Text text = new Text("Game Over");
        text.setStyle("-fx-font-size: 100px;");
        gridPane.getChildren().clear();
        gridPane.add(text, 0, 0);
        // announce the winner
        Text winner = new Text("Winner is player " + (this.currentPlayer == 1 ? 2 : 1));
        winner.setStyle("-fx-font-size: 100px;");
        gridPane.add(winner, 0, 1);
    }


    public static void main(String[] args) {
        launch();
    }

    static private void resetRectangleColors(Rectangle... rectangles) {
        // call resetRectangleColor for each rectangle in the list
        Arrays.stream(rectangles).forEach(MainApplication::resetRectangleColor);
    }

    private static void resetRectangleColor(Rectangle rectangle) {
        if (rectangle == null || rectangle.isDisable()) return;
        int row = (int) rectangle.getProperties().get("row");
        int col = (int) rectangle.getProperties().get("col");
        // set the previous color of the rectangle
        // set the default color of the rectangle
        rectangle.setFill((row + col) % 2 == 0 ? DEFAULT_FILL_COLOR : DEFAULT_FILL_COLOR_2); // Set the fill color to transparent
        rectangle.setStroke(DEFAULT_STROKE_COLOR); // Set the border color
    }

    private Rectangle getNeighbourSquare(Rectangle rectangle) {
        int clickedSquareRow = (int) rectangle.getProperties().get("row");
        int clickedSquareCol = (int) rectangle.getProperties().get("col");
        // check the current player
        if (this.currentPlayer == 1)
            return this.gridPane.getChildren().stream().filter(node -> GridPane.getRowIndex(node) == clickedSquareRow + 1 && GridPane.getColumnIndex(node) == clickedSquareCol && !node.isDisable()).map(node -> (Rectangle) node).findFirst().orElse(null);
        else
            return this.gridPane.getChildren().stream().filter(node -> GridPane.getRowIndex(node) == clickedSquareRow && GridPane.getColumnIndex(node) == clickedSquareCol + 1 && !node.isDisable()).map(node -> (Rectangle) node).findFirst().orElse(null);


    }
}