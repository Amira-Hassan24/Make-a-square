/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author A
 */
public class Board {
    private int size;
    int[][] grid;

    public Board(int size) {
        this.size = size;
        this.grid = new int[size][size];
    }

    public boolean canPlace(int[][] pieceShape, int top, int left) {
        int pr = pieceShape.length, pc = pieceShape[0].length;
        for (int i = 0; i < pr; i++) {
            for (int j = 0; j < pc; j++) {
                if (pieceShape[i][j] == 0) continue;
                int br = top + i, bc = left + j;
                if (br < 0 || br >= size || bc < 0 || bc >= size) {
                    return false;
                }
                if (grid[br][bc] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void place(int[][] pieceShape, int top, int left, int id) {
        int pr = pieceShape.length, pc = pieceShape[0].length;
        for (int i = 0; i < pr; i++) {
            for (int j = 0; j < pc; j++) {
                if (pieceShape[i][j] == 1) {
                    grid[top + i][left + j] = id;
                }
            }
        }
    }

    public void remove(int[][] pieceShape, int top, int left) {
        int pr = pieceShape.length, pc = pieceShape[0].length;
        for (int i = 0; i < pr; i++) {
            for (int j = 0; j < pc; j++) {
                if (pieceShape[i][j] == 1) {
                    grid[top + i][left + j] = 0;
                }
            }
        }
    }

    public boolean isFull() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] == 0) return false;
            }
        }
        return true;
    }

    public int[][] copyGrid() {
        int[][] out = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(grid[i], 0, out[i], 0, size);
        }
        return out;
    }
}