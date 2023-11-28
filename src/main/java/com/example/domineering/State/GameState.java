package com.example.domineering.State;

import com.example.domineering.Player;

public abstract class GameState {

    private Player player;
    private final int[] movesPlayer = {0, 0};
    private int currentPlayer = 1;

    private static final boolean DEBUG = false;


    GameState(Player player) {
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

    public boolean isCurrentPlayerType(Player player) {
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

    public boolean isCurrentPlayerType(int player) {
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

}
