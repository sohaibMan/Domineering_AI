package com.example.domineering.Agent;

import com.example.domineering.GameSearch.GameSearch;
import com.example.domineering.Move.MinMaxAgentMove;
import com.example.domineering.Move.Move;
import com.example.domineering.Position.Position;

public class MinMaxAgent extends Agent {

    private static final int MAX_DEPTH = 3;

    @Override
    public Move makeMove(Position gamePosition, GameSearch domineeringGameSearch) {
        MinMaxAgentMove minMaxAgentMove = new MinMaxAgentMove(gamePosition);

        MinMaxAgentMoveEvaluation minimaxAgentMove = minimax(
                minMaxAgentMove,
                MAX_DEPTH,
                true,
                domineeringGameSearch
        );

        System.out.println("new move" + minimaxAgentMove);

        if (minimaxAgentMove == null) return null;

        return gamePosition.getSquare(minimaxAgentMove.getMinimaxAgentMove().getRow(), minimaxAgentMove.getMinimaxAgentMove().getCol());
    }

    private MinMaxAgentMoveEvaluation minimax(MinMaxAgentMove position, int depth, boolean maximizingPlayer, GameSearch domineeringGameSearch) {
        int currentPlayer = maximizingPlayer ? 2 : 1;

        if (depth == 0 || wonPosition(position, currentPlayer)) {
            float evaluation = positionEvaluation(position, domineeringGameSearch, currentPlayer);
            return new MinMaxAgentMoveEvaluation(position, evaluation);
        }


        MinMaxAgentMove[] possibleMoves = domineeringGameSearch.possibleMovesMinMax(position, currentPlayer);


        MinMaxAgentMoveEvaluation bestMove = new MinMaxAgentMoveEvaluation(null, maximizingPlayer ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY);

        if (maximizingPlayer) {
            float maxEval = Float.NEGATIVE_INFINITY;
            for (MinMaxAgentMove possiblePosition : possibleMoves) {
                MinMaxAgentMoveEvaluation possiblePositionEval = minimax(possiblePosition, depth - 1, false, domineeringGameSearch);
                if (possiblePositionEval != null){
                    float eval = possiblePositionEval.getEvaluation();
                    if (eval > maxEval) {
                        maxEval = eval;
                        bestMove = new MinMaxAgentMoveEvaluation(possiblePositionEval.getMinimaxAgentMove(), eval);
                    }
                }

            }
        } else {
            float minEval = Float.POSITIVE_INFINITY;
            for (MinMaxAgentMove possiblePosition : possibleMoves) {
                MinMaxAgentMoveEvaluation possiblePositionEval = minimax(possiblePosition, depth - 1, true, domineeringGameSearch);
                float eval = possiblePositionEval.getEvaluation();
                if (eval < minEval) {
                    minEval = eval;
                    bestMove = new MinMaxAgentMoveEvaluation(possiblePositionEval.getMinimaxAgentMove(), eval);
                }
            }
        }

        return bestMove;
    }

    private boolean wonPosition(MinMaxAgentMove position, int player) {
        int numSquares = position.getNumSquares();
        for (int row = 0; row < numSquares; row++) {
            for (int col = 0; col < numSquares; col++) {
                if (player == 1 && col < numSquares - 1 && position.isDisabled(row, col) && position.isDisabled(row, col + 1))
                    return false;
                if (player == 2 && row < numSquares - 1 && position.isDisabled(row, col) && position.isDisabled(row + 1, col))
                    return false;
            }
        }

        return true;
    }

    private float positionEvaluation(MinMaxAgentMove pos, GameSearch domineeringGameSearch, int player) {
        int playerMoves = domineeringGameSearch.possibleMovesMinMax(pos, player) != null ? domineeringGameSearch.possibleMovesMinMax(pos, player).length : 0;
        int opponentMoves = domineeringGameSearch.possibleMovesMinMax(pos, 3 - player) != null ? domineeringGameSearch.possibleMovesMinMax(pos, 3 - player).length : 0;

        float evaluation = playerMoves - opponentMoves;

        // Add evaluation based on horizontal domination
        for (int i = 0; i < pos.getNumSquares(); i++) {
            for (int j = 0; j < pos.getNumSquares() - 1; j++) {
                if (pos.isDisabled(i, j) && pos.isDisabled(i, j + 1)) {
                    evaluation += (float) (player == 1 ? 0.1 : -0.1);
                }
            }
        }

        // Add evaluation based on vertical domination
        for (int i = 0; i < pos.getNumSquares() - 1; i++) {
            for (int j = 0; j < pos.getNumSquares(); j++) {
                if (pos.isDisabled(i, j) && pos.isDisabled(i + 1, j)) {
                    evaluation += (float) (player == 1 ? 0.1 : -0.1);
                }
            }
        }

        return evaluation;
    }
}
