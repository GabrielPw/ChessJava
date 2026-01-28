package Game.Chess.Core;

import Game.Chess.Rules.Movement;

import java.util.HashMap;
import java.util.Map;

public class Board {

    private Map<BoardSpace, Piece> spaces = new HashMap<>();

    public void createDefaultBoard(){

        int idNum = 0;

        for (ColEnum col : ColEnum.values()) {
            Piece piece = new Piece(++idNum, PieceTypeEnum.PAWN, PieceColorEnum.BLACK);
            spaces.put(new BoardSpace(col, RowEnum.TWO), piece);
        }

        spaces.put(new BoardSpace(ColEnum.A, RowEnum.ONE), new Piece(++idNum, PieceTypeEnum.ROOK, PieceColorEnum.BLACK));
        spaces.put(new BoardSpace(ColEnum.B, RowEnum.ONE), new Piece(++idNum, PieceTypeEnum.KNIGHT, PieceColorEnum.BLACK));
        spaces.put(new BoardSpace(ColEnum.C, RowEnum.ONE), new Piece(++idNum, PieceTypeEnum.BISHOP, PieceColorEnum.BLACK));
        spaces.put(new BoardSpace(ColEnum.D, RowEnum.ONE), new Piece(++idNum, PieceTypeEnum.QUEEN, PieceColorEnum.BLACK));
        spaces.put(new BoardSpace(ColEnum.E, RowEnum.ONE), new Piece(++idNum, PieceTypeEnum.KING, PieceColorEnum.BLACK));
        spaces.put(new BoardSpace(ColEnum.F, RowEnum.ONE), new Piece(++idNum, PieceTypeEnum.BISHOP, PieceColorEnum.BLACK));
        spaces.put(new BoardSpace(ColEnum.G, RowEnum.ONE), new Piece(++idNum, PieceTypeEnum.KNIGHT, PieceColorEnum.BLACK));
        spaces.put(new BoardSpace(ColEnum.H, RowEnum.ONE), new Piece(++idNum, PieceTypeEnum.ROOK, PieceColorEnum.BLACK));

        for (ColEnum col : ColEnum.values()) {
            spaces.put(new BoardSpace(col, RowEnum.SEVEN), new Piece(++idNum, PieceTypeEnum.PAWN, PieceColorEnum.WHITE));
        }

        spaces.put(new BoardSpace(ColEnum.A, RowEnum.EIGHT), new Piece(++idNum, PieceTypeEnum.ROOK, PieceColorEnum.WHITE));
        spaces.put(new BoardSpace(ColEnum.B, RowEnum.EIGHT), new Piece(++idNum, PieceTypeEnum.KNIGHT, PieceColorEnum.WHITE));
        spaces.put(new BoardSpace(ColEnum.C, RowEnum.EIGHT), new Piece(++idNum, PieceTypeEnum.BISHOP, PieceColorEnum.WHITE));
        spaces.put(new BoardSpace(ColEnum.D, RowEnum.EIGHT), new Piece(++idNum, PieceTypeEnum.QUEEN, PieceColorEnum.WHITE));
        spaces.put(new BoardSpace(ColEnum.E, RowEnum.EIGHT), new Piece(++idNum, PieceTypeEnum.KING, PieceColorEnum.WHITE));
        spaces.put(new BoardSpace(ColEnum.F, RowEnum.EIGHT), new Piece(++idNum, PieceTypeEnum.BISHOP, PieceColorEnum.WHITE));
        spaces.put(new BoardSpace(ColEnum.G, RowEnum.EIGHT), new Piece(++idNum, PieceTypeEnum.KNIGHT, PieceColorEnum.WHITE));
        spaces.put(new BoardSpace(ColEnum.H, RowEnum.EIGHT), new Piece(++idNum, PieceTypeEnum.ROOK, PieceColorEnum.WHITE));

    }

    public boolean move(BoardSpace from, BoardSpace to){

        if (spaces.containsKey(from)){

            Piece piece = spaces.get(from);

            if(Movement.isValid(piece, from, to, this)){

                spaces.remove(from);
                spaces.put(to, piece);

                piece.setAlreadyMoved(true);
                return true;
            }

        }

        return false;
    }

    public Map<BoardSpace, Piece> getSpaces() {
        return spaces;
    }
}
