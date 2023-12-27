package com.example.domineering.Agent;


import com.example.domineering.Move.AlphaBetaAgentMove;
import com.example.domineering.Move.MinMaxAgentMove;

public class MinMaxAgentMoveEvaluation {
    MinMaxAgentMove move;
    float evaluation;

    public MinMaxAgentMoveEvaluation(MinMaxAgentMove move, float evaluation) {
        this.move = move;
        this.evaluation = evaluation;
    }

    public MinMaxAgentMove getMinimaxAgentMove() {
        return move;
    }

    public float getEvaluation() {
        return evaluation;
    }
}