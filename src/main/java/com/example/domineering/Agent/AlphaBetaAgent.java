package com.example.domineering.Agent;

import com.example.domineering.GameSearch.GameSearch;
import com.example.domineering.Move.Move;
import com.example.domineering.Player;
import com.example.domineering.Position.DomineeringPosition;
import com.example.domineering.Position.Position;

public class AlphaBetaAgent extends Agent {

    private static final int MAX_DEPTH = 3;

    @Override
    public Move makeMove(Position gamePosition, GameSearch domineeringGameSearch) {
        return alphaBeta((DomineeringPosition) gamePosition, MAX_DEPTH, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, true, domineeringGameSearch).move();
    }

    private MoveEvaluation alphaBeta(DomineeringPosition position, int depth, float alpha, float beta, boolean maximizingPlayer, GameSearch domineeringGameSearch) {
        if (depth == 0 || domineeringGameSearch.wonPosition(position)) {
            float evaluation = evaluatePosition(position, domineeringGameSearch);
            return new MoveEvaluation(null, evaluation); // Use null for Move in case of leaf nodes
        }

        MoveEvaluation bestMove = null;
        Position[] possibleMoves = domineeringGameSearch.possibleMoves(position, position.getCurrentPlayer());

        if (maximizingPlayer) {
            float maxEval = Float.NEGATIVE_INFINITY;
            for (Position move : possibleMoves) {
                MoveEvaluation moveEval = alphaBeta((DomineeringPosition) move, depth - 1, alpha, beta, false, domineeringGameSearch);
                float eval = moveEval.evaluation();
                if (eval > maxEval) {
                    maxEval = eval;
                    bestMove = new MoveEvaluation(moveEval.move(), eval);
                }
                alpha = Math.max(alpha, maxEval);
                if (beta <= alpha) {
                    break;
                }
            }
        } else {
            float minEval = Float.POSITIVE_INFINITY;
            for (Position move : possibleMoves) {
                MoveEvaluation moveEval = alphaBeta((DomineeringPosition) move, depth - 1, alpha, beta, true, domineeringGameSearch);
                float eval = moveEval.evaluation();
                if (eval < minEval) {
                    minEval = eval;
                    bestMove = new MoveEvaluation(moveEval.move(), eval);
                }
                beta = Math.min(beta, minEval);
                if (beta <= alpha) {
                    break;
                }
            }
        }

        return bestMove;
    }

    private float evaluatePosition(DomineeringPosition position, GameSearch domineeringGameSearch) {
        int player = position.isCurrentPlayer(Player.HUMAN) ? 1 : 2;
        return positionEvaluation(position, player, domineeringGameSearch);
    }


    private float positionEvaluation(DomineeringPosition pos, int player, GameSearch domineeringGameSearch) {
        int playerMoves = domineeringGameSearch.possibleMoves(pos, player) != null ? domineeringGameSearch.possibleMoves(pos, player).length : 0;
        int opponentMoves = domineeringGameSearch.possibleMoves(pos, 3 - player) != null ? domineeringGameSearch.possibleMoves(pos, 3 - player).length : 0;

        float evaluation = playerMoves - opponentMoves;

        // Add evaluation based on horizontal domination
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                if (pos.getSquare(i, j).isDisable() && pos.getSquare(i, j + 1).isDisable()) {
                    evaluation += (float) (player == 1 ? 0.1 : -0.1);
                }
            }
        }

        // Add evaluation based on vertical domination
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (pos.getSquare(i, j).isDisable() && pos.getSquare(i + 1, j).isDisable()) {
                    evaluation += (float) (player == 1 ? 0.1 : -0.1);
                }
            }
        }

        return evaluation;
    }


}
