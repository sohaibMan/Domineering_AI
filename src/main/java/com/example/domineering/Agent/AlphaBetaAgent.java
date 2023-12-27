package com.example.domineering.Agent;

import com.example.domineering.GameSearch.GameSearch;
import com.example.domineering.Move.AlphaBetaAgentMove;
import com.example.domineering.Move.Move;
import com.example.domineering.Position.Position;


public class AlphaBetaAgent extends Agent {

    private static final int MAX_DEPTH = 2;

    @Override
    public Move makeMove(Position gamePosition, GameSearch domineeringGameSearch) {
        // transform position to board
        // -1 = program, 1 = human, 0 = blank
        AlphaBetaAgentMove alphaBetaAgentMove = new AlphaBetaAgentMove(gamePosition);


        AlphaBetaAgentMove alphaBetaAgentMoveResult = alphaBeta(
                alphaBetaAgentMove,
                MAX_DEPTH,
                Float.NEGATIVE_INFINITY,
                Float.POSITIVE_INFINITY,
                true,
                domineeringGameSearch
        ).getAlphaBetaAgentMove();

        System.out.println("new move" + alphaBetaAgentMoveResult);
        //return the element of the board with the move
        return gamePosition.getSquare(alphaBetaAgentMoveResult.getRow(), alphaBetaAgentMoveResult.getCol());
    }

    private AlphaBetaAgentMoveEvaluation alphaBeta(AlphaBetaAgentMove position, int depth, float alpha, float beta, boolean maximizingPlayer, GameSearch domineeringGameSearch) {
        int currentPlayer = maximizingPlayer ? 2 : 1;

        if (depth == 0 || wonPosition(position, currentPlayer)) {
            float evaluation = positionEvaluation(position, domineeringGameSearch, currentPlayer);
            return new AlphaBetaAgentMoveEvaluation(position, evaluation);
        }

        AlphaBetaAgentMoveEvaluation bestMove = new AlphaBetaAgentMoveEvaluation(null, maximizingPlayer ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY);

        for (AlphaBetaAgentMove possibleMove : domineeringGameSearch.possibleMoves(position, currentPlayer)) {
            AlphaBetaAgentMoveEvaluation possibleMoveEval = alphaBeta(possibleMove, depth - 1, alpha, beta, !maximizingPlayer, domineeringGameSearch);

            if (possibleMoveEval != null) {
                float eval = possibleMoveEval.getEvaluation();

                if (maximizingPlayer && eval > bestMove.getEvaluation()) {
                    bestMove = new AlphaBetaAgentMoveEvaluation(possibleMove, eval);
                    alpha = Math.max(alpha, eval);
                } else if (!maximizingPlayer && eval < bestMove.getEvaluation()) {
                    bestMove = new AlphaBetaAgentMoveEvaluation(possibleMove, eval);
                    beta = Math.min(beta, eval);
                }

                if (beta <= alpha) {
                    break;
                }
            }
        }



        return bestMove;
    }


    private boolean wonPosition(AlphaBetaAgentMove position, int player) {
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


    private float positionEvaluation(AlphaBetaAgentMove pos, GameSearch domineeringGameSearch, int player) {
        int playerMoves = domineeringGameSearch.possibleMoves(pos, player) != null ? domineeringGameSearch.possibleMoves(pos, player).length : 0;
        int opponentMoves = domineeringGameSearch.possibleMoves(pos, 3 - player) != null ? domineeringGameSearch.possibleMoves(pos, 3 - player).length : 0;

        float evaluation = playerMoves - opponentMoves;

        // Add evaluation based on horizontal domination
        for (int i = 0; i < pos.getNumSquares(); i++) {
            for (int j = 0; j < pos.getNumSquares() - 1; j++) {
                // j=4 i=4  , 4*5+4 = 24
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
