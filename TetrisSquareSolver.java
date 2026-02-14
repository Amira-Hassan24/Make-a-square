/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author A
 */


import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TetrisSquareSolver extends Application {

    private static final int BOARD_SIZE = 4;
    private static final int ANIMATION_DELAY_MS = 100;

    private FlowPane threadsContainer;
    private TextArea inputArea;
    private Button startBtn;
    private Button stopBtn;
    private CheckBox allowFlipCheck;
    private Label statusLabel;
    private Label solutionsCountLabel;
    private FlowPane previewPane;
    
    private AtomicBoolean stopRequested = new AtomicBoolean(false);
    private AtomicInteger solutionsCount = new AtomicInteger(0);
    
    private List<Color> palette = Arrays.asList(
            Color.web("#E74C3C"), Color.web("#3498DB"), Color.web("#2ECC71"),
            Color.web("#F39C12"), Color.web("#9B59B6"), Color.web("#1ABC9C"),
            Color.web("#E67E22"), Color.web("#34495E")
    );

    private CopyOnWriteArrayList<int[][]> solutions = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("🧩 Tetris Square Solver");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #ecf0f1;");

        VBox left = createLeftPanel();
        
        ScrollPane centerScroll = createCenterPanel();

        root.setLeft(left);
        root.setCenter(centerScroll);

        Scene scene = new Scene(root, 1400, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createLeftPanel() {
        VBox left = new VBox(12);
        left.setPrefWidth(350);
        left.setPadding(new Insets(15));
        left.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-width: 0 1 0 0;");

        Label title = new Label("🧩 Tetris Square Solver");
        title.setFont(Font.font("System", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#2c3e50"));

        Label instructions = new Label(
            "Enter pieces below (rows cols, then matrix):\n" +
            "Separate pieces with blank lines\n\n" 
        );
        instructions.setWrapText(true);
        instructions.setFont(Font.font("Monospaced", 11));
        instructions.setStyle("-fx-text-fill: #7f8c8d;");

        Label inputLabel = new Label("📝 Input Pieces:");
        inputLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        
        inputArea = new TextArea();
        inputArea.setPrefRowCount(10);
        inputArea.setFont(Font.font("Monospaced", 12));
        inputArea.setText(getSampleInput());

        
        Label previewLabel = new Label("👁 Preview:");
        previewLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        
        previewPane = new FlowPane(8, 8);
        previewPane.setPrefHeight(120);
        previewPane.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 8; -fx-border-color: #dee2e6; -fx-border-radius: 4;");
        
        inputArea.textProperty().addListener((obs, old, newVal) -> updatePreview());
        updatePreview();

        allowFlipCheck = new CheckBox("Allow flipping (mirror pieces)");
        allowFlipCheck.setFont(Font.font(11));

        HBox buttonsBox = new HBox(10);
        startBtn = new Button("▶ Start Solving");
        startBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16;");
        startBtn.setOnAction(e -> onStart());

        stopBtn = new Button("⏹ Stop");
        stopBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16;");
        stopBtn.setOnAction(e -> onStop());
        stopBtn.setDisable(true);

        buttonsBox.getChildren().addAll(startBtn, stopBtn);

        statusLabel = new Label("Status: Ready");
        statusLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        statusLabel.setTextFill(Color.web("#2980b9"));

        solutionsCountLabel = new Label();
        solutionsCountLabel.setFont(Font.font(11));
        solutionsCountLabel.setTextFill(Color.web("#16a085"));

        Separator sep1 = new Separator();
        Separator sep2 = new Separator();
        Separator sep3 = new Separator();

        left.getChildren().addAll(
            title,
            sep1,
            instructions,
            inputLabel,
            inputArea,
            previewLabel,
            previewPane,
            sep2,
            allowFlipCheck,
            buttonsBox,
            sep3,
            statusLabel,
            solutionsCountLabel
        );

        return left;
    }

    private ScrollPane createCenterPanel() {
        
    VBox container = new VBox(15);
    container.setPadding(new Insets(15));
    container.setStyle("-fx-background-color: #ecf0f1;");

    Label solverLabel = new Label("⚙ Solver Threads");
    solverLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
    solverLabel.setTextFill(Color.web("#2c3e50"));

    threadsContainer = new FlowPane(15, 15);
    threadsContainer.setPrefWrapLength(700); 

    container.getChildren().addAll(solverLabel, threadsContainer);

    ScrollPane scroll = new ScrollPane(container);
    scroll.setFitToWidth(true);
    scroll.setStyle("-fx-background-color: #ecf0f1;");
    
    return scroll;
}

    private void updatePreview() {
        previewPane.getChildren().clear();
        try {
            List<Piece> pieces = InputParser.parseInput(inputArea.getText());
            for (int i = 0; i < pieces.size(); i++) {
                PiecePreview preview = new PiecePreview(pieces.get(i), getColorForId(i + 1), "Piece " + (i + 1));
                previewPane.getChildren().add(preview.getNode());
            }
        } catch (Exception e) {
           
        }
    }

    private String getSampleInput() {
          return "1 4\n1111\n\n1 4\n1111\n\n1 4\n1111\n\n1 4\n1111";
         // return " ";
    }

    private void onStart() {
        stopRequested.set(false);
        threadsContainer.getChildren().clear();
        solutions.clear();
        solutionsCount.set(0);

        startBtn.setDisable(true);
        stopBtn.setDisable(false);
        updateStatus("Parsing input...");
//        updateSolutionsCount();

        new Thread(() -> {
            List<Piece> pieces;
            try {
                pieces = InputParser.parseInput(inputArea.getText());
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    showError("Input parsing error: " + ex.getMessage());
                    startBtn.setDisable(false);
                    stopBtn.setDisable(true);
                    updateStatus("Error: Invalid input");
                });
                return;
            }

            if (pieces.isEmpty() || pieces.size() > 6) {
                Platform.runLater(() -> {
                    showError("Please provide between 1 and 6 pieces.");
                    startBtn.setDisable(false);
                    stopBtn.setDisable(true);
                    updateStatus("Error: Invalid piece count");
                });
                return;
            }

            boolean allowFlip = allowFlipCheck.isSelected();
            for (Piece p : pieces) {
                p.generateRotations(allowFlip);
            }

            Platform.runLater(() -> updateStatus("Launching " + pieces.size() + " threads..."));

            List<Thread> threads = new ArrayList<>();
            for (int i = 0; i < pieces.size(); i++) {
                List<Piece> ordering = makeOrdering(pieces, i);
                int threadId = i + 1;
                
                BoardPane bp = new BoardPane("Thread " + threadId, BOARD_SIZE, palette);
                Platform.runLater(() -> threadsContainer.getChildren().add(bp.getContainer()));

                ThreadWorker worker = new ThreadWorker(ordering, bp, threadId, 
                    stopRequested, solutions, solutionsCount, ANIMATION_DELAY_MS);
                Thread t = new Thread(worker, "Solver-" + threadId);
                t.setDaemon(true);
                threads.add(t);
                t.start();
            }

            Platform.runLater(() -> updateStatus("Solving... (" + threads.size() + " threads running)"));

            new Thread(() -> {
                try {
                    for (Thread t : threads) {
                        t.join();
                    }
                } catch (InterruptedException ignored) {}

                Platform.runLater(() -> {
                    startBtn.setDisable(false);
                    stopBtn.setDisable(true);
                    if (stopRequested.get()) {
                        updateStatus("Stopped by user");
                    } else if (solutions.isEmpty()) {
                        updateStatus("Finished - No solution found ❌");
                    } else {
                        updateStatus("Finished - " + solutions.size() + " solution(s) found ✅");
                    }
                });
            }, "Monitor").start();

        }, "Startup").start();
    }

    private void onStop() {
        stopRequested.set(true);
        stopBtn.setDisable(true);
        updateStatus("Stopping threads...");
    }

    private void updateStatus(String msg) {
        Platform.runLater(() -> statusLabel.setText("Status: " + msg));
    }

//    public void updateSolutionsCount() {
//        Platform.runLater(() -> 
//            solutionsCountLabel.setText("Solutions found: " + solutionsCount.get())
//        );
//    }

    private List<Piece> makeOrdering(List<Piece> pieces, int firstIndex) {
        List<Piece> copy = new ArrayList<>();
        copy.add(pieces.get(firstIndex));
        for (int i = 0; i < pieces.size(); i++) {
            if (i != firstIndex) copy.add(pieces.get(i));
        }
        return copy;
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.showAndWait();
    }

    private Color getColorForId(int id) {
        return palette.get((id - 1) % palette.size());
    }
}