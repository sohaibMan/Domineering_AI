package com.example.domineering.GameSearch;

import com.example.domineering.Agent.*;
import com.example.domineering.Move.AlphaBetaAgentMove;
import com.example.domineering.Move.MinMaxAgentMove;
import com.example.domineering.Move.Move;
import com.example.domineering.Player;
import com.example.domineering.Position.Position;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class DomineeringGameSearch extends GameSearch {


    @Override
    public boolean wonPosition(Position position) {
        int numSquares = position.getNumSquares();
        for (int row = 0; row < numSquares; row++) {
            for (int col = 0; col < numSquares; col++) {
                Move move = position.getSquare(row, col);
                Move neighbourMove = getNeighbourMove(position, move, position.getCurrentPlayer());
                if (move != null && !move.isDisable() && neighbourMove != null && !neighbourMove.isDisable())
                    return false;
            }
        }

        return true;
    }


    @Override
    public AlphaBetaAgentMove[] possibleMoves(AlphaBetaAgentMove position, int player) {
        System.out.println(position);
        int numSquares = position.getNumSquares();

        // List to store possible positions
        List<AlphaBetaAgentMove> possiblePositions = new ArrayList<>();

        // Iterate through each square in the grid
        for (int row = 0; row < numSquares; row++) {
            for (int col = 0; col < numSquares; col++) {
                // Check if the square is empty
                if (!position.isDisabled(row, col)) {
                    // Check if the adjacent square is also empty based on the player type
                    boolean isValidMove;
                    if (player == 2) {
                        isValidMove = (col < numSquares - 1) && !position.isDisabled(row, col + 1);
                    } else {
                        isValidMove = (row < numSquares - 1) && !position.isDisabled(row + 1, col);
                    }

                    if (isValidMove) {
                        // Create a new position with the move applied and add it to the list
                        AlphaBetaAgentMove newPosition = createNewPosition(position, row, col, player);
                        possiblePositions.add(newPosition);
                    }
                }
            }
        }

        return possiblePositions.toArray(new AlphaBetaAgentMove[0]);
    }

    @Override
    public MinMaxAgentMove[] possibleMovesMinMax(MinMaxAgentMove position, int player) {
        System.out.println(position);
        int numSquares = position.getNumSquares();

        // List to store possible positions
        List<MinMaxAgentMove> possiblePositions = new ArrayList<>();

        // Iterate through each square in the grid
        for (int row = 0; row < numSquares; row++) {
            for (int col = 0; col < numSquares; col++) {
                // Check if the square is empty
                if (!position.isDisabled(row, col)) {
                    // Check if the adjacent square is also empty based on the player type
                    boolean isValidMove;
                    if (player == 2) {
                        isValidMove = (col < numSquares - 1) && !position.isDisabled(row, col + 1);
                    } else {
                        isValidMove = (row < numSquares - 1) && !position.isDisabled(row + 1, col);
                    }

                    if (isValidMove) {
                        // Create a new position with the move applied and add it to the list
                        MinMaxAgentMove newPosition = createNewPosition(position, row, col, player);
                        possiblePositions.add(newPosition);
                    }
                }
            }
        }

        return possiblePositions.toArray(new MinMaxAgentMove[0]);
    }

    // Helper method to create a new position with a move applied
    private AlphaBetaAgentMove createNewPosition(AlphaBetaAgentMove position, int row, int col, int player) {
        AlphaBetaAgentMove newPosition = position.clone();
        //apply move
        newPosition.setMove(row, col, player);
        newPosition.setRow(row);
        newPosition.setCol(col);
        if (player == 1)
            newPosition.setMove(row + 1, col, player);
        else
            newPosition.setMove(row, col + 1, player);

        return newPosition;
    }

    private MinMaxAgentMove createNewPosition(MinMaxAgentMove position, int row, int col, int player) {
        MinMaxAgentMove newPosition = position.clone();
        //apply move
        newPosition.setMove(row, col, player);
        newPosition.setRow(row);
        newPosition.setCol(col);
        if (player == 1)
            newPosition.setMove(row + 1, col, player);
        else
            newPosition.setMove(row, col + 1, player);

        return newPosition;
    }


    public Move getNeighbourMove(Position position, Move move, int player) {
        if (move == null || move.getProperties() == null || !move.getProperties().containsKey("row") || !move.getProperties().containsKey("col")) {
            return null;
        }

        int currentPlayer = position.getCurrentPlayer();
        int clickedSquareRow = (int) move.getProperties().get("row");
        int clickedSquareCol = (int) move.getProperties().get("col");

        // check the current Player
        if (currentPlayer == 1)
            return (Move) position.getGridPane().getChildren().stream().filter(node -> GridPane.getRowIndex(node) == clickedSquareRow + 1 && GridPane.getColumnIndex(node) == clickedSquareCol && !node.isDisable()).map(node -> (Rectangle) node).findFirst().orElse(null);
        else
            return (Move) position.getGridPane().getChildren().stream().filter(node -> GridPane.getRowIndex(node) == clickedSquareRow && GridPane.getColumnIndex(node) == clickedSquareCol + 1 && !node.isDisable()).map(node -> (Rectangle) node).findFirst().orElse(null);
    }


    @Override
    public Move makeMove(Position gamePosition, GameSearch gameSearch) {
        Agent agent = switch (gamePosition.getCurrentPlayerType()) {
            case HUMAN -> new HumanAgent();
            case MINIMAX -> new MinMaxAgent();
            case RANDOM -> new RandomAgent();
            case ALPHA_BETA -> new AlphaBetaAgent();
        };
        return agent.makeMove(gamePosition, gameSearch);
    }

}
