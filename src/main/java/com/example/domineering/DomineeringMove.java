package com.example.domineering;

public class DomineeringMove extends Move {
    private int moveIndex_i;
    private int moveIndex_j;

    public DomineeringMove(int i, int j) {
        this.moveIndex_i = i;
        this.moveIndex_j = j;
    }

    public int getMoveIndex_i() {
        return moveIndex_i;
    }

    public int getMoveIndex_j() {
        return moveIndex_j;
    }

    public void setMoveIndex_i(int moveIndex_i) {
        this.moveIndex_i = moveIndex_i;
    }

    public void setMoveIndex_j(int moveIndex_j) {
        this.moveIndex_j = moveIndex_j;
    }
}
