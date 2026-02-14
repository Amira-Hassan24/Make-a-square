/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author A
 */
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PiecePreview {
    private static final int PREVIEW_CELL_SIZE = 15;
    private VBox container;

    public PiecePreview(Piece piece, Color color, String name) {
        int[][] shape = piece.shape;
        int rows = shape.length;
        int cols = shape[0].length;

        Canvas canvas = new Canvas(cols * PREVIEW_CELL_SIZE + 2, rows * PREVIEW_CELL_SIZE + 2);
        GraphicsContext g = canvas.getGraphicsContext2D();

        g.setFill(Color.WHITE);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double x = c * PREVIEW_CELL_SIZE + 1;
                double y = r * PREVIEW_CELL_SIZE + 1;
                
                if (shape[r][c] == 1) {
                    g.setFill(color);
                } else {
                    g.setFill(Color.web("#f0f0f0"));
                }
                
                g.fillRect(x, y, PREVIEW_CELL_SIZE - 2, PREVIEW_CELL_SIZE - 2);
                g.setStroke(Color.web("#999"));
                g.strokeRect(x, y, PREVIEW_CELL_SIZE - 2, PREVIEW_CELL_SIZE - 2);
            }
        }

        Label label = new Label(name);
        label.setFont(Font.font("System", FontWeight.BOLD, 9));
        label.setTextFill(Color.web("#555"));

        container = new VBox(3, canvas, label);
        container.setPadding(new Insets(4));
        container.setStyle("-fx-alignment: center;");
    }

    public VBox getNode() {
        return container;
    }
}