package com.example.domineering.GameSearch;

import com.example.domineering.Agent.*;
import com.example.domineering.Move.Move;
import com.example.domineering.Player;
import com.example.domineering.Position.Position;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class DomineeringGameSearch extends GameSearch {


    @Override
    public boolean wonPosition(Position position) {
        int numSquares = position.getNumSquares();
        for (int row = 0; row < numSquares; row++) {
            for (int col = 0; col < numSquares; col++) {
                Move move = position.getSquare(row, col);
                Move neighbourMove = getNeighbourMove(position, move);
                if (!move.isDisable() && neighbourMove != null && !neighbourMove.isDisable()) return false;
            }
        }

        return true;
    }


    @Override
    public Position[] possibleMoves(Position p, int player) {
//        todo
        return new Position[0];
    }


    public Move getNeighbourMove(Position position, Move move) {
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
