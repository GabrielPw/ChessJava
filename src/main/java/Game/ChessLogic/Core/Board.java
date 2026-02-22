package Game.ChessLogic.Core;

import java.util.HashMap;
import java.util.Map;

public class Board {

    private Map<BoardSpace, Piece> spaces = new HashMap<>();

    public void createDefaultBoard(){

        int idNum = 0;

        for (ColEnum col : ColEnum.values()) {
            Piece piece = new Piece(++idNum, PieceTypeEnum.PAWN, PieceColorEnum.BLACK);
            spaces.put(new BoardSpace(col, RowEnum.SEVEN), piece);
        }

        spaces.put(new BoardSpace(ColEnum.A, RowEnum.EIGHT), new Piece(++idNum, PieceTypeEnum.ROOK, PieceColorEnum.BLACK));
        spaces.put(new BoardSpace(ColEnum.B, RowEnum.EIGHT), new Piece(++idNum, PieceTypeEnum.KNIGHT, PieceColorEnum.BLACK));
        spaces.put(new BoardSpace(ColEnum.C, RowEnum.EIGHT), new Piece(++idNum, PieceTypeEnum.BISHOP, PieceColorEnum.BLACK));
        spaces.put(new BoardSpace(ColEnum.D, RowEnum.EIGHT), new Piece(++idNum, PieceTypeEnum.QUEEN, PieceColorEnum.BLACK));
        spaces.put(new BoardSpace(ColEnum.E, RowEnum.EIGHT), new Piece(++idNum, PieceTypeEnum.KING, PieceColorEnum.BLACK));
        spaces.put(new BoardSpace(ColEnum.F, RowEnum.EIGHT), new Piece(++idNum, PieceTypeEnum.BISHOP, PieceColorEnum.BLACK));
        spaces.put(new BoardSpace(ColEnum.G, RowEnum.EIGHT), new Piece(++idNum, PieceTypeEnum.KNIGHT, PieceColorEnum.BLACK));
        spaces.put(new BoardSpace(ColEnum.H, RowEnum.EIGHT), new Piece(++idNum, PieceTypeEnum.ROOK, PieceColorEnum.BLACK));

        for (ColEnum col : ColEnum.values()) {
            spaces.put(new BoardSpace(col, RowEnum.TWO), new Piece(++idNum, PieceTypeEnum.PAWN, PieceColorEnum.WHITE));
        }

        spaces.put(new BoardSpace(ColEnum.A, RowEnum.ONE), new Piece(++idNum, PieceTypeEnum.ROOK, PieceColorEnum.WHITE));
        spaces.put(new BoardSpace(ColEnum.B, RowEnum.ONE), new Piece(++idNum, PieceTypeEnum.KNIGHT, PieceColorEnum.WHITE));
        spaces.put(new BoardSpace(ColEnum.C, RowEnum.ONE), new Piece(++idNum, PieceTypeEnum.BISHOP, PieceColorEnum.WHITE));
        spaces.put(new BoardSpace(ColEnum.D, RowEnum.ONE), new Piece(++idNum, PieceTypeEnum.QUEEN, PieceColorEnum.WHITE));
        spaces.put(new BoardSpace(ColEnum.E, RowEnum.ONE), new Piece(++idNum, PieceTypeEnum.KING, PieceColorEnum.WHITE));
        spaces.put(new BoardSpace(ColEnum.F, RowEnum.ONE), new Piece(++idNum, PieceTypeEnum.BISHOP, PieceColorEnum.WHITE));
        spaces.put(new BoardSpace(ColEnum.G, RowEnum.ONE), new Piece(++idNum, PieceTypeEnum.KNIGHT, PieceColorEnum.WHITE));
        spaces.put(new BoardSpace(ColEnum.H, RowEnum.ONE), new Piece(++idNum, PieceTypeEnum.ROOK, PieceColorEnum.WHITE));

    }

    public void createKingAgainstAll(){


        spaces.put(new BoardSpace(ColEnum.E, RowEnum.FIVE), new Piece(1, PieceTypeEnum.KING, PieceColorEnum.BLACK));
        spaces.put(new BoardSpace(ColEnum.E, RowEnum.FOUR), new Piece(2, PieceTypeEnum.PAWN, PieceColorEnum.WHITE));
        spaces.put(new BoardSpace(ColEnum.D, RowEnum.FIVE), new Piece(3, PieceTypeEnum.PAWN, PieceColorEnum.WHITE));
        spaces.put(new BoardSpace(ColEnum.F, RowEnum.FIVE), new Piece(4, PieceTypeEnum.PAWN, PieceColorEnum.WHITE));
        spaces.put(new BoardSpace(ColEnum.E, RowEnum.SIX), new Piece(5, PieceTypeEnum.PAWN, PieceColorEnum.WHITE));

    }

    public void move(BoardSpace from, BoardSpace to){

        Piece piece = spaces.get(from);

        spaces.remove(from);
        spaces.put(to, piece);

        piece.setAlreadyMoved(true);
    }

    public Map<BoardSpace, Piece> getSpaces() {
        return spaces;
    }
}
