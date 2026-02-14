/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author A
 */
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Solver {
    private List<Piece> pieces;
    private Board board;
    private int localSolutionsCount = 0;
    private BoardPane pane;
    private AtomicBoolean stopRequested;
    private CopyOnWriteArrayList<int[][]> solutions;
    private AtomicInteger solutionsCount;
    private int animationDelay;
    private int[][] lastSolution = null;

    public Solver(List<Piece> pieces, BoardPane pane, AtomicBoolean stopRequested,
                  CopyOnWriteArrayList<int[][]> solutions, AtomicInteger solutionsCount, int animationDelay) {
        this.pieces = pieces;
        this.board = new Board(4);
        this.pane = pane;
        this.stopRequested = stopRequested;
        this.solutions = solutions;
        this.solutionsCount = solutionsCount;
        this.animationDelay = animationDelay;
    }

    public void solve() {
        pane.setStatusSearching();
        backtrack(0);
        if (lastSolution != null) {
            pane.updateGrid(lastSolution);
            pane.setStatusFinished();
        } else {
            pane.setStatusNoSolution();
        }
    }

    private void backtrack(int index) {
        if (stopRequested.get()) return;

        if (index == pieces.size()) {
            if (board.isFull()) {
                int[][] sol = board.copyGrid();
                solutions.add(sol);
                localSolutionsCount++;
                solutionsCount.incrementAndGet();
                lastSolution = sol;  
                pane.appendLog("✓ SOLUTION #" + localSolutionsCount + " FOUND!");
                pane.updateGrid(sol);
                
                try { Thread.sleep(800); } catch (InterruptedException e) {}
            }
            return;
        }

        Piece piece = pieces.get(index);
        
        for (int rotIdx = 0; rotIdx < piece.rotations.size(); rotIdx++) {
            if (stopRequested.get()) return;
            
            int[][] rot = piece.rotations.get(rotIdx);
            int pr = rot.length, pc = rot[0].length;
            
            for (int r = 0; r <= 4 - pr; r++) {
                for (int c = 0; c <= 4 - pc; c++) {
                    if (stopRequested.get()) return;
                    
                    if (board.canPlace(rot, r, c)) {
                        board.place(rot, r, c, piece.id);
                        pane.updateGrid(board.copyGrid());
                        
                        try { Thread.sleep(animationDelay); } 
                        catch (InterruptedException e) {}
                        
                        backtrack(index + 1);
                        
                        board.remove(rot, r, c);
                        
                        
                        if (lastSolution == null) {
                            pane.updateGrid(board.copyGrid());
                        }
                        
                        try { Thread.sleep(animationDelay); } 
                        catch (InterruptedException e) {}
                    }
                }
            }
        }
    }

    public int getLocalSolutionsCount() {
        return localSolutionsCount;
    }
}