package Game.ChessLogic.Rules;

import Game.ChessLogic.Core.*;
import org.joml.Vector2i;

import java.util.Arrays;

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

        int dx = Math.abs(fromSpace.col().getIndex() - toSpace.col().getIndex());
        int dy = toSpace.row().getValue() - fromSpace.row().getValue();

        boolean hasPieceOnStart = board.getSpaces().containsKey(fromSpace);
        boolean hasPieceOnDestination = board.getSpaces().containsKey(toSpace);

        Piece piece = board.getSpaces().get(fromSpace);
        int direction = piece.getColor() == PieceColorEnum.WHITE ? 1 : -1;

        if (hasPieceOnDestination && hasPieceOnStart) { // trying to catch.
            if (dx==1 && dy==direction) {

                return !isSameTeam(fromSpace, toSpace, board); // valid catch if not same team.
            }
            return false;
        }

        boolean oneStep = dx == 0 && dy == direction;
        boolean twoStep = dx == 0 && dy == 2 * direction;
        boolean pieceBlockingPath = board.getSpaces().containsKey(new BoardSpace(fromSpace.col(), RowEnum.fromValue(fromSpace.row().getValue()+direction)));
        boolean isPathClear = twoStep && !pieceBlockingPath;

        return  (oneStep || !hasMovedBefore && isPathClear);
    }

    private static boolean knightMovement(BoardSpace fromSpace, BoardSpace toSpace, Board board) {
        int dx = Math.abs(fromSpace.col().getIndex() - toSpace.col().getIndex());
        int dy = Math.abs(toSpace.row().getValue() - fromSpace.row().getValue());

        boolean isValidLShape = (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
        boolean canCapture = !isSameTeam(fromSpace, toSpace, board);

        return isValidLShape && canCapture;
    }

    private static boolean kingMovement(BoardSpace fromSpace, BoardSpace toSpace, Board board) {

        int dx = Math.abs(fromSpace.col().getIndex() - toSpace.col().getIndex());
        int dy = Math.abs(toSpace.row().getValue() - fromSpace.row().getValue());

        boolean isValidShape = dy<=1 && dx <= 1;

        return (isValidShape && !isSameTeam(fromSpace, toSpace, board));
    }

    private static boolean rookMovement(BoardSpace fromSpace, BoardSpace toSpace, Board board) {

        int fromCol = fromSpace.col().getIndex();
        int fromRow = fromSpace.row().getValue();
        int toCol = toSpace.col().getIndex();
        int toRow = toSpace.row().getValue();

        int dx = Math.abs(fromCol - toCol);
        int dy = Math.abs(toRow - fromRow);

        boolean horizontalMove = dx > 0;
        boolean verticalMove = dy > 0;

        boolean validShape = (horizontalMove && !verticalMove) || (!horizontalMove && verticalMove);

        if (isSameTeam(fromSpace, toSpace, board) || !validShape) return false;
        boolean hasPieceBetween = false;

        if (horizontalMove){
            int direction = (int) Math.signum(toCol - fromCol);
            switch (direction){
                case 1:
                    for (int i = fromCol + 1; i < toCol; i++) {
                        hasPieceBetween = board.getSpaces().containsKey(new BoardSpace(ColEnum.fromValue(i), RowEnum.fromValue(fromRow)));
                        if (hasPieceBetween) break;
                    }
                    break;
                case -1:
                    for (int i = fromCol - 1; i > toCol; i--) {
                        hasPieceBetween = board.getSpaces().containsKey(new BoardSpace(ColEnum.fromValue(i), RowEnum.fromValue(fromRow)));
                        System.out.println("Piece between: " + hasPieceBetween);
                        if (hasPieceBetween) break;
                    }
                    break;
            }
            return !hasPieceBetween;
        } else {
            int direction = (int) Math.signum(toRow - fromRow);
            switch (direction){
                case 1:
                    for (int i = fromRow+1; i < fromRow+dy; i++) {
                        hasPieceBetween = board.getSpaces().containsKey(new BoardSpace(ColEnum.fromValue(fromCol), RowEnum.fromValue(i)));
                        if (hasPieceBetween) break;
                    }
                break;
                case -1:
                    for (int i = fromRow - 1; i > toRow; i--) {
                        hasPieceBetween = board.getSpaces().containsKey(new BoardSpace(ColEnum.fromValue(fromCol), RowEnum.fromValue(i)));
                        System.out.println("Piece between: " + hasPieceBetween);
                        if (hasPieceBetween) break;
                    }
                break;
            }
            return !hasPieceBetween;
        }

    }

    private static boolean bishopMovement(BoardSpace fromSpace, BoardSpace toSpace, Board board) {


        Vector2i cols = new Vector2i(fromSpace.col().getIndex(), toSpace.col().getIndex());
        Vector2i rows = new Vector2i(fromSpace.row().getValue(), toSpace.row().getValue());

        int dx = Math.abs(cols.x - cols.y);
        int dy = Math.abs(rows.y - rows.x);

        boolean variatesHorizontally = dx > 0;
        boolean variatesVertically   = dy > 0;
        boolean isValidShape   = variatesHorizontally && variatesVertically && (dx==dy);

        if (!isValidShape) return false;
        boolean hasPieceOnInterval = hasPieceOnInterval(fromSpace, toSpace, board);

        System.out.println("\nHorizontal: " + variatesHorizontally);
        System.out.println("Vertical: " + variatesVertically);
        System.out.println("valid Shape: " + isValidShape);
        System.out.println("blocked path: " + hasPieceOnInterval+"\n");

        return !isSameTeam(fromSpace, toSpace, board) && !hasPieceOnInterval;
    }

    private static boolean hasPieceOnInterval(BoardSpace fromSpace, BoardSpace toSpace, Board board){

        Vector2i cols = new Vector2i(fromSpace.col().getIndex(), toSpace.col().getIndex());
        Vector2i rows = new Vector2i(fromSpace.row().getValue(), toSpace.row().getValue());

        int dx = Math.abs(cols.x - cols.y);
        int dy = Math.abs(rows.y - rows.x);

        boolean positiveY = Math.signum(rows.y - rows.x) == 1;
        boolean positiveX = Math.signum(cols.x - cols.y) == 1;
        boolean diagonal = dx>0 && dy>0;

        if (diagonal){

            int[] rowValues = valuesInBetween(rows.x, rows.y);
            int[] colValues = valuesInBetween(cols.x, cols.y);

            if (rowValues.length == 0 || colValues.length == 0) return false;

            System.out.println("Checking spaces...");
            for (int col : colValues) {
                System.out.print(ColEnum.fromValue(col));
            }

            System.out.println(Arrays.toString(rowValues));
            int count = 0;
            for (int row : rowValues) {

                System.out.println(count);
                int col = colValues[count];
                System.out.println(count + "nd: " + row);
                String actualSpace =  "" + ColEnum.fromValue(col) + row;

                BoardSpace checkedSpace = new BoardSpace(ColEnum.fromValue(col), RowEnum.fromValue(row));
                boolean hasPieceInBetween = board.getSpaces().containsKey(checkedSpace);
                if (hasPieceInBetween) {
                    System.out.println("\n" + actualSpace + " bloqueado!");
                    return true;
                }

                count++;
            }


        }else if (dx > 0){ // horizontal

        } else { // vertical

        }

        return false;
    }

    private static int[] valuesInBetween(int start, int end){ // Return list of numbers in interval (exclusive corners);

        int size = Math.abs(start-end) - 1;
        int[] orderedArray = new int[size];
        int count = 0;
        if (start==end || size <= 0) return new int[]{};
        if (start<end){
            for (int i = start; i < end-1; i++){

                orderedArray[count] = i+1;
                count++;
            }
        }else {
            for (int i = start-1; i > end; i--){
                orderedArray[count] = i;
                count++;
            }
        }

        return orderedArray;
    }

    private static boolean queenMovement(BoardSpace fromSpace, BoardSpace toSpace, Board board) {
        return false;
    }

    private static boolean isSameTeam(BoardSpace fromSpace, BoardSpace toSpace, Board board){

        if (board.getSpaces().containsKey(fromSpace) && board.getSpaces().containsKey(toSpace)){
            Piece captor = board.getSpaces().get(fromSpace);
            Piece target = board.getSpaces().get(toSpace);

            return (captor.getColor() == target.getColor());
        }

        return false;
    }

}
