package com.example.domineering.GameSearch;


import com.example.domineering.Move.Move;
import com.example.domineering.Position.Position;


public abstract class GameSearch {


    public abstract boolean wonPosition(Position p);


    public abstract Position[] possibleMoves(Position p, int player);

    public abstract Move getNeighbourMove(Position position, Move move, int player);

    public abstract Move makeMove(Position gamePosition, GameSearch gameSearch);
}
