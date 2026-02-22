package Game.Renderer.Base;

import Game.ChessLogic.Core.*;
import Game.Renderer.Base.Entities.Sprite;
import Game.Renderer.Base.GUI.TextRenderer;
import Game.Renderer.Base.Utils.COLORS;
import Game.Renderer.Base.Utils.Shader;
import Game.Renderer.Base.Utils.TexturePaths;
import Game.Renderer.Base.Window.Window;
import Game.Renderer.Base.Window.WindowInfo;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.HashMap;
import java.util.Map;

public class ChessRenderer {

    private Window window;
    private final Board board;
    float spriteBaseScale = 480.f;

    private int[] selectedSpace = new int[]{-1, -1};

    private final Shader pieceShader = new Shader("quadrado.vert", "quadrado.frag");
    private final Shader boardShader = new Shader("chess/board.vert", "chess/board.frag");

    private final Map<String, Sprite> pieceSpriteMap = new HashMap<>();
    private final Sprite backgroundSprite = new Sprite(pieceShader, TexturePaths.BACKGROUND_WOOD_02, COLORS.WHITE.getRGB());
    private final Sprite boardSprite = new Sprite(boardShader, TexturePaths.BOARD_WHITE1X1, COLORS.WHITE.getRGB());

    public ChessRenderer(Window window, Board board){

        this.window = window;
        this.board = board;

        for (PieceColorEnum pieceColor : PieceColorEnum.values()) {
            for (PieceTypeEnum pieceType : PieceTypeEnum.values()) {
                String texturePath = TexturePaths.getTexturePath(pieceColor, pieceType);
                Sprite pieceSprite = new Sprite(pieceShader, texturePath, COLORS.WHITE.getRGB());
                pieceSpriteMap.put(texturePath, pieceSprite);
            }
        }
    }

    public void run(WindowInfo windowInfo, TextRenderer textRenderer, BoardSpace selectedSpace){

        Vector2f winCenter = new Vector2f(windowInfo.window().getWidth()/2.f, windowInfo.window().getHeight()/2.f);
        Vector2f boardHalfScale = new Vector2f(boardSprite.getScale().x /2.f, boardSprite.getScale().y /2.f);

        backgroundSprite.setScale(spriteBaseScale * 2.5f);
        backgroundSprite.setPosition(winCenter.x, winCenter.y);
        backgroundSprite.update(windowInfo);
        backgroundSprite.render(windowInfo);

        applyShaderSelectedCell();
        boardSprite.setPosition(winCenter.sub(0, 30.f));
        boardSprite.setScale(spriteBaseScale);
        boardSprite.update(windowInfo);
        boardSprite.render(windowInfo);

        renderCoordinateLabels(windowInfo,textRenderer, boardHalfScale);
        renderPieces(windowInfo, boardHalfScale);

        if (selectedSpace != null) {
            this.selectedSpace[0] = selectedSpace.col().getIndex() - 1;
            this.selectedSpace[1] = 7 - selectedSpace.row().ordinal(); // consistente com render
        } else {
            this.selectedSpace[0] = -1;
            this.selectedSpace[1] = -1;
        }
    }

    private void renderPieces(WindowInfo windowInfo, Vector2f boardHalfScale){

        for (Map.Entry<BoardSpace, Piece> entry : board.getSpaces().entrySet()) {

            Piece piece = entry.getValue();
            BoardSpace space = entry.getKey();

            float boardCellSize = boardSprite.getScale().x / 8.f;

            Vector2f boardPos = boardSprite.getPosition();

            int colIndex = space.col().ordinal(); // A=0, B=1, ..., H=7
            int rowIndex = space.row().ordinal(); // ONE=0 → 7, EIGHT=7 → 0

            float x = colIndex * boardCellSize;
            float y = rowIndex * boardCellSize;

            Vector2f spritePos = new Vector2f(boardPos)
                    .sub(boardHalfScale)          // vai para bottom-left do tabuleiro
                    .add(x + boardCellSize/2f, y + boardCellSize/2f); // centro da célula

            Sprite sprite = pieceSpriteMap.get(TexturePaths.getTexturePath(piece.getColor(), piece.getType()));
            sprite.setPosition(spritePos);
            sprite.setScale(new Vector2f(boardSprite.getScale()).div(8.f));
            sprite.update(windowInfo);
            sprite.render(windowInfo);
        }
    }

    void renderCoordinateLabels(WindowInfo windowInfo, TextRenderer textRenderer, Vector2f boardHalfScale){

        for (int col = 1; col <= 8; col++) {

            float gap = boardSprite.getScale().x / 8.f;
            Vector2f textPos = new Vector2f(boardSprite.getPosition())
                    .sub(boardHalfScale)
                    .add((col - 1) * gap, 0)
                    .add(gap / 3.f, -20);

            char letter = (char) ('A' + (col - 1));
            textRenderer.setScale(0.8f);
            textRenderer.setTextColor(COLORS.BLACK.getRGB());

            textRenderer.renderText(String.valueOf(letter), textPos, new Vector2f(256.f), windowInfo.window().getProjection(), new Matrix4f());
        }

        for (int row = 1; row <= 8; row++) {

            float gap = boardSprite.getScale().y / 8.f;
            Vector2f textPos = new Vector2f(boardSprite.getPosition())
                    .sub(boardHalfScale)
                    .add(0, (row - 1) * gap)
                    .add(-20, gap / 3.f);

            textRenderer.setScale(0.8f);
            textRenderer.setTextColor(COLORS.BLACK.getRGB());

            textRenderer.renderText(String.valueOf(row), textPos, new Vector2f(256.f), windowInfo.window().getProjection(), new Matrix4f());
        }

    }

    void applyShaderSelectedCell(){
        boardShader.use();
        boardShader.addUniform2i("selectedCell", selectedSpace);
    }

    public Sprite getBoardSprite() {
        return boardSprite;
    }
}
