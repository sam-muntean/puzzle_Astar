import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class Main {

    public static boolean isSolvable(int[][] puzzle) {
        int parity = 0;
        int gridWidth = 4;
        List<Integer> puzz = new ArrayList<>();

        for(int i = 0; i <= 3; i++)
            for(int j = 0; j <= 3; j++) {
                puzz.add(puzzle[i][j]);
        }
        int row = 0, blankRow = 0;
        for (int i = 0; i < puzz.size(); i++) {
            int j = i+1;
            if (i % gridWidth == 0)
                row++;
            if (puzz.get(i) == 0) {
                blankRow = row;
                continue;
            }
            for (j = i+1; j < puzz.size(); j++) {
                if (puzz.get(i) > puzz.get(j) && puzz.get(j) != 0) {
                    parity++;
                }
            }
        }
        if (gridWidth % 2 == 0) {
            if (blankRow % 2 == 0)
                return parity % 2 != 0;
            else
                return parity % 2 == 0;
        } else
            return parity % 2 == 0;
    }


//    public static int[][] moves(int[][] mat, Node root){
//        int[][] output;
//
//
//        return output;
//    }

    public static int distance(int[][] mat) {
        int distance = 0;
        int puzz[][] = mat;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.println("puzz[i][j]: " + puzz[i][j]);

                if (puzz[i][j] != 0){
                    distance += abs(i - puzz[i][j]/4);
                    distance += abs(j - (puzz[i][j]%4));
                }
                System.out.println("distanceeeeeee!!!!: "+distance);
            }
        }
        System.out.println("final distance!!!!: "+distance);

        return distance;
    }

    public static void main(String[] args) {

        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 0, 9, 8, 10, 11, 12, 13, 14, 15};
        int[] ordered = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0};
        int[][] puzzle = new int[4][4];
        int[][] goal = new int[4][4];

        int distance;

        int line = 0;
        while (line < 4) {
            for (int i = 0; i <= 3; i++) {
                puzzle[line][i] = numbers[i + line * 4];
                goal[line][i] = ordered[i + line * 4];
            }
            line++;
        }

        for(int i=0; i<=3; i++) {
            for (int j = 0; j <= 3; j++)
                System.out.print(puzzle[i][j] + " ");
            System.out.println();
        }
        boolean isSolvable = isSolvable(puzzle);
        System.out.println("is this solvable?" + isSolvable(puzzle));
        Node root = new Node();
        Tree tree = new Tree();
        if (isSolvable) {
            root = tree.insert(root, puzzle);
            tree.traversePreorder(root, 5);

        }
        distance = distance(puzzle);
        System.out.println("Distance in main " + distance);
    }
}
