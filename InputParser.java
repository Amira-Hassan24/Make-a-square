/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author A
 */
import java.util.*;

public class InputParser {
    
    public static List<Piece> parseInput(String text) {
        String[] lines = text.split("\\r?\\n");
        List<Piece> pieces = new ArrayList<>();
        int i = 0;

        while (i < lines.length) {
            while (i < lines.length && lines[i].trim().isEmpty()) i++;
            if (i >= lines.length) break;

            String[] rc = lines[i++].trim().split("\\s+");
            if (rc.length < 2) {
                throw new IllegalArgumentException("Expected 'rows columns' format at line " + i);
            }
            
            int r = Integer.parseInt(rc[0]);
            int c = Integer.parseInt(rc[1]);

            if (r <= 0 || c <= 0 || r > 10 || c > 10) {
                throw new IllegalArgumentException("Invalid dimensions: " + r + "x" + c);
            }

            int[][] shape = new int[r][c];
            for (int rr = 0; rr < r; rr++) {
                if (i >= lines.length) {
                    throw new IllegalArgumentException("Missing rows for piece");
                }
                String row = lines[i++].trim();
                if (row.length() < c) {
                    throw new IllegalArgumentException("Row too short: " + row);
                }
                for (int cc = 0; cc < c; cc++) {
                    char ch = row.charAt(cc);
                    shape[rr][cc] = (ch == '1') ? 1 : 0;
                }
            }
            
            Piece p = new Piece(pieces.size() + 1, shape);
            pieces.add(p);
        }

        return pieces;
    }
}
