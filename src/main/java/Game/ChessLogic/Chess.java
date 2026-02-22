package Game.ChessLogic;

import Game.ChessLogic.Core.Board;
import Game.ChessLogic.Core.BoardSpace;
import Game.ChessLogic.Core.Piece;
import Game.ChessLogic.Rules.Movement;

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

        if (selectedCell == null && board.getSpaces().containsKey(clicked)){
            selectedCell = clicked;
        }else if (board.getSpaces().containsKey(selectedCell)){ // About to move piece.

            Piece piece = board.getSpaces().get(selectedCell);
            if(Movement.isValid(piece, selectedCell, clicked, board)){
                System.out.println("Resultado: Movimento válido.\n");
                board.move(selectedCell, clicked);
            }else {

                System.out.println("Resultado: Movimento inválido.\n");
            }
            if (board.getSpaces().containsKey(clicked)) {
                selectedCell = clicked;
            }
        }
    }

    public BoardSpace getSelectedCell() {
        return selectedCell;
    }
}
