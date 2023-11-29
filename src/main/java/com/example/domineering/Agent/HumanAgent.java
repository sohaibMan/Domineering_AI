package com.example.domineering.Agent;

import com.example.domineering.GameSearch.GameSearch;
import com.example.domineering.Move.Move;
import com.example.domineering.Position.Position;


public class HumanAgent extends Agent {
    @Override
    public Move makeMove(Position gamePosition, GameSearch domineeringGameSearch) {
        //null implementation ( human doesn't make moves using an algorithm )
        return null;
    }
}
