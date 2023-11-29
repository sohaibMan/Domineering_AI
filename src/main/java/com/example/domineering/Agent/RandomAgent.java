package com.example.domineering.Agent;

import com.example.domineering.GameSearch.GameSearch;
import com.example.domineering.Move.Move;
import com.example.domineering.Position.Position;

import java.util.List;

public class RandomAgent extends Agent {
    @Override
    public Move makeMove(Position gamePosition, GameSearch domineeringGameSearch) {
        List<Move> unPlayedMoves = gamePosition.getGridPane().getChildren().stream().filter(node -> {
            Move neighbourSquare = domineeringGameSearch.getNeighbourMove(gamePosition, (Move) node, gamePosition.getCurrentPlayer());
            return !node.isDisable() && neighbourSquare != null && !neighbourSquare.isDisable();
        }).map(node -> (Move) node).toList();

        if (!unPlayedMoves.isEmpty()) {
            return unPlayedMoves.get((int) (Math.random() * unPlayedMoves.size()));
        }
        return null;

    }
}
