package com.example.domineering.Position;

import com.example.domineering.Move.Move;
import com.example.domineering.Player;
import javafx.scene.layout.GridPane;

public abstract class Position implements Cloneable {

    private Player player;
    private final int[] movesPlayer = {0, 0};
    private int currentPlayer = 1;

    private static final int NUM_SQUARES = 5; // Number of squares in each dimension

    private static final boolean DEBUG = false;

    private final GridPane gridPane = new GridPane();


    Position(Player player) {
        this.player = player;
    }


    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getCurrentPlayerType() {
        return player;
    }

    public void setCurrentPlayerType(Player player) {
        this.player = player;
    }

    public boolean isCurrentPlayer(Player player) {
        return this.player == player;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void switchPlayer() {
        if (currentPlayer == 1) {
            currentPlayer = 2;
        } else {
            currentPlayer = 1;
        }
    }

    public boolean isCurrentPlayer(int player) {
        return currentPlayer == player;
    }

    public int getMovesPlayer(int player) {
        if (player == 1) {
            return movesPlayer[0];
        } else {
            return movesPlayer[1];
        }
    }

    public void incrementMovesPlayer(int player) {
        if (player == 1) {
            movesPlayer[0]++;
        } else {
            movesPlayer[1]++;
        }
    }

    public void setMovesPlayer(int player, int moves) {
        if (player == 1) {
            movesPlayer[0] = moves;
        } else {
            movesPlayer[1] = moves;
        }
    }

    public boolean isDebugMode() {
        return DEBUG;
    }


    public int getNumSquares() {
        return NUM_SQUARES;
    }

    public Move getSquare(int row, int col) {
        return (Move) gridPane.getChildren().get(row * NUM_SQUARES + col);
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public void reset() {
        currentPlayer = 1;
        movesPlayer[0] = 0;
        movesPlayer[1] = 0;
    }

    @Override
    public Position clone() {
        try {
            return (Position) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
