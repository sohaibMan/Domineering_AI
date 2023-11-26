package com.example.domineering;

public class Domineering extends GameSearch {
    @Override
    public boolean drawnPosition(Position p) {
        if (GameSearch.DEBUG) System.out.println("drawnPosition("+p+")");

        boolean ret = true;
        DomineeringPosition pos = (DomineeringPosition) p;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (pos.board[i][j] == DomineeringPosition.BLANK) {
                    ret = false;
                    break;
                }
            }
        }

        if (GameSearch.DEBUG) System.out.println("     ret=" + ret);
        return ret;
    }

    @Override
    public boolean wonPosition(Position p, boolean player) {
        if (GameSearch.DEBUG) System.out.println("wonPosition("+p+","+player+")");
        boolean ret = false;
        DomineeringPosition pos = (DomineeringPosition) p;

        if ((possibleMoves(p, player).length == possibleMoves(p, !player).length && possibleMoves(p, player) != null) ) return true;
        else if (possibleMoves(p, player).length > possibleMoves(p, !player).length) return true;

        if (GameSearch.DEBUG) System.out.println("     ret="+ret);
        return ret;
    }


    @Override
    public float positionEvaluation(Position p, boolean player) {
        return 0;
    }

    @Override
    public void printPosition(Position p) {

    }


    // TODO : |Souhaib| i know this method is very slow, i ll optimize it later !!
    @Override
    public Position[] possibleMoves(Position p, boolean player) {
        if (GameSearch.DEBUG) System.out.println("possibleMoves(" + p + "," + player + ")");

        DomineeringPosition pos = (DomineeringPosition) p;
        int count = 0;

        if (player) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 5; j++) {
                    if (pos.board[i][j] == 0 && pos.board[i + 1][j] == 0) {
                        count++;
                    }
                }
            }
        } else {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 4; j++) {
                    if (pos.board[i][j] == 0 && pos.board[i][j + 1] == 0) {
                        count++;
                    }
                }
            }
        }

        if (count == 0) return null;

        Position[] ret = new Position[count];
        count = 0;

        if (player) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 5; j++) {
                    if (pos.board[i][j] == 0 && pos.board[i + 1][j] == 0) {
                        DomineeringPosition pos2 = new DomineeringPosition();
                        for (int k = 0; k < 5; k++) {
                            for (int l = 0; l < 5; l++) {
                                pos2.board[k][l] = pos.board[k][l];
                            }
                        }
                        pos2.board[i][j] = DomineeringPosition.HUMAN;
                        pos2.board[i + 1][j] = DomineeringPosition.HUMAN;
                        ret[count++] = pos2;
                    }
                }
            }
        } else {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 4; j++) {
                    if (pos.board[i][j] == 0 && pos.board[i][j + 1] == 0) {
                        DomineeringPosition pos2 = new DomineeringPosition();
                        for (int k = 0; k < 5; k++) {
                            for (int l = 0; l < 5; l++) {
                                pos2.board[k][l] = pos.board[k][l];
                            }
                        }
                        pos2.board[i][j] = DomineeringPosition.PROGRAM;
                        pos2.board[i][j + 1] = DomineeringPosition.PROGRAM;
                        ret[count++] = pos2;
                    }
                }
            }
        }

        return ret;
    }


    @Override
    public Position makeMove(Position p, boolean player, Move move) {
        return null;
    }

    @Override
    public boolean reachedMaxDepth(Position p, int depth) {
        return false;
    }

    @Override
    public Move createMove() {
        return null;
    }
}
