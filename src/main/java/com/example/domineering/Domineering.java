package com.example.domineering;

import java.util.Scanner;

public class Domineering extends GameSearch {
    public boolean drawnPosition(Position p) {
        if (GameSearch.DEBUG) System.out.println("drawnPosition(" + p + ")");

        DomineeringPosition pos = (DomineeringPosition) p;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (pos.board[i][j] == DomineeringPosition.BLANK) {
                    if (GameSearch.DEBUG) System.out.println("     ret=false");
                    return false;
                }
            }
        }

        if (GameSearch.DEBUG) System.out.println("     ret=true");
        return true;
    }


    @Override
    public boolean wonPosition(Position p, boolean player) {
        if (GameSearch.DEBUG) System.out.println("wonPosition(" + p + "," + player + ")");

        DomineeringPosition pos = (DomineeringPosition) p;
        boolean playerCanMove = hasMoves(pos, player);
        boolean opponentCanMove = hasMoves(pos, !player);

        return !opponentCanMove;
    }


    private boolean hasMoves(DomineeringPosition pos, boolean player) {
        Position[] moves = possibleMoves(pos, player);
        return moves != null && moves.length > 0;
    }


    @Override
    public float positionEvaluation(Position p, boolean player) {
        DomineeringPosition pos = (DomineeringPosition) p;

        int playerMoves = possibleMoves(p, player) != null ? possibleMoves(p, player).length : 0;
        int opponentMoves = possibleMoves(p, !player) != null ? possibleMoves(p, !player).length : 0;

        float evaluation = playerMoves - opponentMoves;

        // Add evaluation based on horizontal domination
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                if (pos.board[i][j] == DomineeringPosition.HUMAN && pos.board[i][j + 1] == DomineeringPosition.HUMAN) {
                    evaluation += (float) (player ? 0.1 : -0.1);
                } else if (pos.board[i][j] == DomineeringPosition.PROGRAM && pos.board[i][j + 1] == DomineeringPosition.PROGRAM) {
                    evaluation += (float) (player ? -0.1 : 0.1);
                }
            }
        }

        // Add evaluation based on vertical domination
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (pos.board[i][j] == DomineeringPosition.HUMAN && pos.board[i + 1][j] == DomineeringPosition.HUMAN) {
                    evaluation += (float) (player ? 0.1 : -0.1);
                } else if (pos.board[i][j] == DomineeringPosition.PROGRAM && pos.board[i + 1][j] == DomineeringPosition.PROGRAM) {
                    evaluation += (float) (player ? -0.1 : 0.1);
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


    @Override
    public Position[] possibleMoves(Position p, boolean player) {
        if (GameSearch.DEBUG) System.out.println("possibleMoves(" + p + "," + player + ")");

        DomineeringPosition pos = (DomineeringPosition) p;
        int count = 0;

        int rowLimit = player ? 4 : 5;
        int colLimit = player ? 5 : 4;

        for (int i = 0; i < rowLimit; i++) {
            for (int j = 0; j < colLimit; j++) {
                if (pos.board[i][j] == 0 && (player ? pos.board[i + 1][j] == 0 : pos.board[i][j + 1] == 0)) {
                    count++;
                }
            }
        }

        if (count == 0) return null;

        Position[] ret = new Position[count];
        count = 0;

        for (int i = 0; i < rowLimit; i++) {
            for (int j = 0; j < colLimit; j++) {
                if (pos.board[i][j] == 0 && (player ? pos.board[i + 1][j] == 0 : pos.board[i][j + 1] == 0)) {
                    DomineeringPosition pos2 = pos.clonePosition();
                    pos2.board[i][j] = player ? DomineeringPosition.HUMAN : DomineeringPosition.PROGRAM;
                    pos2.board[i + (player ? 1 : 0)][j + (player ? 0 : 1)] = player ? DomineeringPosition.HUMAN : DomineeringPosition.PROGRAM;
                    ret[count++] = pos2;
                }
            }
        }

        return ret;
    }


    @Override
    public Position makeMove(Position p, boolean player, Move move) {
        DomineeringPosition pos = (DomineeringPosition) p;
        DomineeringMove m = (DomineeringMove) move;

        DomineeringPosition pos2 = pos.clonePosition();

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
        boolean currentPlayer = (depth % 2 == 0) ? PROGRAM : HUMAN;

        if (wonPosition(p, currentPlayer) || drawnPosition(p)) {
            return true;
        }

        return depth >= 5;
    }


    @Override
    public Move createMove() {
        Scanner scanner = new Scanner(System.in);

        if (GameSearch.DEBUG) System.out.println("Enter move index (i, j) separated by space:");

        int i = scanner.nextInt();
        int j = scanner.nextInt();

        return new DomineeringMove(i, j);
    }

    public static void main(String[] args) {
        DomineeringPosition p = new DomineeringPosition();
        Domineering domineeringGame = new Domineering();
        domineeringGame.playGame(p, true);
    }
}
