/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author A
 */
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadWorker implements Runnable {
    private List<Piece> ordering;
    private BoardPane pane;
    private int threadId;
    private AtomicBoolean stopRequested;
    private CopyOnWriteArrayList<int[][]> solutions;
    private AtomicInteger solutionsCount;
    private int animationDelay;

    public ThreadWorker(List<Piece> ordering, BoardPane pane, int threadId,
                       AtomicBoolean stopRequested, CopyOnWriteArrayList<int[][]> solutions,
                       AtomicInteger solutionsCount, int animationDelay) {
        this.ordering = deepCopyPieces(ordering);
        this.pane = pane;
        this.threadId = threadId;
        this.stopRequested = stopRequested;
        this.solutions = solutions;
        this.solutionsCount = solutionsCount;
        this.animationDelay = animationDelay;
    }

    @Override
    public void run() {
        pane.setTitle("Thread " + threadId + " ⚙ Running...");
        pane.appendLog("Started with piece order: " + getPieceIds());
        pane.appendLog("Searching for solutions...");
        pane.appendLog("---");
        
        Solver solver = new Solver(ordering, pane, stopRequested, solutions, solutionsCount, animationDelay);
        solver.solve();
        
        if (stopRequested.get()) {
            pane.setStatusStopped();
            pane.setTitle("Thread " + threadId + " ⏹ Stopped");
        } else {
            String status = solver.getLocalSolutionsCount() > 0 ? "✓ Done" : "✗ Done";
            pane.setTitle("Thread " + threadId + " " + status + " (" + solver.getLocalSolutionsCount() + " solutions)");
        }
        
        pane.appendLog("---");
        pane.appendLog("Finished! Found " + solver.getLocalSolutionsCount() + " solution(s)");
    }

    private String getPieceIds() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ordering.size(); i++) {
            sb.append(ordering.get(i).id);
            if (i < ordering.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    private List<Piece> deepCopyPieces(List<Piece> src) {
        List<Piece> out = new ArrayList<>();
        for (Piece p : src) {
            Piece np = new Piece(p.id, p.shape);
            np.rotations = new ArrayList<>(p.rotations);
            out.add(np);
        }
        return out;
    }
}