import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.incrementExact;
import static java.lang.Math.pow;

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


    public static List<int[][]> moves(Tree tree, int[][] mat, Node root){
        int[][] m = mat;
        List<int[][]> output = new ArrayList<>();

        int posjZero = -1;
        int posiZero = -1;
        int aux;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (m[i][j] == 0) {
                    posjZero = j;
                    posiZero = i;
//                    System.out.println("posiZero " + posiZero + " posjZero " + posjZero );
                }
            }
        }

//        move UP
        if(posiZero > 0){
            aux= m[posiZero][posjZero];
            m[posiZero][posjZero] = m[posiZero-1][posjZero];
            m[posiZero-1][posjZero] = aux;
            int[][] lol = new int[4][4];
            for(int i = 0; i<=3; i++)
                for (int j = 0; j<=3; j++)
                    lol[i][j] = m[i][j];
            output.add(lol);
            aux= m[posiZero][posjZero];
            m[posiZero][posjZero] = m[posiZero-1][posjZero];
            m[posiZero-1][posjZero] = aux;
        }

//        move DOWN
        if(posiZero < 3){
            aux= m[posiZero][posjZero];
            m[posiZero][posjZero] = m[posiZero+1][posjZero];
            m[posiZero+1][posjZero] = aux;
            int[][] lol = new int[4][4];
            for(int i = 0; i<=3; i++)
                for (int j = 0; j<=3; j++)
                    lol[i][j] = m[i][j];
            output.add(lol);
            aux= m[posiZero][posjZero];
            m[posiZero][posjZero] = m[posiZero+1][posjZero];
            m[posiZero+1][posjZero] = aux;
        }

//        move LEFT
        if (posjZero > 0){
            aux= m[posiZero][posjZero];
            m[posiZero][posjZero] = m[posiZero][posjZero-1];
            m[posiZero][posjZero-1] = aux;
            int[][] lol = new int[4][4];
            for(int i = 0; i<=3; i++)
                for (int j = 0; j<=3; j++)
                    lol[i][j] = m[i][j];
            output.add(lol);
            aux= m[posiZero][posjZero];
            m[posiZero][posjZero] = m[posiZero][posjZero-1];
            m[posiZero][posjZero-1] = aux;
        }

//        move RIGHT
        if (posjZero < 3){
            aux= m[posiZero][posjZero];
            m[posiZero][posjZero] = m[posiZero][posjZero+1];
            m[posiZero][posjZero+1] = aux;
            int[][] lol = new int[4][4];
            for(int i = 0; i<=3; i++)
                for (int j = 0; j<=3; j++)
                    lol[i][j] = m[i][j];
            output.add(lol);
            aux= m[posiZero][posjZero];
            m[posiZero][posjZero] = m[posiZero][posjZero+1];
            m[posiZero][posjZero+1] = aux;
        }

        tree.insert(root, output);
        return output;
    }

    public static double distance(int[][] mat) {
        double distance = 0;
        double no = 0;
        int puzz[][] = new int[4][4];
        for(int i = 0; i<=3; i++)
            for (int j = 0; j<=3; j++)
                puzz[i][j] = mat[i][j];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
//                System.out.println("puzz[i][j]: " + puzz[i][j]);

                if (puzz[i][j] != 0){
                    distance += abs(i - puzz[i][j]/4.0);
                    distance += abs(j - (puzz[i][j]%4));
                }
//                System.out.println("distanceeeeeee!!!!: "+distance);
            }
        }
        System.out.println("final distance!!!!: "+distance);

        return distance;
    }

    public static void solveAStar(int[][] puzzle, int[][] goal, Node root, Tree tree) {
        List<int[][]> puzz = new ArrayList<>();
        puzz.add(puzzle);
        List<Pair<Double, List<int[][]>>> front = new ArrayList<>();

        front.add(new Pair<>(distance(puzzle), puzz));

        List<int[][]> expanded = new ArrayList<>();
        Pair<Double, List<int[][]>> path = new Pair<>(null, null);
        Pair<Double, List<int[][]>> newPath = new Pair<>(null, null);
        int[][] endNode = new int[4][4];
        //int[][] endNode = new int[4][4];
        int expandedStates = 0;
        while (front.size() > 0) {
            int i = 0;
            for (int j = 1; j < front.size(); j++) {
                if (front.get(i).getKey() > front.get(j).getKey()) {
                    i = j;
                }
            }
            path = front.get(i);
            if (front.size() > i+1)
                front.remove(i + 1);
            endNode = path.getValue().get(path.getValue().size() - 1);
            if (Arrays.deepEquals(goal, endNode)){
                System.out.println("GGGGGGGGGGGGGGGGGGGGGGGGGGAAAAAAAAAAAAAAATTTTTTTTTTTTTTAAAAAAAAA");
                break;}
            boolean t = false;
            for (int[][] ii : expanded) {
                if (Arrays.deepEquals(ii, endNode)) {
                    t = true;
                    //System.out.println("GGGGGGGGGGGGGGGGGGGGGGGGGGAAAAAAAAAAAAAAATTTTTTTTTTTTTTAAAAAAAAA");
                    break;
                }
            }
            if (t)
                continue;
            boolean tt = false;
            for (int[][] k : moves(tree, endNode, root)) {
                //k in expanded
                for (int[][] kk : expanded) {
                    if (Arrays.deepEquals(kk, k))
                        //System.out.println("GGGGGGGGGGGGGGGGGGGGGGGGGGAAAAAAAAAAAAAAATTTTTTTTTTTTTTAAAAAAAAA");
                        t = true;
                }
                if (t)
                    continue;
                List<int[][]> toA = new ArrayList<>();
                toA = path.getValue();
                toA.add(k);
                Pair<Double, List<int[][]>> toAdd = new Pair<Double, List<int[][]>>(path.getKey() + distance(k) - distance(endNode),
                        toA);
                newPath = toAdd;
                front.add(newPath);
                expanded.add(endNode);
            }
            expandedStates++;
        }
        System.out.println("expanded state" + expandedStates);

    }

    public static void main(String[] args) {

        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 0, 9, 8, 10, 11, 12, 13, 14, 15};
        int[] ordered = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0};
        int[][] puzzle = new int[4][4];
        int[][] goal = new int[4][4];

        double distance;

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
        List<int[][]> p = new ArrayList<>();
        Node root = new Node(p);
        Tree tree = new Tree();
        if (isSolvable) {
            root = tree.insert(root, p);
            tree.traversePreorder(root, 5);
            solveAStar(puzzle, goal, root, tree);
        }
        distance = distance(puzzle);
        System.out.println("Distance in main " + distance);

//        List<int[][]> output = moves(tree,puzzle,root);
//        System.out.println("\nOutput from moves: ");
//        for (int[][] m : output){
//            for (int i = 0; i < 4; i++){
//                for (int j = 0; j < 4; j++){
//                    System.out.print(m[i][j] + " ");
//                }
//                System.out.println();
//            }
//            System.out.println();
//        }
    }
}
