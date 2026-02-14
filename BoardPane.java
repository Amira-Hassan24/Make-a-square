/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author A
 */
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class BoardPane {
    private static final int CELL_SIZE = 40;
    
    private VBox container;
    private Canvas canvas;
    private Label titleLabel;
    private Label statusLabel;
    private TextArea logArea;
    private int boardSize;
    private List<Color> palette;

    public BoardPane(String name, int boardSize, List<Color> palette) {
        this.boardSize = boardSize;
        this.palette = palette;
        
        container = new VBox(10);
        container.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #bdc3c7; " +
            "-fx-border-width: 2; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 15; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);"
        );
        container.setPrefWidth(500);
        container.setMaxWidth(500);

        // Title
        titleLabel = new Label(name);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.web("#2c3e50"));
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setMaxWidth(Double.MAX_VALUE);

        Separator sep1 = new Separator();

        // Status label
        statusLabel = new Label("⚙ Searching...");
        statusLabel.setFont(Font.font("System", FontWeight.BOLD, 11));
        statusLabel.setTextFill(Color.web("#3498db"));
        statusLabel.setAlignment(Pos.CENTER);
        statusLabel.setMaxWidth(Double.MAX_VALUE);

        // Canvas في box
        canvas = new Canvas(boardSize * CELL_SIZE + 2, boardSize * CELL_SIZE + 2);
        drawEmptyGrid();
        
        VBox canvasBox = new VBox(canvas);
        canvasBox.setAlignment(Pos.CENTER);
        canvasBox.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 10; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Log area
        logArea = new TextArea();
        logArea.setPrefRowCount(8);
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setFont(Font.font("Monospaced", 9));
        logArea.setStyle("-fx-control-inner-background: #fafafa;");
        
        VBox logBox = new VBox(5);
        Label logLabel = new Label("📋 Solutions Log:");
        logLabel.setFont(Font.font("System", FontWeight.BOLD, 11));
        logLabel.setTextFill(Color.web("#555"));
        logBox.getChildren().addAll(logLabel, logArea);

        // ✅ البورد واللوج جنب بعض في HBox
        HBox contentBox = new HBox(15);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.getChildren().addAll(canvasBox, logBox);

        Separator sep2 = new Separator();
        
        container.getChildren().addAll(titleLabel, sep1, statusLabel, contentBox);
    }

    private void drawEmptyGrid() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setFill(Color.WHITE);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                double x = c * CELL_SIZE + 1;
                double y = r * CELL_SIZE + 1;
                g.setFill(Color.web("#ecf0f1"));
                g.fillRect(x, y, CELL_SIZE - 2, CELL_SIZE - 2);
                g.setStroke(Color.web("#bdc3c7"));
                g.strokeRect(x, y, CELL_SIZE - 2, CELL_SIZE - 2);
            }
        }
    }

    public void updateGrid(int[][] grid) {
        Platform.runLater(() -> drawGrid(grid));
    }

    private void drawGrid(int[][] grid) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                int val = grid[r][c];
                double x = c * CELL_SIZE + 1;
                double y = r * CELL_SIZE + 1;
                
                if (val == 0) {
                    g.setFill(Color.web("#ecf0f1"));
                } else {
                    g.setFill(getColorForId(val));
                }
                
                g.fillRect(x, y, CELL_SIZE - 2, CELL_SIZE - 2);
                g.setStroke(Color.web("#34495e"));
                g.setLineWidth(1.5);
                g.strokeRect(x, y, CELL_SIZE - 2, CELL_SIZE - 2);
            }
        }
    }

    public void setStatusSearching() {
        Platform.runLater(() -> {
            statusLabel.setText("⚙ Searching...");
            statusLabel.setTextFill(Color.web("#3498db"));
        });
    }

    public void setStatusFinished() {
        Platform.runLater(() -> {
            statusLabel.setText("✓ Final Solution");
            statusLabel.setTextFill(Color.web("#27ae60"));
        });
    }

    public void setStatusNoSolution() {
        Platform.runLater(() -> {
            statusLabel.setText("✗ No Solution");
            statusLabel.setTextFill(Color.web("#e74c3c"));
        });
    }

    public void setStatusStopped() {
        Platform.runLater(() -> {
            statusLabel.setText("⏹ Stopped");
            statusLabel.setTextFill(Color.web("#95a5a6"));
        });
    }

    public void appendLog(String s) {
        Platform.runLater(() -> {
            logArea.appendText(s + "\n");
            logArea.setScrollTop(Double.MAX_VALUE);
        });
    }

    public void setTitle(String s) {
        Platform.runLater(() -> titleLabel.setText(s));
    }

    public VBox getContainer() {
        return container;
    }

    private Color getColorForId(int id) {
        return palette.get((id - 1) % palette.size());
    }
}