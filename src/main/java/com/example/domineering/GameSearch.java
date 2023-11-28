package com.example.domineering;

import java.util.*;

public abstract class GameSearch {

    public static final boolean DEBUG = false;

    public static final boolean PROGRAM = false;
    public static final boolean HUMAN = true;

    /**
     *  Notes:  PROGRAM false -1,  HUMAN true 1
     */

    /*
     * Abstract methods:
     */

    public abstract boolean drawnPosition(Position p);
    public abstract boolean wonPosition(Position p, boolean player);
    public abstract float positionEvaluation(Position p, boolean player);
    public abstract void printPosition(Position p);
    public abstract Position [] possibleMoves(Position p, boolean player);
    public abstract Position makeMove(Position p, boolean player, Move move);
    public abstract boolean reachedMaxDepth(Position p, int depth);
    public abstract Move createMove();

    /*
     * Search utility methods:
     */

    protected Vector alphaBeta(int depth, Position p, boolean player) {
        Vector v = alphaBetaHelper(depth, p, player, 1000000.0f, -1000000.0f);
        //System.out.println("^^ v(0): " + v.elementAt(0) + ", v(1): " + v.elementAt(1));
        return v;
    }

    protected Vector alphaBetaHelper(int depth, Position p, boolean player, float alpha, float beta) {
        if (GameSearch.DEBUG) System.out.println("alphaBetaHelper("+depth+","+p+","+alpha+","+beta+")");

        if (reachedMaxDepth(p, depth)) {
            Vector v = new Vector(2);
            float value = positionEvaluation(p, player);
            v.addElement(new Float(value));
            v.addElement(null);
            if(GameSearch.DEBUG) {
                System.out.println(" alphaBetaHelper: max depth at " + depth + ", value=" + value);
            }
            return v;
        }

        Vector best = new Vector();
        Position[] moves = possibleMoves(p, player);

        if (moves == null || moves.length == 0) {
            Vector v = new Vector(2);
            float value = positionEvaluation(p, player);
            v.addElement(new Float(value));
            v.addElement(null);
            if (GameSearch.DEBUG) {
                System.out.println(" alphaBetaHelper: no possible moves, value=" + value);
            }
            return v;
        }

        for (int i = 0; i < moves.length; i++) {
            Vector v2 = alphaBetaHelper(depth + 1, moves[i], !player, -beta, -alpha);
            float value = -((Float)v2.elementAt(0)).floatValue();

            if (value > beta) {
                if(GameSearch.DEBUG) System.out.println(" ! ! ! value=" + value + ", beta=" + beta);
                beta = value;
                best = new Vector();
                best.addElement(moves[i]);

                Enumeration enum2 = v2.elements();
                enum2.nextElement();

                while (enum2.hasMoreElements()) {
                    Object o = enum2.nextElement();
                    if (o != null) best.addElement(o);
                }
            }

            /**
             * Use the alpha-beta cutoff test to abort search if we
             * found a move that proves that the previous move in the
             * move chain was dubious
             */
            if (beta >= alpha) {
                break;
            }
        }

        Vector v3 = new Vector();
        v3.addElement(new Float(beta));

        Enumeration enum2 = best.elements();

        while (enum2.hasMoreElements()) {
            v3.addElement(enum2.nextElement());
        }

        return v3;
    }



    public void playGame(Position startingPosition, boolean humanPlayFirst) {
        if (!humanPlayFirst) {
            Vector v = alphaBeta(0, startingPosition, PROGRAM);
            startingPosition = (Position) v.elementAt(1);
        }

        while (true) {
            printPosition(startingPosition);

            if (wonPosition(startingPosition, PROGRAM)) {
                System.out.println("Program won");
                break;
            }

            if (wonPosition(startingPosition, HUMAN)) {
                System.out.println("Human won");
                break;
            }

            System.out.print("\nYour move:");
            Move move = createMove();
            startingPosition = makeMove(startingPosition, HUMAN, move);
            printPosition(startingPosition);

            if (wonPosition(startingPosition, HUMAN)) {
                System.out.println("Human won");
                break;
            }

            Vector v = alphaBeta(0, startingPosition, PROGRAM);

            if (v == null) {
                System.out.println("Program won");
                break;
            }

            Enumeration enum2 = v.elements();

            while (enum2.hasMoreElements()) {
                System.out.println(" next element: " + enum2.nextElement());
            }

            startingPosition = (Position) v.elementAt(1);

            if (startingPosition == null) {
                System.out.println("Program won");
                break;
            }
        }
    }

}
