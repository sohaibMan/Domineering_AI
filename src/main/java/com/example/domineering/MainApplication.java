package com.example.domineering;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;


public class MainApplication extends Application {

    private static final int BOX_SIZE = 400; // Size of the box
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
        // todo center the window and add menu bar result and other stuff
        stage.setTitle("Welcome to Domineering Game !");


        int squareSize = BOX_SIZE / NUM_SQUARES;

        for (int row = 0; row < NUM_SQUARES; row++) {
            for (int col = 0; col < NUM_SQUARES; col++) {
                Rectangle square = new Rectangle(squareSize, squareSize);
                square.setFill((row + col) % 2 == 0 ? DEFAULT_FILL_COLOR : DEFAULT_FILL_COLOR_2); // Set the fill color to transparent
                square.setStroke(DEFAULT_STROKE_COLOR); // Set the border color

                // Add the square to the grid
                gridPane.add(square, col, row);
                if (DEBUG) {
                    // type tye in the square( col,row) for debugging
                    gridPane.add(new Text(col + "," + row), col, row);
                }
                // add the (col,row) to the square for debugging
                square.getProperties().put("col", col);
                square.getProperties().put("row", row);

                // Add a click event listener to each square
                square.setOnMouseClicked(this::onSquareClicked);
                // Add a hover event listener to each square
                // todo refactor this code
                square.setOnMouseEntered(event -> {
                    // filling the bottom square with the color of the current player
                    Rectangle hoveredSquare = (Rectangle) event.getSource();
                    // filling the bottom square with the color of the current player
                    Rectangle neighbourSquare = getNeighbourSquare((Rectangle) event.getSource());
                    if (neighbourSquare == null) return;

                    Paint currentPlayerHoverColor = this.currentPlayer == 1 ? FIRST_PLAYER_HOVER_COLOR : SECOND_PLAYER_HOVER_COLOR;
                    Paint currentPlayerFillColor = this.currentPlayer == 1 ? FIRST_PLAYER_HOVER_FILL_COLOR : SECOND_PLAYER_HOVER_FILL_COLOR;
                    // temporarily filling the hovered square with the color of the current player
                    hoveredSquare.setStroke(currentPlayerHoverColor);
                    hoveredSquare.setFill(currentPlayerFillColor);
                    // filling the bottom square with the color of the current player
                    neighbourSquare.setStroke(currentPlayerHoverColor);
                    neighbourSquare.setFill(currentPlayerFillColor);
                });

                // Add a hover event listener to each square
                square.setOnMouseExited(event -> {
                    Rectangle hoveredSquare = (Rectangle) event.getSource();
                    Rectangle neighbourSquare = getNeighbourSquare((Rectangle) event.getSource());
                    // filling the bottom square with the color of the current player
                    resetRectangleColors(hoveredSquare, neighbourSquare);
                });
            }
        }
        Scene scene = new Scene(gridPane, BOX_SIZE * NUM_SQUARES, BOX_SIZE * NUM_SQUARES);
        stage.setScene(scene);
        stage.show();
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