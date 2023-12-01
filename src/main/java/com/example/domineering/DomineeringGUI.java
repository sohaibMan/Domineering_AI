package com.example.domineering;

import com.example.domineering.Agent.AlphaBetaAgent;
import com.example.domineering.GameSearch.DomineeringGameSearch;
import com.example.domineering.GameSearch.GameSearch;
import com.example.domineering.Helpers.DomineeringHelpers;
import com.example.domineering.Helpers.Helpers;
import com.example.domineering.Move.DomineeringMove;
import com.example.domineering.Move.Move;
import com.example.domineering.Position.DomineeringPosition;
import com.example.domineering.Position.Position;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Arrays;

import static com.example.domineering.DomineeringGUIConfig.*;


public class DomineeringGUI extends Application {

    // the state of the game
    private final Position gamePosition = new DomineeringPosition(Player.HUMAN);
    private final Label adversaryLabel = new Label(gamePosition.getCurrentPlayerType().toString());
    private final Move movePlayer1 = new DomineeringMove((BOX_SIZE * gamePosition.getNumSquares()) / 13, (BOX_SIZE * gamePosition.getNumSquares()) / 13);


    private Label movesPlayer1Label;
    private Label maxPossibleMovesPlayer1Label;
    private Label movesPlayer2Label;
    private Label maxPossibleMovesPlayer2Label;
    private final Move movePlayer2 = new DomineeringMove((BOX_SIZE * gamePosition.getNumSquares()) / 13, (BOX_SIZE * gamePosition.getNumSquares()) / 13);
    //     game search
    GameSearch domineeringGameSearch = new DomineeringGameSearch();
    // game helpers
    Helpers domineeringHelpers = new DomineeringHelpers();

    Pane root = new Pane() ;
    Button Showhint = new Button("Show hint");
    private Label hintStatusLabel;
    private static int hintCount = 0;


    static private void resetMoveColors(Move... moves) {
        // call resetMoveColor for each move in the list
        Arrays.stream(moves).forEach(DomineeringGUI::resetMoveColor);
    }

    private static void resetMoveColor(Move move) {
        if (move == null || move.isDisable()) return;
        int row = (int) move.getProperties().get("row");
        int col = (int) move.getProperties().get("col");
        // set the previous color of the move
        // set the default color of the move
        move.setFill((row + col) % 2 == 0 ? DEFAULT_FILL_COLOR : DEFAULT_FILL_COLOR_2); // Set the fill color to transparent
        move.setStroke(DEFAULT_STROKE_COLOR); // Set the border color
    }

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
            }

        });
    }

    @Override
    public void start(Stage stage) {
        // Set the title of the window
        stage.setTitle("Welcome to Domineering Game!");

        int squareSize = (BOX_SIZE / gamePosition.getNumSquares()) * 3;

        for (int row = 0; row < gamePosition.getNumSquares(); row++) {
            for (int col = 0; col < gamePosition.getNumSquares(); col++) {
                Move square = new DomineeringMove(squareSize, squareSize);
                square.setFill((row + col) % 2 == 0 ? DEFAULT_FILL_COLOR : DEFAULT_FILL_COLOR_2);
                square.setStroke(DEFAULT_STROKE_COLOR);
                square.setStrokeWidth(2);
                // Add the square to the grid
                gamePosition.getGridPane().add(square, col, row);
                if (gamePosition.isDebugMode()) {
                    gamePosition.getGridPane().add(new Text(col + "," + row), col, row);
                }

                square.getProperties().put("col", col);
                square.getProperties().put("row", row);

                square.setOnMouseClicked(this::onSquareClicked);

                square.setOnMouseEntered(event -> {
                    if (gamePosition.isCurrentPlayer(2) && !gamePosition.isCurrentPlayer(Player.HUMAN)) return;
                    Move hoveredSquare = (Move) event.getSource();
                    Move neighbourSquare = domineeringGameSearch.getNeighbourMove(gamePosition, (Move) event.getSource(), gamePosition.getCurrentPlayer());
                    if (neighbourSquare == null) return;

                    Paint currentPlayerHoverColor = gamePosition.isCurrentPlayer(1) ? FIRST_PLAYER_HOVER_COLOR : SECOND_PLAYER_HOVER_COLOR;
                    Paint currentPlayerFillColor = gamePosition.isCurrentPlayer(1) ? FIRST_PLAYER_HOVER_FILL_COLOR : SECOND_PLAYER_HOVER_FILL_COLOR;

                    hoveredSquare.setStroke(currentPlayerHoverColor);
                    hoveredSquare.setFill(currentPlayerFillColor);
                    neighbourSquare.setStroke(currentPlayerHoverColor);
                    neighbourSquare.setFill(currentPlayerFillColor);
                });

                square.setOnMouseExited(event -> {
                    Move hoveredSquare = (Move) event.getSource();
                    Move neighbourSquare = domineeringGameSearch.getNeighbourMove(gamePosition, (Move) event.getSource(), gamePosition.getCurrentPlayer());
                    resetMoveColors(hoveredSquare, neighbourSquare);
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
        //add a move
        Move move = new DomineeringMove((int) ((BOX_SIZE * gamePosition.getNumSquares()) / 1.3), (int) ((BOX_SIZE * gamePosition.getNumSquares()) / 1.5));
        move.setFill(Color.TRANSPARENT);
        move.setStroke(Color.BLACK);
        move.setStrokeWidth(2);
        movePlayer1.setStroke(Color.BLACK);
        // set the position of the text
        movePlayer1.setFill(FIRST_PLAYER_COLOR);
        movePlayer2.setStroke(Color.BLACK);
        movePlayer2.setFill(DEFAULT_FILL_COLOR);
        if (gamePosition.isCurrentPlayer(1)) {
            movePlayer1.setFill(FIRST_PLAYER_COLOR);
            movePlayer2.setFill(DEFAULT_FILL_COLOR);
        } else {
            movePlayer2.setFill(SECOND_PLAYER_COLOR);
            movePlayer1.setFill(DEFAULT_FILL_COLOR);
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

        maxPossibleMovesPlayer1Label = new Label(String.valueOf(countMaxPossibleMoves(1)));
        table.add(maxPossibleMovesPlayer1Label, 1, 2);
        maxPossibleMovesPlayer2Label = new Label(String.valueOf(countMaxPossibleMoves(2)));
        table.add(maxPossibleMovesPlayer2Label, 2, 2);


        // Create a MenuBar
        MenuBar menuBar = new MenuBar();

        // Create a Option menu
        Menu optionsMenu = new Menu("Options");

        // Create menu items
        MenuItem newGameMenuItem = new MenuItem("New game");
        MenuItem saveGameMenuItem = new MenuItem("Save game");
        MenuItem loadGameMenuItem = new MenuItem("Load game");
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

        Showhint.setOnAction(event -> showHint());

        hintStatusLabel = new Label("Hints: 0/3");

        // set event handlers for menu items
        humanPlayerMenuItem.setOnAction(e -> {
            restartGame();
            gamePosition.setCurrentPlayerType(Player.HUMAN);
            adversaryLabel.setText(gamePosition.getCurrentPlayerType().toString());
            root.getChildren().remove(Showhint);
            root.getChildren().remove(hintStatusLabel);
        });
        randomPlayerMenuItem.setOnAction(e -> {
            restartGame();
            gamePosition.setCurrentPlayerType(Player.RANDOM);
            adversaryLabel.setText(gamePosition.getCurrentPlayerType().toString());
            root.getChildren().remove(Showhint);
            root.getChildren().remove(hintStatusLabel);
        });
        minMaxPlayerMenuItem.setOnAction(e -> {
            restartGame();
            gamePosition.setCurrentPlayerType(Player.MINIMAX);
            adversaryLabel.setText(gamePosition.getCurrentPlayerType().toString());
            root.getChildren().add(Showhint);
            root.getChildren().add(hintStatusLabel);
        });
        alphaBetaPlaeryMenuItem.setOnAction(e -> {
            restartGame();
            gamePosition.setCurrentPlayerType(Player.ALPHA_BETA);
            adversaryLabel.setText(gamePosition.getCurrentPlayerType().toString());
            root.getChildren().add(Showhint);
            root.getChildren().add(hintStatusLabel);
        });


        // Set event handlers for menu items
        newGameMenuItem.setOnAction(e -> restartGame());
        exitMenuItem.setOnAction(e -> exitGame());
        saveGameMenuItem.setOnAction(e -> domineeringHelpers.saveGame(gamePosition));
        loadGameMenuItem.setOnAction(e -> {
            domineeringHelpers.loadGame(gamePosition);
            // Update the UI
            updateUI();
        });

        // Add menu items to the Options menu
        optionsMenu.getItems().addAll(newGameMenuItem, saveGameMenuItem, loadGameMenuItem, exitMenuItem);

        // Add the File menu to the MenuBar
        menuBar.getMenus().add(optionsMenu);
        menuBar.getMenus().add(playerSettingMenu);
        menuBar.setStyle("-fx-background-color: #bfbaba; -fx-font-size: 15px; -fx-font-weight: bold; -fx-font-family: Monospaced;");

        // creat a new scene with the vbox and Menubar as the root

        title.setLayoutY(120);
        title.setLayoutX(450);
        gamePosition.getGridPane().setLayoutX(100);
        gamePosition.getGridPane().setLayoutY(200);
        move.setLayoutX(730);
        move.setLayoutY(185);
        Showhint.setLayoutX(960);
        Showhint.setLayoutY(400);
        Showhint.setStyle("-fx-background-color:GRAY; -fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold; -fx-font-family: Monospaced;");
        hintStatusLabel.setLayoutX(960);
        hintStatusLabel.setLayoutY(380);
        hintStatusLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-font-family: Monospaced;");
        table.setLayoutX(870);
        table.setLayoutY(520);
        movePlayer1.setLayoutX(800);
        movePlayer1.setLayoutY(200);
        movePlayer2.setLayoutX(1150);
        movePlayer2.setLayoutY(200);
        root.getChildren().add(title);
        root.getChildren().add(gamePosition.getGridPane());
        root.getChildren().add(menuBar);
        root.getChildren().add(move);
        root.getChildren().add(table);
        root.getChildren().add(movePlayer1);
        root.getChildren().add(movePlayer2);




        Scene scene = new Scene(root, BOX_SIZE * gamePosition.getNumSquares(), BOX_SIZE * gamePosition.getNumSquares());

        // Set the scene to the stage
        stage.setScene(scene);

        // Set the minimum size of the stage to create a square window
        stage.setMinWidth(BOX_SIZE * gamePosition.getNumSquares() + 20); // Consider the margin
        stage.setMinHeight(BOX_SIZE * gamePosition.getNumSquares());
        stage.setFullScreen(true);

        // Show the stage
        stage.show();

    }

    private void restartGame() {
        gamePosition.getGridPane().getChildren().forEach(node -> {
            if (node instanceof Move square) {
                square.setDisable(false);
                resetMoveColors(square);
            }
        });
        maxPossibleMovesPlayer1Label.setText(String.valueOf(countMaxPossibleMoves(1)));
        maxPossibleMovesPlayer2Label.setText(String.valueOf(countMaxPossibleMoves(2)));
        movesPlayer1Label.setText("0");
        movesPlayer2Label.setText("0");
        hintStatusLabel.setText("Hints: 0/3");
        hintCount = 0;
        Showhint.setDisable(false);
        this.gamePosition.reset();

        updateUI();
    }

    // Event handler for square click
    private void onSquareClicked(MouseEvent event) {
        // the human player can only play when it's his turn
        // write the current player to the box
        Move clickedSquare = (Move) event.getSource();
        clickedSquare.getProperties().put("player", gamePosition.getCurrentPlayer());
        if (gamePosition.isCurrentPlayer(Player.HUMAN) || gamePosition.isCurrentPlayer(1)) {
            makeMove(event);
            if (!gamePosition.isCurrentPlayer(Player.HUMAN)) {
                // make the agent play
                Move move = domineeringGameSearch.makeMove(gamePosition, domineeringGameSearch);
                assert move != null;// if that happens then the game is over
                makeMove(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, null, 0, false, false, false, false, false, false, false, false, false, false, null) {
                    @Override
                    public Object getSource() {
                        return move;
                    }
                });
            }
        }
    }


    // Hint
    private void showHint() {

//        AlphaBetaAgent alphaBetaAgent = new AlphaBetaAgent();



            if (gamePosition.isCurrentPlayer(Player.HUMAN) || gamePosition.isCurrentPlayer(1)) {
                if (hintCount < 3) {

                    Move hintMove = domineeringGameSearch.makeMove(gamePosition, domineeringGameSearch);

                    makeMove(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, null, 0, false, false, false, false, false, false, false, false, false, false, null) {
                    @Override
                    public Object getSource() {
                        return hintMove;
                    }
                });

                hintCount++;

                hintStatusLabel.setText("Hints: " + hintCount + "/3");

                if (hintCount == 3) {
                    Showhint.setDisable(true);

                    hintStatusLabel.setText("Max Hints Reached!");
                }
                    Move opponentMove = domineeringGameSearch.makeMove(gamePosition, domineeringGameSearch);

                    makeMove(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, null, 0, false, false, false, false, false, false, false, false, false, false, null) {
                        @Override
                        public Object getSource() {
                            return opponentMove;
                        }
                    });
                }

            }




    }



    public static void main(String[] args) {
        launch();
    }

    private void makeMove(MouseEvent event) {
        Move neighbourSquare = domineeringGameSearch.getNeighbourMove(gamePosition, (Move) event.getSource(), gamePosition.getCurrentPlayer());
        if (neighbourSquare != null) {
            Move clickedSquare = (Move) event.getSource();

            if (gamePosition.isCurrentPlayer(1)) {
                gamePosition.incrementMovesPlayer(1);
                movesPlayer1Label.setText(String.valueOf(gamePosition.getMovesPlayer(1)));
                movePlayer2.setFill(SECOND_PLAYER_COLOR);
                movePlayer2.setStroke(Color.BLACK);
                movePlayer1.setFill(DEFAULT_FILL_COLOR);
                movePlayer1.setStroke(Color.BLACK);
            } else {
                gamePosition.incrementMovesPlayer(2);
                movesPlayer2Label.setText(String.valueOf(gamePosition.getMovesPlayer(2)));
                movePlayer1.setFill(FIRST_PLAYER_COLOR);
                movePlayer1.setStroke(Color.BLACK);
                movePlayer2.setFill(DEFAULT_FILL_COLOR);
                movePlayer2.setStroke(Color.BLACK);
            }
            Paint currentPlayerColor = gamePosition.isCurrentPlayer(1) ? FIRST_PLAYER_COLOR : SECOND_PLAYER_COLOR;
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

            //switch Player
            gamePosition.switchPlayer();

            // check if the game is over
            if (domineeringGameSearch.wonPosition(gamePosition)) {
                // announce that the game is over
                Alert alert = getWiningAlert();
                alert.show();
                restartGame();
            }

            // update the max possible moves
            maxPossibleMovesPlayer1Label.setText(String.valueOf(countMaxPossibleMoves(1)));
            maxPossibleMovesPlayer2Label.setText(String.valueOf(countMaxPossibleMoves(2)));
        }

    }

    private Alert getWiningAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        gamePosition.switchPlayer();
        int winnerPlayer = gamePosition.getCurrentPlayer();
        Text text = new Text("Winner is Player " + winnerPlayer);
        text.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-font-family: Monospaced;");
        alert.setContentText(text.getText());
        return alert;
    }

    //update ui (restore the UI to the last saved state)
    private void updateUI() {
        // Update the number of moves for each player
        movesPlayer1Label.setText(String.valueOf(gamePosition.getMovesPlayer(1)));
        movesPlayer2Label.setText(String.valueOf(gamePosition.getMovesPlayer(2)));

        // Update the color of the current player
        if (gamePosition.isCurrentPlayer(1)) {
            movePlayer1.setFill(FIRST_PLAYER_COLOR);
            movePlayer1.setStroke(Color.BLACK);
            movePlayer2.setFill(DEFAULT_FILL_COLOR);
            movePlayer2.setStroke(Color.BLACK);
        } else {
            movePlayer1.setFill(DEFAULT_FILL_COLOR);
            movePlayer1.setStroke(Color.BLACK);
            movePlayer2.setFill(SECOND_PLAYER_COLOR);
            movePlayer2.setStroke(Color.BLACK);
        }

        // Update the color of each square
        for (int row = 0; row < gamePosition.getNumSquares(); row++) {
            for (int col = 0; col < gamePosition.getNumSquares(); col++) {
                Move square = gamePosition.getSquare(row, col);
                if (square.isDisable()) {
                    square.setFill(square.getFill().equals(FIRST_PLAYER_COLOR) ? FIRST_PLAYER_COLOR : SECOND_PLAYER_COLOR);
                    square.setStroke(DEFAULT_STROKE_COLOR);
                } else {
                    square.setFill((row + col) % 2 == 0 ? DEFAULT_FILL_COLOR : DEFAULT_FILL_COLOR_2);
                    square.setStroke(DEFAULT_STROKE_COLOR);
                }
            }
        }
    }

    private int countMaxPossibleMoves(int player) {
        int maxPossibleMoves = 0;
        for (int row = 0; row < gamePosition.getNumSquares(); row++) {
            for (int col = 0; col < gamePosition.getNumSquares(); col++) {
                Move move = (Move) gamePosition.getGridPane().getChildren().get(row * gamePosition.getNumSquares() + col);
                Move neighbourMove = domineeringGameSearch.getNeighbourMove(gamePosition, move, player);
                if (!move.isDisable() && neighbourMove != null && !neighbourMove.isDisable()) maxPossibleMoves++;
            }
        }
        return maxPossibleMoves;
    }

}
