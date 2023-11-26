package com.example.domineering;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Arrays;


public class MainApplication extends Application {

    private static final int BOX_SIZE = 140; // Size of the box
    private static final int NUM_SQUARES = 5; // Number of squares in each dimension

    private int currentPlayer = 1; // 1 for horizontal Player, 2 for vertical Player

    private Player player = Player.HUMAN;

    //? first Player is horizontal Player
    private static final Color FIRST_PLAYER_COLOR = Color.RED;
    private static final Color FIRST_PLAYER_HOVER_COLOR = Color.web("#ffc9c9");
    private static final Color FIRST_PLAYER_HOVER_FILL_COLOR = Color.web("#e03131");

    //? second Player is vertical Player
    private static final Color SECOND_PLAYER_COLOR = Color.web("#1864ab");
    private static final Color SECOND_PLAYER_HOVER_COLOR = Color.web("#1864ab");
    private static final Color SECOND_PLAYER_HOVER_FILL_COLOR = Color.web("74c0fc");

    private static final Color DEFAULT_FILL_COLOR = Color.TRANSPARENT;
    private static final Color DEFAULT_FILL_COLOR_2 = Color.GRAY;
    private static final Color DEFAULT_STROKE_COLOR = Color.BLACK;

    private int movesPlayer1 = 0;
    private int movesPlayer2 = 0;

    private Label movesPlayer1Label;
    private Label maxPossibleMovesPlayer1Label;
    private Label movesPlayer2Label;
    private Label maxPossibleMovesPlayer2Label;

    private final Label adversaryLabel = new Label(this.player.toString());
    private final Rectangle rectanglePlayer1 = new Rectangle((double) (BOX_SIZE * NUM_SQUARES) / 13, (double) (BOX_SIZE * NUM_SQUARES) / 13);
    private final Rectangle rectanglePlayer2 = new Rectangle((double) (BOX_SIZE * NUM_SQUARES) / 13, (double) (BOX_SIZE * NUM_SQUARES) / 13);


    private static final boolean DEBUG = false;

    private final GridPane gridPane = new GridPane();

    @Override
    public void start(Stage stage) {
        // Set the title of the window
        stage.setTitle("Welcome to Domineering Game!");


        int squareSize = (BOX_SIZE / NUM_SQUARES) * 3;

        for (int row = 0; row < NUM_SQUARES; row++) {
            for (int col = 0; col < NUM_SQUARES; col++) {
                Rectangle square = new Rectangle(squareSize, squareSize);
                square.setFill((row + col) % 2 == 0 ? DEFAULT_FILL_COLOR : DEFAULT_FILL_COLOR_2);
                square.setStroke(DEFAULT_STROKE_COLOR);
                square.setStrokeWidth(2);
                // Add the square to the grid
                gridPane.add(square, col, row);
                if (DEBUG) {
                    gridPane.add(new Text(col + "," + row), col, row);
                }

                square.getProperties().put("col", col);
                square.getProperties().put("row", row);

                square.setOnMouseClicked(this::onSquareClicked);

                square.setOnMouseEntered(event -> {
                    if (currentPlayer == 2 && player != Player.HUMAN) return;
                    Rectangle hoveredSquare = (Rectangle) event.getSource();
                    Rectangle neighbourSquare = getNeighbourSquare((Rectangle) event.getSource(), this.currentPlayer);
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
                    Rectangle neighbourSquare = getNeighbourSquare((Rectangle) event.getSource(), this.currentPlayer);
                    resetRectangleColors(hoveredSquare, neighbourSquare);
                });
            }
        }
        //add a title to the game
        Text title = new Text("Domineering Game");
        title.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; -fx-font-family: Monospaced;");
        //add a color black to the title
        title.setFill(Color.BLACK);
        title.setStroke(Color.GRAY);
        title.setStrokeWidth(2);
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), title);
        // Define the animation properties (move up and down)
        translateTransition.setFromY(0);
        translateTransition.setToY(-20);
        translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
        translateTransition.setAutoReverse(true);
        // Start the animation
        translateTransition.play();
        //add a rectangle
        Rectangle rectangle = new Rectangle((BOX_SIZE * NUM_SQUARES) / 1.3, (BOX_SIZE * NUM_SQUARES) / 1.5);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(2);
        rectanglePlayer1.setStroke(Color.BLACK);
        // set the position of the text
        rectanglePlayer1.setFill(FIRST_PLAYER_COLOR);
        rectanglePlayer2.setStroke(Color.BLACK);
        rectanglePlayer2.setFill(DEFAULT_FILL_COLOR);
        if (this.currentPlayer == 1) {
            rectanglePlayer1.setFill(FIRST_PLAYER_COLOR);
            rectanglePlayer2.setFill(DEFAULT_FILL_COLOR);
        } else {
            rectanglePlayer2.setFill(SECOND_PLAYER_COLOR);
            rectanglePlayer1.setFill(DEFAULT_FILL_COLOR);
        }
        GridPane table = new GridPane();
        table.setHgap(10);  // horizontal gap between the columns
        table.setVgap(10); // vertical gap between the rows
        table.setPadding(new Insets(10)); // the margin of the grid pane
        //style du tableau
        table.setStyle("-fx-background-color: #bfbaba; -fx-font-size: 15px; -fx-font-weight: bold; -fx-font-family: Monospaced;");
        // add titles to the table
        table.add(new Label("Player"), 0, 0);
        table.add(new Label("Moves"), 0, 1);
        movesPlayer1Label = new Label("0");
        table.add(movesPlayer1Label, 1, 1);
        table.add(new Label("Maximal Moves"), 0, 2);
        movesPlayer2Label = new Label("0");
        table.add(movesPlayer2Label, 2, 1);
        table.add(new Label("Human<you>"), 1, 0);
        table.add(adversaryLabel, 2, 0);

        maxPossibleMovesPlayer1Label = new Label(String.valueOf(CountMaxPossibleMoves(1)));
        table.add(maxPossibleMovesPlayer1Label, 1, 2);
        maxPossibleMovesPlayer2Label = new Label(String.valueOf(CountMaxPossibleMoves(2)));
        table.add(maxPossibleMovesPlayer2Label, 2, 2);


        // Create a MenuBar
        MenuBar menuBar = new MenuBar();

        // Create a Option menu
        Menu optionsMenu = new Menu("Options");

        // Create menu items
        MenuItem newGameMenuItem = new MenuItem("New game");
        MenuItem exitMenuItem = new MenuItem("Exit");

        // create game setting menu
        Menu playerSettingMenu = new Menu("Player mode");
        // create menu items
        MenuItem humanPlayerMenuItem = new MenuItem("Human");
        MenuItem randomPlayerMenuItem = new MenuItem("Random agent");
        MenuItem minMaxPlayerMenuItem = new MenuItem("Minimax agent");
        MenuItem alphaBetaPlaeryMenuItem = new MenuItem("Alpha-beta agent");

        // add menu items to the Player setting menu
        playerSettingMenu.getItems().addAll(humanPlayerMenuItem, randomPlayerMenuItem, minMaxPlayerMenuItem, alphaBetaPlaeryMenuItem);

        // set event handlers for menu items
        humanPlayerMenuItem.setOnAction(e -> {
            restartGame();
            this.player = Player.HUMAN;
            adversaryLabel.setText(this.player.toString());
        });
        randomPlayerMenuItem.setOnAction(e -> {
            restartGame();
            this.player = Player.RANDOM;
            adversaryLabel.setText(this.player.toString());
        });
        minMaxPlayerMenuItem.setOnAction(e -> {
            restartGame();
            this.player = Player.MINIMAX;
            adversaryLabel.setText(this.player.toString());
        });
        alphaBetaPlaeryMenuItem.setOnAction(e -> {
            restartGame();
            this.player = Player.ALPHA_BETA;
            adversaryLabel.setText(this.player.toString());
        });


        // Set event handlers for menu items
        newGameMenuItem.setOnAction(e -> restartGame());
        exitMenuItem.setOnAction(e -> exitGame());

        // Add menu items to the Options menu
        optionsMenu.getItems().addAll(newGameMenuItem, exitMenuItem);

        // Add the File menu to the MenuBar
        menuBar.getMenus().add(optionsMenu);
        menuBar.getMenus().add(playerSettingMenu);
        menuBar.setStyle("-fx-background-color: #bfbaba; -fx-font-size: 15px; -fx-font-weight: bold; -fx-font-family: Monospaced;");

        // creat a new scene with the vbox and Menubar as the root
        Pane root = new Pane();
        title.setLayoutY(120);
        title.setLayoutX(450);
        gridPane.setLayoutX(100);
        gridPane.setLayoutY(200);
        rectangle.setLayoutX(730);
        rectangle.setLayoutY(185);
        table.setLayoutX(870);
        table.setLayoutY(520);
        rectanglePlayer1.setLayoutX(800);
        rectanglePlayer1.setLayoutY(200);
        rectanglePlayer2.setLayoutX(1150);
        rectanglePlayer2.setLayoutY(200);
        root.getChildren().add(title);
        root.getChildren().add(gridPane);
        root.getChildren().add(menuBar);
        root.getChildren().add(rectangle);
        root.getChildren().add(table);
        root.getChildren().add(rectanglePlayer1);
        root.getChildren().add(rectanglePlayer2);


        Scene scene = new Scene(root, BOX_SIZE * NUM_SQUARES, BOX_SIZE * NUM_SQUARES);

        // Set the scene to the stage
        stage.setScene(scene);

        // Set the minimum size of the stage to create a square window
        stage.setMinWidth(BOX_SIZE * NUM_SQUARES + 20); // Consider the margin
        stage.setMinHeight(BOX_SIZE * NUM_SQUARES);
        stage.setFullScreen(true);

        // Show the stage
        stage.show();

    }


    // TODO: RESTART GAME
    private void restartGame() {
        gridPane.getChildren().forEach(node -> {
            if (node instanceof Rectangle square) {
                square.setDisable(false);
                resetRectangleColor(square);
            }
        });
        movesPlayer1 = 0;
        movesPlayer1Label.setText(String.valueOf(movesPlayer1));
        movesPlayer2 = 0;
        movesPlayer2Label.setText(String.valueOf(movesPlayer2));
        maxPossibleMovesPlayer1Label.setText(String.valueOf(CountMaxPossibleMoves(1)));
        maxPossibleMovesPlayer2Label.setText(String.valueOf(CountMaxPossibleMoves(2)));
        if (this.currentPlayer == 1) {
            rectanglePlayer1.setFill(FIRST_PLAYER_COLOR);
            rectanglePlayer1.setStroke(Color.BLACK);
            rectanglePlayer2.setFill(DEFAULT_FILL_COLOR);
            rectanglePlayer2.setStroke(Color.BLACK);
        } else {
            rectanglePlayer2.setFill(SECOND_PLAYER_COLOR);
            rectanglePlayer2.setStroke(Color.BLACK);
            rectanglePlayer1.setFill(DEFAULT_FILL_COLOR);
            rectanglePlayer1.setStroke(Color.BLACK);
        }
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
            }  // User clicked "No," do nothing

        });
    }


    // Event handler for square click
    private void onSquareClicked(MouseEvent event) {
        // the human player can only play when it's his turn
        if (this.player == Player.HUMAN || this.currentPlayer == 1) {
            onSquareClickedHuman(event);
        }
    }

    private void onSquareClickedHuman(MouseEvent event) {
        Rectangle neighbourSquare = getNeighbourSquare((Rectangle) event.getSource(), this.currentPlayer);
        if (neighbourSquare != null) {
            Rectangle clickedSquare = (Rectangle) event.getSource();

            if (this.currentPlayer == 1) {
                movesPlayer1++;
                movesPlayer1Label.setText(String.valueOf(movesPlayer1));
            } else {
                movesPlayer2++;
                movesPlayer2Label.setText(String.valueOf(movesPlayer2));
            }
            if (this.currentPlayer == 2) {
                rectanglePlayer1.setFill(FIRST_PLAYER_COLOR);
                rectanglePlayer1.setStroke(Color.BLACK);
                rectanglePlayer2.setFill(DEFAULT_FILL_COLOR);
                rectanglePlayer2.setStroke(Color.BLACK);
            } else {
                rectanglePlayer2.setFill(SECOND_PLAYER_COLOR);
                rectanglePlayer2.setStroke(Color.BLACK);
                rectanglePlayer1.setFill(DEFAULT_FILL_COLOR);
                rectanglePlayer1.setStroke(Color.BLACK);
            }
            Paint currentPlayerColor = this.currentPlayer == 1 ? FIRST_PLAYER_COLOR : SECOND_PLAYER_COLOR;
            Paint currentPlayerStorkColor = DEFAULT_STROKE_COLOR;
            // filling the bottom square with the color of the current Player
            // filling the square with the color of the current Player
            clickedSquare.setFill(currentPlayerColor);
            clickedSquare.setStroke(currentPlayerStorkColor);
            // disable the square
            clickedSquare.setDisable(true);
            // filling the bottom square with the color of the current Player
            neighbourSquare.setFill(currentPlayerColor);
            neighbourSquare.setStroke(currentPlayerStorkColor);
            // disable the square
            neighbourSquare.setDisable(true);

            //toggle Player
            this.currentPlayer = this.currentPlayer == 1 ? 2 : 1;

            // check if the game is over
            checkIfGameIsOver();
            // update the max possible moves
            maxPossibleMovesPlayer1Label.setText(String.valueOf(CountMaxPossibleMoves(1)));
            maxPossibleMovesPlayer2Label.setText(String.valueOf(CountMaxPossibleMoves(2)));
        }

    }


    private void checkIfGameIsOver() {
        // enhance this function
        for (int row = 0; row < NUM_SQUARES; row++) {
            for (int col = 0; col < NUM_SQUARES; col++) {
                Rectangle square = (Rectangle) gridPane.getChildren().get(row * NUM_SQUARES + col);
                Rectangle neighbourSquare = getNeighbourSquare(square, this.currentPlayer);
                if (!square.isDisable() && neighbourSquare != null && !neighbourSquare.isDisable()) return;
            }
        }
        // announce that the game is over
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        Text text = new Text("Winner is Player " + (this.currentPlayer == 1 ? 2 : 1));
        text.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-font-family: Monospaced;");
        alert.setContentText(text.getText());
        alert.showAndWait();
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


    private Rectangle getNeighbourSquare(Rectangle rectangle, int currentPlayer) {
        int clickedSquareRow = (int) rectangle.getProperties().get("row");
        int clickedSquareCol = (int) rectangle.getProperties().get("col");
        // check the current Player
        if (currentPlayer == 1)
            return this.gridPane.getChildren().stream().filter(node -> GridPane.getRowIndex(node) == clickedSquareRow + 1 && GridPane.getColumnIndex(node) == clickedSquareCol && !node.isDisable()).map(node -> (Rectangle) node).findFirst().orElse(null);
        else
            return this.gridPane.getChildren().stream().filter(node -> GridPane.getRowIndex(node) == clickedSquareRow && GridPane.getColumnIndex(node) == clickedSquareCol + 1 && !node.isDisable()).map(node -> (Rectangle) node).findFirst().orElse(null);
    }

    private int CountMaxPossibleMoves(int currentPlayer) {
        int maxPossibleMoves = 0;
        for (int row = 0; row < NUM_SQUARES; row++) {
            for (int col = 0; col < NUM_SQUARES; col++) {
                Rectangle square = (Rectangle) gridPane.getChildren().get(row * NUM_SQUARES + col);
                Rectangle neighbourSquare = getNeighbourSquare(square, currentPlayer);
                if (!square.isDisable() && neighbourSquare != null && !neighbourSquare.isDisable()) maxPossibleMoves++;
            }
        }
        return maxPossibleMoves;
    }

}
