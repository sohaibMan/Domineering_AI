package com.example.domineering.GameSearch;


import com.example.domineering.Move.AlphaBetaAgentMove;
import com.example.domineering.Move.MinMaxAgentMove;
import com.example.domineering.Move.Move;
import com.example.domineering.Position.Position;


public abstract class GameSearch {


    public abstract boolean wonPosition(Position p);


    public abstract AlphaBetaAgentMove[] possibleMoves(AlphaBetaAgentMove p, int player);

    public abstract MinMaxAgentMove[] possibleMovesMinMax(MinMaxAgentMove p, int player);

    public abstract Move getNeighbourMove(Position position, Move move, int player);

    public abstract Move makeMove(Position gamePosition, GameSearch gameSearch);
}
