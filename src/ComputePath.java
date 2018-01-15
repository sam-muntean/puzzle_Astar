import javafx.util.Pair;
import sun.awt.Mutex;

import java.util.*;
import java.util.concurrent.Callable;

public class ComputePath implements Callable<Pair<Double, List<int[][]>>> {
    Set<int[][]> expanded;
    Pair<Double, List<int[][]>> path;
    int[][] endNode;
    Node root;
    Tree tree;
    int direction;
    Mutex m;
    List<int[][]> addTree;

    public ComputePath(List<int[][]> addTree, Mutex m, Set<int[][]> expanded, Pair<Double, List<int[][]>> path, int[][] endNode, Node root, Tree tree, int direction) {
        this.expanded = expanded;
        this.path = path;
        this.endNode = endNode;
        this.root = root;
        this.tree = tree;
        this.direction = direction;
        this.m = m;
        this.addTree = addTree;
    }

    @Override
    public Pair<Double, List<int[][]>> call() throws Exception {
        int[][] m = new int[4][4];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                m[i][j] = endNode[i][j];

        List<int[][]> output = new ArrayList<>();

        int posjZero = -1;
        int posiZero = -1;
        int aux;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (m[i][j] == 0) {
                    posjZero = j;
                    posiZero = i;
                    //break;
                }
            }
        }
        //System.out.println("posi" + posiZero + "posj" + posjZero);
        if (posiZero != -1 && posjZero != -1) {
            //UP
            if (direction == 0) {
                if (posiZero > 0) {
                    aux = m[posiZero][posjZero];
                    m[posiZero][posjZero] = m[posiZero - 1][posjZero];
                    m[posiZero - 1][posjZero] = aux;
                    int[][] lol = new int[4][4];
                    for (int i = 0; i <= 3; i++)
                        for (int j = 0; j <= 3; j++)
                            lol[i][j] = m[i][j];
                    output.add(0, lol);
                    aux = m[posiZero][posjZero];
                    m[posiZero][posjZero] = m[posiZero - 1][posjZero];
                    m[posiZero - 1][posjZero] = aux;
                }
            }
            //DOWN
            else if (direction == 1) {
                if (posiZero < 3) {
                    aux = m[posiZero][posjZero];
                    m[posiZero][posjZero] = m[posiZero + 1][posjZero];
                    m[posiZero + 1][posjZero] = aux;
                    int[][] lol = new int[4][4];
                    for (int i = 0; i <= 3; i++)
                        for (int j = 0; j <= 3; j++)
                            lol[i][j] = m[i][j];
                    output.add(0, lol);
                    aux = m[posiZero][posjZero];
                    m[posiZero][posjZero] = m[posiZero + 1][posjZero];
                    m[posiZero + 1][posjZero] = aux;
                }
            }
            //LEFT
            else if (direction == 2) {
                if (posjZero > 0) {
                    aux = m[posiZero][posjZero];
                    m[posiZero][posjZero] = m[posiZero][posjZero - 1];
                    m[posiZero][posjZero - 1] = aux;
                    int[][] lol = new int[4][4];
                    for (int i = 0; i <= 3; i++)
                        for (int j = 0; j <= 3; j++)
                            lol[i][j] = m[i][j];
                    output.add(0 ,lol);
                    aux = m[posiZero][posjZero];
                    m[posiZero][posjZero] = m[posiZero][posjZero - 1];
                    m[posiZero][posjZero - 1] = aux;
                }
            }
            //RIGHT
            else if (direction == 3) {
                if (posjZero < 3) {
                    aux = m[posiZero][posjZero];
                    m[posiZero][posjZero] = m[posiZero][posjZero + 1];
                    m[posiZero][posjZero + 1] = aux;
                    int[][] lol = new int[4][4];
                    for (int i = 0; i <= 3; i++)
                        for (int j = 0; j <= 3; j++)
                            lol[i][j] = m[i][j];
                    output.add(0, lol);
                    aux = m[posiZero][posjZero];
                    m[posiZero][posjZero] = m[posiZero][posjZero + 1];
                    m[posiZero][posjZero + 1] = aux;
                }
            }
        }
        if (output.size() != 0) {
            boolean tt = false;
            this.m.lock();
            for (Iterator<int[][]> iterator = expanded.iterator(); iterator.hasNext(); ) {
                if (this.egal(iterator.next(), output.get(0))){
                  tt = true;
                    break;
                }
            }
            this.m.unlock();
            if (tt) {
                return null;
            }
            double distance = path.getKey() + Main.distance(output.get(0)) - Main.distance(endNode);
            List<int[][]> toA = new ArrayList<>();
            toA.addAll(path.getValue());
            toA.add(output.get(0));
            //tree.insert(root,output);

            return new Pair<>(distance, toA);
        }
        return null;
    }

    private boolean egal(int[][] next, int[][] ints) {
        for(int i = 0; i <= 3; i++)
            for(int j = 0; j <= 3; j++) {
            if (next[i][j] != ints[i][j])
                return false;
            }
        return true;
    }
}
