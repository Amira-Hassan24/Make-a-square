/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author A
 */
import java.util.*;

public class Piece {
    int id;
    int[][] shape;
    List<int[][]> rotations = new ArrayList<>();

    public Piece(int id, int[][] shape) {
        this.id = id;
        this.shape = shape;
    }

    public void generateRotations(boolean allowFlip) {
        rotations.clear();
        Set<String> seen = new HashSet<>();
        
        int[][] curr = shape;
        for (int i = 0; i < 4; i++) {
            String key = matrixToString(curr);
            if (!seen.contains(key)) {
                rotations.add(curr);
                seen.add(key);
            }
            curr = rotate90(curr);
        }
        
        if (allowFlip) {
            int[][] flip = mirrorHorizontal(shape);
            curr = flip;
            for (int i = 0; i < 4; i++) {
                String key = matrixToString(curr);
                if (!seen.contains(key)) {
                    rotations.add(curr);
                    seen.add(key);
                }
                curr = rotate90(curr);
            }
        }
    }

    private String matrixToString(int[][] m) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : m) {
            for (int val : row) {
                sb.append(val);
            }
            sb.append("|");
        }
        return sb.toString();
    }

    public static int[][] rotate90(int[][] m) {
        int r = m.length, c = m[0].length;
        int[][] out = new int[c][r];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                out[j][r - 1 - i] = m[i][j];
            }
        }
        return out;
    }

    public static int[][] mirrorHorizontal(int[][] m) {
        int r = m.length, c = m[0].length;
        int[][] out = new int[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                out[i][c - 1 - j] = m[i][j];
            }
        }
        return out;
    }
}
