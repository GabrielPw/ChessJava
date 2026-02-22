package Game.ChessLogic.Core;

public enum ColEnum {
    A(1),
    B(2),
    C(3),
    D(4),
    E(5),
    F(6),
    G(7),
    H(8);

    private final int index;

    ColEnum(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static ColEnum fromValue(int value) {
        for (ColEnum col : ColEnum.values()) {
            if (col.index == value) {
                return col;
            }
        }
        throw new IllegalArgumentException("Valor inválido: " + value);
    }
}
