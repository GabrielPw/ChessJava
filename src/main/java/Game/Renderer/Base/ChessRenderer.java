package Game.Renderer.Base;

import Game.Chess.Core.*;
import Game.Main;
import Game.Renderer.Base.Entities.Sprite;
import Game.Renderer.Base.GUI.TextRenderer;
import Game.Renderer.Base.Utils.COLORS;
import Game.Renderer.Base.Utils.Shader;
import Game.Renderer.Base.Utils.TexturePaths;
import Game.Renderer.Base.Window.WindowInfo;
import Game.Renderer.Utils.Mouse;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ChessRenderer {

    private final Board board;

    private int[] selectedSpace = new int[]{-1, -1};
    private final Map<Piece, Sprite> pieceSprites = new HashMap<>();
    private final Shader pieceShader = new Shader("quadrado.vert", "quadrado.frag");
    private final Shader boardShader = new Shader("chess/board.vert", "chess/board.frag");

    private final Sprite backgroundSprite = new Sprite(pieceShader, TexturePaths.BACKGROUND_WOOD, COLORS.WHITE.getRGB());
    private final Sprite boardSprite = new Sprite(boardShader, TexturePaths.BOARD, COLORS.WHITE.getRGB());

    public ChessRenderer(Board board){

        this.board = board;

        board.getSpaces().forEach((space, piece) -> {

            if (piece == null) return;

            String texturePath = switch (piece.getType()) {
                case PAWN   -> piece.getColor() == PieceColorEnum.WHITE ? TexturePaths.WHITE_PAWN   : TexturePaths.BLACK_PAWN;
                case KING   -> piece.getColor() == PieceColorEnum.WHITE ? TexturePaths.WHITE_KING   : TexturePaths.BLACK_KING;
                case QUEEN  -> piece.getColor() == PieceColorEnum.WHITE ? TexturePaths.WHITE_QUEEN  : TexturePaths.BLACK_QUEEN;
                case ROOK   -> piece.getColor() == PieceColorEnum.WHITE ? TexturePaths.WHITE_ROOK   : TexturePaths.BLACK_ROOK;
                case BISHOP -> piece.getColor() == PieceColorEnum.WHITE ? TexturePaths.WHITE_BISHOP : TexturePaths.BLACK_BISHOP;
                case KNIGHT -> piece.getColor() == PieceColorEnum.WHITE ? TexturePaths.WHITE_KNIGHT : TexturePaths.BLACK_KNIGHT;
            };

            Sprite sprite = new Sprite(pieceShader, texturePath, COLORS.WHITE.getRGB());
            pieceSprites.put(piece, sprite);
        });

    }


    public void run(WindowInfo windowInfo, TextRenderer textRenderer, BoardSpace selectedSpace){

        float spriteBaseScale = 480.f;
        Vector2f winCenter = new Vector2f(windowInfo.window().getWidth()/2.f, windowInfo.window().getHeight()/2.f);

        backgroundSprite.setScale(spriteBaseScale * 2.5f);
        backgroundSprite.setPosition(winCenter.x, winCenter.y);
        backgroundSprite.update(windowInfo);
        backgroundSprite.render(windowInfo);

        boardSprite.setPosition(winCenter.sub(0, 30.f));
        boardSprite.setScale(spriteBaseScale);
        Vector2f boardHalfScale = new Vector2f(boardSprite.getScale().x /2.f, boardSprite.getScale().y /2.f);

        setSelectedCell();
        boardSprite.update(windowInfo);
        boardSprite.render(windowInfo);

        for (int col = 1; col <= 8; col++) {

            float gap = boardSprite.getScale().x / 8.f;
            Vector2f textPos = new Vector2f(boardSprite.getPosition())
                    .sub(boardHalfScale)
                    .add((col - 1) * gap, 0)
                    .add(gap / 3.f, -20);

            char letter = (char) ('A' + (col - 1));
            renderBoardCoordinates(String.valueOf(letter), windowInfo, textRenderer, textPos);
        }

        for (int row = 1; row <= 8; row++) {

            float gap = boardSprite.getScale().y / 8.f;
            Vector2f textPos = new Vector2f(boardSprite.getPosition())
                    .sub(boardHalfScale)
                    .add(0, (row - 1) * gap)
                    .add(-20, gap / 3.f);

            renderBoardCoordinates(String.valueOf(row), windowInfo, textRenderer, textPos);
        }


        board.getSpaces().forEach((space, piece) -> {

            Sprite sprite = pieceSprites.get(piece);

            int colIndex = space.col().ordinal();
            int rowIndex = 7 - space.row().ordinal();

            float squareSize = boardSprite.getScale().x / 8f;
            Vector2f boardBottomLeft = new Vector2f(winCenter).sub(boardHalfScale);

            float x = boardBottomLeft.x
                    + colIndex * squareSize
                    + squareSize / 2f;

            float y = boardBottomLeft.y
                    + rowIndex * squareSize
                    + squareSize / 2f;

            sprite.setScale(spriteBaseScale / 8.f);
            sprite.setPosition(x, y);

            sprite.update(windowInfo);
            sprite.render(windowInfo);

        });

        if (selectedSpace != null) {
            this.selectedSpace[0] = selectedSpace.col().getIndex() - 1;
            this.selectedSpace[1] = 7 - selectedSpace.row().ordinal(); // consistente com render
        } else {
            this.selectedSpace[0] = -1;
            this.selectedSpace[1] = -1;
        }

        setSelectedCell();
    }

    void renderBoardCoordinates(String text, WindowInfo windowInfo, TextRenderer textRenderer, Vector2f textPos){

        textRenderer.setScale(0.8f);
        textRenderer.setTextColor(COLORS.BLACK.getRGB());

        textRenderer.renderText(text, textPos, new Vector2f(256.f), windowInfo.window().getProjection(), new Matrix4f());

    }
    void setSelectedCell(){
        boardShader.use();
        boardShader.addUniform2i("selectedCell", selectedSpace);
    }

    public Sprite getBoardSprite() {
        return boardSprite;
    }
}
