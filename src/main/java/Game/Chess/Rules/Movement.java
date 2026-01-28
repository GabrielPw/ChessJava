package Game.Chess.Rules;

import Game.Chess.Core.Board;
import Game.Chess.Core.BoardSpace;
import Game.Chess.Core.Piece;
import Game.Chess.Core.PieceColorEnum;
import org.joml.Vector2i;

public class Movement {

    public static boolean isValid(Piece piece, BoardSpace from, BoardSpace to, Board board){

        switch (piece.getType()){

            case PAWN ->    {return pawnMovement(from, to, board, piece.isAlreadyMoved());}
            case KING ->    {return kingMovement(from, to, board);}
            case ROOK ->    {return rookMovement(from, to, board);}
            case BISHOP ->  {return bishopMovement(from, to, board);}
            case KNIGHT ->  {return knightMovement(from, to, board);}
            case QUEEN ->   {return queenMovement(from, to, board);}
        }

        return false;
    }

    private static boolean pawnMovement(BoardSpace fromSpace, BoardSpace toSpace, Board board, boolean hasMovedBefore) {


        Vector2i from = new Vector2i(fromSpace.col().getIndex(), fromSpace.row().getValue());
        Vector2i to = new Vector2i(toSpace.col().getIndex(), toSpace.row().getValue());

        int direction = board.getSpaces().get(fromSpace).getColor() == PieceColorEnum.WHITE ? 1 : -1;

        boolean catchMove = from.equals(new Vector2i(to).add(1, direction)) || from.equals(new Vector2i(to).add(-1, direction));
        if (board.getSpaces().containsKey(toSpace)){

            PieceColorEnum pieceColor = board.getSpaces().get(fromSpace).getColor();
            PieceColorEnum targetColor = board.getSpaces().get(toSpace).getColor();
            if (!targetColor.equals(pieceColor) && catchMove){

                // catch
                System.out.println("Capturou peça com PEÃO!");
                return true;
            }

            System.out.println("movimento inválido.");
            return false;

        }

        boolean oneStep = from.equals(new Vector2i(to).add(0, direction));
        boolean twoStep = from.equals(new Vector2i(to).add(0, 2 * direction)) && !hasMovedBefore;

        return oneStep || twoStep;
    }

    private static boolean kingMovement(BoardSpace from, BoardSpace to, Board board) {
        return false;
    }

    private static boolean rookMovement(BoardSpace from, BoardSpace to, Board board) {
        return false;
    }

    private static boolean bishopMovement(BoardSpace from, BoardSpace to, Board board) {
        return false;
    }

    private static boolean knightMovement(BoardSpace from, BoardSpace to, Board board) {

        int xDelta = from.col().getIndex() - to.col().getIndex();
        System.out.println("XDelta: " + xDelta);
        return true;
    }

    private static boolean queenMovement(BoardSpace from, BoardSpace to, Board board) {
        return false;
    }


}
