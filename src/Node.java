public class Node {

    Node left = null;
    Node right = null;
    Node midd1 = null;
    Node midd2 = null;
    int[][] data = new int[4][4];

    public Node(int[][] value) {
        data = value;
    }

    public Node() {
    }

    public void printData() {
        for(int i=0; i<=3; i++) {
            for (int j = 0; j <= 3; j++)
                System.out.print(data[i][j] + " ");
            System.out.println();
        }
    }
}
