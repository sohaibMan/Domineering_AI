package com.example.domineering.GameSearch;

import com.example.domineering.Agent.*;
import com.example.domineering.Move.Move;
import com.example.domineering.Player;
import com.example.domineering.Position.DomineeringPosition;
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
                if (!move.isDisable() && neighbourMove != null && !neighbourMove.isDisable()) return false;
            }
        }

        return true;
    }


    @Override
    public Position[] possibleMoves(Position p, int player) {
        DomineeringPosition position = (DomineeringPosition) p;
        int numSquares = position.getNumSquares();

        // List to store possible positions
        List<DomineeringPosition> possiblePositions = new ArrayList<>();

        // Iterate through each square in the grid
        for (int row = 0; row < numSquares; row++) {
            for (int col = 0; col < numSquares; col++) {
                // Check if the square is empty
                Move move = position.getSquare(row, col);
                if (!move.isDisable()) {
                    // Check if the adjacent square is also empty based on the player type
                    boolean isValidMove;
                    if (player == 1) {
                        isValidMove = (col < numSquares - 1) && !position.getSquare(row, col + 1).isDisable();
                    } else {
                        isValidMove = (row < numSquares - 1) && !position.getSquare(row + 1, col).isDisable();
                    }

                    if (isValidMove) {
                        // Create a new position with the move applied and add it to the list
                        DomineeringPosition newPosition = createNewPosition(position, row, col, player);
                        possiblePositions.add(newPosition);
                    }
                }
            }
        }

        return possiblePositions.toArray(new DomineeringPosition[0]);
    }

    // Helper method to create a new position with a move applied
    private DomineeringPosition createNewPosition(DomineeringPosition position, int row, int col, int player) {
        DomineeringPosition newPosition = new DomineeringPosition(position.getCurrentPlayerType());
        // Copy the board state
        for (int i = 0; i < position.getNumSquares(); i++) {
            for (int j = 0; j < position.getNumSquares(); j++) {
                Move currentMove = position.getSquare(i, j);
                if (currentMove != null) {
                    // Initialize the move if it's null
                    if (newPosition.getSquare(i, j) == null) {
                        newPosition.getGridPane().add(new Move(0, 0), j, i);
                    }
                    newPosition.getSquare(i, j).setDisable(currentMove.isDisable());
                }
            }
        }
        // Apply the move
        if (player == 1 && newPosition.getSquare(row, col) != null && newPosition.getSquare(row, col + 1) != null) {
            newPosition.getSquare(row, col).setDisable(true);
            newPosition.getSquare(row, col + 1).setDisable(true);
        } else if (player == 2 && newPosition.getSquare(row, col) != null && newPosition.getSquare(row + 1, col) != null) {
            newPosition.getSquare(row, col).setDisable(true);
            newPosition.getSquare(row + 1, col).setDisable(true);
        }

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
            case Player.HUMAN -> new HumanAgent();
            case Player.MINIMAX -> new MinMaxAgent();
            case Player.RANDOM -> new RandomAgent();
            case Player.ALPHA_BETA -> new AlphaBetaAgent();
        };
        return agent.makeMove(gamePosition, gameSearch);
    }

}
