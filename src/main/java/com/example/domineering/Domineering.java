package com.example.domineering;

import java.util.Scanner;

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
//        boolean ret = false;
        DomineeringPosition pos = (DomineeringPosition) p;

        if ((possibleMoves(p, player).length == possibleMoves(p, !player).length && possibleMoves(p, player) != null) ) return true;
        else if (possibleMoves(p, player).length > possibleMoves(p, !player).length) return true;

        if (GameSearch.DEBUG) System.out.println("     ret="+false);
        return false;
    }


    @Override
    public float positionEvaluation(Position p, boolean player) {
        DomineeringPosition pos = (DomineeringPosition) p;

        int playerMoves = possibleMoves(p, player) != null ? possibleMoves(p, player).length : 0;
        int opponentMoves = possibleMoves(p, !player) != null ? possibleMoves(p, !player).length : 0;

        float evaluation = playerMoves - opponentMoves;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (player && pos.board[i][j] == DomineeringPosition.HUMAN && pos.board[i + 1][j] == DomineeringPosition.HUMAN) {
                    evaluation += 0.1;
                } else if (!player && pos.board[i][j] == DomineeringPosition.PROGRAM && pos.board[i][j + 1] == DomineeringPosition.PROGRAM) {
                    evaluation += 0.1;
                }
            }
        }

        return evaluation;
    }


    @Override
    public void printPosition(Position p) {
        System.out.println("Board position:");

        DomineeringPosition pos = (DomineeringPosition) p;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (pos.board[i][j] == DomineeringPosition.HUMAN) {
                    System.out.print("H");
                } else if (pos.board[i][j] == DomineeringPosition.PROGRAM) {
                    System.out.print("P");
                } else {
                    System.out.print("0");
                }
            }
            System.out.println();
        }
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
        DomineeringPosition pos = (DomineeringPosition) p;
        DomineeringMove m = (DomineeringMove) move;

        DomineeringPosition pos2 = new DomineeringPosition();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                pos2.board[i][j] = pos.board[i][j];
            }
        }

        if (player) {
            pos2.board[m.getMoveIndex_i()][m.getMoveIndex_j()] = DomineeringPosition.HUMAN;
            pos2.board[m.getMoveIndex_i() + 1][m.getMoveIndex_j()] = DomineeringPosition.HUMAN;
        } else {
            pos2.board[m.getMoveIndex_i()][m.getMoveIndex_j()] = DomineeringPosition.PROGRAM;
            pos2.board[m.getMoveIndex_i()][m.getMoveIndex_j() + 1] = DomineeringPosition.PROGRAM;
        }

        return pos2;
    }


    @Override
    public boolean reachedMaxDepth(Position p, int depth) {
        if (depth >= 5) {
            return true;
        }

        if (wonPosition(p, false) || wonPosition(p, true) || drawnPosition(p)) {
            return true;
        }

        return false;
    }


    @Override
    public Move createMove() {
        Scanner scanner = new Scanner(System.in);

        if (GameSearch.DEBUG) System.out.println("Enter move index (i, j) separated by space:");

        int i = scanner.nextInt();
        int j = scanner.nextInt();

        DomineeringMove move = new DomineeringMove(i, j);
//        move.setMoveIndex_i(i);
//        move.setMoveIndex_j(j);

        return move;
    }

    public static void main(String[] args) {
        DomineeringPosition p = new DomineeringPosition();
        Domineering domineeringGame = new Domineering();
        domineeringGame.playGame(p, true);
    }
}
