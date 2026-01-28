package Game.Chess;

import Game.Chess.Core.Board;
import Game.Chess.Core.BoardSpace;
import Game.Chess.Core.Piece;
import Game.Chess.Rules.Movement;

public class Chess {

    private BoardSpace selectedCell;
    private Board board = new Board();
    public Chess(){

        board.createDefaultBoard();
    }

    public Board getBoard() {
        return board;
    }

    public void onCellClicked(BoardSpace clicked) {

        if (selectedCell == null){ // No space selected

            this.selectedCell = clicked;
        }else {
            boolean containsPiece = board.getSpaces().containsKey(clicked);

            if (containsPiece){
                selectedCell = clicked;
            } else {
                selectedCell = null;
            }
        }
    }

    public BoardSpace getSelectedCell() {
        return selectedCell;
    }
}
