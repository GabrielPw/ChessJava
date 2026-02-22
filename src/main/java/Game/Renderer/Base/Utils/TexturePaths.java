package Game.Renderer.Base.Utils;

import Game.ChessLogic.Core.PieceColorEnum;
import Game.ChessLogic.Core.PieceTypeEnum;

public class TexturePaths {


    public static String WINDOW_ICON = "src/main/resources/images/chess/logo.png";

    public static String BOARD_WHITE1X1 = "src/main/resources/images/chess/white_1x1.png";
    public static String BACKGROUND_PAPER = "src/main/resources/images/chess/background_paper.jpg";
    public static String BACKGROUND_WOOD_01 = "src/main/resources/images/chess/background_wood_1.jpg";
    public static String BACKGROUND_WOOD_02 = "src/main/resources/images/chess/background_wood_2.jpg";

    // Pieces
    public static String PIECES_BASE_PATH = "src/main/resources/images/chess/pieces/";

    public static String WHITE_PAWN = "src/main/resources/images/chess/pieces/white_pawn.png";
    public static String BLACK_PAWN = "src/main/resources/images/chess/pieces/black_pawn.png";

    public static String WHITE_ROOK = "src/main/resources/images/chess/pieces/white_rook.png";
    public static String BLACK_ROOK = "src/main/resources/images/chess/pieces/black_rook.png";

    public static String WHITE_QUEEN = "src/main/resources/images/chess/pieces/white_queen.png";
    public static String BLACK_QUEEN = "src/main/resources/images/chess/pieces/black_queen.png";

    public static String WHITE_KNIGHT = "src/main/resources/images/chess/pieces/white_knight.png";
    public static String BLACK_KNIGHT = "src/main/resources/images/chess/pieces/black_knight.png";

    public static String WHITE_KING = "src/main/resources/images/chess/pieces/white_king.png";
    public static String BLACK_KING = "src/main/resources/images/chess/pieces/black_king.png";

    public static String WHITE_BISHOP = "src/main/resources/images/chess/pieces/white_bishop.png";
    public static String BLACK_BISHOP = "src/main/resources/images/chess/pieces/black_bishop.png";

    public static String getPiecePath(PieceTypeEnum type, PieceColorEnum color){
        return PIECES_BASE_PATH + color + "_" + type + ".png";
    }

    public static String getTexturePath(PieceColorEnum color, PieceTypeEnum type) {
        return switch (type) {
            case PAWN   -> color == PieceColorEnum.WHITE ? TexturePaths.WHITE_PAWN   : TexturePaths.BLACK_PAWN;
            case KING   -> color == PieceColorEnum.WHITE ? TexturePaths.WHITE_KING   : TexturePaths.BLACK_KING;
            case QUEEN  -> color == PieceColorEnum.WHITE ? TexturePaths.WHITE_QUEEN  : TexturePaths.BLACK_QUEEN;
            case ROOK   -> color == PieceColorEnum.WHITE ? TexturePaths.WHITE_ROOK   : TexturePaths.BLACK_ROOK;
            case BISHOP -> color == PieceColorEnum.WHITE ? TexturePaths.WHITE_BISHOP : TexturePaths.BLACK_BISHOP;
            case KNIGHT -> color == PieceColorEnum.WHITE ? TexturePaths.WHITE_KNIGHT : TexturePaths.BLACK_KNIGHT;
        };
    }
}
