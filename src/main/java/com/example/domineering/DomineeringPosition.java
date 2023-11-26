package com.example.domineering;

public class DomineeringPosition extends Position {
    public static final int BLANK = 0;
    public static final int HUMAN = 1;
    public static final int PROGRAM = -1;

    int[][] board = new int[5][5];

    public String toString() {
        StringBuilder sb = new StringBuilder("[ \n");

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                sb.append(board[i][j]);
                if (j < 4) {
                    sb.append(", ");
                }
            }
            if (i < 4) {
                sb.append("; \n");
            }
        }

        sb.append("\n]");
        return sb.toString();
    }

    public static void main(String[] args){
        DomineeringPosition domineeringPosition = new DomineeringPosition();
        System.out.println(domineeringPosition.toString());
    }
}

