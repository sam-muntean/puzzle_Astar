import javafx.util.Pair;
import sun.awt.Mutex;

import java.util.*;
import java.util.concurrent.*;

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
                }
            }
        }


        tree.insert(root, output);
        return output;
    }

    public static double distance(int[][] mat) {
        double distance = 0;
        int puzz[][] = mat;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (puzz[i][j] != 0){
                    distance += abs(i - puzz[i][j]/4.0);
                    distance += abs(j - (puzz[i][j]%4));
                }
            }
        }
        return distance;
    }

    public static void solveAStar(int[][] puzzle, int[][] goal, Node root, Tree tree) throws ExecutionException, InterruptedException {
        List<int[][]> puzz = new ArrayList<>();
        puzz.add(puzzle);
        List<Pair<Double, List<int[][]>>> front = new ArrayList<>();
        front.add(new Pair<>(distance(puzzle), puzz));

        Set<int[][]> expanded = new LinkedHashSet<>();
        Pair<Double, List<int[][]>> path = new Pair<>(null, null);
        Pair<Double, List<int[][]>> newPath;
        int[][] endNode ;
        int expandedStates = 0;
        while (front.size() > 0) {
            int i = 0;
            for (int j = 1; j < front.size(); j++) {
                if (front.get(i).getKey() > front.get(j).getKey()) {
                    i = j;
                }
            }
            if (i == 0 && front.size() == 1) {
                path = front.get(i);
                front.remove(0);
            } else if (i == 0 && front.size() != 1) {
                path = front.get(i);
                front.remove(0);

            } else if (front.size() > i) {
                path = front.get(i);
                front.remove(i);
            }

            endNode = path.getValue().get(path.getValue().size() - 1);
            if (Arrays.deepEquals(goal, endNode)) {
                System.out.println("GGGGGGGGGGGGGGGGGGGGGGGGGGAAAAAAAAAAAAAAATTTTTTTTTTTTTTAAAAAAAAA");
                break;
            }

            if (expanded.contains(endNode))
                continue;

//            boolean tt = false;
//            for (int[][] k : moves(tree, endNode, root)) {
//                //k in expanded
//                for (int[][] kk : expanded) {
//                    if (Arrays.deepEquals(kk, k)){
//                        tt = true;
//                        break;
//                    }
//                }
//                if (tt) {
//                    tt = false;
//                    continue;
//                }
//                List<int[][]> toA = new ArrayList<>();
//                toA.addAll(path.getValue());
//                toA.add(k);
//
//                newPath = new Pair<>(path.getKey() + distance(k) - distance(endNode), toA);
//                front.add(newPath);
//                expanded.add(endNode);
//            }
            Mutex mutex = new Mutex();

            ExecutorService executor = Executors.newFixedThreadPool(4);
            List<Future<Pair<Double, List<int[][]>>>> newPaths = new ArrayList<>();
            List<int[][]> addTree = new ArrayList<>();
            for(int idx = 0; idx < 4; idx++) {
                int direction = idx;
                Callable<Pair<Double, List<int[][]>>> task = new ComputePath(addTree, mutex, expanded, path, endNode, root, tree, direction);
                newPaths.add(executor.submit(task));
            }

            for(int idx = 0; idx < 4; idx++) {
                newPath = newPaths.get(idx).get();
                mutex.lock();
                if (newPath != null) {
                    front.add(newPath);
                    expanded.add(endNode);
                }
                mutex.unlock();
            }
            executor.shutdownNow();

            expandedStates++;
        }
        System.out.println("expanded state " + expandedStates);
        System.out.println("Solution: ");
        for (int[][] a : path.getValue()) {
            for (int i = 0; i <= 3; i++) {
                for (int j = 0; j <= 3; j++)
                    System.out.print(a[i][j] + "\t");
                System.out.println();
            }
            System.out.println("---------------");
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 0, 9, 8, 10, 11, 12, 13, 14, 15};
        int[] ordered = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        int[][] puzzle = new int[4][4];
        int[][] goal = new int[4][4];


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
        System.out.println("is this solvable? - " + isSolvable(puzzle));
        List<int[][]> p = new ArrayList<>();
        Node root = new Node(p);
        Tree tree = new Tree();
        if (isSolvable) {
            root = tree.insert(root, p);
            tree.traversePreorder(root, 5);
            solveAStar(puzzle, goal, root, tree);
        }
    }
}
