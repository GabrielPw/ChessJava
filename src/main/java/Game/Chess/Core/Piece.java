package Game.Chess.Core;

public class Piece {

    private final int ID;
    private PieceTypeEnum type;
    private PieceColorEnum color;
    private boolean alreadyMoved;

    public Piece(int id, PieceTypeEnum type, PieceColorEnum color){
        this.ID = id;
        this.type = type;
        this.color = color;
    }

    public PieceTypeEnum getType() {
        return type;
    }

    public PieceColorEnum getColor() {
        return color;
    }

    public int getID() {
        return this.ID;
    }

    public boolean isAlreadyMoved() {
        return alreadyMoved;
    }

    public void setAlreadyMoved(boolean alreadyMoved) {
        this.alreadyMoved = alreadyMoved;
    }
}
