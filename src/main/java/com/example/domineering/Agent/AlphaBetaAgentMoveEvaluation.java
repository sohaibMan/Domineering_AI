package com.example.domineering.Agent;


import com.example.domineering.Move.AlphaBetaAgentMove;

public class AlphaBetaAgentMoveEvaluation {
    AlphaBetaAgentMove move;
    float evaluation;

    public AlphaBetaAgentMoveEvaluation(AlphaBetaAgentMove move, float evaluation) {
        this.move = move;
        this.evaluation = evaluation;
    }

    public AlphaBetaAgentMove getAlphaBetaAgentMove() {
        return move;
    }

    public float getEvaluation() {
        return evaluation;
    }
}